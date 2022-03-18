import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class TestFork extends RecursiveAction {

    GetPageMap pageMap;
    String href;

    public TestFork(GetPageMap pageMap, String href) {
        this.pageMap = pageMap;
        this.href = href;
    }

    @Override
    protected void compute() {
        GetPageMap page = new GetPageMap("https://www.svetlovka.ru");
        page.getHrefsOfPage(href);
        TestFork test = new TestFork(pageMap, href);
        test.invoke();
    }

}
