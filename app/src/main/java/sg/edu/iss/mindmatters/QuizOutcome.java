package sg.edu.iss.mindmatters;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

public class QuizOutcome {
        @SerializedName("id")
        private int id;
        @SerializedName("lastTaken")
        private Calendar lastTaken;
        @SerializedName("quizOutcome")
        private String quizOutcome;


        private User user;

        public QuizOutcome() {
            super();
            // TODO Auto-generated constructor stub
        }

        public QuizOutcome(Calendar lastTaken, String quizOutcome, User user) {
            this.lastTaken = lastTaken;
            this.quizOutcome = quizOutcome;
            this.user = user;
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
            return "QuizOutcome [id=" + id + ", lastTaken=" + lastTaken + ", quizOutcome=" + quizOutcome + ", user=" + user
                    + "]";
        }

    }

