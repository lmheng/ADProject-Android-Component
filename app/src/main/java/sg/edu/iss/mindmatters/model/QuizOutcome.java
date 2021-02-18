package sg.edu.iss.mindmatters.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class QuizOutcome {
        @Expose
        @SerializedName("id")
        private Integer id;
        @Expose
        @SerializedName("nextQuiz")
        private String nextQuiz;
        @Expose
        @SerializedName("quizOutcome")
        private String quizOutcome;
        private transient User user;

        public QuizOutcome() {

        }
    public QuizOutcome(Integer id, String quizOutcome) {
        this.id = id;
        this.quizOutcome = quizOutcome;
    }

        public QuizOutcome(String nextQuiz, String quizOutcome, User user) {
                this.nextQuiz = nextQuiz;
                this.quizOutcome = quizOutcome;
                this.user = user;
            }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getNextQuiz() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(nextQuiz, dtf);
    }

    public String parseQuiz() {
            LocalDate quiz = getNextQuiz();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return quiz.format(dtf);
    }

    public String getQuizOutcome() {
        return quizOutcome;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "QuizOutcome{" +
                "id=" + id +
                "nextQuiz=" + nextQuiz +
                ",quizOutcome='" + quizOutcome +
                '}';
    }

    public String[] quizOutcomeData() {
            return new String[] {parseQuiz(), quizOutcome};
    }

}

