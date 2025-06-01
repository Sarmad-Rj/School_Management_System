package ui;

import dao.StudentFeeDAO;
import dao.TeacherSalaryDAO;
import dao.WorkerSalaryDAO;
import theme.UITheme;
import dao.ClassDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;
import java.util.List;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;



public class FinanceDashboardFrame extends JFrame implements ActionListener {

    private final JPanel cardPanel;


    public FinanceDashboardFrame() {
        setTitle("Financial Record");
        setSize(1000, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        UITheme.applyFrameDefaults(this);
        setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = UITheme.createTitleLabel("Financial Record Dashboard");
        JPanel headerPanel = UITheme.createHeaderPanel(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Sidebar
        SidebarMenuPanel sidebar = new SidebarMenuPanel(this);
        sidebar.setPreferredSize(new Dimension(200, 0));
        add(sidebar, BorderLayout.WEST);

        // Card panel (center)
        cardPanel = new JPanel(new CardLayout());
        cardPanel.setBackground(UITheme.LIGHT_GRAY);

        // Add placeholder panels
        cardPanel.add(createStudentFeePanel(), "incomePanel");
        cardPanel.add(createExpensePanel(), "expensePanel");


        add(cardPanel, BorderLayout.CENTER);
    }

    private JPanel createStudentFeePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.LIGHT_GRAY);

        JComboBox<String> classCombo = new JComboBox<>(ClassDAO.getAllClassNames());
        JTextField sectionField = new JTextField(5);
        classCombo.setSelectedItem("Nursery");
        sectionField.setText("A");

        JButton loadButton = new JButton("Load Students");
        UITheme.stylePrimaryButton(loadButton);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(UITheme.LIGHT_GRAY);
        topPanel.add(new JLabel("Class:"));
        topPanel.add(classCombo);
        topPanel.add(new JLabel("Section:"));
        topPanel.add(sectionField);
        topPanel.add(loadButton);

        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setBackground(UITheme.LIGHT_GRAY);
        topContainer.add(topPanel);
        panel.add(topContainer, BorderLayout.NORTH);

        // Card layout center panel
        JPanel centerPanel = new JPanel(new CardLayout());
        panel.add(centerPanel, BorderLayout.CENTER);

        // Chart setup
        Map<String, Double> stats = StudentFeeDAO.getMonthlyFeeStats();
        double paid = stats.getOrDefault("paidAmount", 0.0);
        double unpaid = stats.getOrDefault("totalAmount", 0.0) - paid;
        FeeDonutChart chart = new FeeDonutChart(paid, unpaid);

        JLabel donutSummary = new JLabel(String.format("This Month: Collected Rs. %.0f out of Rs. %.0f", paid, paid + unpaid));
        donutSummary.setFont(new Font("Segoe UI", Font.BOLD, 16));
        donutSummary.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBackground(UITheme.LIGHT_GRAY);
        chartContainer.add(chart, BorderLayout.CENTER);
        chartContainer.add(donutSummary, BorderLayout.SOUTH);
        centerPanel.add(chartContainer, "chart");

        // Table setup
        String[] columns = {"ID", "Name", "Roll No", "Amount", "Paid This Month", "Mark Paid"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 5;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 5 ? Boolean.class : String.class;
            }
        };
        JTable table = new JTable(model);
        JScrollPane tableScroll = new JScrollPane(table);
        centerPanel.add(tableScroll, "table");

        // Table double click fee history
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        int studentId = Integer.parseInt(model.getValueAt(row, 0).toString());
                        showFeeHistory(studentId);
                    }
                }
            }
        });

        JButton payButton = new JButton("Mark Selected as Paid");
        UITheme.stylePrimaryButton(payButton);
        JButton exportButton = new JButton("Export Summary to PDF");
        UITheme.stylePrimaryButton(exportButton);

        JLabel summaryLabel = new JLabel("Summary: ");
        summaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        summaryPanel.setBackground(UITheme.LIGHT_GRAY);
        summaryPanel.add(summaryLabel);

        JPanel payPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        payPanel.setBackground(UITheme.LIGHT_GRAY);
        payPanel.add(payButton);
        payPanel.add(exportButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(UITheme.LIGHT_GRAY);
        bottomPanel.add(summaryPanel, BorderLayout.WEST);
        bottomPanel.add(payPanel, BorderLayout.EAST);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        payButton.setVisible(false);
        exportButton.setVisible(true);

        // Load button logic
        loadButton.addActionListener(e -> {
            ((CardLayout) centerPanel.getLayout()).show(centerPanel, "table");
            payButton.setVisible(true);
            exportButton.setVisible(false);
            model.setRowCount(0);
            String selectedClass = (String) classCombo.getSelectedItem();
            String section = sectionField.getText().trim();
            int classId = ClassDAO.getClassIdByName(selectedClass);

            List<Map<String, Object>> students = StudentFeeDAO.getStudentsByClassAndSection(classId, section);
            for (Map<String, Object> s : students) {
                model.addRow(new Object[]{
                        s.get("id"),
                        s.get("name"),
                        s.get("roll_no"),
                        s.get("admission_fee"),
                        ((boolean) s.get("fee_paid")) ? "Yes" : "No",
                        false
                });
            }

            int total = students.size();
            int paidCount = (int) students.stream().filter(s -> (boolean) s.get("fee_paid")).count();
            int unpaidCount = total - paidCount;
            double totalPaid = students.stream()
                    .filter(s -> (boolean) s.get("fee_paid"))
                    .mapToDouble(s -> (double) s.get("admission_fee"))
                    .sum();

            summaryLabel.setText("Summary: Paid " + paidCount + " / " + total +
                    " | Unpaid: " + unpaidCount + " | Total Collected: Rs. " + totalPaid);
        });

        payButton.addActionListener(e -> {
            int classId = ClassDAO.getClassIdByName((String) classCombo.getSelectedItem());
            String section = sectionField.getText().trim();
            int marked = 0;

            for (int i = 0; i < model.getRowCount(); i++) {
                boolean checked = (boolean) model.getValueAt(i, 5);
                String paidStatus = (String) model.getValueAt(i, 4);
                if (checked && paidStatus.equals("No")) {
                    int studentId = Integer.parseInt(model.getValueAt(i, 0).toString());
                    double amount = Double.parseDouble(model.getValueAt(i, 3).toString());
                    if (StudentFeeDAO.markFeePaid(studentId, classId, section, amount)) {
                        model.setValueAt("Yes", i, 4);
                        marked++;
                    }
                }
            }

            JOptionPane.showMessageDialog(this, marked + " student(s) marked as fee paid.");
        });

        exportButton.addActionListener(e -> {
            pdf.ExportStudentFeePDF.exportSummary(summaryLabel.getText(), panel);
        });


        // Show chart on first load
        SwingUtilities.invokeLater(() -> {
            ((CardLayout) centerPanel.getLayout()).show(centerPanel, "chart");
            payButton.setVisible(false);
            exportButton.setVisible(true);
        });

        return panel;
    }

    class FeeDonutChart extends JPanel {
        private double paid, unpaid;

        public FeeDonutChart(double paid, double unpaid) {
            this.paid = paid;
            this.unpaid = unpaid;
            setPreferredSize(new Dimension(300, 300));
            setBackground(UITheme.LIGHT_GRAY);
        }

        public void setData(double paid, double unpaid) {
            this.paid = paid;
            this.unpaid = unpaid;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            int diameter = 200;
            int x = getWidth() / 2 - diameter / 2;
            int y = getHeight() / 2 - diameter / 2;

            double total = paid + unpaid;
            int paidAngle = (int) Math.round((paid / total) * 360);

            g2.setColor(new Color(76, 175, 80));
            g2.fillArc(x, y, diameter, diameter, 90, -paidAngle);

            g2.setColor(Color.LIGHT_GRAY);
            g2.fillArc(x, y, diameter, diameter, 90 - paidAngle, -(360 - paidAngle));

            g2.setColor(getBackground());
            g2.fillOval(x + 50, y + 50, diameter - 100, diameter - 100);

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            String label = String.format("Rs. %.0f / %.0f", paid, paid + unpaid);
            int textWidth = g2.getFontMetrics().stringWidth(label);
            g2.drawString(label, getWidth() / 2 - textWidth / 2, getHeight() / 2);
        }
    }

//    private JPanel createStudentFeePanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.setBackground(UITheme.LIGHT_GRAY);
//
//        Map<String, Double> stats = StudentFeeDAO.getMonthlyFeeStats();
//        double paid = stats.getOrDefault("paidAmount", 0.0);
//        double unpaid = stats.getOrDefault("totalAmount", 0.0) - paid;
//
//        FeeDonutChart chart = new FeeDonutChart(paid, unpaid);
//        panel.add(chart, BorderLayout.CENTER);
//
//        JLabel summary = new JLabel(String.format("This Month: Collected Rs. %.0f out of Rs. %.0f",
//                paid, paid + unpaid));
//        summary.setFont(new Font("Segoe UI", Font.BOLD, 16));
//        summary.setHorizontalAlignment(SwingConstants.CENTER);
//        panel.add(summary, BorderLayout.SOUTH);
//
//        return panel;
//    }

    private JPanel createPlaceholderPanel(String message) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UITheme.LIGHT_GRAY);
        JLabel label = new JLabel(message);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(label);
        return panel;
    }

    private JPanel createExpensePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.LIGHT_GRAY);
//        C:\Users\HP\OneDrive\Desktop\School_Management_System\images\Salary.png
        LocalDate now = LocalDate.now();
        String monthYear = now.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + now.getYear();
        JLabel dateLabel = UITheme.createIconLabel(
                "  Salaries for: " + monthYear,
                "C:\\Users\\HP\\OneDrive\\Desktop\\School_Management_System\\images\\Salary.png",
                24
        );

        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dateLabel.setBorder(new EmptyBorder(10, 10, 10, 10));

        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dateLabel.setBorder(new EmptyBorder(10, 10, 10, 10));


        // Top selector (Teacher/Worker)
        JRadioButton teacherRadio = new JRadioButton("Teacher");
        JRadioButton workerRadio = new JRadioButton("Worker");
        teacherRadio.setSelected(true);

        ButtonGroup group = new ButtonGroup();
        group.add(teacherRadio);
        group.add(workerRadio);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(UITheme.LIGHT_GRAY);
        topPanel.add(new JLabel("View Salaries for:"));
        topPanel.add(teacherRadio);
        topPanel.add(workerRadio);
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setBackground(UITheme.LIGHT_GRAY);
        topContainer.add(dateLabel);
        topContainer.add(topPanel);
        panel.add(topContainer, BorderLayout.NORTH);


        // Table setup
        String[] columns = {"ID", "Name", "Contact", "Paid?", "Paid Date", "Mark Paid"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) {
                return column == 5;
            }

            @Override public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 5 ? Boolean.class : String.class;
            }
        };

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Summary
        JLabel summaryLabel = new JLabel("Summary: ");
        summaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        summaryPanel.setBackground(UITheme.LIGHT_GRAY);
        summaryPanel.add(summaryLabel);

        // Pay button
        JButton payButton = new JButton("Mark Selected as Paid");
        UITheme.stylePrimaryButton(payButton);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(UITheme.LIGHT_GRAY);
        bottomPanel.add(payButton);

        // Combine bottom
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(UITheme.LIGHT_GRAY);
        southPanel.add(summaryPanel, BorderLayout.WEST);
        southPanel.add(bottomPanel, BorderLayout.EAST);
        panel.add(southPanel, BorderLayout.SOUTH);

        // Load salaries
        Runnable loadSalaries = () -> {
            model.setRowCount(0);
            List<Map<String, String>> salaries = teacherRadio.isSelected()
                    ? TeacherSalaryDAO.getCurrentTeacherSalaryStatus()
                    : WorkerSalaryDAO.getCurrentWorkerSalaryStatus();

            int paid = 0;
            for (Map<String, String> row : salaries) {
                boolean isPaid = row.get("paid").equalsIgnoreCase("Yes");
                if (isPaid) paid++;
                model.addRow(new Object[]{
                        row.get("id"),
                        row.get("name"),
                        row.get("contact"),
                        isPaid ? "Yes" : "No",
                        row.get("date"),
                        false
                });
            }

            int total = salaries.size();
            summaryLabel.setText("Summary: Paid " + paid + " / " + total +
                    " | Unpaid: " + (total - paid));
        };

        // Action listeners
        teacherRadio.addActionListener(e -> loadSalaries.run());
        workerRadio.addActionListener(e -> loadSalaries.run());

        payButton.addActionListener(e -> {
            int paid = 0;
            for (int i = 0; i < model.getRowCount(); i++) {
                boolean checked = (boolean) model.getValueAt(i, 5);
                String status = (String) model.getValueAt(i, 3);
                if (checked && status.equals("No")) {
                    int id = Integer.parseInt(model.getValueAt(i, 0).toString());
                    if (teacherRadio.isSelected()) {
                        TeacherSalaryDAO.markAsPaid(id);
                    } else {
                        WorkerSalaryDAO.markAsPaid(id);
                    }
                    paid++;
                }
            }

            JOptionPane.showMessageDialog(this, paid + " staff marked as paid.");
            loadSalaries.run();
        });

        // Initial load
        SwingUtilities.invokeLater(loadSalaries);

        return panel;
    }

    private void showFeeHistory(int studentId) {
        List<String[]> history = StudentFeeDAO.getFeeHistory(studentId);

        JFrame frame = new JFrame("Fee History");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(this);

        String[] columns = {"Month", "Year", "Amount", "Paid Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (String[] row : history) {
            model.addRow(row);
        }

        JTable table = new JTable(model);
        frame.add(new JScrollPane(table));
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        CardLayout cl = (CardLayout) cardPanel.getLayout();

        switch (command) {
            case "Income" -> cl.show(cardPanel, "incomePanel");
            case "Expenses" -> cl.show(cardPanel, "expensePanel");
            case "Back" -> {
                dispose();
                new VPDashboard().setVisible(true);
            }
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FinanceDashboardFrame().setVisible(true));
    }
}
