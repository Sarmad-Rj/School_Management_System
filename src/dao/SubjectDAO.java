package dao;

import models.Subject;
import db.DBConnection;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class SubjectDAO {
    public boolean addSubject(String subjectName) {
        String checkSql = "SELECT COUNT(*) FROM subjects WHERE LOWER(subject_name) = LOWER(?)";
        String insertSql = "INSERT INTO subjects (subject_name) VALUES (?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, subjectName);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Subject already exists.");
                return false; // Don't insert duplicate
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, subjectName);
                insertStmt.executeUpdate();
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Subject> getAllSubjects() {
        List<Subject> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM subjects";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new Subject(rs.getInt("subject_id"), rs.getString("subject_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Integer> getSelectedSubjectIds(JPanel checkboxPanel) {
        List<Integer> ids = new ArrayList<>();
        for (Component comp : checkboxPanel.getComponents()) {
            if (comp instanceof JCheckBox checkbox && checkbox.isSelected()) {
                ids.add(Integer.parseInt(checkbox.getActionCommand()));
            }
        }
        return ids;
    }

    public static boolean assignSubjectsToClass(int classId, List<Integer> subjectIds) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO class_subjects (class_id, subject_id) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            for (int subjectId : subjectIds) {
                stmt.setInt(1, classId);
                stmt.setInt(2, subjectId);
                stmt.addBatch();
            }
            stmt.executeBatch();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isSubjectAssignedToClass(int classId, int subjectId) {
        String sql = "SELECT 1 FROM class_subjects WHERE class_id = ? AND subject_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classId);
            stmt.setInt(2, subjectId);
            return stmt.executeQuery().next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Subject getSubjectById(int subjectId) {
        String sql = "SELECT * FROM subjects WHERE subject_id = ?";
        try (Connection conn = db.DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, subjectId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Subject(
                        rs.getInt("subject_id"),
                        rs.getString("subject_name")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
