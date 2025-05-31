package ui;

import dao.ClassDAO;
import dao.SubjectDAO;
import dao.TeacherDAO;
import dao.TeacherAssignmentDAO;
import models.ClassItem;
import models.Subject;
import models.Teacher;
import theme.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;


public class RegisterTeacherForm extends JFrame {

    private JTextField nameField, usernameField, emailField, contactField, cnicField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JPanel checkboxPanel;
    private Map<JCheckBox, int[]> assignmentMap = new HashMap<>();

    public RegisterTeacherForm() {
        setTitle("Register Teacher");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        UITheme.applyFrameDefaults(this);


        JPanel panel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = new JTextField(20);
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        emailField = new JTextField(20);
        contactField = new JTextField(20);
        registerButton = new JButton("Register");
        cnicField = new JTextField(20);

        int y = 0;
        panelAdd(formPanel, gbc, new JLabel("Name:"), nameField, y++);
        panelAdd(formPanel, gbc, new JLabel("Username:"), usernameField, y++);
        panelAdd(formPanel, gbc, new JLabel("Password:"), passwordField, y++);
        panelAdd(formPanel, gbc, new JLabel("Email:"), emailField, y++);
        panelAdd(formPanel, gbc, new JLabel("Contact:"), contactField, y++);
        panelAdd(formPanel, gbc, new JLabel("CNIC:"), cnicField, y++);

        gbc.gridy = y;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        formPanel.add(new JLabel("Assign Subjects:"), gbc);

        gbc.gridy++;

        checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(checkboxPanel);
        scrollPane.setPreferredSize(new Dimension(500, 200));
        formPanel.add(scrollPane, gbc);

        gbc.gridy++;
        formPanel.add(registerButton, gbc);
        panel.add(formPanel, BorderLayout.CENTER);
        add(panel);

        loadClassSubjects();

        registerButton.addActionListener(e -> registerTeacher());
        setVisible(true);
    }

    private void panelAdd(JPanel panel, GridBagConstraints gbc, JLabel label, JComponent field, int y) {
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(label, gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void loadClassSubjects() {
        checkboxPanel.removeAll();
        assignmentMap.clear();
        List<ClassItem> classes = ClassDAO.getAllClasses();
        List<Subject> subjects = SubjectDAO.getAllSubjects();

        for (ClassItem c : classes) {
            for (Subject s : subjects) {
                if (SubjectDAO.isSubjectAssignedToClass(c.getId(), s.getId())) {
                    String label = c.toString() + " → " + s.getName();
                    JCheckBox checkBox = new JCheckBox(label);
                    checkboxPanel.add(checkBox);
                    assignmentMap.put(checkBox, new int[]{c.getId(), s.getId()});
                }
            }
        }

        checkboxPanel.revalidate();
        checkboxPanel.repaint();
    }

    private void registerTeacher() {
        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText().trim();
        String contact = contactField.getText().trim();
        String cnic = cnicField.getText().trim();


        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.");
            return;
        }

        Teacher teacher = new Teacher(name, "", "", username, password, email, contact, cnic);

        if (TeacherDAO.registerTeacher(teacher)) {
            List<int[]> assignments = new ArrayList<>();
            for (Map.Entry<JCheckBox, int[]> entry : assignmentMap.entrySet()) {
                if (entry.getKey().isSelected()) {
                    assignments.add(entry.getValue());
                }
            }

            TeacherAssignmentDAO.assignSubjectsToTeacher(username, assignments);
            JOptionPane.showMessageDialog(this, "✅ Teacher registered and assigned!");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Failed to register teacher.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterTeacherForm::new);
    }
}