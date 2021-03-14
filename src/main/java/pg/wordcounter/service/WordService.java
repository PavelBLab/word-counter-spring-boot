package pg.wordcounter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pg.wordcounter.dao.WordRepository;
import pg.wordcounter.model.Word;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class WordService implements WordFrequencyAnalyzer {

    private WordRepository wordRepository;

    private String text;
    private Map<String, Integer> library;
    private List<String> listOfWords;
    private List<Integer> wordIndexes;

    // Create a Logger
    Logger logger = Logger.getLogger(WordService.class.getName());

    public WordService() {
        this.library = new HashMap<>();
    }

    @Autowired
    public WordService(WordRepository wordRepository) {
        this();
        this.wordRepository = wordRepository;
    }

    private void setListOfWords() {
        this.listOfWords = new ArrayList<>();
    }

    private void setWordIndexes() {
        this.wordIndexes = new ArrayList<>();
    }

    public Map<String, Integer> getLibrary() {
        return library;
    }

    /**
     * @return an ArrayList of all WordFrequency instances from the database
     * WordFrequency instance contains String word and Integer frequency
     */
    public List<WordFrequency> getAllWords() {
        setText(text);

        List<WordFrequency> wordList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : this.library.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            wordList.add(new WordFrequencyClass(key, value));
        }
        return wordList;
    }

    /**
     * @return a cleaned String without [.,!....]
     */
    public String textCleaner(String text) {
        String cleanedText = text;
        cleanedText = cleanedText.replaceAll("[^a-zA-Z\\s]", " ")
                .replaceAll("\\s+", " ")
                .toLowerCase()
                .trim();
        return cleanedText;
    }

    public String getText() {
        return text;
    }

    /**
     * populating a HashMap with key - String word, value - Integer frequency
     * adding words and frequencies to the database
     */
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

        /*
         * Alternative solution ==> this.listOfWords.addAll(Arrays.asList(cleanedText.split(" ")));
         * Alternative solution ==> this.library = this.listOfWords.stream().collect(Collectors.toMap(k -> k, v -> 1, Integer::sum));
         */

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

        for (Map.Entry<String, Integer> entry : library.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            // Create a new word in repository
            if (wordRepository.findByWordName(key).isEmpty()) {
                Word word = new Word(key, value);
                wordRepository.save(word);
            } else {
                // Update an existing word in repository
                Integer frequency = library.get(key);
                Word wordInRepo = wordRepository.findByWordName(key).get(0);
                wordInRepo.setFrequency(frequency);
                wordRepository.save(wordInRepo);
            }
        }
    }

    /**
     * @param text is an uncleaned text
     *             <p>
     *             Alternative solution
     *             List<Word> words = new ArrayList<>();
     *             wordRepository.findAll().forEach(words::add);
     *             logger.log(Level.INFO, String.valueOf(words));
     *             return words.get(0).getFrequency();
     * @return frequency (int) of the most repeated word
     */
    @Override
    public int calculateHighestFrequency(String text) {
        setText(text);
        Integer highestFrequency = 0;
        Optional<Map.Entry<String, Integer>> entry = this.library.entrySet().stream().findFirst();

        if (entry.isPresent()) {
            highestFrequency = entry.get().getValue();
        }

        List<Word> words = new ArrayList<>();
        wordRepository.findAll().forEach(words::add);
        logger.log(Level.INFO, String.valueOf(words));
        logger.log(Level.INFO, String.valueOf(words.get(0)));

        return highestFrequency;
    }


    /**
     * @param text is an uncleaned text
     * @param word which occurrence should be found in the text
     * @return frequency (int) of the requested word
     */
    @Override
    public int calculateFrequencyForWord(String text, String word) {
        setText(text);
        try {
            return this.library.get(word);
        } catch (NullPointerException e) {
            logger.log(Level.INFO, "The text does not contain the entered word. Choose another word.\n", e);
            return 0;
        }
    }


    /**
     * @param text is an uncleaned text
     * @param n    a number of returned (word, frequency)
     * @return a list of (word, frequency) pairs
     */
    @Override
    public List<WordFrequency> calculateMostFrequentNWords(String text, int n) {
        setText(text);

        List<WordFrequency> mostFrequentNWordsList = new ArrayList<>();
        int count = 0;
        for (Map.Entry<String, Integer> entry : this.library.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();

            if (n < 0) {
                logger.log(Level.INFO, "Enter a number greater then 0");
                return mostFrequentNWordsList;
            }

            if (count == n) {
                break;
            }

            mostFrequentNWordsList.add(new WordFrequencyClass(key, value));
            count++;

        }
        return mostFrequentNWordsList;
    }
}


