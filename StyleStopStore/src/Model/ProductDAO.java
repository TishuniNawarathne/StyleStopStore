package Model;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Polymorphism + Abstraction: interface defines DAO contract
interface ProductDataAccess {
    void create(Product p) throws SQLException;
    Product findById(long id) throws SQLException;
    Product findById(int id) throws SQLException;
    List<Product> findAll() throws SQLException;
    List<Product> findByNameLike(String q) throws SQLException;
    List<Product> findLowStock(int threshold) throws SQLException;
    void update(Product p) throws SQLException;
    void delete(int id) throws SQLException;
    void changeId(int oldId, int newId) throws SQLException;
    void resequenceIds() throws SQLException;
}

// Inheritance + Abstraction: base DAO with common behavior
abstract class BaseDAO<T> {
    protected String entityName() { return "Entity"; }
}

// Concrete DAO implements interface (Polymorphism) and overrides base (Inheritance)
public class ProductDAO extends BaseDAO<Product> implements ProductDataAccess {
    @Override
    protected String entityName() { return "Product"; }

    // Create with entity (standard)
    public void create(Product p) throws SQLException {
        String sql = "INSERT INTO products (name, category, size, price, stock, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getCategory());
            ps.setString(3, p.getSize());
            ps.setDouble(4, p.getPrice());
            ps.setInt(5, p.getStock());
            ps.setString(6, p.getStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    p.setId(keys.getInt(1));
                }
            }
        }
    }

    // Overloading: convenience create with fields
    public void create(String name, String category, String size, double price, int stock, String status) throws SQLException {
        Product p = new Product(0, name, category, size, price, stock, status);
        create(p);
    }

    // Overloading: find by long delegates to int
    public Product findById(long id) throws SQLException {
        return findById((int) id);
    }

    // Find by int id
    public Product findById(int id) throws SQLException {
        String sql = "SELECT id, name, category, size, price, stock, status FROM products WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        }
        return null;
    }

    public List<Product> findAll() throws SQLException {
        String sql = "SELECT id, name, category, size, price, stock, status FROM products";
        List<Product> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    public List<Product> findByNameLike(String q) throws SQLException {
        String sql = "SELECT id, name, category, size, price, stock, status FROM products WHERE name LIKE ?";
        List<Product> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + q + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }
        return list;
    }

    public List<Product> findLowStock(int threshold) throws SQLException {
        String sql = "SELECT id, name, category, size, price, stock, status FROM products WHERE stock <= ?";
        List<Product> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }
        return list;
    }

    public void update(Product p) throws SQLException {
        String sql = "UPDATE products SET name = ?, category = ?, size = ?, price = ?, stock = ?, status = ? WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getCategory());
            ps.setString(3, p.getSize());
            ps.setDouble(4, p.getPrice());
            ps.setInt(5, p.getStock());
            ps.setString(6, p.getStatus());
            ps.setInt(7, p.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void changeId(int oldId, int newId) throws SQLException {
        String sql = "UPDATE products SET id = ? WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, newId);
            ps.setInt(2, oldId);
            ps.executeUpdate();
        }
    }

    public void resequenceIds() throws SQLException {
        List<Product> list = findAll();
        list.sort((p1, p2) -> Integer.compare(p1.getId(), p2.getId()));
        
        int newId = 1;
        for (Product p : list) {
            if (p.getId() != newId) {
                changeId(p.getId(), newId);
            }
            newId++;
        }
        
        String sql = "ALTER TABLE products AUTO_INCREMENT = 1";
        try (Connection c = DBConnection.getConnection();
             Statement s = c.createStatement()) {
            s.executeUpdate(sql);
        }
    }

    // Abstraction: maps JDBC ResultSet to domain object
    private Product map(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setCategory(rs.getString("category"));
        p.setSize(rs.getString("size"));
        p.setPrice(rs.getDouble("price"));
        p.setStock(rs.getInt("stock"));
        p.setStatus(rs.getString("status"));
        return p;
    }
}
