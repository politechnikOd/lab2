package Model;

import javax.persistence.*;

@Entity
@Table(name = "question", schema = "maksruslan", catalog = "")
public class QuestionEntity {
    private int questionid;
    private String questionauthor;
    private String questiontext;
    private LevelEntity levelByLevelid;
    private TopicEntity topicByTopicid;

    @Id
    @Column(name = "questionid", nullable = false)
    public int getQuestionid() {
        return questionid;
    }

    public void setQuestionid(int questionid) {
        this.questionid = questionid;
    }

    @Basic
    @Column(name = "questionauthor", nullable = true, length = 255)
    public String getQuestionauthor() {
        return questionauthor;
    }

    public void setQuestionauthor(String questionauthor) {
        this.questionauthor = questionauthor;
    }

    @Basic
    @Column(name = "questiontext", nullable = true, length = -1)
    public String getQuestiontext() {
        return questiontext;
    }

    public void setQuestiontext(String questiontext) {
        this.questiontext = questiontext;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionEntity that = (QuestionEntity) o;

        if (questionid != that.questionid) return false;
        if (questionauthor != null ? !questionauthor.equals(that.questionauthor) : that.questionauthor != null)
            return false;
        if (questiontext != null ? !questiontext.equals(that.questiontext) : that.questiontext != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = questionid;
        result = 31 * result + (questionauthor != null ? questionauthor.hashCode() : 0);
        result = 31 * result + (questiontext != null ? questiontext.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "levelid", referencedColumnName = "levelid", nullable = false)
    public LevelEntity getLevelByLevelid() {
        return levelByLevelid;
    }

    public void setLevelByLevelid(LevelEntity levelByLevelid) {
        this.levelByLevelid = levelByLevelid;
    }

    @ManyToOne
    @JoinColumn(name = "topicid", referencedColumnName = "topicid", nullable = false)
    public TopicEntity getTopicByTopicid() {
        return topicByTopicid;
    }

    public void setTopicByTopicid(TopicEntity topicByTopicid) {
        this.topicByTopicid = topicByTopicid;
    }
}
