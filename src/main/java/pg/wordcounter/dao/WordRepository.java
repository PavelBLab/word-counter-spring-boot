package pg.wordcounter.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pg.wordcounter.model.Word;

import java.util.List;

@Repository
public interface WordRepository extends CrudRepository<Word, Integer> {

    List<Word> findByWordName(String word);

}
