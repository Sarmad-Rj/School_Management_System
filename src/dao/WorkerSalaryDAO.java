package dao;

import db.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class WorkerSalaryDAO {

    public static void ensureCurrentMonthEntries() {
        String sql = "SELECT worker_id FROM workers";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            LocalDate now = LocalDate.now();
            int month = now.getMonthValue();
            int year = now.getYear();

            while (rs.next()) {
                int workerId = rs.getInt("worker_id");
                if (!existsForMonth(workerId, month, year)) {
                    insertDefault(workerId, month, year);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean existsForMonth(int workerId, int month, int year) {
        String sql = "SELECT COUNT(*) FROM worker_salaries WHERE worker_id = ? AND month = ? AND year = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, workerId);
            stmt.setInt(2, month);
            stmt.setInt(3, year);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void insertDefault(int workerId, int month, int year) {
        String sql = "INSERT INTO worker_salaries (worker_id, month, year, paid_status) VALUES (?, ?, ?, 'No')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, workerId);
            stmt.setInt(2, month);
            stmt.setInt(3, year);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Map<String, String>> getCurrentWorkerSalaryStatus() {
        List<Map<String, String>> list = new ArrayList<>();
        LocalDate now = LocalDate.now();

        String sql = """
        SELECT w.worker_id, w.name, w.contact, s.paid_status, NULL AS paid_date
        FROM workers w
        JOIN worker_salaries s ON w.worker_id = s.worker_id
        WHERE s.month = ? AND s.year = ?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, now.getMonthValue());
            stmt.setInt(2, now.getYear());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, String> map = new HashMap<>();
                map.put("id", rs.getString("worker_id"));
                map.put("name", rs.getString("name"));
                map.put("contact", rs.getString("contact"));
                map.put("paid", rs.getString("paid_status"));
                map.put("date", ""); // If you want to track paid_date, add that to the table
                list.add(map);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void markAsPaid(int workerId) {
        LocalDate now = LocalDate.now();
        String sql = """
        UPDATE worker_salaries
        SET paid_status = 'Yes'
        WHERE worker_id = ? AND month = ? AND year = ?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, workerId);
            stmt.setInt(2, now.getMonthValue());
            stmt.setInt(3, now.getYear());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
