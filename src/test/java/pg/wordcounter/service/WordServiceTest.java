package pg.wordcounter.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pg.wordcounter.dao.WordRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

 class WordServiceTest {

     private WordRepository wordRepository = Mockito.mock(WordRepository.class);


     @Test
     @DisplayName("Checking how a text is preprocessed")
     void textCleaner() {
         String text = "The sun,,, !!shines over111 the,!) lake.";
         WordService wordService = new WordService(wordRepository);
         assertEquals("the sun shines over the lake", wordService.textCleaner(text));

     }

     @Test
     @DisplayName("Checking how a text is parsed to a hashMap")
     void setText() {
         String text = "The sun shines over the lake. Ducks swimming in the lake. The lake was beautiful.";
         WordService wordService = new WordService(wordRepository);

         wordService.setText(text);
         assertEquals(4, wordService.getLibrary().get("the"));
     }

     @Test
     @DisplayName("Checking the calculation of the highest frequency")
    void calculateHighestFrequency() {
        String text = "The sun shines over the lake. Ducks swimming in the lake. The lake was beautiful.";

        WordService wordService = new WordService(wordRepository);
        assertEquals(4, wordService.calculateHighestFrequency(text));

    }

    @Test
    @DisplayName("Checking the calculation of the frequency of the specific word")
    void calculateFrequencyForWord() {
        String text = "The sun shines over the lake. Ducks swimming in the lake. The lake was beautiful.";

        WordService wordService = new WordService(wordRepository);
        assertEquals(4, wordService.calculateFrequencyForWord(text, "the"));
    }

    @Test
    @DisplayName("Checking the calculation of a number of words")
    void calculateMostFrequentNWords() {
        String text = "The sun shines over the lake. Ducks swimming in the lake. The lake was beautiful.";

        WordService wordService = new WordService(wordRepository);
        List<WordFrequency> words = new ArrayList<>();
        words.add(new WordFrequencyClass("the", 4));
        words.add(new WordFrequencyClass("lake", 3));

        List<WordFrequency> result = wordService.calculateMostFrequentNWords(text, 2);

        for (WordFrequency wordFrequency : words) {
            assertTrue(result.contains(wordFrequency));
        }
    }
}