package models;

public class Student {
    private String name;
    private String fatherName;
    private int age;
    private String studentClass;
    private String previousSchool;
    private String guardianContact;
    private double admissionFee;
    private String admissionDate;

    // Constructor
    public Student(String name, String fatherName, int age, String studentClass,
                   String previousSchool, String guardianContact, double admissionFee, String admissionDate) {
        this.name = name;
        this.fatherName = fatherName;
        this.age = age;
        this.studentClass = studentClass;
        this.previousSchool = previousSchool;
        this.guardianContact = guardianContact;
        this.admissionFee = admissionFee;
        this.admissionDate = admissionDate;
    }

    // ✅ GETTERS
    public String getName() { return name; }
    public String getFatherName() { return fatherName; }
    public int getAge() { return age; }
    public String getStudentClass() { return studentClass; }
    public String getPreviousSchool() { return previousSchool; }
    public String getGuardianContact() { return guardianContact; }
    public double getAdmissionFee() { return admissionFee; }
    public String getAdmissionDate() { return admissionDate; }
}
