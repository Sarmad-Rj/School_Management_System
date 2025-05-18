package dao;

import db.DBConnection;
import models.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentDAO {

    public static boolean registerStudent(Student student) {
        String sql = "INSERT INTO students (name, father_name, age, student_class, prev_school, guardian_contact, " +
                "admission_fee, admission_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getName());
            stmt.setString(2, student.getFatherName());
            stmt.setInt(3, student.getAge());
            stmt.setString(4, student.getStudentClass());
            stmt.setString(5, student.getPreviousSchool());
            stmt.setString(6, student.getGuardianContact());
            stmt.setDouble(7, student.getAdmissionFee());
            stmt.setString(8, student.getAdmissionDate());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
