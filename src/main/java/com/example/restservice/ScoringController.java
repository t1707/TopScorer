package com.example.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Controller
@RequestMapping(path="/demo")
public class ScoringController {

    @Autowired
    private ScoreRepository scoreRepository;

    @PostMapping(path = "/createScore", consumes = "application/json", produces = "application/json")
    public @ResponseBody Score createScore(@RequestBody Score s) {
            Score res = scoreRepository.save(s);
            return res;
    }

    @GetMapping(path="/getScore/{id}", produces = "application/json")
    public @ResponseBody Optional<Score> getScore(@PathVariable Integer id) {
        return scoreRepository.findById(id);
    }

    @GetMapping(path="/deleteScore/{id}", produces = "application/json")
    public @ResponseBody String deleteScore(@PathVariable Integer id) {
        try {
            scoreRepository.deleteById(id);
            return "Success";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    @PostMapping(path="/getScores", consumes = "application/json", produces = "application/json")
    public @ResponseBody HashMap<String, Object> getScores(@RequestBody PageRequestPayload pageRequestPayload) {

        Pageable paging = PageRequest.of(pageRequestPayload.getPage(),
                pageRequestPayload.getSize(),
                Sort.by(pageRequestPayload.getSortId())
        );

        String player = pageRequestPayload.getPlayers();
        String[] players = player.split(",");

        String before = pageRequestPayload.getBefore();
        String after = pageRequestPayload.getAfter();

        HashMap<String, Object> res = new HashMap<>();
        Page<Score> playersPage;
        if (!player.equals("")) {
            playersPage = scoreRepository.findByAllFilters(Arrays.asList(players), before, after, paging);
        }
        else {
            playersPage = scoreRepository.findByDateFilter(before, after, paging);
        }
        res.put("scores", playersPage);
        return res;
    }

    @GetMapping(path="/getHistory/{player}", produces = "application/json")
    public @ResponseBody HashMap<String, Object> getHistory(@PathVariable String player) {
        HashMap<String, Object> res = new HashMap<>();
        List<Score> playerScores = scoreRepository.findByName(player);

        Supplier<Stream<Score>> streamSupplier = playerScores::stream;
        Optional<Score> topScore = streamSupplier.get().max(Comparator.comparing(Score::getScore));
        Optional<Score> lowScore = streamSupplier.get().min(Comparator.comparing(Score::getScore));
        Double avgScore = (1.0 * streamSupplier.get().mapToInt(Score::getScore).sum()) / playerScores.size();

        res.put("topScore", topScore);
        res.put("lowScore", lowScore);
        res.put("avgScore", avgScore);
        res.put("scores", playerScores);
        return res;
    }

    @GetMapping(path="/health")
    public @ResponseBody String health() {
        return "OK";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Score> all() {
        return scoreRepository.findAll();
    }
}