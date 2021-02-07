package sg.edu.iss.mindmatters;



import com.google.gson.annotations.SerializedName;

public class Resource {
    @SerializedName("id")
    private Long id;
    @SerializedName("urlCode")
    private String urlCode;
    @SerializedName("description")
    private String description;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;

    public Resource(Long id, String urlCode, String description, String name, String type) {
        this.id = id;
        this.urlCode = urlCode;
        this.description = description;
        this.name = name;
        this.type = type;
    }

    public Resource(String urlCode, String description, String name, String type) {
        super();
        this.urlCode = urlCode;
        this.description = description;
        this.name = name;
        this.type=type;
    }
    public Resource() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlCode() {
        return urlCode;
    }

    public void setUrlCode(String urlCode) {
        this.urlCode = urlCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", urlCode='" + urlCode + '\'' +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
