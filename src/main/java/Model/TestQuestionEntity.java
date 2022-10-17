package Model;

import javax.persistence.*;

@Entity
@Table(name = "test_question", schema = "maksruslan", catalog = "")
public class TestQuestionEntity {
    private int testquestion;
    private TestEntity testByTestid;
    private QuestionEntity questionByQuestionid;

    @Id
    @Column(name = "testquestion", nullable = false)
    public int getTestquestion() {
        return testquestion;
    }

    public void setTestquestion(int testquestion) {
        this.testquestion = testquestion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestQuestionEntity that = (TestQuestionEntity) o;

        if (testquestion != that.testquestion) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return testquestion;
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
    @JoinColumn(name = "questionid", referencedColumnName = "questionid", nullable = false)
    public QuestionEntity getQuestionByQuestionid() {
        return questionByQuestionid;
    }

    public void setQuestionByQuestionid(QuestionEntity questionByQuestionid) {
        this.questionByQuestionid = questionByQuestionid;
    }
}
