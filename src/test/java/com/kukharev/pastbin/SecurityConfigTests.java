package com.kukharev.pastbin;

import com.kukharev.pastbin.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(SecurityConfig.class)
public class SecurityConfigTests {

    @Autowired
    private WebApplicationContext context;

    @Test
    @WithMockUser
    public void testApiEndpointIsAccessible() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
        mockMvc.perform(get("/api/text-blocks")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").password("password")))
                .andExpect(status().isOk());
    }

    @Test
    public void testOtherEndpointsRequireAuthentication() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
        mockMvc.perform(get("/some-other-endpoint"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testPublicApiEndpointIsAccessibleWithoutAuthentication() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
        mockMvc.perform(get("/api/public"))
                .andExpect(status().isOk());
    }
}
