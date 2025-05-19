package dao;

import db.DBConnection;
import models.Worker;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class WorkerDAO {

    public static boolean registerWorker(Worker worker) {
        String sql = "INSERT INTO workers (name, age, field, contact, salary, hire_date, id_card_no) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, worker.getName());
            stmt.setInt(2, worker.getAge());
            stmt.setString(3, worker.getField());
            stmt.setString(4, worker.getContact());
            stmt.setDouble(5, worker.getSalary());
            stmt.setDate(6, java.sql.Date.valueOf(worker.getHireDate()));
            stmt.setString(7, worker.getIdCard());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
