package Model;

import javax.persistence.*;

@Entity
@Table(name = "student", schema = "maksruslan", catalog = "")
public class StudentEntity {
    private int studentid;
    private String studentname;
    private String studentsurname;
    private String studentemail;
    private Integer usertype;
    private String login;
    private String password;

    @Id
    @Column(name = "studentid", nullable = false)
    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    @Basic
    @Column(name = "studentname", nullable = false, length = 255)
    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    @Basic
    @Column(name = "studentsurname", nullable = false, length = 255)
    public String getStudentsurname() {
        return studentsurname;
    }

    public void setStudentsurname(String studentsurname) {
        this.studentsurname = studentsurname;
    }

    @Basic
    @Column(name = "studentemail", nullable = false, length = 255)
    public String getStudentemail() {
        return studentemail;
    }

    public void setStudentemail(String studentemail) {
        this.studentemail = studentemail;
    }

    @Basic
    @Column(name = "usertype", nullable = true)
    public Integer getUsertype() {
        return usertype;
    }

    public void setUsertype(Integer usertype) {
        this.usertype = usertype;
    }

    @Basic
    @Column(name = "login", nullable = true, length = 30)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic
    @Column(name = "password", nullable = true, length = 30)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudentEntity that = (StudentEntity) o;

        if (studentid != that.studentid) return false;
        if (studentname != null ? !studentname.equals(that.studentname) : that.studentname != null) return false;
        if (studentsurname != null ? !studentsurname.equals(that.studentsurname) : that.studentsurname != null)
            return false;
        if (studentemail != null ? !studentemail.equals(that.studentemail) : that.studentemail != null) return false;
        if (usertype != null ? !usertype.equals(that.usertype) : that.usertype != null) return false;
        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = studentid;
        result = 31 * result + (studentname != null ? studentname.hashCode() : 0);
        result = 31 * result + (studentsurname != null ? studentsurname.hashCode() : 0);
        result = 31 * result + (studentemail != null ? studentemail.hashCode() : 0);
        result = 31 * result + (usertype != null ? usertype.hashCode() : 0);
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
