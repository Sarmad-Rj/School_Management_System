package dao;

import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;

public class AttendanceDAO {

    public static boolean saveAttendance(int studentId, int classId, LocalDate date, String status) {
        String sql = "INSERT INTO attendance (student_id, class_id, date_attended, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, classId);
            stmt.setDate(3, Date.valueOf(date));
            stmt.setString(4, status);

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void updateAttendancePercentage(int studentId) {
        String sqlTotal = "SELECT COUNT(*) FROM attendance WHERE student_id = ?";
        String sqlPresent = "SELECT COUNT(*) FROM attendance WHERE student_id = ? AND status = 'Present'";
        String sqlUpdate = "UPDATE students SET attendance_percentage = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement totalStmt = conn.prepareStatement(sqlTotal);
             PreparedStatement presentStmt = conn.prepareStatement(sqlPresent);
             PreparedStatement updateStmt = conn.prepareStatement(sqlUpdate)) {

            totalStmt.setInt(1, studentId);
            ResultSet rsTotal = totalStmt.executeQuery();
            rsTotal.next();
            int total = rsTotal.getInt(1);

            presentStmt.setInt(1, studentId);
            ResultSet rsPresent = presentStmt.executeQuery();
            rsPresent.next();
            int present = rsPresent.getInt(1);

            double percentage = (total == 0) ? 0 : ((double) present / total) * 100;

            updateStmt.setDouble(1, percentage);
            updateStmt.setInt(2, studentId);
            updateStmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
