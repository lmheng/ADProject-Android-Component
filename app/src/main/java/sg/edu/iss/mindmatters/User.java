package sg.edu.iss.mindmatters;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    private Long id;

    @SerializedName("userName")
    private String userName;

    @SerializedName("phone")
    private String phone;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    public Long getId() {
        return id;
    }

    public User(){

    }

    public User(Long id, String userName, String phone, String email, String password, String firstName, String lastName) {
        this.id = id;
        this.userName = userName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
