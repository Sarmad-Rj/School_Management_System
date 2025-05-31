package ui;

import dao.AttendanceDAO;
import dao.StudentDAO;
import models.Student;

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
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top date selector
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Date (yyyy-MM-dd):"));

        dateField = new JTextField(10);
        dateField.setText(LocalDate.now().toString()); // Default to today's date
        dateField.setToolTipText("Format: YYYY-MM-DD");
        topPanel.add(dateField);
        add(topPanel, BorderLayout.NORTH);

        // Center: student list with checkboxes
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        List<Student> students = StudentDAO.getStudentsByClassId(classId);
        for (Student s : students) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel nameLabel = new JLabel(s.getName());
            JCheckBox presentBox = new JCheckBox("Present");

            row.add(nameLabel);
            row.add(presentBox);
            centerPanel.add(row);

            presentMap.put(s.getId(), presentBox);
        }

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Submit button
        submitButton.addActionListener(e -> handleSubmit());
        add(submitButton, BorderLayout.SOUTH);
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
