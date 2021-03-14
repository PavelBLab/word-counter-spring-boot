package pg.wordcounter.dto;

public class HighestFrequencyResult {

    private Integer count;

    public HighestFrequencyResult() {
    }

    public HighestFrequencyResult(Integer count) {
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
