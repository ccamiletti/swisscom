package com.swisscom.demo.controller;

import com.swisscom.demo.model.AppointmentTBO;
import com.swisscom.demo.service.AppointmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = "/appointments/v1")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(final AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping()
    public List<AppointmentTBO> getAppointments() {
        return appointmentService.getAppointments();
    }


}
