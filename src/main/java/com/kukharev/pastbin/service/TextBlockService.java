package com.kukharev.pastbin.service;

import com.kukharev.pastbin.exception.ResourceNotFoundException;
import com.kukharev.pastbin.model.TextBlock;
import com.kukharev.pastbin.repository.TextBlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TextBlockService {

    @Autowired
    private TextBlockRepository textBlockRepository;

    @Autowired
    private HashGeneratorService hashGeneratorService;

    public String createTextBlock(String text, long expiryTime) {
        TextBlock textBlock = new TextBlock();
        textBlock.setText(text);
        textBlock.setExpiryTime(LocalDateTime.now().plusSeconds(expiryTime));
        String hash = hashGeneratorService.generateHash(text + System.currentTimeMillis());
        textBlock.setHash(hash);
        textBlockRepository.save(textBlock);
        return hash;
    }

    public TextBlock getTextBlock(String hash) {
        return textBlockRepository.findByHash(hash)
                .orElseThrow(() -> new ResourceNotFoundException("TextBlock not found with hash " + hash));
    }
}