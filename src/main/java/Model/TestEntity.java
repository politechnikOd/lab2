package Model;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "test", schema = "maksruslan", catalog = "")
public class TestEntity {
    private int testid;
    private Date testdatecreation;
    private Time testtime;

    @Id
    @Column(name = "testid", nullable = false)
    public int getTestid() {
        return testid;
    }

    public void setTestid(int testid) {
        this.testid = testid;
    }

    @Basic
    @Column(name = "testdatecreation", nullable = true)
    public Date getTestdatecreation() {
        return testdatecreation;
    }

    public void setTestdatecreation(Date testdatecreation) {
        this.testdatecreation = testdatecreation;
    }

    @Basic
    @Column(name = "testtime", nullable = false)
    public Time getTesttime() {
        return testtime;
    }

    public void setTesttime(Time testtime) {
        this.testtime = testtime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestEntity that = (TestEntity) o;

        if (testid != that.testid) return false;
        if (testdatecreation != null ? !testdatecreation.equals(that.testdatecreation) : that.testdatecreation != null)
            return false;
        if (testtime != null ? !testtime.equals(that.testtime) : that.testtime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = testid;
        result = 31 * result + (testdatecreation != null ? testdatecreation.hashCode() : 0);
        result = 31 * result + (testtime != null ? testtime.hashCode() : 0);
        return result;
    }
}
