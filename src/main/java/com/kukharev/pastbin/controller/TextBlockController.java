package com.kukharev.pastbin.controller;

import com.kukharev.pastbin.model.TextBlock;
import com.kukharev.pastbin.service.TextBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/text-blocks")
public class TextBlockController {

    @Autowired
    private TextBlockService textBlockService;

    @PostMapping
    public ResponseEntity<String> createTextBlock(@RequestBody TextBlockRequest textBlockRequest) throws NoSuchAlgorithmException {
        if (textBlockRequest.getText() == null || textBlockRequest.getText().isEmpty())
            return ResponseEntity.badRequest().build();

        String hash = textBlockService.createTextBlock(textBlockRequest.getText());
        return ResponseEntity.ok(hash);
    }

    @GetMapping("/{hash}")
    public ResponseEntity<TextBlock> getTextBlock(@PathVariable String hash) {
        TextBlock textBlock = textBlockService.getTextBlock(hash);
        return ResponseEntity.ok(textBlock);
    }
}