import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "indeex")
public class DaoIndex {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne()
    @JoinColumn(name = "page_id", referencedColumnName = "id")
    private DaoPage page;

    @ManyToOne()
    @JoinColumn(name = "lemma_id", referencedColumnName = "id")
    private DaoLemma lemma;

    @Column(name = "raank")
    float rank;

    public DaoIndex() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DaoPage getPage() {
        return page;
    }

    public void setPage(DaoPage page) {
        this.page = page;
    }

    public DaoLemma getLemma() {
        return lemma;
    }

    public void setLemma(DaoLemma lemma) {
        this.lemma = lemma;
    }

    public float getRank() {
        return rank;
    }

    public void setRank(float rank) {
        this.rank = rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DaoIndex daoIndex = (DaoIndex) o;
        return id == daoIndex.id && Float.compare(daoIndex.rank, rank) == 0 && Objects.equals(page, daoIndex.page) && Objects.equals(lemma, daoIndex.lemma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, page, lemma, rank);
    }

    @Override
    public String toString() {
        return "DaoIndex{" +
                "id=" + id +
                ", page=" + page +
                ", lemma=" + lemma +
                ", rank=" + rank +
                '}';
    }
}
