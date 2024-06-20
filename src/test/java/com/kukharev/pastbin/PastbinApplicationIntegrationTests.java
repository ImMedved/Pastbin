package com.kukharev.pastbin;

import com.kukharev.pastbin.model.TextBlock;
import com.kukharev.pastbin.repository.TextBlockRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
        long expiryTime = 60L;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        Map<String, Object> request = Map.of("text", text, "expiryTime", expiryTime);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> createResponse = restTemplate.postForEntity("http://localhost:" + port + "/api/text-blocks", entity, String.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        String hash = createResponse.getBody();

        ResponseEntity<TextBlock> getResponse = restTemplate.getForEntity("http://localhost:" + port + "/api/text-blocks/" + hash, TextBlock.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getText()).isEqualTo(text);

        // Verify expiration
        ResponseEntity<String> expiredResponse = restTemplate.getForEntity("http://localhost:" + port + "/api/text-blocks/" + hash, String.class);
        assertThat(expiredResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testCreateTextBlockInvalidInput() {
        String text = "";
        long expiryTime = -1L;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        Map<String, Object> request = Map.of("text", text, "expiryTime", expiryTime);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> createResponse = restTemplate.postForEntity("http://localhost:" + port + "/api/text-blocks", entity, String.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
