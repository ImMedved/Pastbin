package com.kukharev.pastbin;

import com.kukharev.pastbin.model.TextBlock;
import com.kukharev.pastbin.repository.TextBlockRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PastbinApplicationIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TextBlockRepository textBlockRepository;

    @Test
    public void testCreateAndGetTextBlock() {
        String text = "Hello, world!";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> request = Map.of("text", text);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> createResponse = restTemplate.postForEntity(getTextBlocksUrl(), entity, String.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        String hash = createResponse.getBody();
        assertThat(hash).isNotNull();

        ResponseEntity<TextBlock> getResponse = restTemplate.getForEntity(getTextBlockUrl(hash), TextBlock.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getText()).isEqualTo(text);

        ResponseEntity<String> expiredResponse = restTemplate.getForEntity(getTextBlockUrl(hash), String.class);
        assertThat(expiredResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testCreateTextBlockInvalidInput() {
        String text = "";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> request = Map.of("text", text);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> createResponse = restTemplate.postForEntity(getTextBlocksUrl(), entity, String.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST); // Ожидаемый статус 400 BAD_REQUEST
    }

    private String getTextBlocksUrl() {
        return "http://localhost:" + port + "/api/text-blocks";
    }

    private String getTextBlockUrl(String hash) {
        return "http://localhost:" + port + "/api/text-blocks/" + hash;
    }
}

