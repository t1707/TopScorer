package com.example.restservice;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class IntegrationTest {

    @MockBean
    private ScoreRepository scoreRepository;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeAll
    public void setup() {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

    }

    @Test
    public void test_getScore() throws Exception {

        when(scoreRepository.findById(1)).thenReturn(Optional.of(getScoreOfId1()));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/demo/getScore/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.player").value("player1"))
                .andExpect(jsonPath("$.time").value("2020-12-31 00:00:00"))
                .andExpect(jsonPath("$.score").value(17));
    }

    @Test
    public void test_createScore() throws Exception {

        when(scoreRepository.save(any(Score.class))).thenReturn(getScoreOfId1());
        this.mockMvc.perform(post("/demo/createScore")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.player").value("player1"))
                .andExpect(jsonPath("$.time").value("2020-12-31 00:00:00"))
                .andExpect(jsonPath("$.score").value(17));
    }

    @Test
    public void test_getHistory() throws Exception {

        when(scoreRepository.findByName("player1")).thenReturn(getScoresOfPlayer1());
        this.mockMvc.perform(get("/demo/getHistory/{player}", "player1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.topScore.score").value(23))
                .andExpect(jsonPath("$.topScore.time").value("2021-1-30 00:00:00"))
                .andExpect(jsonPath("$.lowScore.score").value(17))
                .andExpect(jsonPath("$.lowScore.time").value("2020-12-31 00:00:00"))
                .andExpect(jsonPath("$.avgScore").value(20.0))
                .andExpect(jsonPath("$.scores.size()").value(2));
    }

    @Test
    public void test_getScores() throws Exception {

        PageRequestPayload pageRequestPayload = new PageRequestPayload();
        pageRequestPayload.setPlayers("player1, player2");
        when(scoreRepository.findByAllFilters(any(List.class), anyString(), anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl(getScoresOfPlayer1()));

        String response = "{\"scores\":" +
                "{\"content\":[{\"id\":1,\"player\":\"player1\",\"score\":17,\"time\":\"2020-12-31 00:00:00\"}" +
                ",{\"id\":2,\"player\":\"player1\",\"score\":23,\"time\":\"2021-1-30 00:00:00\"}]," +
                "\"pageable\":\"INSTANCE\",\"totalPages\":1,\"totalElements\":2,\"last\":true,\"size\":2,\"number\":0," +
                "\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true}," +
                "\"numberOfElements\":2,\"first\":true,\"empty\":false}}";

        this.mockMvc.perform(post("/demo/getScores")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"players\":\"player1,player2\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andExpect(jsonPath("$.scores.totalPages").value(1))
                .andExpect(jsonPath("$.scores.totalElements").value(2.0))
                .andExpect(jsonPath("$.scores.content.size()").value(2))
                .andExpect(jsonPath("$.scores.content[?(@.id == 1)].time").value("2020-12-31 00:00:00"))
                .andExpect(jsonPath("$.scores.content[?(@.id == 2)].score").value(23));
    }

    // Utility function for Score object
    public Score getScoreOfId1() {

        Score score = new Score();
        score.setId(1);
        score.setPlayer("player1");
        score.setScore(17);
        score.setTime("2020-12-31 00:00:00");

        return score;

    }

    // Utility function for List<Score>
    public List<Score> getScoresOfPlayer1() {

        Score score = new Score();
        score.setId(1);
        score.setPlayer("player1");
        score.setScore(17);
        score.setTime("2020-12-31 00:00:00");

        Score score2 = new Score();
        score2.setId(2);
        score2.setPlayer("player1");
        score2.setScore(23);
        score2.setTime("2021-1-30 00:00:00");

        return Arrays.asList(score, score2);

    }

}
