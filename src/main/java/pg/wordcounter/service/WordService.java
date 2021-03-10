package pg.wordcounter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pg.wordcounter.dao.WordRepository;
import pg.wordcounter.model.Word;

import java.util.*;

@Service
public class WordService implements WordFrequencyAnalyzer {

    private WordRepository wordRepository;

    private String text;
    private Map<String, Integer> library;
    private List<String> listOfWords;
    private List<Integer> wordIndexes;

    {
        this.library = new HashMap<>();
        setListOfWords();
        setWordIndexes();

//        text = "TikTok, known in China as Douyin, is a video-sharing social networking " +
////                "service owned by Chinese company ByteDance. The social media platform is used to make a variety of short-form " +
////                "videos, from genres like dance, comedy, and education, that have a duration from fifteen seconds to one minute " +
////                "(three minutes for some users). TikTok is an international version of Douyin, which was originally released " +
////                "in the Chinese market in September 2016. Later, TikTok was launched in 2017 for iOS and Android in most markets" +
////                " outside of mainland China; however, it only became available worldwide after merging with another Chinese social " +
////                "media service, Musical.ly, on August 2, 2018.\n" +
////                "\n" +
////                "TikTok and Douyin have almost the same user interface but no access to each other's content. Their servers are each " +
////                "based in the market where the respective app is available. The two products are similar, but features are not " +
////                "identical. Douyin includes an in-video search feature that can search by people's face for more videos of them and " +
////                "other features such as buying, booking hotels and making geo-tagged reviews. Since its launch in 2016, TikTok/Douyin " +
////                "rapidly gained popularity in East Asia, South Asia, Southeast Asia, the United States, Turkey, Russia, and other " +
////                "parts of the world. As of October 2020, TikTok surpassed over 2 billion mobile downloads worldwide.\n" +
////                "\n" +
////                "Vanessa Pappas is the CEO of TikTok, having assumed the position following the resignation of Kevin A. Mayer on 27 " +
////                "August 2020. On 3 August 2020, US President Donald Trump threatened to ban TikTok in the United States on " +
////                "15 September if negotiations for the company to be bought by Microsoft or a different \"very American\" company failed." +
////                " On 6 August, Trump signed two executive orders banning US \"transactions\" with TikTok and WeChat to its respective " +
////                "parent companies ByteDance and Tencent, set to take effect 45 days after the signing. A planned ban of the app on " +
////                "20 September 2020 was postponed by a week and then blocked by a federal judge. The app has been" +
////                " banned by the government of India since June 2020 along with 223 other Chinese apps in response to a border clash " +
////                "with China.Pakistan banned TikTok citing 'immoral' and 'indecent' videos on 9 October 2020 but reversed its ban ten" +
////                " days later on 19 October 2020";

    }

    @Autowired
    public WordService(WordRepository wordRepository) {
        super();
        this.wordRepository = wordRepository;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        setListOfWords();
        setWordIndexes();
        this.library = new HashMap<>();

        // Cleaning a text from special characters and lowering cases
        String cleanedText = textCleaner(text);

        for (int i = 0; i < cleanedText.length(); i++) {
            if (Character.isWhitespace(cleanedText.charAt(i))) {
                this.wordIndexes.add(i);
            }
        }

        this.wordIndexes.add(0, 0);
        this.wordIndexes.add(this.wordIndexes.size(), cleanedText.length());

        for (int i = 0; i < this.wordIndexes.size() - 1; i++) {
            this.listOfWords.add(cleanedText.substring(this.wordIndexes.get(i), this.wordIndexes.get(i + 1)).trim());
        }

        for (String word : this.listOfWords) {
            if (this.library.get(word) == null) {
                this.library.put(word, 1);
            } else {
                int frequency = this.library.get(word) + 1;
                this.library.put(word, frequency);
            }
        }

        // LinkedHashMap preserve the ordering of elements in which they are inserted
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        this.library.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

        this.library = sortedMap;

        for (String key: library.keySet()) {

            // Create a new word in repository
            if (wordRepository.findByWord(key).size() == 0) {
                Word word = new Word(key, library.get(key));
                wordRepository.save(word);
            } else {
                // Update an existing word in repository
                Integer frequency = library.get(key);
                Word wordInRepo = wordRepository.findByWord(key).get(0);
                wordInRepo.setFrequency(frequency);
                wordRepository.save(wordInRepo);
            }
        }
    }

    private void setListOfWords() {
        this.listOfWords = new ArrayList<>();
    }

    private void setWordIndexes() {
        this.wordIndexes = new ArrayList<>();
    }

    private String textCleaner(String text) {
        String cleanedText = text;
        cleanedText = cleanedText.replaceAll("[^a-zA-z\\s]", "").replaceAll("\\s+", " ").toLowerCase();
        //System.out.println(cleanedText);
        return cleanedText;
    }

    public List<WordFrequency> getAllWords() {
        setText(text);

        List<WordFrequency> wordList = new ArrayList<>();
        for (String key: this.library.keySet()) {
            wordList.add(new WordFrequencyClass(key, this.library.get(key)));
        }
        return wordList;
    }

    @Override
    public int calculateHighestFrequency(String text) {
        setText(text);

        int maxFrequency = 0;
        for (int num: this.library.values()) {
            if (maxFrequency < num) {
                maxFrequency = num;
            }
        }

        //System.out.println(this.library.entrySet().stream().findFirst().get().getKey());
        return maxFrequency;
    }

    //TODO
    @Override
    public int calculateFrequencyForWord(String text, String word) {
        setText(text);
        try {
            //System.out.println(word);
            return this.library.get(word);
        } catch (NullPointerException e) {
            System.out.println(e + "\nThere is no such a word. Choose another word");
            return 0;
        }
    }

    @Override
    public List<WordFrequency> calculateMostFrequentNWords(String text, int n) {
        setText(text);

        List<WordFrequency> mostFrequentNWordsList = new ArrayList<>();
        int count = 0;
        for (String key: this.library.keySet()) {

            if (n < 0) {
                System.out.println("Enter a number greater then 0");
                break;
            }

            if (count == n) {
                break;
            }

            mostFrequentNWordsList.add(new WordFrequencyClass(key, this.library.get(key)));
            count++;

        }
        return mostFrequentNWordsList;
    }



//    public static void main(String[] args) {
//        WordService wordService = new WordService();
//        String text = "The, sun,,, shines!!! over the the the lake lake lake";
//        String text = "TikTok, known in China as Douyin, is a video-sharing social networking " +
//                "service owned by Chinese company ByteDance. The social media platform is used to make a variety of short-form " +
//                "videos, from genres like dance, comedy, and education, that have a duration from fifteen seconds to one minute " +
//                "(three minutes for some users). TikTok is an international version of Douyin, which was originally released " +
//                "in the Chinese market in September 2016. Later, TikTok was launched in 2017 for iOS and Android in most markets" +
//                " outside of mainland China; however, it only became available worldwide after merging with another Chinese social " +
//                "media service, Musical.ly, on August 2, 2018.\n" +
//                "\n" +
//                "TikTok and Douyin have almost the same user interface but no access to each other's content. Their servers are each " +
//                "based in the market where the respective app is available. The two products are similar, but features are not " +
//                "identical. Douyin includes an in-video search feature that can search by people's face for more videos of them and " +
//                "other features such as buying, booking hotels and making geo-tagged reviews. Since its launch in 2016, TikTok/Douyin " +
//                "rapidly gained popularity in East Asia, South Asia, Southeast Asia, the United States, Turkey, Russia, and other " +
//                "parts of the world. As of October 2020, TikTok surpassed over 2 billion mobile downloads worldwide.\n" +
//                "\n" +
//                "Vanessa Pappas is the CEO of TikTok, having assumed the position following the resignation of Kevin A. Mayer on 27 " +
//                "August 2020. On 3 August 2020, US President Donald Trump threatened to ban TikTok in the United States on " +
//                "15 September if negotiations for the company to be bought by Microsoft or a different \"very American\" company failed." +
//                " On 6 August, Trump signed two executive orders banning US \"transactions\" with TikTok and WeChat to its respective " +
//                "parent companies ByteDance and Tencent, set to take effect 45 days after the signing. A planned ban of the app on " +
//                "20 September 2020 was postponed by a week and then blocked by a federal judge. The app has been" +
//                " banned by the government of India since June 2020 along with 223 other Chinese apps in response to a border clash " +
//                "with China.Pakistan banned TikTok citing 'immoral' and 'indecent' videos on 9 October 2020 but reversed its ban ten" +
//                " days later on 19 October 2020";

//        System.out.println(wordService.calculateHighestFrequency(text));
//        System.out.println(wordService.calculateFrequencyForWord(text,"other"));
//        System.out.println(wordService.calculateMostFrequentNWords(text,  10));
//        System.out.println(wordService.getAllWords());
//
//
//
//    }
}


