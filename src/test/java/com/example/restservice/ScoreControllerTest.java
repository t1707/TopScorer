package com.example.restservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RestController
public class ScoreControllerTest {

    MockMvc mockMvc;

    @Mock
    private ScoreRepository scoreRepository;

    @InjectMocks
    private ScoringController scoringController;

    @BeforeEach
    public void initmocks() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(scoringController).build();
    }

    @Test
    public void test_getScore() throws Exception {

        Score score = new Score();
        score.setId(1);
        score.setPlayer("player1");
        score.setScore(17);
        score.setTime("2020-12-31 00:00:00");

        when(scoreRepository.findById(1)).thenReturn(Optional.of(score));
        this.mockMvc.perform(get("/demo/getScore/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.player").value("player1"))
                .andExpect(jsonPath("$.time").value("2020-12-31 00:00:00"))
                .andExpect(jsonPath("$.score").value(17));

    }
}
