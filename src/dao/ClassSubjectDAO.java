package dao;

import db.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClassSubjectDAO {

    public static List<Integer> getSubjectIdsForClass(int classId) {
        List<Integer> subjectIds = new ArrayList<>();
        String sql = "SELECT subject_id FROM class_subjects WHERE class_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                subjectIds.add(rs.getInt("subject_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjectIds;
    }

    public static boolean assignSubjectToClass(int classId, int subjectId) {
        String sql = "INSERT INTO class_subjects (class_id, subject_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setInt(2, subjectId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // handle unique constraint violation for duplicate assignment
            if (e.getErrorCode() == 1) { // Oracle unique constraint violation code
                System.out.println("Subject already assigned to this class");
            }
            else {
                e.printStackTrace();
            }
            return false;
        }
    }

    public static boolean removeSubjectFromClass(int classId, int subjectId) {
        String sql = "DELETE FROM class_subjects WHERE class_id = ? AND subject_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setInt(2, subjectId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
