package ui;

import dao.AttendanceDAO;
import dao.StudentDAO;
import models.Student;
import theme.UITheme;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadAttendancePanel extends JPanel {

    private final int classId;
    private final Map<Integer, JCheckBox> presentMap = new HashMap<>();
    private final JButton submitButton = new JButton("Submit Attendance");
    private final JTextField dateField;

    public UploadAttendancePanel(int classId) {
        this.classId = classId;

        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.LIGHT_GRAY); // Maintain theme

        // Top date selector
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(UITheme.WHITE);
        topPanel.add(new JLabel("Select Date (yyyy-MM-dd):"));

        dateField = new JTextField(10);
        dateField.setText(LocalDate.now().toString());
        dateField.setToolTipText("Format: YYYY-MM-DD");
        topPanel.add(dateField);
        add(topPanel, BorderLayout.NORTH);

        // Center: Student list with checkboxes (organized layout)
        JPanel centerPanel = new JPanel(new GridBagLayout()); // Better alignment
        centerPanel.setBackground(UITheme.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        List<Student> students = StudentDAO.getStudentsByClassId(classId);
        int row = 0;
        for (Student s : students) {
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 1.0;
            JLabel nameLabel = new JLabel(s.getName());
            nameLabel.setFont(UITheme.DEFAULT_FONT);
            centerPanel.add(nameLabel, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0;
            JCheckBox presentBox = new JCheckBox("Present");
            centerPanel.add(presentBox, gbc);

            presentMap.put(s.getId(), presentBox);
            row++;
        }

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Submit Button
        UITheme.stylePrimaryButton(submitButton);
        submitButton.addActionListener(e -> handleSubmit());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(UITheme.LIGHT_GRAY);
        bottomPanel.add(submitButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void handleSubmit() {
        LocalDate date;
        try {
            date = LocalDate.parse(dateField.getText().trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.");
            return;
        }

        boolean allSaved = true;

        for (Map.Entry<Integer, JCheckBox> entry : presentMap.entrySet()) {
            int studentId = entry.getKey();
            String status = entry.getValue().isSelected() ? "Present" : "Absent";

            boolean saved = AttendanceDAO.saveAttendance(studentId, classId, date, status);
            AttendanceDAO.updateAttendancePercentage(studentId);
            if (!saved) allSaved = false;
        }

        if (allSaved) {
            JOptionPane.showMessageDialog(this, "✅ Attendance saved successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "❌ Some attendance entries failed.");
        }
    }
}
