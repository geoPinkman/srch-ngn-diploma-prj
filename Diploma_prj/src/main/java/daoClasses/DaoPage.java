package daoClasses;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "page")
public class DaoPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //@Type(type = "org.hibernate.type.TextType")
    private String path;

    private int code;

    @Type(type = "org.hibernate.type.TextType")
    private String content;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "page", cascade = CascadeType.ALL)
    private List<DaoIndex> indexes = new ArrayList<>();

    public DaoPage(String path, int code, String content) {
        this.path = path;
        this.code = code;
        this.content = content;
    }

    public DaoPage() {
    }

    public List<DaoIndex> getIndexes() {
        return indexes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "DaoPage{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", code=" + code +
                ", content='" + content + '\'' +
                ", indexes=" + indexes +
                '}';
    }
}
