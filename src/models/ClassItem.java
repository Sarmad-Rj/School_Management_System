package models;

public class ClassItem {
    private int id;
    private String className;
    private String section;

    public ClassItem(int id, String className, String section) {
        this.id = id;
        this.className = className;
        this.section = section;
    }

    public int getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public String getSection() {
        return section;
    }
// getters and setters

    @Override
    public String toString() {
        return className + " - " + section;  // this helps for combo boxes or lists
    }
}
