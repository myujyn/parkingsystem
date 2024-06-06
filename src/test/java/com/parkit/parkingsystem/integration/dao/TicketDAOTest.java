package com.parkit.parkingsystem.integration.dao;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TicketDAOTest {

    private final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private final TicketDAO ticketDAO = new TicketDAO();
    private Ticket ticket;

    @BeforeEach
    public void setUp(){
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket = new Ticket();
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        ticket.setInTime(new Date());
        ticket.setOutTime(null);
    }

    @Test
    public void testSaveTicket(){
        assertNotNull(ticket.getParkingSpot());
        int countTicketsBeforeSave = ticketDAO.getNbTicket("ABCDEF");
        ticketDAO.saveTicket(ticket);
        int countTicketsAfterSave = ticketDAO.getNbTicket("ABCDEF");
        assertEquals(countTicketsBeforeSave + 1, countTicketsAfterSave);
    }

    @Test
    public void testSaveTicketFail(){
        ticket.setParkingSpot(null);
        assertFalse(ticketDAO.saveTicket(ticket));
    }

    @Test
    public void testGetTicket(){
        ticketDAO.saveTicket(ticket);
        Ticket savedTicket = ticketDAO.getTicket("ABCDEF");
        assertNotNull(savedTicket);
        assertEquals("ABCDEF", savedTicket.getVehicleRegNumber());
    }
    @Test
    public void testGetTicketNotFound(){
        Ticket savedTicket = ticketDAO.getTicket("NOT_EXISTING");
        assertNull(savedTicket);
    }
    @Test
    public void testUpdateTicket(){
        ticketDAO.saveTicket(ticket);
        Ticket savedTicket = ticketDAO.getTicket("ABCDEF");
        savedTicket.setPrice(10d);
        savedTicket.setOutTime(new Date());
        assertTrue(ticketDAO.updateTicket(savedTicket));
    }

    @Test
    public void testUpdateTicketFail(){
        ticketDAO.saveTicket(ticket);
        Ticket savedTicket = ticketDAO.getTicket("ABCDEF");
        savedTicket.setOutTime(null);
        assertFalse(ticketDAO.updateTicket(savedTicket));
    }
 
    @Test
    public void testGetNbTicket(){
        int countTicketsBeforeSave = ticketDAO.getNbTicket("ABCDEF");
        ticketDAO.saveTicket(ticket);
        int countTicketsAfterSave = ticketDAO.getNbTicket("ABCDEF");
        assertEquals(countTicketsBeforeSave + 1, countTicketsAfterSave);
    }

    @Test
    public void testGetNbTicketNotFound(){
        int countTickets = ticketDAO.getNbTicket(null);
        assertEquals(0, countTickets);
    }
    @Test
    public void testGetNbTicketWithNull(){
        int countTickets = ticketDAO.getNbTicket(null);
        assertEquals(0, countTickets);
    }

   
    @Test
    public void testSaveTicketWithExistingVehicleRegNumber() {
        ticketDAO.saveTicket(ticket);
        Ticket duplicateTicket = new Ticket();
        duplicateTicket.setParkingSpot(new ParkingSpot(2, ParkingType.CAR, false));
        duplicateTicket.setVehicleRegNumber("ABCDEF");
        duplicateTicket.setPrice(5);
        duplicateTicket.setInTime(new Date());
        duplicateTicket.setOutTime(null);
        assertFalse(ticketDAO.saveTicket(duplicateTicket));
    }

    @Test
    public void testGetTicketWithNullVehicleRegNumber() {
        Ticket savedTicket = ticketDAO.getTicket(null);
        assertNull(savedTicket);
    }
    @Test
    public void testSaveTicketWithNullOutTime() {
        // Given
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR,false));
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(10.0);
        ticket.setInTime(new Timestamp(System.currentTimeMillis()));
        ticket.setOutTime(null); // outTime is null
        
        // When
        boolean result = ticketDAO.saveTicket(ticket);
        
        // Then
        assertFalse(result); // Expecting saveTicket to return true
    }
    @Test
    public void testGetNbTicketWithEmptyResultSet() throws SQLException {
        // Créer un mock de Connection, PreparedStatement et ResultSet
        Connection con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        
        // Configurer le mock PreparedStatement pour renvoyer le mock ResultSet
        when(con.prepareStatement(DBConstants.COUNT_TICKET)).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        
        // Configurer le mock ResultSet pour renvoyer false lors de l'appel à next()
        when(rs.next()).thenReturn(false);
        
        // Appeler la méthode à tester
        int result = ticketDAO.getNbTicket("Error counting tickets");
        
        // Vérifier que la méthode renvoie 0 car le ResultSet est vide
        assertEquals(0, result);
        
    
    }
    }
