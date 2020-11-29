package com.example.restservice;

import javax.persistence.*;

@Entity
public class Score {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String player;
    private Integer score;
    private String time;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getScore() {
        return score;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }
}
