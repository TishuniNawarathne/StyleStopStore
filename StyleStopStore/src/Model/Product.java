package Model;

// Inheritance base + Encapsulation: id is private with getters/setters
abstract class ProductBase {
    private int id;
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}

// Concrete subclass: Product extends ProductBase (Inheritance)
public class Product extends ProductBase {
    // Encapsulation: private fields with getters/setters
    private String name;
    private String category;
    private String size;
    private double price;
    private int stock;
    private String status;

    public Product() {}

    // Overloading: constructor with full details
    public Product(int id, String name, String category, String size, double price, int stock, String status) {
        setId(id);
        this.name = name;
        this.category = category;
        this.size = size;
        this.price = price;
        this.stock = stock;
        this.status = status;
    }

    // Overloading: constructor with minimal details
    public Product(String name, double price) {
        setId(0);
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    // Overloading: same method name, different parameter type
    public void setPrice(int price) { this.price = price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Overriding: customize object string representation
    @Override
    public String toString() {
        return name + " " + category + " " + size;
    }
}
