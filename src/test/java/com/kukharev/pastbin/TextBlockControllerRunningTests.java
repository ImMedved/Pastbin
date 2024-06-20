package com.kukharev.pastbin;

import com.kukharev.pastbin.controller.TextBlockController;
import com.kukharev.pastbin.controller.TextBlockRequest;
import com.kukharev.pastbin.exception.ResourceNotFoundException;
import com.kukharev.pastbin.model.TextBlock;
import com.kukharev.pastbin.service.TextBlockService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class TextBlockControllerRunningTests {

    @Mock
    private TextBlockService textBlockService;

    @InjectMocks
    private TextBlockController textBlockController;

    @Test
    public void testCreateTextBlock() {
        TextBlockRequest request = new TextBlockRequest();
        request.setText("test text");
        request.setExpiryTime(60);

        when(textBlockService.createTextBlock(anyString(), anyLong())).thenReturn("testHash");

        ResponseEntity<String> response = textBlockController.createTextBlock(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("testHash", response.getBody());
    }

    @Test
    public void testGetTextBlock() {
        String hash = "testHash";
        TextBlock textBlock = new TextBlock();
        textBlock.setHash(hash);
        textBlock.setText("test text");

        when(textBlockService.getTextBlock(hash)).thenReturn(textBlock);

        ResponseEntity<TextBlock> response = textBlockController.getTextBlock(hash);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(hash, response.getBody().getHash());
        assertEquals("test text", response.getBody().getText());
    }

    @Test
    public void testGetTextBlockNotFound() {
        String hash = "nonExistentHash";

        when(textBlockService.getTextBlock(hash)).thenThrow(new ResourceNotFoundException("TextBlock not found with hash " + hash));

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> textBlockController.getTextBlock(hash));

        assertEquals("TextBlock not found with hash nonExistentHash", exception.getMessage());
    }
}
