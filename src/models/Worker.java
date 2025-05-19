package models;

public class Worker {
    private String name;
    private int age;
    private String field;
    private String contact;
    private double salary;
    private String hireDate;
    private String idCard;

    public Worker(String name, int age, String field, String contact, double salary, String hireDate, String idCard) {
        this.name = name;
        this.age = age;
        this.field = field;
        this.contact = contact;
        this.salary = salary;
        this.hireDate = hireDate;
        this.idCard = idCard;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getField() { return field; }
    public String getContact() { return contact; }
    public double getSalary() { return salary; }
    public String getHireDate() { return hireDate; }
    public String getIdCard() { return idCard; }
}
