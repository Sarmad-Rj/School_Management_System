package dao;

import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ClassSubjectDAO {

    public static void assignSubjectToClass(int classId, int subjectId) {
        String sql = "MERGE INTO class_subjects cs " +
                "USING dual ON (cs.class_id = ? AND cs.subject_id = ?) " +
                "WHEN NOT MATCHED THEN INSERT (class_id, subject_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classId);
            stmt.setInt(2, subjectId);
            stmt.setInt(3, classId);
            stmt.setInt(4, subjectId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeSubjectFromClass(int classId, int subjectId) {
        String sql = "DELETE FROM class_subjects WHERE class_id = ? AND subject_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classId);
            stmt.setInt(2, subjectId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Set<Integer> getSubjectIdsByClass(int classId) {
        Set<Integer> subjectIds = new HashSet<>();
        String sql = "SELECT subject_id FROM class_subjects WHERE class_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                subjectIds.add(rs.getInt("subject_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjectIds;
    }

    public static void removeAllSubjectsFromClass(int classId) {
        String sql = "DELETE FROM class_subjects WHERE class_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
