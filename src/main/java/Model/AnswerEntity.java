package Model;

import javax.persistence.*;

@Entity
@Table(name = "answer", schema = "maksruslan", catalog = "")
public class AnswerEntity {
    private int answerid;
    private String answertext;

    @Id
    @Column(name = "answerid", nullable = false)
    public int getAnswerid() {
        return answerid;
    }

    public void setAnswerid(int answerid) {
        this.answerid = answerid;
    }

    @Basic
    @Column(name = "answertext", nullable = false, length = 255)
    public String getAnswertext() {
        return answertext;
    }

    public void setAnswertext(String answertext) {
        this.answertext = answertext;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnswerEntity that = (AnswerEntity) o;

        if (answerid != that.answerid) return false;
        if (answertext != null ? !answertext.equals(that.answertext) : that.answertext != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = answerid;
        result = 31 * result + (answertext != null ? answertext.hashCode() : 0);
        return result;
    }
}
