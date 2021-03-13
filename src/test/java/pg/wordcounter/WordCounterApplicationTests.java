package pg.wordcounter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import pg.wordcounter.controller.WordController;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WordCounterApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private WordController controller;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    void greetingShouldReturnDefaultMessage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/",
                String.class)).contains("Hello, I am a Word Counter API");
    }

}
