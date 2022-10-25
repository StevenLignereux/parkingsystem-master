package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static junit.framework.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    public static void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    public void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    public static void tearDown(){

    }

    @Test
    public void testParkingACar(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
        Ticket testTicket = ticketDAO.getTicket("ABCDEF");

        assertNotNull(testTicket);

        int nextSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

        assertNotEquals(1, nextSlot);
    }

    @Test
    public void testParkingLotExit(){
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        // generate a ticket
//        Ticket ticket = new Ticket();
//
//        ticket.setInTime(new Date(System.currentTimeMillis() - 60 * 60 * 1000));
//        ticket.setOutTime(null);
//        ticket.setPrice(0);
//        ticket.setVehicleRegNumber("ABCDEF");
//        ticket.setId(1);
//        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));

        parkingService.processExitingVehicle();

        //TODO: check that the fare generated and out time are populated correctly in the database
        Ticket testTicket = ticketDAO.getTicket("ABCDEF");

        System.out.println("*** testParkingLotExit ticket "  + testTicket.getPrice());
        System.out.println("*** testParkingLotExit ticket "  + testTicket.getInTime());
        System.out.println("*** testParkingLotExit ticket "  + testTicket.getOutTime());

//        assertNotEquals(null, testTicket.getOutTime());
//        assertNotEquals(0, testTicket.getPrice());
        assertTrue(testTicket.getPrice() > 0);
        assertNotNull(testTicket.getOutTime());
    }

}
