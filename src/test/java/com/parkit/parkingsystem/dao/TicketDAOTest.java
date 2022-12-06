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
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));
        t.setInTime(inTime);
        t.setOutTime(new Date());

        ticketDAO.saveTicket(t);

        Ticket bddTicket = ticketDAO.getTicket("ABCDEF");

        assertEquals(ParkingType.CAR, bddTicket.getParkingSpot().getParkingType());
        assertEquals(1, bddTicket.getParkingSpot().getId());
        assertEquals("ABCDEF", bddTicket.getVehicleRegNumber());
        assertEquals(15, bddTicket.getPrice());

        assertNotNull(bddTicket.getInTime());
        assertNotNull(bddTicket.getOutTime());
    }


    @Test
    public void getTicketTest() {
        Ticket ticketTest = new Ticket();
        ticketTest.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        ticketTest.setVehicleRegNumber("ABCDEF");
        ticketTest.setPrice(15);
        ticketTest.setInTime(new Date());
        ticketTest.setOutTime(new Date());
        ticketDAO.saveTicket(ticketTest);

        String vehReg = "ABCDEF";

        Ticket actual = ticketDAO.getTicket(vehReg);

        assertEquals(ticketTest.getVehicleRegNumber(), actual.getVehicleRegNumber());
        assertEquals(ticketTest.getPrice(), actual.getPrice());

        // Les dates n'ont pas le même format, afin de simplifier les choses on vérifie que les dates du ticket actuel ne sont pas null
        assertNotNull(actual.getInTime());
        assertNotNull(actual.getOutTime());

        assertEquals(ticketTest.getParkingSpot().getId(), actual.getParkingSpot().getId());
    }

    // Vérifier que la donnée existe
    @Test
    public void getNumberOccurrenceTest() {

        Ticket t = new Ticket();
        t.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        t.setVehicleRegNumber("ABCDEF");
        t.setPrice(15);
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));
        t.setInTime(inTime);
        t.setOutTime(new Date());

        ticketDAO.saveTicket(t);

        String vehReg = "ABCDEF";

        int expected = 1;
        int actual = ticketDAO.getTicketOccurence(vehReg);

        assertEquals(expected, actual);
    }

    @Test
    public void updateTicketTest() {

        Date outTime = new Date();

        Ticket updateTicketTest = new Ticket();
        updateTicketTest.setOutTime(outTime);

        boolean isUpdateWork = ticketDAO.updateTicket(updateTicketTest);

        assertTrue(isUpdateWork);
    }

    @Test
    public void updateTicketFailTest() {

        Ticket updateTicketFailTest = new Ticket();
        updateTicketFailTest.setOutTime(null);

        boolean isUpdateNotWork = ticketDAO.updateTicket(updateTicketFailTest);

        assertFalse(isUpdateNotWork);
    }
}
