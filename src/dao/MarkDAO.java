package dao;

import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MarkDAO {

    public static boolean saveMark(int studentId, int subjectId, int classId, String term, int marks) {
        String sql = "INSERT INTO marks (student_id, subject_id, class_id, exam_term, marks) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, subjectId);
            stmt.setInt(3, classId);
            stmt.setString(4, term);
            stmt.setInt(5, marks);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
