package Model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "test_student", schema = "maksruslan", catalog = "")
public class TestStudentEntity {
    private int teststudentid;
    private Integer teststudentmark;
    private Timestamp teststudentdate;
    private TestEntity testByTestid;
    private StudentEntity studentByStudentid;

    @Id
    @Column(name = "teststudentid", nullable = false)
    public int getTeststudentid() {
        return teststudentid;
    }

    public void setTeststudentid(int teststudentid) {
        this.teststudentid = teststudentid;
    }

    @Basic
    @Column(name = "teststudentmark", nullable = true)
    public Integer getTeststudentmark() {
        return teststudentmark;
    }

    public void setTeststudentmark(Integer teststudentmark) {
        this.teststudentmark = teststudentmark;
    }

    @Basic
    @Column(name = "teststudentdate", nullable = true)
    public Timestamp getTeststudentdate() {
        return teststudentdate;
    }

    public void setTeststudentdate(Timestamp teststudentdate) {
        this.teststudentdate = teststudentdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestStudentEntity that = (TestStudentEntity) o;

        if (teststudentid != that.teststudentid) return false;
        if (teststudentmark != null ? !teststudentmark.equals(that.teststudentmark) : that.teststudentmark != null)
            return false;
        if (teststudentdate != null ? !teststudentdate.equals(that.teststudentdate) : that.teststudentdate != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = teststudentid;
        result = 31 * result + (teststudentmark != null ? teststudentmark.hashCode() : 0);
        result = 31 * result + (teststudentdate != null ? teststudentdate.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "testid", referencedColumnName = "testid", nullable = false)
    public TestEntity getTestByTestid() {
        return testByTestid;
    }

    public void setTestByTestid(TestEntity testByTestid) {
        this.testByTestid = testByTestid;
    }

    @ManyToOne
    @JoinColumn(name = "studentid", referencedColumnName = "studentid", nullable = false)
    public StudentEntity getStudentByStudentid() {
        return studentByStudentid;
    }

    public void setStudentByStudentid(StudentEntity studentByStudentid) {
        this.studentByStudentid = studentByStudentid;
    }
}
