package Session;

import Model.StudentEntity;

public final class UserSession {
    private static UserSession instance;

    private StudentEntity student;

    public UserSession(StudentEntity student) {
        this.student = student;
    }

    public static UserSession getInstance(StudentEntity student) {
        if (instance == null) {
            instance = new UserSession(student);
        }
        return instance;
    }

    public static UserSession getInstance() {
        return instance;
    }

    public StudentEntity getStudent() {
        return student;
    }

    public void cleanUserSession() {
        student = null;
    }


}
