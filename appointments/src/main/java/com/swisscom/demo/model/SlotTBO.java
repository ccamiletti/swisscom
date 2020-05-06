package com.swisscom.demo.model;

import java.time.LocalTime;

public class SlotTBO {

    private final long id;
    private final LocalTime start;
    private final LocalTime end;

    public SlotTBO(final long id, final LocalTime start, final LocalTime end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    public long getId() {
        return id;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

}
