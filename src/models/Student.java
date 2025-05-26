package models;

public class Student {
    private int id;
    private String name;
    private String fatherName;
    private int age;
    private int classId;
    private String previousSchool;
    private String guardianContact;
    private double admissionFee;
    private String admissionDate;
    private double attendancePercentage;
    private String feePaid;

    // Constructor for registration
    public Student(String name, String fatherName, int age, int classId,
                   String previousSchool, String guardianContact, double admissionFee, String admissionDate) {
        this.name = name;
        this.fatherName = fatherName;
        this.age = age;
        this.classId = classId;
        this.previousSchool = previousSchool;
        this.guardianContact = guardianContact;
        this.admissionFee = admissionFee;
        this.admissionDate = admissionDate;
    }

    // Constructor for loading from DB
    public Student(int id, String name, String fatherName, int age, int classId,
                   String previousSchool, String guardianContact, double admissionFee,
                   String admissionDate, double attendancePercentage, String feePaid) {
        this.id = id;
        this.name = name;
        this.fatherName = fatherName;
        this.age = age;
        this.classId = classId;
        this.previousSchool = previousSchool;
        this.guardianContact = guardianContact;
        this.admissionFee = admissionFee;
        this.admissionDate = admissionDate;
        this.attendancePercentage = attendancePercentage;
        this.feePaid = feePaid;
    }

    // ✅ Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getFatherName() { return fatherName; }
    public int getAge() { return age; }
    public int getClassId() { return classId; }
    public String getPreviousSchool() { return previousSchool; }
    public String getGuardianContact() { return guardianContact; }
    public double getAdmissionFee() { return admissionFee; }
    public String getAdmissionDate() { return admissionDate; }
    public double getAttendancePercentage() { return attendancePercentage; }
    public String getFeePaid() { return feePaid; }
}
