import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "lemma")
public class DaoLemma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String lemma;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "lemma", cascade = CascadeType.ALL)
    private List<DaoIndex> indexes = new ArrayList<>();

    private int frequency;

    public DaoLemma() {
    }

    public List<DaoIndex> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<DaoIndex> indexes) {
        this.indexes = indexes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "DaoLemma{" +
                "id=" + id +
                ", lemma='" + lemma + '\'' +
                ", indexes=" + indexes +
                ", frequency=" + frequency +
                '}';
    }
}
