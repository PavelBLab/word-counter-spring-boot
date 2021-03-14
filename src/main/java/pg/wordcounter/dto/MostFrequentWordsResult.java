package pg.wordcounter.dto;

import pg.wordcounter.service.WordFrequency;

import java.util.List;

public class MostFrequentWordsResult {

    List<WordFrequency> wordFrequencies;

    public MostFrequentWordsResult() {
    }

    public MostFrequentWordsResult(List<WordFrequency> wordFrequencies) {
        this.wordFrequencies = wordFrequencies;
    }

    public List<WordFrequency> getWordFrequencies() {
        return wordFrequencies;
    }

    public void setWordFrequencies(List<WordFrequency> wordFrequencies) {
        this.wordFrequencies = wordFrequencies;
    }
}
