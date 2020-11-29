package com.example.restservice;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class ScoreRepositoryTest {

    @Autowired
    JdbcTemplate jdbctemplate;

    @Autowired
    ScoreRepository scoreRepository;

    public static boolean compareScoreItem(Score s1, Score s2) {
        return s1.getId().equals(s2.getId())
                && s1.getScore().equals(s2.getScore())
                && s1.getPlayer().equals(s2.getPlayer())
                && s1.getTime().equals(s2.getTime());
    }

    @BeforeTransaction
    public void initdb() throws Exception {
        Connection connection = Objects.requireNonNull(jdbctemplate.getDataSource()).getConnection();
        IDatabaseConnection dbconnection = new DatabaseConnection(connection);
        try {
            IDataSet dataset = new CsvDataSet(new File("src/test/resources/"));
            DatabaseOperation.CLEAN_INSERT.execute(dbconnection, dataset);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
            dbconnection.close();
        }
    }


    @AfterTransaction
    public void teardown() throws Exception{
        Connection connection = Objects.requireNonNull(jdbctemplate.getDataSource()).getConnection();
        IDatabaseConnection dbconnection = new DatabaseConnection(connection);
        try {
            IDataSet dataset = new CsvDataSet(new File("src/test/resources/"));
            DatabaseOperation.DELETE_ALL.execute(dbconnection, dataset);

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
            dbconnection.close();
        }
    }

    @Test
    public void test_findByName() {

        List<Score> actualItem = scoreRepository.findByName("player1");

        assertEquals(actualItem.size(), 3);

        List<Score> actualItem2 = scoreRepository.findByName("player4");
        assertEquals(actualItem2.size(), 1);

        Score actualItemHead = actualItem2.get(0);

        assertEquals(actualItemHead.getId(), new Integer(5));
        assertEquals(actualItemHead.getPlayer(), "player4");
        assertEquals(actualItemHead.getScore(), new Integer(15));
        assertEquals(actualItemHead.getTime(), "2019-05-17 11:00:00");
    }

    @Test
    public void test_findByAllFilters() {
        PageRequestPayload pageRequestPayload = new PageRequestPayload();
        Pageable paging = PageRequest.of(pageRequestPayload.getPage(),
                pageRequestPayload.getSize(),
                Sort.by(pageRequestPayload.getSortId())
        );
        Page<Score> actualItem = scoreRepository.findByAllFilters(Arrays.asList("player1"),
                "2050-12-10 11:00:00","2000-12-10 11:00:00", paging);

        assertEquals(actualItem.getTotalElements(), 3L);
        assertEquals(actualItem.getContent().size(), 3);

        Score expectedItem = new Score();
        expectedItem.setTime("2017-11-10 11:00:00");
        expectedItem.setScore(9);
        expectedItem.setPlayer("player1");
        expectedItem.setId(7);

        assertTrue(compareScoreItem(actualItem.getContent().get(2), expectedItem));

    }

    @Test
    public void test_findByDateFilter() {
        PageRequestPayload pageRequestPayload = new PageRequestPayload();
        Pageable paging = PageRequest.of(pageRequestPayload.getPage(),
                pageRequestPayload.getSize(),
                Sort.by(pageRequestPayload.getSortId())
        );
        Page<Score> actualItem = scoreRepository.findByDateFilter("2015-01-10 11:00:00",
                "2012-12-10 11:00:00", paging);

        assertEquals(actualItem.getTotalElements(), 1L);
        assertEquals(actualItem.getContent().size(), 1);

        Score expectedItem = new Score();
        expectedItem.setTime("2014-12-10 16:00:00");
        expectedItem.setScore(100);
        expectedItem.setPlayer("player3");
        expectedItem.setId(9);

        assertTrue(compareScoreItem(actualItem.getContent().get(0), expectedItem));

    }

    @Test
    public void test_Pagination() {

        Pageable paging0 = PageRequest.of(0,
                2,
                Sort.by("score")
        );

        Pageable paging1 = PageRequest.of(1,
                2,
                Sort.by("score")
        );

        Score expectedItem1 = new Score();
        expectedItem1.setTime("2010-12-10 11:00:00");
        expectedItem1.setScore(1);
        expectedItem1.setPlayer("player1");
        expectedItem1.setId(1);

        Page<Score> actualItem1 = scoreRepository.findByAllFilters(Arrays.asList("player1"),
                "2018-12-10 11:00:00","2009-12-10 11:00:00", paging0);

        assertEquals(actualItem1.getTotalElements(), 2L);
        assertEquals(actualItem1.getContent().size(), 2);

        Score actualItemHead = actualItem1.getContent().get(0);
        assertTrue(compareScoreItem(actualItemHead, expectedItem1));

        Page<Score> actualItem2 = scoreRepository.findByAllFilters(Arrays.asList("player3", "player5"),
                "2025-12-10 11:00:00","2000-12-10 11:00:00", paging1);

        assertEquals(actualItem2.getTotalElements(), 4L);
        assertEquals(actualItem2.getContent().size(), 2);

        Score actualItem2Tail = actualItem2.getContent().get(1);
        Score expectedItem2 = new Score();
        expectedItem2.setTime("2014-12-10 16:00:00");
        expectedItem2.setScore(100);
        expectedItem2.setPlayer("player3");
        expectedItem2.setId(9);
        assertTrue(compareScoreItem(actualItem2Tail, expectedItem2));

    }
}
