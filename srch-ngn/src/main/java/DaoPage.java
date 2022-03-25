import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Generated;
import org.hibernate.mapping.Value;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "page")
public class DaoPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String path;

    private int status;
    @Column()
    private String content;

    public DaoPage() {
    }

    public DaoPage(int id, String path, int status, String content) {
        this.id = id;
        this.path = path;
        this.status = status;
        this.content = content;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContext() {
        return content;
    }

    public void setContext(String context) {
        this.content = content;
    }
}
