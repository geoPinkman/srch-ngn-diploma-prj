import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.io.IOException;
import java.util.*;

public class MainTest {
    public static void main(String[] args) throws IOException {
        
        String test = "индекс индексация индексировать индексы индексов индексированый";
        getMorphMap(test).forEach((p,l) -> System.out.println(p + " - " + l));

    }

    private static List<String> getCorrectText(String text) {
        String[] arrText = text.split(" ");
        List<String> result = new ArrayList<>();
        for(String element : arrText) {
            String corrEl = element.toLowerCase().replaceAll("[^а-яА-Я]", "");
            if (!(corrEl.isEmpty() & corrEl.isBlank())) {
                result.add(corrEl);
            }
        }
        return result;
    }
//    public static List<List<String>> getNormalForms(List<String> list) throws IOException{
//        LuceneMorphology luceneMorph = new RussianLuceneMorphology();
//        List<String> result = new ArrayList<>(list);
//        List<List<String>> allInfo = new ArrayList<>();
//        for (String word : result) {
//            List<String> morphList = luceneMorph.getNormalForms(word);
//            allInfo.add(morphList);
//        }
//        return allInfo;
//    }

    private static List<List<String>> getMorphInfo(List<String> list) throws IOException{
        LuceneMorphology luceneMorph = new RussianLuceneMorphology();
        List<String> result = new ArrayList<>(list);
        List<List<String>> allInfo = new ArrayList<>();
        for (String word : result) {
            List<String> morphList = luceneMorph.getMorphInfo(word);
            allInfo.add(morphList);
        }
        return allInfo;
    }
    private static List<String> getCorrectList(List<List<String>> someList) {
        List<String> result = new ArrayList<>();
        for (List<String> eachList : someList) {
            result.addAll(eachList);
        }
        List<String> tempList = new ArrayList<>(result);
            for (String morphWord : tempList) {
                if (morphWord.contains("СОЮЗ")
                        | morphWord.contains("ЧАСТ")
                        | morphWord.contains("ПРЕДЛ")
                        | morphWord.contains("МЕЖД"))
                {
                    result.remove(morphWord);
                }
            }
            result = getResult(result);
        return result;
    }

    private static List<String> getResult(List<String> list) {
        List<String> result = new ArrayList<>();
        for (String line : list) {
            String[] lineArr = line.split("\\|");
            result.add(lineArr[0]);
        }
        return result;
    }

    public static Map<String, Integer> getMorphMap(String text) throws IOException {
        List<String> listForMap = getCorrectList(getMorphInfo(getCorrectText(text)));
        Map<String, Integer> resultMap = new Hashtable<>();
        for (String word : listForMap) {
            if (resultMap.containsKey(word)) {
                int count = resultMap.remove(word);
                resultMap.put(word, ++count);
            }
            else {
                resultMap.put(word, 1);
            }
        }
        return resultMap;
    }
}
