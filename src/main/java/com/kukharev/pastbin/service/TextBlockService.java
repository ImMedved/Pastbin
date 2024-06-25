package com.kukharev.pastbin.service;

import com.kukharev.pastbin.exception.ResourceNotFoundException;
import com.kukharev.pastbin.model.TextBlock;
import com.kukharev.pastbin.repository.TextBlockRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TextBlockService {

    @Autowired
    private TextBlockRepository textBlockRepository;

    @Autowired
    private HashGeneratorService hashGeneratorService;

    @Transactional
    public String createTextBlock(String text) {
        TextBlock textBlock = new TextBlock();
        textBlock.setText(text);
        textBlock.setCreatedAt(LocalDateTime.now());
        String hash = hashGeneratorService.generateHash(text);
        textBlock.setHash(hash);
        textBlockRepository.save(textBlock);
        return hash;
    }

    public TextBlock getTextBlock(String hash) {
        return textBlockRepository.findByHash(hash)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("TextBlock not found with hash " + hash));
    }

    @Transactional
    public void deleteTextBlock(String hash) {
        TextBlock textBlock = textBlockRepository.findByHash(hash)
                .orElseThrow(() -> new ResourceNotFoundException("TextBlock not found with hash " + hash));

        textBlockRepository.delete(textBlock);
    }
}
