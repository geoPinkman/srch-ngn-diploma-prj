import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveTask;

public class NewTask extends RecursiveTask<Set<Set<String>>> {
    private static final int THRESHOLD = 100;
    Page page;
    Set<String> hrefsList;

    public NewTask(Page page, Set<String> hrefsList) {
        this.page = page;
        this.hrefsList = hrefsList;
    }

    @Override
    protected Set<Set<String>> compute() {
        Set<Set<String>> result = new HashSet<>();

        Set<String> first = new HashSet<>();
        Set<String> second = new HashSet<>();
        List<String> stringList = new LinkedList<>(hrefsList);

        if (hrefsList.size() <= THRESHOLD) {
            result = getHrefsOfHrefs(hrefsList);
        }
        else {
            int firstLength = hrefsList.size() / 2;
            int secondLength = hrefsList.size() - firstLength;

            for (int i = 0; i < firstLength; i++) {
                first.add(stringList.remove(0));
            }
            for (int i = 0; i < secondLength; i++) {
                second.add(stringList.remove(0));
            }
            NewTask task1 = new NewTask(page, first);
            task1.fork();
            NewTask task2 = new NewTask(page, second);
            task2.fork();

            result.addAll(task2.join());
            result.addAll(task1.join());
        }
        return result;
    }
    public Set<Set<String>> getHrefsOfHrefs(Set<String> hrefs) {
        Set<Set<String>> setOfEachHref = new HashSet<>();
        for (String href : hrefs) {
            setOfEachHref.add(page.getHrefsOnPage(href));
        }
        return setOfEachHref;
    }
}