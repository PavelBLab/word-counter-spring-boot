package pg.wordcounter.service;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        WordFrequencyClass that = (WordFrequencyClass) o;

        return frequency == that.frequency && Objects.equals(word, that.word);
    }

    @Override
    public String toString() {
        return "(" + word + "," + frequency + ")";
    }
}
