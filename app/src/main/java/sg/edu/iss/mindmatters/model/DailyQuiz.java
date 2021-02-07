package sg.edu.iss.mindmatters.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DailyQuiz {

    private int id;
    private int q1;
    private String q2;
    private int q3;
    private String username;
    private Calendar date;

    DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public DailyQuiz() {
    }

    public DailyQuiz(int id, int q1, String q2, int q3, String username, Calendar date) {
        this.id = id;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
        this.username = username;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQ1() {
        return q1;
    }

    public void setQ1(int q1) {
        this.q1 = q1;
    }

    public String getQ2() {
        return q2;
    }

    public void setQ2(String q2) {
        this.q2 = q2;
    }

    public int getQ3() {
        return q3;
    }

    public void setQ3(int q3) {
        this.q3 = q3;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "DailyQuiz{" +
                "id=" + id +
                ", q1=" + q1 +
                ", q2='" + q2 + '\'' +
                ", q3=" + q3 +
                ", username='" + username + '\'' +
                ", date='" + sdf.format(date.getTime()) + '\'' +
                '}';
    }

}