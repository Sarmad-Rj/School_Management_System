package models;

public class Teacher {
    private int id;
    private String name;
    private String subject;
    private String assignedClass;
    private String username;
    private String password;
    private String email;
    private String contact;
    private String cnic;

    // Constructor for loading from DB
    public Teacher(int id, String name, String subject, String assignedClass,
                   String username, String password, String email, String contact, String cnic) {
        this.id = id;
        this.name = name;
        this.subject = subject;
        this.assignedClass = assignedClass;
        this.username = username;
        this.password = password;
        this.email = email;
        this.contact = contact;
        this.cnic = cnic;
    }

    // Constructor without ID (for registration)
    public Teacher(String name, String subject, String assignedClass,
                   String username, String password, String email, String contact, String cnic) {
        this.name = name;
        this.subject = subject;
        this.assignedClass = assignedClass;
        this.username = username;
        this.password = password;
        this.email = email;
        this.contact = contact;
        this.cnic = cnic;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSubject() { return subject; }
    public String getAssignedClass() { return assignedClass; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getContact() { return contact; }
    public String getCnic() { return cnic; }
}
