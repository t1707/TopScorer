package com.example.restservice;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RestController;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RestController
public class ScoreTest {

    Score score = new Score();
    @Test
    public void testConstructor() {
        score.setId(1);
        score.setScore(0);
        score.setPlayer("test");
        score.setTime("2020-11-25 10:00:00");

        assertEquals(new Integer(1), score.getId());
        assertEquals(new Integer(0), score.getScore());
        assertEquals("test", score.getPlayer());
        assertEquals("2020-11-25 10:00:00", score.getTime());
    }

}
