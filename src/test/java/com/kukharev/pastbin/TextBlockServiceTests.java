package com.kukharev.pastbin;

import com.kukharev.pastbin.model.TextBlock;
import com.kukharev.pastbin.repository.TextBlockRepository;
import com.kukharev.pastbin.exception.ResourceNotFoundException;
import com.kukharev.pastbin.service.HashGeneratorService;
import com.kukharev.pastbin.service.TextBlockService;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TextBlockServiceTests {

    @Mock
    private TextBlockRepository textBlockRepository;

    @Mock
    private HashGeneratorService hashGeneratorService;

    @InjectMocks
    private TextBlockService textBlockService;

    @RepeatedTest(10)
    public void testCreateTextBlock() {
        String text = "Test text";
        String hash = "testHash";

        when(hashGeneratorService.generateHash(anyString())).thenReturn(hash);
        when(textBlockRepository.save(any(TextBlock.class))).thenAnswer(i -> i.getArguments()[0]);

        String resultHash = textBlockService.createTextBlock(text);

        assertEquals(hash, resultHash);
        verify(textBlockRepository, times(1)).save(any(TextBlock.class));
    }

    @RepeatedTest(10)
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

    @RepeatedTest(10)
    public void testGetTextBlockNotFound() {
        String hash = "nonExistentHash";

        when(textBlockRepository.findByHash(hash)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> textBlockService.getTextBlock(hash));
    }
}
