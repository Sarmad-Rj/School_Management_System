package ui;

import dao.WorkerDAO;
import models.Worker;
import theme.UITheme;

import javax.swing.*;
import java.awt.*;

public class RegisterWorkerForm extends JFrame {
    private JTextField nameField, ageField, fieldField, contactField, salaryField, hireDateField, idCardField;
    private JButton registerButton;

    public RegisterWorkerForm() {
        setTitle("Register Worker");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        UITheme.applyFrameDefaults(this);


        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = new JTextField(20);
        ageField = new JTextField(20);
        fieldField = new JTextField(20);
        contactField = new JTextField(20);
        salaryField = new JTextField(20);
        hireDateField = new JTextField(20);
        idCardField = new JTextField(20);
        registerButton = new JButton("Register");

        int y = 0;
        panelAdd(panel, gbc, new JLabel("Name:"), nameField, y++);
        panelAdd(panel, gbc, new JLabel("Age:"), ageField, y++);
        panelAdd(panel, gbc, new JLabel("Field (Work Type):"), fieldField, y++);
        panelAdd(panel, gbc, new JLabel("Contact:"), contactField, y++);
        panelAdd(panel, gbc, new JLabel("Monthly Salary:"), salaryField, y++);
        panelAdd(panel, gbc, new JLabel("Hire Date (YYYY-MM-DD):"), hireDateField, y++);
        panelAdd(panel, gbc, new JLabel("ID Card No:"), idCardField, y++);

        gbc.gridx = 1;
        gbc.gridy = y;
        panel.add(registerButton, gbc);

        add(panel);

        registerButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                int age = Integer.parseInt(ageField.getText().trim());
                String field = fieldField.getText().trim();
                String contact = contactField.getText().trim();
                double salary = Double.parseDouble(salaryField.getText().trim());
                String hireDate = hireDateField.getText().trim();
                String idCard = idCardField.getText().trim();

                if (name.isEmpty() || field.isEmpty() || contact.isEmpty() || idCard.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all required fields.");
                    return;
                }

                Worker worker = new Worker(name, age, field, contact, salary, hireDate, idCard);
                boolean success = WorkerDAO.registerWorker(worker);

                if (success) {
                    JOptionPane.showMessageDialog(this, "✅ Worker registered successfully!");
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Failed to register worker.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number format.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        setVisible(true);
    }

    private void panelAdd(JPanel panel, GridBagConstraints gbc, JLabel label, JComponent field, int y) {
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(label, gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void clearFields() {
        nameField.setText("");
        ageField.setText("");
        fieldField.setText("");
        contactField.setText("");
        salaryField.setText("");
        hireDateField.setText("");
        idCardField.setText("");
    }

    public static void main(String[] args) {
        new RegisterWorkerForm();
    }
}
