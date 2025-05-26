package models;

public class TeacherAssignment {
    private String className;
    private String subjectName;

    public TeacherAssignment(String className, String subjectName) {
        this.className = className;
        this.subjectName = subjectName;
    }

    public String getClassName() {
        return className;
    }

    public String getSubjectName() {
        return subjectName;
    }
}
