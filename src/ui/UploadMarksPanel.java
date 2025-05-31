package ui;

import dao.MarkDAO;
import dao.StudentDAO;
import models.Student;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class UploadMarksPanel extends JPanel {
    private JComboBox<String> termComboBox;
    private JPanel studentMarksPanel;
    private JButton submitButton;

    private final int classId;
    private final int subjectId;

    private final Map<Integer, JTextField> markFields = new HashMap<>();

    public UploadMarksPanel(int classId, int subjectId) {
        this.classId = classId;
        this.subjectId = subjectId;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Term selection
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select Term:"));
        termComboBox = new JComboBox<>(new String[]{"First", "Second", "Final"});
        topPanel.add(termComboBox);
        add(topPanel, BorderLayout.NORTH);

        // Students list with input fields
        studentMarksPanel = new JPanel();
        studentMarksPanel.setLayout(new BoxLayout(studentMarksPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(studentMarksPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Submit button
        submitButton = new JButton("Submit Marks");
        submitButton.addActionListener(e -> handleSubmit());
        add(submitButton, BorderLayout.SOUTH);

        loadStudentList();
    }

    private void loadStudentList() {
        List<Student> students = StudentDAO.getStudentsByClassId(classId);
        for (Student s : students) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel nameLabel = new JLabel(s.getName());
            JTextField markField = new JTextField(5);
            markFields.put(s.getId(), markField);

            row.add(nameLabel);
            row.add(markField);
            studentMarksPanel.add(row);
        }
    }

    private void handleSubmit() {
        String term = (String) termComboBox.getSelectedItem();
        boolean allValid = true;

        for (Map.Entry<Integer, JTextField> entry : markFields.entrySet()) {
            int studentId = entry.getKey();
            String markText = entry.getValue().getText().trim();

            if (markText.isEmpty()) continue;

            try {
                int marks = Integer.parseInt(markText);
                boolean saved = MarkDAO.saveMark(studentId, subjectId, classId, term, marks);
                if (!saved) allValid = false;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid marks entered for a student. Please enter numbers only.");
                return;
            }
        }

        if (allValid) {
            JOptionPane.showMessageDialog(this, "✅ Marks submitted successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "❌ Some marks failed to save. Check console for errors.");
        }
    }
}
