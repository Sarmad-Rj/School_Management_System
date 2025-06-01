package dao;

import db.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class StudentFeeDAO {

    public static List<Map<String, Object>> getStudentsByClassAndSection(int classId, String section) {
        List<Map<String, Object>> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE class_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, classId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> student = new HashMap<>();
                student.put("id", rs.getInt("id"));
                student.put("name", rs.getString("name"));
                student.put("roll_no", rs.getInt("id")); // assuming id is used as roll no
                student.put("admission_fee", rs.getDouble("admission_fee"));
                student.put("fee_paid", isFeePaidThisMonth(rs.getInt("id")));
                students.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }
    public static boolean isFeePaidThisMonth(int studentId) {
        LocalDate now = LocalDate.now();
        String sql = "SELECT COUNT(*) FROM student_fees WHERE student_id = ? AND month = ? AND year = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, now.getMonthValue());
            stmt.setInt(3, now.getYear());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    public static boolean markFeePaid(int studentId, int classId, String section, double amount) {
        LocalDate now = LocalDate.now();
        String sql = "INSERT INTO student_fees (student_id, class_id, section, month, year, amount) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, classId);
            stmt.setString(3, section);
            stmt.setInt(4, now.getMonthValue());
            stmt.setInt(5, now.getYear());
            stmt.setDouble(6, amount);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    public static String getPaidDateThisMonth(int studentId) {
        LocalDate now = LocalDate.now();
        String sql = "SELECT TO_CHAR(paid_date, 'DD-Mon-YYYY') FROM student_fees WHERE student_id = ? AND month = ? AND year = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, now.getMonthValue());
            stmt.setInt(3, now.getYear());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown";
    }
    public static List<String[]> getFeeHistory(int studentId) {
        List<String[]> history = new ArrayList<>();
        String sql = "SELECT month, year, amount, TO_CHAR(paid_date, 'DD-Mon-YYYY') FROM student_fees WHERE student_id = ? ORDER BY year DESC, month DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                history.add(new String[] {
                        rs.getString(1), // month
                        rs.getString(2), // year
                        rs.getString(3), // amount
                        rs.getString(4)  // paid_date
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return history;
    }
    public static Map<String, Double> getMonthlyFeeStats() {
        Map<String, Double> map = new HashMap<>();
        LocalDate now = LocalDate.now();

        String paidSql = """
        SELECT COUNT(*), SUM(amount) FROM student_fees
        WHERE month = ? AND year = ?
    """;

        String totalSql = "SELECT COUNT(*), SUM(admission_fee) FROM students";

        try (Connection conn = DBConnection.getConnection()) {
            // Paid data
            try (PreparedStatement stmt = conn.prepareStatement(paidSql)) {
                stmt.setInt(1, now.getMonthValue());
                stmt.setInt(2, now.getYear());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    map.put("paidCount", rs.getDouble(1));
                    map.put("paidAmount", rs.getDouble(2));
                }
            }

            // Total data
            try (PreparedStatement stmt = conn.prepareStatement(totalSql)) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    map.put("totalCount", rs.getDouble(1));
                    map.put("totalAmount", rs.getDouble(2));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }

}
