package com.example.restservice;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PageRequestPayloadTest {

    @Test
    public void testConstructor() {
        PageRequestPayload pageRequestPayload = new PageRequestPayload();
        pageRequestPayload.setAfter("2010-11-25 10:00:00");
        pageRequestPayload.setPlayers("test1,test2");
        pageRequestPayload.setSortId("id");

        assertEquals(new Integer(0), pageRequestPayload.getPage());
        assertEquals(new Integer(3), pageRequestPayload.getSize());
        assertEquals("id", pageRequestPayload.getSortId());
        assertEquals("9999-99-99 00:00:00", pageRequestPayload.getBefore());
        assertEquals("2010-11-25 10:00:00", pageRequestPayload.getAfter());
        assertEquals("test1,test2", pageRequestPayload.getPlayers());
    }
}
