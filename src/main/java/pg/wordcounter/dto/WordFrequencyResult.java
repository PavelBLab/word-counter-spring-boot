package pg.wordcounter.dto;

public class WordFrequencyResult {

    Integer count;

    public WordFrequencyResult() {
    }

    public WordFrequencyResult(Integer count) {
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
