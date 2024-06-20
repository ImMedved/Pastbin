package com.kukharev.pastbin;

import com.kukharev.pastbin.model.TextBlock;
import com.kukharev.pastbin.repository.TextBlockRepository;
import com.kukharev.pastbin.exception.ResourceNotFoundException;
import com.kukharev.pastbin.service.HashGeneratorService;
import com.kukharev.pastbin.service.TextBlockService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TextBlockServiceRunningTests {

    @Mock
    private TextBlockRepository textBlockRepository;

    @Mock
    private HashGeneratorService hashGeneratorService;

    @InjectMocks
    private TextBlockService textBlockService;

    @Test
    public void testCreateTextBlock() {
        String text = "Test text";
        long expiryTime = 60L;
        String hash = "testHash";

        when(hashGeneratorService.generateHash(anyString())).thenReturn(hash);
        when(textBlockRepository.save(any(TextBlock.class))).thenAnswer(i -> i.getArguments()[0]);

        String resultHash = textBlockService.createTextBlock(text, expiryTime);

        assertEquals(hash, resultHash);
        verify(textBlockRepository, times(1)).save(any(TextBlock.class));

        TextBlock savedTextBlock = (TextBlock) verify(textBlockRepository, times(1)).save(any(TextBlock.class));
        assertEquals(text, savedTextBlock.getText());
        assertEquals(hash, savedTextBlock.getHash());
        //assertTrue(savedTextBlock.getExpiryTime().isAfter(LocalDateTime.now()));
    }

    @Test
    public void testGetTextBlock() {
        String hash = "testHash";
        TextBlock textBlock = new TextBlock();
        textBlock.setHash(hash);
        textBlock.setText("Test text");

        when(textBlockRepository.findByHash(hash)).thenReturn(Optional.of(textBlock));

        TextBlock result = textBlockService.getTextBlock(hash);

        assertEquals(hash, result.getHash());
        assertEquals("Test text", result.getText());
    }

    @Test
    public void testGetTextBlockNotFound() {
        String hash = "nonExistentHash";

        when(textBlockRepository.findByHash(hash)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> textBlockService.getTextBlock(hash));
    }

    @Test
    public void testHashGeneration() {
        String text = "Test text";
        long timestamp = System.currentTimeMillis();
        String hashInput = text + timestamp;

        String generatedHash = hashGeneratorService.generateHash(hashInput);
        assertNotNull(generatedHash);
        assertEquals(8, generatedHash.length());
    }
}

