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

    public void setLibrary(Map<String, Integer> library) {
        this.library = library;
    }

    public List<WordFrequency> getAllWords() {
        setText(text);

        List<WordFrequency> wordList = new ArrayList<>();
        for (String key : this.library.keySet()) {
            wordList.add(new WordFrequencyClass(key, this.library.get(key)));
        }
        return wordList;
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

        // Alternative solution
        // this.listOfWords.addAll(Arrays.asList(cleanedText.split(" ")));
        // this.library = this.listOfWords.stream().collect(Collectors.toMap(k -> k, v -> 1, Integer::sum));

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

        for (String key : library.keySet()) {
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

    private String textCleaner(String text) {
        String cleanedText = text;
        cleanedText = cleanedText.replaceAll("[^a-zA-Z\\s]", " ").replaceAll("\\s+", " ").toLowerCase();
        //System.out.println(cleanedText);
        return cleanedText;
    }

    @Override
    public int calculateHighestFrequency(String text) {
        setText(text);
        Integer number;
        // System.out.println(this.library.entrySet().stream().findFirst().get().getKey());

        if (this.library.entrySet().stream().findFirst().isPresent()) {
            return this.library.entrySet().stream().findFirst().get().getValue();
        } else {
            return 0;
        }
    }

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
}


