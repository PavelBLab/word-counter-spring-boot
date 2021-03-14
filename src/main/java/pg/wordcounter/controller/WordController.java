package pg.wordcounter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pg.wordcounter.dto.HighestFrequencyResult;
import pg.wordcounter.dto.MostFrequentWordsResult;
import pg.wordcounter.dto.WordFrequencyResult;
import pg.wordcounter.service.WordFrequency;
import pg.wordcounter.service.WordService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class WordController {

    private final WordService wordService;
    private static final String PRINT = "print";

    // Create a Logger
    static final Logger logger = Logger.getLogger(WordController.class.getName());

    @Autowired
    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping(value = "/")
    public @ResponseBody
    String getGritting() {
        return "Hello, I am a Word Counter API";
    }

    // Rest controller
    @PostMapping(value = "/highest-frequency-rest")
    public @ResponseBody
    HighestFrequencyResult getHighestFrequency(@RequestBody String text) {
        return new HighestFrequencyResult(wordService.calculateHighestFrequency(text));
    }

    // Submission form
    @GetMapping(value = "/highest-frequency")
    public String getHighestFrequency(@RequestParam(value = "inputtedText", required = false) String text,
                                      Model model) {

        if (text != null) {
            Integer output = wordService.calculateHighestFrequency(text);
            model.addAttribute(PRINT, output);
        }
        return "highest-frequency";
    }

    // Rest controller
    @PostMapping(value = "/get-word-frequency-rest/{wordName}")
    public @ResponseBody
    WordFrequencyResult getFrequencyForWord(@RequestBody String text, @PathVariable String wordName) {
        return new WordFrequencyResult(wordService.calculateFrequencyForWord(text, wordName));
    }

    // Submission form
    @GetMapping(value = "/get-word-frequency")
    public String getFrequencyForWord(@RequestParam(value = "inputtedText", required = false) String text,
                                      @RequestParam(value = "inputtedWord", required = false) String word,
                                      Model model) {
        if (text != null) {
            logger.log(Level.INFO, text);
            logger.log(Level.INFO, word);
            Integer output = wordService.calculateFrequencyForWord(text, word);
            model.addAttribute(PRINT, output);
        }
        return "get-word-frequency";
    }

    // Rest controller
    @PostMapping(value = "/get-most-frequent-words-rest/{numberOfWords}")
    public @ResponseBody
    MostFrequentWordsResult getMostFrequentWords(@RequestBody String text, @PathVariable Integer numberOfWords) {
        return new MostFrequentWordsResult(wordService.calculateMostFrequentNWords(text, numberOfWords));
    }

    // Submission form
    @GetMapping(value = "/get-most-frequent-words")
    public String getMostFrequentWords(@RequestParam(value = "inputtedText", required = false) String text,
                                       @RequestParam(value = "inputtedNumberOfWords", required = false) Integer numberOfWords,
                                       Model model) {
        if (text != null) {
            logger.log(Level.INFO, text);
            logger.log(Level.INFO, String.valueOf(numberOfWords));

            List<WordFrequency> output = wordService.calculateMostFrequentNWords(text, numberOfWords);
            model.addAttribute(PRINT, output);
        }
        return "get-most-frequent-words";
    }
}
