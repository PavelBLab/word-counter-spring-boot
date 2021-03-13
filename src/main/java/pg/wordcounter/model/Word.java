package pg.wordcounter.model;

import javax.persistence.*;

@Entity
@Table(name = "WORD")
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "WORD_ID")
    private Integer id;
    @Column(name = "WORD_NAME")
    private String wordName;
    @Column(name = "FREQUENCY")
    private Integer frequency;

    public Word() {
    }

    public Word(String wordName, Integer frequency) {
        this.wordName = wordName;
        this.frequency = frequency;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", word='" + wordName + '\'' +
                ", frequency=" + frequency +
                '}';
    }
}
