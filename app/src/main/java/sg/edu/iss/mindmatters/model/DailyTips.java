package sg.edu.iss.mindmatters.model;

import com.google.gson.annotations.SerializedName;

public class DailyTips {
    @SerializedName("id")
    private Long id;
    @SerializedName("quotes")
    private String quotes;

    public DailyTips() {
    }

    public DailyTips(Long id, String quotes) {
        this.id = id;
        this.quotes = quotes;
    }

    public Long getId() {
        return id;
    }

    public String getQuotes() {
        return quotes;
    }

    @Override
    public String toString() {
        return "DailyTips{" +
                "id=" + id +
                ", quotes='" + quotes + '\'' +
                '}';
    }
}
