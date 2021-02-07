package sg.edu.iss.mindmatters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

public class QuizOutcome {
        @Expose
        @SerializedName("id")
        private Integer id;
        private transient Calendar lastTaken;
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

        public QuizOutcome(Calendar lastTaken, String quizOutcome, User user) {
                this.lastTaken = lastTaken;
                this.quizOutcome = quizOutcome;
                this.user = user;
            }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Calendar getLastTaken() {
        return lastTaken;
    }

    public void setLastTaken(Calendar lastTaken) {
        this.lastTaken = lastTaken;
    }

    public String getQuizOutcome() {
        return quizOutcome;
    }

    public void setQuizOutcome(String quizOutcome) {
        this.quizOutcome = quizOutcome;
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
                ",quizOutcome='" + quizOutcome +
                '}';
    }

}

