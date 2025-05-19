// dao/PasswordResetDAO.java
package dao;

import java.sql.*;
import java.time.LocalDateTime;

public class PasswordResetDAO {
    private Connection conn;

    public PasswordResetDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean isTokenValid(String email, String token) throws SQLException {
        String sql = "SELECT expiry_time FROM password_resets WHERE email = ? AND token = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, token);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp expiryTs = rs.getTimestamp("expiry_time");
                    return expiryTs.toLocalDateTime().isAfter(LocalDateTime.now());
                }
                return false;
            }
        }
    }

    public void deleteToken(String email, String token) throws SQLException {
        String sql = "DELETE FROM password_resets WHERE email = ? AND token = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, token);
            ps.executeUpdate();
        }
    }
}
