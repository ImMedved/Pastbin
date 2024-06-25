package com.kukharev.pastbin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testApiEndpointAccessibleToAuthenticatedUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/example"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testApiEndpointAccessibleToAnonymousUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/example"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testOtherEndpointsRequireAuthentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/other"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}

