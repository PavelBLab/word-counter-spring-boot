package pg.wordcounter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pg.wordcounter.service.WordFrequency;
import pg.wordcounter.service.WordService;

import java.util.List;

@Controller
public class WordController {

    private WordService wordService;
    private String input = "I am a Word Counter API";

    @Autowired
    public WordController(WordService wordService) {
        this.wordService = wordService;
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public @ResponseBody String getGritting() {
        return input;
    }

//    @RequestMapping(value = "/savetext", method = RequestMethod.POST)
//    public @ResponseBody void addText(@RequestBody String text) {
//        wordService.setText(text);
//    }
//
//    @RequestMapping(value = "/gettext", method = RequestMethod.GET)
//    public @ResponseBody String getText() {
//        return wordService.getText();
//    }
//
//    @RequestMapping(value = "/words", method = RequestMethod.GET)
//    public @ResponseBody List<WordFrequency> getAllWords() {
//        return wordService.getAllWords();
//    }

//    @RequestMapping(value = "/highestfrequency", method = RequestMethod.POST)
//    public @ResponseBody Integer getHighestFrequency(@RequestBody String text) {
//        return wordService.calculateHighestFrequency(text);
//    }
//
//    @RequestMapping(value = "/getwordfrequency/{word}", method = RequestMethod.POST)
//    public @ResponseBody Integer getFrequencyForWord(@RequestBody String text, @PathVariable String word) {
//        return wordService.calculateFrequencyForWord(text, word);
//    }
//
//    @RequestMapping(value = "/getmostrrequentwords/{numberOfWords}", method = RequestMethod.POST)
//    public @ResponseBody List<WordFrequency> HighestFrequency(@RequestBody String text, @PathVariable Integer numberOfWords) {
//        return wordService.calculateMostFrequentNWords(text, numberOfWords);
//    }


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

    @RequestMapping(value = "/getwordfrequency", method = {RequestMethod.POST, RequestMethod.GET})
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


    @RequestMapping(value = "/getmostfrequentwords", method = {RequestMethod.POST, RequestMethod.GET})
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


//    @RequestMapping(value = "/getmostrrequentwords/{numberOfWords}", method = RequestMethod.POST)
//    public @ResponseBody List<WordFrequency> HighestFrequency(@RequestBody String text, @PathVariable Integer numberOfWords) {
//        return wordService.calculateMostFrequentNWords(text, numberOfWords);
//    }






}
