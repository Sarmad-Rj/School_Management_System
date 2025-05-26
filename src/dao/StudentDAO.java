package dao;

import db.DBConnection;
import models.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // Register a new student using class_id
    public static boolean registerStudent(Student student) {
        String sql = "INSERT INTO students (name, father_name, age, class_id, prev_school, guardian_contact, " +
                "admission_fee, admission_date, attendance_percentage, fee_paid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getName());
            stmt.setString(2, student.getFatherName());
            stmt.setInt(3, student.getAge());
            stmt.setInt(4, student.getClassId());
            stmt.setString(5, student.getPreviousSchool());
            stmt.setString(6, student.getGuardianContact());
            stmt.setDouble(7, student.getAdmissionFee());
            stmt.setString(8, student.getAdmissionDate());
            stmt.setDouble(9, student.getAttendancePercentage());
            stmt.setString(10, student.getFeePaid());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fetch all students from DB
    public static List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("father_name"),
                        rs.getInt("age"),
                        rs.getInt("class_id"),
                        rs.getString("prev_school"),
                        rs.getString("guardian_contact"),
                        rs.getDouble("admission_fee"),
                        rs.getString("admission_date"),
                        rs.getDouble("attendance_percentage"),
                        rs.getString("fee_paid")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Optional: Get students by class_id
    public static List<Student> getStudentsByClassId(int classId) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE class_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, classId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("father_name"),
                        rs.getInt("age"),
                        rs.getInt("class_id"),
                        rs.getString("prev_school"),
                        rs.getString("guardian_contact"),
                        rs.getDouble("admission_fee"),
                        rs.getString("admission_date"),
                        rs.getDouble("attendance_percentage"),
                        rs.getString("fee_paid")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
