import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveTask;

public class NewTask extends RecursiveTask<Set<String>> {
    private static final int THRESHOLD = 100;
    Parser parser;
    Set<String> hrefsList;

    public NewTask(Parser parser, Set<String> hrefsList) {
        this.parser = parser;
        this.hrefsList = hrefsList;
    }

    @Override
    protected Set<String> compute() {
        Set<String> result = new HashSet<>();
        if (hrefsList.size() <= THRESHOLD) {
            result = getHrefsOfHrefs(hrefsList);
        } else {
            Set<String> stringSetSmallerSize = new HashSet<>();
            List<String> stringList = new LinkedList<>(hrefsList);
            List<NewTask> taskList = new LinkedList<>();
            int newLength = hrefsList.size();
            while (true) {
                if (newLength > THRESHOLD) {
                    for (int i = 0; i < THRESHOLD; i++) {
                        stringSetSmallerSize.add(stringList.remove(0));
                    }
                    taskList.add(new NewTask(parser, stringSetSmallerSize));
                    newLength -= THRESHOLD;
                } else {
                    for (int i = 0; i < newLength; i++) {
                        stringSetSmallerSize.add(stringList.remove(0));
                    }
                    taskList.add(new NewTask(parser, stringSetSmallerSize));
                    break;
                }
            }
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
            parser.getHrefsOnPage(href).forEach(line -> setOfEachHref.add(line));
        }
        return setOfEachHref;
    }
}