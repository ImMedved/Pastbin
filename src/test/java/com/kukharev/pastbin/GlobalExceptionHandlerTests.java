package com.kukharev.pastbin;

import com.kukharev.pastbin.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest
@Import(GlobalExceptionHandler.class)
public class GlobalExceptionHandlerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHandleResourceNotFoundException() throws Exception {
        mockMvc.perform(get("/api/text-blocks/nonExistentHash"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("TextBlock not found with hash nonExistentHash"));
    }

    @Test
    public void testHandleInternalServerError() throws Exception {
        mockMvc.perform(get("/api/causeError"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred"));
    }
}
