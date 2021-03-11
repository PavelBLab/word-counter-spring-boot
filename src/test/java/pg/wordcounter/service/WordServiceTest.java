package pg.wordcounter.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pg.wordcounter.dao.WordRepository;

import java.util.ArrayList;
import java.util.List;

public class WordServiceTest {

    private WordRepository wordRepository = Mockito.mock(WordRepository.class);

    @Test
    @DisplayName("Checking how a text is preprocessed and parsed to a hashMap")
    void setText() {
        String text = "The sun shines over the lake. Ducks swimming in the lake. The lake was beautiful.";
        WordService wordService = new WordService(wordRepository);

        wordService.setText(text);
        Assertions.assertEquals(4, wordService.getLibrary().get("the"));

    }

    @Test
    @DisplayName("Checking the calculation of the highest frequency")
    void calculateHighestFrequency() {
        String text = "The sun shines over the lake. Ducks swimming in the lake. The lake was beautiful.";

        WordService wordService = new WordService(wordRepository);
        Assertions.assertEquals(4, wordService.calculateHighestFrequency(text));

    }

    @Test
    @DisplayName("Checking the calculation of the frequency of the specific word")
    void calculateFrequencyForWord() {
        String text = "The sun shines over the lake. Ducks swimming in the lake. The lake was beautiful.";

        WordService wordService = new WordService(wordRepository);
        Assertions.assertEquals(4, wordService.calculateFrequencyForWord(text, "the"));

    }

    @Test
    @DisplayName("Checking the calculation of a number of words")
    void calculateMostFrequentNWords() {
        String text = "The sun shines over the lake. Ducks swimming in the lake. The lake was beautiful.";

        WordService wordService = new WordService(wordRepository);
        List<WordFrequency> words = new ArrayList<>();
        words.add(new WordFrequencyClass("the", 4));
        words.add(new WordFrequencyClass("lake", 3));

        Assertions.assertEquals(words.toString(), wordService.calculateMostFrequentNWords(text, 2).toString());
    }
}