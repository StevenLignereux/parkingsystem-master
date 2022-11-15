package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.parkit.parkingsystem.constants.ParkingType.CAR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ParkingSpotDAOTest {
    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @BeforeAll
    public static void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    public void setUpPerTest() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void getNextAvailableSlotTest() {
        int actual = parkingSpotDAO.getNextAvailableSlot(CAR);

        assertEquals(1, actual);
    }

    @Test
    public void updateParkingTest() {
        ParkingSpot parkingSpot = new ParkingSpot(1, CAR, false);

        parkingSpotDAO.updateParking(parkingSpot);

        assertFalse(parkingSpot.isAvailable());
        assertEquals(1, parkingSpot.getId());
    }
}
