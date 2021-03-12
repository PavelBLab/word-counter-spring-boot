package pg.wordcounter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pg.wordcounter.service.WordFrequency;
import pg.wordcounter.service.WordService;

import java.util.List;

@Controller
public class WordController {

    private final WordService wordService;

    @Autowired
    public WordController(WordService wordService) {
        this.wordService = wordService;
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public @ResponseBody String getGritting() {
        return "I am a Word Counter API";
    }

    @RequestMapping(value = "/highestfrequency", method = RequestMethod.GET)
    public String getHighestFrequency(@RequestParam(value = "inputtedText", required = false) String text,
                                      Model model) {

        if (text != null) {
            //System.out.println(text);
            Integer output = wordService.calculateHighestFrequency(text);
            model.addAttribute("print", output);
        }
        return "highestfrequency";
    }

    // Alternative
    @RequestMapping(value = "/highestfrequencypost", method = RequestMethod.POST)
    public @ResponseBody
    Integer getHighestFrequency(@RequestBody String text) {
        return wordService.calculateHighestFrequency(text);
    }


    @RequestMapping(value = "/getwordfrequency", method = RequestMethod.GET)
    public String getFrequencyForWord(@RequestParam(value = "inputtedText", required = false) String text,
                                      @RequestParam(value = "inputtedWord", required = false) String word,
                                      Model model) {
        if (text != null) {
            System.out.println(text);
            System.out.println(word);
            Integer output = wordService.calculateFrequencyForWord(text, word);
            model.addAttribute("print", output);
        }
        return "getwordfrequency";
    }

    //Alternative
    @RequestMapping(value = "/getwordfrequencypost/{word}", method = RequestMethod.POST)
    public @ResponseBody
    Integer getFrequencyForWord(@RequestBody String text, @PathVariable String word) {
        return wordService.calculateFrequencyForWord(text, word);
    }


    @RequestMapping(value = "/getmostfrequentwords", method = RequestMethod.GET)
    public String getMostFrequentWords(@RequestParam(value = "inputtedText", required = false) String text,
                                       @RequestParam(value = "inputtedNumberOfWords", required = false) Integer numberOfWords,
                                       Model model) {
        if (text != null) {
            System.out.println(text);
            System.out.println(numberOfWords);
            List<WordFrequency> output = wordService.calculateMostFrequentNWords(text, numberOfWords);
            model.addAttribute("print", output);
        }
        return "getmostfrequentwords";
    }

    // Alternative
    @RequestMapping(value = "/getmostfrequentwordspost/{numberOfWords}", method = RequestMethod.POST)
    public @ResponseBody
    List<WordFrequency> HighestFrequency(@RequestBody String text, @PathVariable Integer numberOfWords) {
        return wordService.calculateMostFrequentNWords(text, numberOfWords);
    }
}
