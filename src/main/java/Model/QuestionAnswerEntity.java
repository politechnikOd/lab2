package Model;

import javax.persistence.*;

@Entity
@Table(name = "question_answer", schema = "maksruslan", catalog = "")
public class QuestionAnswerEntity {
    private int questionanswerid;
    private byte iscorrect;
    private QuestionEntity questionByQuestionid;
    private AnswerEntity answerByAnswerid;

    @Id
    @Column(name = "questionanswerid", nullable = false)
    public int getQuestionanswerid() {
        return questionanswerid;
    }

    public void setQuestionanswerid(int questionanswerid) {
        this.questionanswerid = questionanswerid;
    }

    @Basic
    @Column(name = "iscorrect", nullable = false)
    public byte getIscorrect() {
        return iscorrect;
    }

    public void setIscorrect(byte iscorrect) {
        this.iscorrect = iscorrect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionAnswerEntity that = (QuestionAnswerEntity) o;

        if (questionanswerid != that.questionanswerid) return false;
        if (iscorrect != that.iscorrect) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = questionanswerid;
        result = 31 * result + (int) iscorrect;
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "questionid", referencedColumnName = "questionid", nullable = false)
    public QuestionEntity getQuestionByQuestionid() {
        return questionByQuestionid;
    }

    public void setQuestionByQuestionid(QuestionEntity questionByQuestionid) {
        this.questionByQuestionid = questionByQuestionid;
    }

    @ManyToOne
    @JoinColumn(name = "answerid", referencedColumnName = "answerid", nullable = false)
    public AnswerEntity getAnswerByAnswerid() {
        return answerByAnswerid;
    }

    public void setAnswerByAnswerid(AnswerEntity answerByAnswerid) {
        this.answerByAnswerid = answerByAnswerid;
    }
}
