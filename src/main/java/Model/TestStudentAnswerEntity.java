package Model;

import javax.persistence.*;

@Entity
@Table(name = "test_student_answer", schema = "maksruslan", catalog = "")
public class TestStudentAnswerEntity {
    private int teststudentanswerid;
    private TestStudentEntity testStudentByTestStudentId;
    private QuestionAnswerEntity questionAnswerByQuestionAnswerId;

    @Id
    @Column(name = "teststudentanswerid", nullable = false)
    public int getTeststudentanswerid() {
        return teststudentanswerid;
    }

    public void setTeststudentanswerid(int teststudentanswerid) {
        this.teststudentanswerid = teststudentanswerid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestStudentAnswerEntity that = (TestStudentAnswerEntity) o;

        if (teststudentanswerid != that.teststudentanswerid) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return teststudentanswerid;
    }

    @ManyToOne
    @JoinColumn(name = "test_student_id", referencedColumnName = "teststudentid", nullable = false)
    public TestStudentEntity getTestStudentByTestStudentId() {
        return testStudentByTestStudentId;
    }

    public void setTestStudentByTestStudentId(TestStudentEntity testStudentByTestStudentId) {
        this.testStudentByTestStudentId = testStudentByTestStudentId;
    }

    @ManyToOne
    @JoinColumn(name = "question_answer_id", referencedColumnName = "questionanswerid", nullable = false)
    public QuestionAnswerEntity getQuestionAnswerByQuestionAnswerId() {
        return questionAnswerByQuestionAnswerId;
    }

    public void setQuestionAnswerByQuestionAnswerId(QuestionAnswerEntity questionAnswerByQuestionAnswerId) {
        this.questionAnswerByQuestionAnswerId = questionAnswerByQuestionAnswerId;
    }
}
