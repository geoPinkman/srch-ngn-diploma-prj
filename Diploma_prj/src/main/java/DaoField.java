import javax.persistence.*;

@Entity
@Table(name = "field")
public class DaoField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String selector;
    private float weight;

    public DaoField() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "DaoField{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", selector='" + selector + '\'' +
                ", weight=" + weight +
                '}';
    }
}
