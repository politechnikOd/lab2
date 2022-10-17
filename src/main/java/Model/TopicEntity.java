package Model;

import javax.persistence.*;

@Entity
@Table(name = "topic", schema = "maksruslan", catalog = "")
public class TopicEntity {
    private int topicid;
    private String topicname;
    private SubjectEntity subjectBySubjectid;

    @Id
    @Column(name = "topicid", nullable = false)
    public int getTopicid() {
        return topicid;
    }

    public void setTopicid(int topicid) {
        this.topicid = topicid;
    }

    @Basic
    @Column(name = "topicname", nullable = false, length = 255)
    public String getTopicname() {
        return topicname;
    }

    public void setTopicname(String topicname) {
        this.topicname = topicname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopicEntity that = (TopicEntity) o;

        if (topicid != that.topicid) return false;
        if (topicname != null ? !topicname.equals(that.topicname) : that.topicname != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = topicid;
        result = 31 * result + (topicname != null ? topicname.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "subjectid", referencedColumnName = "subjectid", nullable = false)
    public SubjectEntity getSubjectBySubjectid() {
        return subjectBySubjectid;
    }

    public void setSubjectBySubjectid(SubjectEntity subjectBySubjectid) {
        this.subjectBySubjectid = subjectBySubjectid;
    }
}
