import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;
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

    public DaoPage(String path, int code, String content) {
        this.path = path;
        this.code = code;
        this.content = content;
    }

    public DaoPage() {
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
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DaoPage daoPage = (DaoPage) o;
        return id == daoPage.id && code == daoPage.code && Objects.equals(path, daoPage.path) && Objects.equals(content, daoPage.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, path, code, content);
    }
}
