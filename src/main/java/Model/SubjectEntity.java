package Model;

import javax.persistence.*;

@Entity
@Table(name = "subject", schema = "maksruslan", catalog = "")
public class SubjectEntity {
    private int subjectid;
    private String subjectname;

    @Id
    @Column(name = "subjectid", nullable = false)
    public int getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(int subjectid) {
        this.subjectid = subjectid;
    }

    @Basic
    @Column(name = "subjectname", nullable = false, length = 255)
    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubjectEntity that = (SubjectEntity) o;

        if (subjectid != that.subjectid) return false;
        if (subjectname != null ? !subjectname.equals(that.subjectname) : that.subjectname != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = subjectid;
        result = 31 * result + (subjectname != null ? subjectname.hashCode() : 0);
        return result;
    }
}
