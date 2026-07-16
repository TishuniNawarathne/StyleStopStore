package Model;

// Inheritance base + Encapsulation: id is private with getters/setters
abstract class UserBase {
    private int id;
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}

// Concrete subclass: User extends UserBase (Inheritance)
public class User extends UserBase {
    // Encapsulation: private fields with getters/setters
    private String name;
    private String gender;
    private String tel;
    private String username;
    private String passwordHash;
    private String role;

    public User() {}

    // Overloading: constructor with full details
    public User(int id, String name, String gender, String tel, String username, String passwordHash, String role) {
        setId(id);
        this.name = name;
        this.gender = gender;
        this.tel = tel;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    // Overloading: constructor variant (auth-focused)
    public User(String username, String passwordHash) {
        setId(0);
        this.username = username;
        this.passwordHash = passwordHash;
    }

    // Overloading: constructor variant (profile-focused)
    public User(String name, String gender, String tel) {
        setId(0);
        this.name = name;
        this.gender = gender;
        this.tel = tel;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // Overriding: customize object string representation
    @Override
    public String toString() {
        return name + " " + username + " " + role;
    }
}
