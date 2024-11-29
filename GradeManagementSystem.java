import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class GradeManagementSystem extends JFrame {
    private JTextField studentNameField;
    private JTextField gradeField;
    private JTextArea reportArea;
    private Map<String, List<Integer>> grades;
    private boolean reportFinalized;

    public GradeManagementSystem() {
        grades = new HashMap<>();
        reportFinalized = false;

        setTitle("Grade Management System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        inputPanel.add(new JLabel("Student Name:"));
        studentNameField = new JTextField();
        inputPanel.add(studentNameField);

        inputPanel.add(new JLabel("Grade:"));
        gradeField = new JTextField();
        inputPanel.add(gradeField);

        JButton saveButton = new JButton("Save Grade");
        saveButton.addActionListener(new SaveGradeAction());
        inputPanel.add(saveButton);

        JButton generateReportButton = new JButton("Generate Report");
        generateReportButton.addActionListener(new GenerateReportAction());
        inputPanel.add(generateReportButton);

        add(inputPanel, BorderLayout.NORTH);

        reportArea = new JTextArea();
        add(new JScrollPane(reportArea), BorderLayout.CENTER);

        JButton finalizeReportButton = new JButton("Finalize Report");
        finalizeReportButton.addActionListener(new FinalizeReportAction());
        add(finalizeReportButton, BorderLayout.SOUTH);
    }

    private class SaveGradeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (reportFinalized) {
                JOptionPane.showMessageDialog(null, "Report is finalized. No changes allowed.");
                return;
            }

            String studentName = studentNameField.getText();
            int grade;
            try {
                grade = Integer.parseInt(gradeField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid grade. Please enter a number.");
                return;
            }

            grades.computeIfAbsent(studentName, k -> new ArrayList<>()).add(grade);
            studentNameField.setText("");
            gradeField.setText("");
        }
    }

    private class GenerateReportAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            StringBuilder report = new StringBuilder();
            LocalDate currentDate = LocalDate.now();
            report.append("Monthly Report\n");
            report.append("Date: ").append(currentDate).append("\n");
            report.append("====================\n");
            for (Map.Entry<String, List<Integer>> entry : grades.entrySet()) {
                String studentName = entry.getKey();
                List<Integer> studentGrades = entry.getValue();
                double average = studentGrades.stream().mapToInt(Integer::intValue).average().orElse(0.0);
                report.append("Student: ").append(studentName).append("\n");
                report.append("Grades: ").append(studentGrades).append("\n");
                report.append("Average: ").append(String.format("%.2f", average)).append("\n");
                report.append("--------------------\n");
            }
            reportArea.setText(report.toString());
            saveReportToFile(report.toString());
        }
    }

    private void saveReportToFile(String report) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Data/Student/Grades/monthly_report.txt"))) {
            writer.write(report);
            JOptionPane.showMessageDialog(null, "Report saved to monthly_report.txt");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error saving report to file.");
        }
    }

    private class FinalizeReportAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            reportFinalized = true;
            JOptionPane.showMessageDialog(null, "Report finalized. No further changes allowed.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GradeManagementSystem frame = new GradeManagementSystem();
            frame.setVisible(true);
        });
    }
}
