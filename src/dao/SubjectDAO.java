package dao;

import models.Subject;
import db.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {
    public static List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT subject_id, subject_name FROM subjects ORDER BY subject_name")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Subject s = new Subject(rs.getInt("subject_id"), rs.getString("subject_name"));
                subjects.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    public static boolean addSubject(String subjectName) {
        String sql = "INSERT INTO subjects (subject_name) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subjectName);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
