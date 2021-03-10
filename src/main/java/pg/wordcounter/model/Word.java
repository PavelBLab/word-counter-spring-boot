package pg.wordcounter.model;

import javax.persistence.*;

@Entity
@Table(name = "WORD")
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "WORD_ID")
    private Integer id;
    @Column(name = "WORD")
    private String word;
    @Column(name = "FREQUENCY")
    private Integer frequency;

    public Word() {
    }

    public Word(String word, Integer frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
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
                ", word='" + word + '\'' +
                ", frequency=" + frequency +
                '}';
    }
}
