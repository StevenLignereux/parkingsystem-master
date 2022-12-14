package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
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


import java.util.Date;

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
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    public static void tearDown() {

    }

    @Test
    public void testParkingACar() {
        when(inputReaderUtil.readSelection()).thenReturn(1);

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();

        Ticket abcdef = ticketDAO.getTicket("ABCDEF");
        assertNotNull(abcdef);

        int nextAvailableSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        assertNotEquals(1, nextAvailableSlot);
    }

    @Test
    public void testParkingABike() {
        when(inputReaderUtil.readSelection()).thenReturn(1);

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();

        Ticket abcdef = ticketDAO.getTicket("ABCDEF");
        assertNotNull(abcdef);

        int nextAvailableSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
        assertNotEquals(1, nextAvailableSlot);
    }


    @Test
    public void testParkingLotExit() {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        Ticket t = new Ticket();
        t.setInTime(new Date(System.currentTimeMillis() - 60 * 60 * 1000));
        t.setOutTime(null);
        t.setPrice(0);
        t.setVehicleRegNumber("ABCDEF");
        t.setId(1);
        t.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        ticketDAO.saveTicket(t);
        parkingSpotDAO.updateParking(t.getParkingSpot());

        //WHEN
        parkingService.processExitingVehicle();

        //THEN
        //check that the fare generated and out time are populated correctly in the database
        Ticket abcdef = ticketDAO.getTicket("ABCDEF");
        assertNotEquals(null, abcdef.getOutTime());
        assertNotEquals(0, abcdef.getPrice());
    }

}
