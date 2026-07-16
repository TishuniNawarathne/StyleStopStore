package Model;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Polymorphism + Abstraction: interface defines DAO contract
interface UserDataAccess {
    void create(User u) throws SQLException;
    User findByUsername(String username) throws SQLException;
    default String type() { return "user"; }
}

// Concrete DAO implements interface (Polymorphism)
public class UserDAO implements UserDataAccess {
    private void ensureTable(Connection c) throws SQLException {
        String createSql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "gender VARCHAR(16) NOT NULL, " +
                "tel VARCHAR(32) NOT NULL, " +
                "username VARCHAR(64) NOT NULL UNIQUE, " +
                "password_hash VARCHAR(64) NOT NULL, " +
                "role VARCHAR(16) NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        try (Statement s = c.createStatement()) {
            s.executeUpdate(createSql);
            try {
                s.executeUpdate("ALTER TABLE users ADD COLUMN role VARCHAR(16) NOT NULL DEFAULT 'clerk'");
            } catch (SQLException ignore) {}
        }
    }

    public void create(User u) throws SQLException {
        String insertSql = "INSERT INTO users (name, gender, tel, username, password_hash, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection()) {
            ensureTable(c);
            try (PreparedStatement ps = c.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, u.getName());
                ps.setString(2, u.getGender());
                ps.setString(3, u.getTel());
                ps.setString(4, u.getUsername());
                ps.setString(5, u.getPasswordHash());
                ps.setString(6, u.getRole());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        u.setId(keys.getInt(1));
                    }
                }
            }
        }
    }

    // Overloading: convenience create with fields
    public void create(String name, String gender, String tel, String username, String passwordHash, String role) throws SQLException {
        User u = new User(0, name, gender, tel, username, passwordHash, role);
        create(u);
    }

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT id, name, gender, tel, username, password_hash, role FROM users WHERE username = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        }
        return null;
    }

    // Abstraction: maps JDBC ResultSet to domain object
    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setName(rs.getString("name"));
        u.setGender(rs.getString("gender"));
        u.setTel(rs.getString("tel"));
        u.setUsername(rs.getString("username"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setRole(rs.getString("role"));
        return u;
    }
}
