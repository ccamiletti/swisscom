package com.swisscom.demo.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swisscom.demo.model.AppointmentBO;
import com.swisscom.demo.model.AppointmentTBO;
import com.swisscom.demo.model.SlotTBO;

public class AppointmentService {

	public static void main(String[] args) {
		List<AppointmentTBO> list = new AppointmentService().getAppointments();
		list.forEach(app -> {
			System.out.println(app.getDate());
			app.getStots().forEach(s -> {
				System.out.println(s.getId() + " " + s.getStart() + " " + s.getEnd() );
			});
			System.out.println("--------------------");
		});
	}

	private static boolean isEqualDate(LocalDateTime ld1, LocalDateTime ld2) {
		boolean equalDate = Boolean.FALSE;
		if (ld1.toLocalDate().equals(ld2.toLocalDate())) {
			equalDate = Boolean.TRUE;
		}
		return equalDate;
	}
	
	private Map<LocalDateTime, Set<SlotTBO>> createAppointmentLinkedMap(List<AppointmentBO> appointmentList) {
		Map<LocalDateTime, Set<SlotTBO>> appointmentLinkedMap = new LinkedHashMap<>();		

		//order list to put the data in the LinkedHashMap
		appointmentList.sort((d1,d2) -> d1.getStart().compareTo(d2.getStart()));
		
    	LocalDateTime init = appointmentList.get(0).getStart();
    	appointmentLinkedMap.put(init, new HashSet<SlotTBO>());
    	SlotTBO slotTBO = null;
    	for (AppointmentBO bo : appointmentList) {
    		if (isEqualDate(bo.getStart(), init)) {
    			slotTBO = new SlotTBO(bo.getId(), bo.getStart().toLocalTime(), bo.getEnd().toLocalTime());
    			appointmentLinkedMap.get(init).add(slotTBO);
    		}else {
    			slotTBO = new SlotTBO(bo.getId(), bo.getStart().toLocalTime(), bo.getEnd().toLocalTime());
    			init = bo.getStart();
    			Set<SlotTBO> slotSet = new HashSet<SlotTBO>();
    			slotSet.add(slotTBO);
    			appointmentLinkedMap.put(init, slotSet);
    		}	
    	}
    	return appointmentLinkedMap;
	}

	public List<AppointmentTBO> getAppointments() {

    	List<AppointmentBO> appointmentList = getAppointmentsFromAnotherService();
    	// First step we are going to create a LinkdedMap, the key is the date(without the time) and value is going to be all appointment for this date
    	Map<LocalDateTime, Set<SlotTBO>> appointmentLinkedMap = this.createAppointmentLinkedMap(appointmentList);

    	// Second step , we are going to get just one slot in the morning (from 00:01 to 12:00) and one in the arfernoon (from 12:01 to 00:00)
		List<AppointmentTBO> appointmentTBOList = new ArrayList<AppointmentTBO>();
		appointmentLinkedMap.entrySet().forEach(entry -> {
    		List<SlotTBO> slotsList = new ArrayList<SlotTBO>();
    		AppointmentTBO appointmentTBO = null; 
    		entry.getValue().stream().filter(s -> s.getStart().getHour() < 12).findFirst().ifPresent(s -> {
        		slotsList.add(s);
    		});
    		entry.getValue().stream().filter(s -> s.getStart().getHour() >= 12).findFirst().ifPresent(s -> {
        		slotsList.add(s);
    		});
    		appointmentTBO = new AppointmentTBO(entry.getKey().toLocalDate(), slotsList);
    		appointmentTBOList.add(appointmentTBO);
    	});
        
        return appointmentTBOList;
    }

    // ------------------------------------------------------------------
    // Logic to simulate appointments from another service
    // ------------------------------------------------------------------

    private static List<AppointmentBO> getAppointmentsFromAnotherService() {
        try {
            return getAppointmentsFromFile("src/main/resources/appointments.json");
        } catch (IOException e) {
            e.printStackTrace();
            return  Arrays.asList();
        }
    }

    private static List<AppointmentBO> getAppointmentsFromFile(final String pathName) throws IOException {
        final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return Arrays.asList(mapper.readValue(new File(pathName), AppointmentBO[].class));
    }

}
