package com.kukharev.pastbin;

import com.kukharev.pastbin.controller.TextBlockRequest;
import com.kukharev.pastbin.model.TextBlock;
import com.kukharev.pastbin.service.TextBlockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class TextBlockControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TextBlockService textBlockService;

    @Test
    public void createTextBlock() throws Exception {
        TextBlockRequest request = new TextBlockRequest();
        request.setText("test text");
        request.setExpiryTime(60);

        when(textBlockService.createTextBlock(anyString(), anyLong())).thenReturn("testHash");

        mockMvc.perform(post("/api/text-blocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"test text\", \"expiryTime\":60}"))
                .andExpect(status().isOk())
                .andExpect(content().string("testHash"));
    }

    @Test
    public void getTextBlock() throws Exception {
        TextBlock textBlock = new TextBlock();
        textBlock.setHash("testHash");
        textBlock.setText("test text");

        when(textBlockService.getTextBlock("testHash")).thenReturn(textBlock);

        mockMvc.perform(get("/api/text-blocks/testHash"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hash").value("testHash"))
                .andExpect(jsonPath("$.text").value("test text"));
    }
}
