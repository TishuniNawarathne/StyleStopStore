package Controller;

import Model.Product;
import Model.ProductDAO;
import java.sql.SQLException;
import java.util.List;

// Abstraction: controller orchestrates operations using DAO (composition)
public class ProductController {
    private final ProductDAO dao;

    public ProductController() {
        this.dao = new ProductDAO();
    }

    // Delegation: calls DAO methods
    public List<Product> loadAll() throws SQLException {
        return dao.findAll();
    }

    public Product loadById(int id) throws SQLException {
        return dao.findById(id);
    }

    public List<Product> searchByName(String q) throws SQLException {
        return dao.findByNameLike(q);
    }

    public List<Product> restockAlerts(int threshold) throws SQLException {
        return dao.findLowStock(threshold);
    }

    public void add(Product p) throws SQLException {
        dao.create(p);
        dao.resequenceIds();
    }

    public void update(Product p) throws SQLException {
        dao.update(p);
    }

    public void delete(int id) throws SQLException {
        dao.delete(id);
        dao.resequenceIds();
    }

    public void changeId(int oldId, int newId) throws SQLException {
        dao.changeId(oldId, newId);
    }
}
