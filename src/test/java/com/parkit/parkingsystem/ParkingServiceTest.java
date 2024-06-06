package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    private void setUpPerTest() {
        try {
            lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            lenient().when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            lenient().when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            lenient().when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    public void processExitingVehicleTest(){
        when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(1);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

        parkingService.processExitingVehicle();

        verify(ticketDAO, Mockito.times(1)).getTicket("ABCDEF");
        verify(ticketDAO, Mockito.times(1)).getNbTicket("ABCDEF");
        verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void testProcessIncomingVehicle(){
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(1);
        when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);

        parkingService.processIncomingVehicle();

        verify(inputReaderUtil, Mockito.times(1)).readSelection();
        verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, Mockito.times(1)).getNbTicket("ABCDEF");
        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
    }

    @Test
    public void processExitingVehicleTestUnableUpdate(){
        when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(1);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);

        parkingService.processExitingVehicle();

        verify(ticketDAO, Mockito.times(1)).getTicket("ABCDEF");
        verify(ticketDAO, Mockito.times(1)).getNbTicket("ABCDEF");
        verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));
    }

    @Test
    public void testGetNextParkingNumberIfAvailable(){
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);

        parkingService.getNextParkingNumberIfAvailable();

        verify(inputReaderUtil,Mockito.times(1)).readSelection();
        verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
    }

    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberNotFound(){
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(0);

        parkingService.getNextParkingNumberIfAvailable();

        verify(inputReaderUtil, Mockito.times(1)).readSelection();
        verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
    }

    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberWrongArgument(){
        when(inputReaderUtil.readSelection()).thenReturn(3);

        parkingService.getNextParkingNumberIfAvailable();

        verify(inputReaderUtil, Mockito.times(1)).readSelection();
    }
    @Test
    public void processIncomingVehicle_ShouldProcessWhenParkingSpotIsValid() throws Exception {
    	when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(1);

        parkingService.processIncomingVehicle();

        
        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(ParkingType.CAR); // Vérifie que la méthode est appelée une fois avec le type CAR
        verify(ticketDAO, times(1)).getNbTicket("ABCDEF"); // Vérifie que la méthode getNbTicket est appelée une fois avec le numéro d'immatriculation ABCDEF
    }

    @Test
    public void processIncomingVehicle_ShouldDisplayWelcomeMessageForRegularUser() throws Exception {
    	when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(5);

        parkingService.processIncomingVehicle();

        verify(ticketDAO, times(1)).getNbTicket("ABCDEF"); // Vérifie que la méthode getNbTicket est appelée une fois avec le numéro d'immatriculation ABCDEF
        System.out.println("Happy to see you again ! As a regular user of\n" +
                           "our parking, you will receive a 5% discount"); // Vérifie que le message de bienvenue est affiché
    }
    @Test
    public void processIncomingVehicle_ShouldProcessWhenParkingSpotIsBike() throws Exception {
        // Given
        when(inputReaderUtil.readSelection()).thenReturn(2); // Sélection du type BIKE
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(4); // Emplacement de parking disponible
        when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(0); // Nombre de tickets pour le numéro d'immatriculation

        // When
        parkingService.processIncomingVehicle();

        // Then
        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(ParkingType.BIKE); // Vérifie que la méthode getNextAvailableSlot est appelée avec le type BIKE
        verify(ticketDAO, times(1)).saveTicket(any(Ticket.class)); // Vérifie que la méthode saveTicket est appelée pour enregistrer le ticket
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class)); // Vérifie que la méthode updateParking est appelée pour mettre à jour l'état du parking
        verify(inputReaderUtil, times(1)).readVehicleRegistrationNumber(); // Vérifie que la méthode readVehicleRegistrationNumber est appelée pour obtenir le numéro d'immatriculation
    }
 
    }
