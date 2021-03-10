package pg.wordcounter.service;

public class WordFrequencyClass implements WordFrequency {

    private String word;
    private int frequency;

    public WordFrequencyClass(String word, int frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    @Override
    public String getWord() {
        return this.word;
    }

    @Override
    public int getFrequency() {
        return this.frequency;
    }

    @Override
    public String toString() {
        return "(" + word + "," + frequency + ")";
    }
}
