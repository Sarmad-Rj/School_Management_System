package ui;

import dao.MarkDAO;
import dao.StudentDAO;
import models.Student;
import theme.UITheme;

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
        setBackground(UITheme.LIGHT_GRAY); // Maintain consistency

        // Top Section - Term Selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(UITheme.WHITE);
        topPanel.add(new JLabel("Select Term:"));

        termComboBox = new JComboBox<>(new String[]{"First", "Second", "Final"});
        UITheme.styleComboBox(termComboBox); // Apply custom styling
        topPanel.add(termComboBox);

        add(topPanel, BorderLayout.NORTH);

        // Center Section - Organized Student Marks Input
        JPanel centerPanel = new JPanel(new GridBagLayout());
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
            JTextField markField = new JTextField(5);
            centerPanel.add(markField, gbc);

            markFields.put(s.getId(), markField);
            row++;
        }

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Section - Submit Button
        submitButton = new JButton("Submit Marks");
        UITheme.stylePrimaryButton(submitButton);
        submitButton.addActionListener(e -> handleSubmit());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(UITheme.LIGHT_GRAY);
        bottomPanel.add(submitButton);
        add(bottomPanel, BorderLayout.SOUTH);
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
