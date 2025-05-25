package dao;

import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class TeacherAssignmentDAO {

    public static void assignSubjectsToTeacher(String username, List<int[]> assignments) {
        String sql = "INSERT INTO teacher_assignments (teacher_username, class_id, subject_id) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int[] pair : assignments) {
                stmt.setString(1, username);
                stmt.setInt(2, pair[0]); // class_id
                stmt.setInt(3, pair[1]); // subject_id
                stmt.addBatch();
            }

            stmt.executeBatch();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
