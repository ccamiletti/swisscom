package com.swisscom.demo.model;

import java.time.LocalDate;
import java.util.List;

public class AppointmentTBO {

    private final LocalDate date;
    private final List<SlotTBO> stots;

    public AppointmentTBO(final LocalDate date, final List<SlotTBO> stots) {
        this.date = date;
        this.stots = stots;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<SlotTBO> getStots() {
        return stots;
    }
}
