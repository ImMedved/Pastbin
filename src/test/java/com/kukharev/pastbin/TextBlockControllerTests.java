package com.kukharev.pastbin;

import com.kukharev.pastbin.controller.TextBlockController;
import com.kukharev.pastbin.controller.TextBlockRequest;
import com.kukharev.pastbin.model.TextBlock;
import com.kukharev.pastbin.service.TextBlockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TextBlockControllerTests {

    @InjectMocks
    private TextBlockController textBlockController;

    @Mock
    private TextBlockService textBlockService;

    @Captor
    private ArgumentCaptor<String> textCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @RepeatedTest(5)
    public void testCreateTextBlock() {
        TextBlockRequest request = new TextBlockRequest();
        request.setText("Test text");

        String expectedHash = generateHash(request.getText());

        when(textBlockService.createTextBlock(request.getText())).thenReturn(expectedHash);

        ResponseEntity<String> response = textBlockController.createTextBlock(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedHash, response.getBody());
    }

    private String generateHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes).substring(0, 8);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(5)
    public void testGetTextBlock() {
        String testHash = "testHash";
        TextBlock expectedTextBlock = new TextBlock();
        expectedTextBlock.setHash(testHash);
        expectedTextBlock.setText("Test text");

        when(textBlockService.getTextBlock(testHash)).thenReturn(expectedTextBlock);

        ResponseEntity<TextBlock> response = textBlockController.getTextBlock(testHash);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testHash, response.getBody().getHash());
        assertEquals("Test text", response.getBody().getText());

        verify(textBlockService).getTextBlock(textCaptor.capture());
        assertEquals(testHash, textCaptor.getValue());
    }
}