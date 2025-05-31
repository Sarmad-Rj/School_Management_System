package dao;

import db.DBConnection;
import models.Teacher;
import models.TeacherAssignment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO {

    public static boolean registerTeacher(Teacher teacher) {
        String insertTeacher = "INSERT INTO teachers (name, subject, assigned_class, username, password, email, contact, cnic) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String insertUser = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt1 = conn.prepareStatement(insertTeacher);
             PreparedStatement stmt2 = conn.prepareStatement(insertUser)) {

            // Insert into teachers table
            stmt1.setString(1, teacher.getName());
            stmt1.setString(2, teacher.getSubject());
            stmt1.setString(3, teacher.getAssignedClass());
            stmt1.setString(4, teacher.getUsername());
            stmt1.setString(5, teacher.getPassword());
            stmt1.setString(6, teacher.getEmail());
            stmt1.setString(7, teacher.getContact());
            stmt1.setString(8, teacher.getCnic());
            stmt1.executeUpdate();

            // Insert into users table
            stmt2.setString(1, teacher.getUsername());
            stmt2.setString(2, teacher.getPassword());
            stmt2.setString(3, "Teacher");
            stmt2.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Teacher getTeacherByUsername(String username) {
        String sql = "SELECT * FROM teachers WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Teacher(
                        rs.getString("name"),
                        rs.getString("subject"),
                        rs.getString("assigned_class"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("contact"),
                        rs.getString("cnic")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getUsernameByEmail(String email) {
        String sql = "SELECT username FROM teachers WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<TeacherAssignment> getAssignmentsByUsername(String username) {
        List<TeacherAssignment> list = new ArrayList<>();

        String sql = """
        SELECT c.class_name, s.subject_name
        FROM teacher_assignments ta
        JOIN classes c ON ta.class_id = c.class_id
        JOIN subjects s ON ta.subject_id = s.subject_id
        WHERE ta.teacher_username = ?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String className = rs.getString("class_name");
                String subjectName = rs.getString("subject_name");
                list.add(new TeacherAssignment(className, subjectName));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<Teacher> getAllTeachers() {
        List<Teacher> list = new ArrayList<>();
        String sql = "SELECT * FROM teachers";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Teacher t = new Teacher(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("subject"),
                        rs.getString("assigned_class"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("contact"),
                        rs.getString("cnic")
                );
                list.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<TeacherAssignment> getAllDetailedAssignments() {
        List<TeacherAssignment> list = new ArrayList<>();

        String sql = """
        SELECT c.class_name || '-' || c.section AS class_name,
               s.subject_name,
               t.name AS teacher_name
        FROM teacher_assignments ta
        JOIN classes c ON ta.class_id = c.class_id
        JOIN subjects s ON ta.subject_id = s.subject_id
        JOIN teachers t ON ta.teacher_username = t.username
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String className = rs.getString("class_name");
                String subjectName = rs.getString("subject_name");
                String teacherName = rs.getString("teacher_name");

                list.add(new TeacherAssignment(className, subjectName + " - " + teacherName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

}