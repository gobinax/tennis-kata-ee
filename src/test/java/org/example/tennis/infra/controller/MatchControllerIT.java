package org.example.tennis.infra.controller;

import org.example.tennis.WebApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class MatchControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_create_match() throws Exception {
        mockMvc.perform(
                post("/tennis/match")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"player1\": \"Momo\", \"player2\": \"Federer\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1, \"player1\": \"Momo\", \"player2\": \"Federer\"}"));
    }
}
