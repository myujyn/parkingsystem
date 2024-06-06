package com.parkit.parkingsystem.integration.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

public class ParkingSpotDAOTest {

    private ParkingSpotDAO parkingSpotDAO;

    @BeforeEach
    public void setUp(){
        DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;

        ParkingSpot spot1 = new ParkingSpot(1, ParkingType.CAR, true);
        parkingSpotDAO.updateParking(spot1);
    }

    @Test
    public void testGetNextAvailableSlotForCar(){
        int nextSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        assertEquals (1, nextSlot);
    }

    @Test
    public void testGetNextAvailableSlotForBike(){
        assertEquals(4, parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE));
    }

    @Test
    public void testUpdateParkingSpot(){
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }

    @Test
    public void testUpdateParkingSpotFailed(){
        ParkingSpot parkingSpot = new ParkingSpot(6, ParkingType.CAR,true);
        assertFalse(parkingSpotDAO.updateParking(parkingSpot));
    }
}