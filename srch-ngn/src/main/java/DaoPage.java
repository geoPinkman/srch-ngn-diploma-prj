import java.util.ArrayList;
import java.util.List;

public class DaoPage {
    //private int id;
    private String path;
    private int status;
    private String context;

    public static List<DaoPage> list = new ArrayList<>();

    public DaoPage(String path, int status, String context) {
        this.path = path;
        this.status = status;
        this.context = context;
    }

    public static void setList(DaoPage page) {
        list.add(page);
    }
}
