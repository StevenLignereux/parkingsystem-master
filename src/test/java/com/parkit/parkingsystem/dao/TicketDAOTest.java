package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TicketDAOTest {
    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @BeforeAll
    public static void setUp() {
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;

        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    public void setUpPerTest() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void saveTicketTest() {
        Ticket t = new Ticket();
        t.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        t.setVehicleRegNumber("ABCDEF");
        t.setPrice(15);
        t.setInTime(new Date());
        t.setOutTime(new Date());

        boolean result = ticketDAO.saveTicket(t);

        assertFalse(result);

        Ticket bddTicket = ticketDAO.getTicket("ABCDEF");

        assertEquals(ParkingType.CAR, bddTicket.getParkingSpot().getParkingType());
        assertEquals(1, bddTicket.getParkingSpot().getId());
        assertEquals("ABCDEF", bddTicket.getVehicleRegNumber());
        assertEquals(15, bddTicket.getPrice());

        assertNotNull(bddTicket.getInTime());
        assertNotNull(bddTicket.getOutTime());
    }

    @Test
    public void saveTicketNoOutTimeTest() {
        Ticket t = new Ticket();
        t.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        t.setVehicleRegNumber("ABCDEF");
        t.setPrice(15);
        t.setInTime(new Date());
        t.setOutTime(null);

        boolean result = ticketDAO.saveTicket(t);

        assertFalse(result);

        Ticket bddTicket = ticketDAO.getTicket("ABCDEF");

        assertEquals(ParkingType.CAR, bddTicket.getParkingSpot().getParkingType());
        assertEquals(1, bddTicket.getParkingSpot().getId());
        assertEquals("ABCDEF", bddTicket.getVehicleRegNumber());
        assertEquals(15, bddTicket.getPrice());

        assertNotNull(bddTicket.getInTime());
        assertNull(bddTicket.getOutTime());
    }

    @Test
    public void getTicketTest() {
        Ticket t = new Ticket();
        t.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        t.setVehicleRegNumber("ABCDEF");
        t.setPrice(15);
        t.setInTime(new Date());
        t.setOutTime(new Date());
        ticketDAO.saveTicket(t);

        String vehReg = "ABCDEF";

        Ticket actual = ticketDAO.getTicket(vehReg);

        assertEquals(t.getVehicleRegNumber(), actual.getVehicleRegNumber());
        assertEquals(t.getPrice(), actual.getPrice());

        assertNotNull(actual.getOutTime());
        assertNotNull(actual.getInTime());
    }

    @Test
    public void getNumberOccurrenceTest() {
        String vehReg = "ABCDEF";

        int expected = 0;
        int actual = ticketDAO.getTicketOccurence(vehReg);

        assertEquals(expected, actual);
    }
}
