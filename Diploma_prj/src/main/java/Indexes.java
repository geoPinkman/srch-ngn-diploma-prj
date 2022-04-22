import java.io.IOException;
import java.util.*;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;


public class Indexes {
//    public static void main(String[] args) {
//        String test = "как бы-тур И. К. Лол";
//        List<String> testList = getCorrectRussianText(test);//.forEach(System.out::println);
//        testList.forEach(System.out::println);
//        getMorphInfo(testList).forEach(System.out::println);
//        getMorphMap(test).forEach((l,p) -> System.out.println(l + " - " + p));
//    }
    private static List<String> getCorrectRussianText(String text) {
        String[] arrText = text.split(" ");
        List<String> result = new ArrayList<>();
        for(String element : arrText) {
            String corrEl = element.toLowerCase().replaceAll("[^а-я]", " ");
            if (corrEl.contains(" ")) {
                String[] word = corrEl.split(" ");
                for (String wordPart : word) {
                    result.add(wordPart.trim());
                }
            } else if (!(corrEl.isEmpty())){
                result.add(corrEl.trim());
            }
        }
        return result;
    }

    private static List<List<String>> getMorphInfo(List<String> list) {
        List<List<String>> allInfo = new ArrayList<>();
        try {
            LuceneMorphology luceneMorph = new RussianLuceneMorphology();
            List<String> result = new ArrayList<>(list);
            for (String word : result) {
                if (!word.isEmpty() & !word.isBlank()) {
                    List<String> morphList = luceneMorph.getMorphInfo(word);
                    allInfo.add(morphList);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
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

    public static Map<String, Integer> getMorphMap(String text) {
        Map<String, Integer> resultMap = new TreeMap<>();
        List<String> listForMap = getCorrectList(getMorphInfo(getCorrectRussianText(text)));
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

    private static Map<String, Float> getRank(String text, float weight) {
        Map<String, Float> resultRankMap = new Hashtable<>();
        Map<String, Integer> tempMap = getMorphMap(text);
        for(var line : tempMap.entrySet()) {
            resultRankMap.put(line.getKey(), line.getValue() * weight);
        }
        return resultRankMap;
    }

    public static Map<String, Float> rankMap(String content, Map<String, Float> indexes) {
        Elements page = Jsoup.parse(content).getAllElements();
        Map<String, Float> indexMap = new Hashtable<>(indexes);
        Map<String, Float> rankMap = new TreeMap<>();
        indexMap.forEach((selector,weight) -> {
            String text = page.select(selector).text();
            Map<String, Float> ranks = getRank(text, weight);
            for (Map.Entry<String, Float> lineOfMap : ranks.entrySet()) {
                String word = lineOfMap.getKey();
                Float rank = lineOfMap.getValue();
                if (!rankMap.containsKey(word)) {
                    rankMap.put(word, rank);
                } else {
                    Float tempRank = rankMap.remove(word);
                    rankMap.put(word, rank + tempRank);
                }
            }
        });
        return rankMap;
    }
}
