package com.example.restservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScoreRepository extends PagingAndSortingRepository<Score, Integer> {

    @Query("SELECT s from Score s where s.player = :player")
    List<Score> findByName(@Param("player") String player);

    @Query("SELECT s from Score s where s.player in (:player) and s.time >= :after and s.time <= :before")
    Page<Score> findByAllFilters(@Param("player") List<String> players, @Param("before") String before, @Param("after") String after, Pageable paging);

    @Query("SELECT s from Score s where s.time >= :after and s.time <= :before")
    Page<Score> findByDateFilter(@Param("before") String before, @Param("after") String after, Pageable paging);

}
