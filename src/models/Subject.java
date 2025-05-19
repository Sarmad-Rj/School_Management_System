package models;

public class Subject {
    private int id;
    private String subjectName;

    public Subject(int id, String subjectName) {
        this.id = id;
        this.subjectName = subjectName;
    }

    // getters and setters

    @Override
    public String toString() {
        return subjectName;
    }

    public int getId() {
        return id;
    }

    public String getSubjectName() {
        return subjectName;
    }
}
