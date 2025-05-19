package models;

public class Teacher {
    private String name;
    private String subject;
    private String assignedClass;
    private String username;
    private String password;
    private String email;
    private String contact;

    public Teacher(String name, String subject, String assignedClass, String username, String password, String email, String contact) {
        this.name = name;
        this.subject = subject;
        this.assignedClass = assignedClass;
        this.username = username;
        this.password = password;
        this.email = email;
        this.contact = contact;
    }

    // Getters
    public String getName() { return name; }
    public String getSubject() { return subject; }
    public String getAssignedClass() { return assignedClass; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getContact() { return contact; }

}
