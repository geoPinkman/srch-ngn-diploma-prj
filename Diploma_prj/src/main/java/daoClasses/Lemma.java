package daoClasses;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "lemma")
public class Lemma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String lemma;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "lemma", cascade = CascadeType.ALL)
    private List<Index> indexes = new ArrayList<>();

    private int frequency;

    public Lemma() {
    }
    public Lemma(int id, String lemma, int frequency) {
        this.id = id;
        this.lemma = lemma;
        this.frequency = frequency;
    }

    public List<Index> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<Index> indexes) {
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
