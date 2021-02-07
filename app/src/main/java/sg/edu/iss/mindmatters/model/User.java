package sg.edu.iss.mindmatters.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @Expose
    @SerializedName("id")
    private Long id;
    @Expose
    @SerializedName("userName")
    private String userName;
    @Expose
    @SerializedName("phone")
    private String phone;
    @Expose
    @SerializedName("email")
    private String email;
    @Expose
    @SerializedName("password")
    private String password;

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }
    @Expose
    @SerializedName("enabled")
    private Boolean isEnabled;

    public User(){

    }
    public User(Long id, String userName, String phone, String email, String password, String firstName, String lastName) {
        this.id = id;
        this.userName = userName;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
