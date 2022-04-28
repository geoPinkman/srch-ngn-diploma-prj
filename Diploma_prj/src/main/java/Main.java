import daoClasses.Lemma;
import daoClasses.Page;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
//    public static void main(String[] args) {
//        Connector connector = new Connector();
//        Map<String, Float> selectors = connector.getSelectorFields();
//        Parser parser = new Parser("https://www.svetlovka.ru/");
//        Set<String> stringSet = new HashSet<>();
//        try {
//            FileReader fileReader = new FileReader("hrefs.txt");
//            Scanner sc = new Scanner(fileReader);
//            while (sc.hasNextLine()) {
//                stringSet.add(sc.nextLine());
//            }
//            fileReader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (stringSet.isEmpty()) {
//            stringSet.add(parser.getUrl());
//        }
//        parser.getFullSet().addAll(stringSet);
//        parser.findNewHrefs(parser.getHrefsOfHrefs(parser.getHrefsOnPage(parser.getUrl())));
//        Set<String> findHrefs = parser.getFullSet();
//        for (String path : findHrefs) {
//            Page page = Parser.getPage(path);
//            connector.addPage(page);
//            Map<String, Float> rankMapOfLemmasOnPage = Indexes.rankMap(page.getContent(), selectors);
//            for (Map.Entry<String, Float> lemmas : rankMapOfLemmasOnPage.entrySet()) {
//                connector.saveLemma(lemmas.getKey());
//                Lemma lemma = connector.getLemma(lemmas.getKey());
//                page = connector.getPage(page.getPath());
//                connector.addIndexes(page, lemma, lemmas.getValue());
//            }
//        }
//        connector.closeSession();
//    }
}
