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
        System.out.println(Thread.currentThread().getName());
        TestFork task = new TestFork(pageMap, href);
        task.fork();
        pageMap.getHrefsOfPage(href);


    }

}
