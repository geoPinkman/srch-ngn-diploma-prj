import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveTask;

public class NewTask extends RecursiveTask<Set<String>> {
    private static final int THRESHOLD = 200;
    Page page;
    Set<String> hrefsList;

    public NewTask(Page page, Set<String> hrefsList) {
        this.page = page;
        this.hrefsList = hrefsList;
    }

    @Override
    protected Set<String> compute() {
        Set<String> result = new HashSet<>();

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

            List<NewTask> taskList = new LinkedList<>();
            NewTask task1 = new NewTask(page, first);
            taskList.add(task1);
            NewTask task2 = new NewTask(page, second);
            taskList.add(task2);
            for (NewTask task : taskList) {
                task.fork();
            }
            for (NewTask task : taskList) {
                result.addAll(task.join());
            }
        }
        return result;
    }
    public Set<String> getHrefsOfHrefs(Set<String> hrefs) {
        Set<String> setOfEachHref = new HashSet<>();
        for (String href : hrefs) {
            page.getHrefsOnPage(href).forEach(line -> setOfEachHref.add(line));
        }
        return setOfEachHref;
    }
}