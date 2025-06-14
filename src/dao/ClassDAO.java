package dao;

import models.ClassItem;  // We'll create a simple model for ClassItem
import db.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClassDAO {

    public static List<ClassItem> getAllClasses() {
        List<ClassItem> classes = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT class_id, class_name, section FROM classes ORDER BY class_name")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ClassItem c = new ClassItem(rs.getInt("class_id"), rs.getString("class_name"), rs.getString("section"));
                classes.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }

    public static boolean addClass(String className, String section) {
        String sql = "INSERT INTO classes (class_name, section) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, className);
            ps.setString(2, section);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean assignSubjectToClass(int classId, String subjectName) {
        try (Connection conn = DBConnection.getConnection()) {
            String getSubjectIdSQL = "SELECT subject_id FROM subjects WHERE subject_name = ?";
            PreparedStatement ps1 = conn.prepareStatement(getSubjectIdSQL);
            ps1.setString(1, subjectName);
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                int subjectId = rs.getInt("subject_id");

                String insertSQL = "INSERT INTO class_subjects (class_id, subject_id) VALUES (?, ?)";
                PreparedStatement ps2 = conn.prepareStatement(insertSQL);
                ps2.setInt(1, classId);
                ps2.setInt(2, subjectId);

                return ps2.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<String> getAllSubjectNames() {
        List<String> subjects = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT subject_name FROM subjects";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                subjects.add(rs.getString("subject_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    public static ClassItem getClassById(int classId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM classes WHERE class_id = ?")) {
            stmt.setInt(1, classId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ClassItem(
                        rs.getInt("class_id"),
                        rs.getString("class_name"),
                        rs.getString("section")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[] getAllClassNames() {
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT class_name FROM classes ORDER BY class_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(rs.getString("class_name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list.toArray(new String[0]);
    }

    public static int getClassIdByName(String name) {
        String sql = "SELECT class_id FROM classes WHERE class_name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("class_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1; // not found
    }
}
