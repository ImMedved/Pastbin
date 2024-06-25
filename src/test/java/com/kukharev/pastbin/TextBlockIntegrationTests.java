package com.kukharev.pastbin;

import com.kukharev.pastbin.exception.ResourceNotFoundException;
import com.kukharev.pastbin.model.TextBlock;
import com.kukharev.pastbin.repository.TextBlockRepository;
import com.kukharev.pastbin.service.TextBlockService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TextBlockIntegrationTests {

    @Autowired
    private TextBlockService textBlockService;

    @RepeatedTest(10)
    public void testTextBlockScenario() {
        String randomText = UUID.randomUUID().toString(); // Случайный текст
        TextBlock textBlock = new TextBlock();
        textBlock.setText(randomText);
        System.out.println(LocalDateTime.now());

        String hash = textBlockService.createTextBlock
                (textBlock.getText());

        //assertTrue(TextBlockRepository.existsByHash(hash));

        TextBlock retrievedBlock = textBlockService.getTextBlock(hash);
        assertNotNull(retrievedBlock);
        assertEquals(hash, retrievedBlock.getHash());
        assertEquals(randomText, retrievedBlock.getText());

        textBlockService.deleteTextBlock(hash);

        assertThrows(ResourceNotFoundException.class, () -> textBlockService.getTextBlock(hash));
    }
}
