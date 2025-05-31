package dao;

import db.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class TeacherSalaryDAO {

    public static void ensureCurrentMonthEntries() {
        String sql = "SELECT id FROM teachers";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            LocalDate now = LocalDate.now();
            int month = now.getMonthValue();
            int year = now.getYear();

            while (rs.next()) {
                int teacherId = rs.getInt("id");
                if (!existsForMonth(teacherId, month, year)) {
                    insertDefault(teacherId, month, year);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean existsForMonth(int teacherId, int month, int year) {
        String sql = "SELECT COUNT(*) FROM teacher_salaries WHERE teacher_id = ? AND month = ? AND year = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, teacherId);
            stmt.setInt(2, month);
            stmt.setInt(3, year);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void insertDefault(int teacherId, int month, int year) {
        String sql = "INSERT INTO teacher_salaries (teacher_id, month, year, paid_status) VALUES (?, ?, ?, 'No')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, teacherId);
            stmt.setInt(2, month);
            stmt.setInt(3, year);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, String> getCurrentSalaryStatus() {
        Map<Integer, String> map = new HashMap<>();
        String sql = "SELECT teacher_id, paid_status FROM teacher_salaries WHERE month = ? AND year = ?";

        LocalDate now = LocalDate.now();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, now.getMonthValue());
            stmt.setInt(2, now.getYear());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                map.put(rs.getInt("teacher_id"), rs.getString("paid_status")); // Only "Yes" or "No"
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static void markAsPaid(int teacherId) {
        LocalDate now = LocalDate.now();
        String sql = "UPDATE teacher_salaries SET paid_status = 'Yes', paid_date = SYSDATE WHERE teacher_id = ? AND month = ? AND year = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, teacherId);
            stmt.setInt(2, now.getMonthValue());
            stmt.setInt(3, now.getYear());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, String> getPaidDates() {
        Map<Integer, String> map = new HashMap<>();
        String sql = "SELECT teacher_id, TO_CHAR(paid_date, 'DD-Mon-YYYY') AS paid_date FROM teacher_salaries WHERE month = ? AND year = ?";

        LocalDate now = LocalDate.now();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, now.getMonthValue());
            stmt.setInt(2, now.getYear());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                map.put(rs.getInt("teacher_id"), rs.getString("paid_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }

}
