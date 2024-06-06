package com.parkit.parkingsystem.integration.config;

import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.config.DataBaseConfig;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBaseConfigTest {

    @Test
    public void testGetConnection() throws ClassNotFoundException, SQLException {
        // Given
        DataBaseConfig dataBaseConfig = new DataBaseConfig();
        
        // When
        Connection connection = dataBaseConfig.getConnection();
        
        // Then
        assertNotNull(connection);
        assertFalse(connection.isClosed());
        
        // Clean up
        dataBaseConfig.closeConnection(connection);
    }

    @Test
    public void testCloseConnection() throws SQLException {
        // Given
        DataBaseConfig dataBaseConfig = new DataBaseConfig();
        Connection connection = mock(Connection.class);
        
        // When
        dataBaseConfig.closeConnection(connection);
        
        // Then
        verify(connection, times(1)).close();
    }

    @Test
    public void testClosePreparedStatement() throws SQLException {
        // Given
        DataBaseConfig dataBaseConfig = new DataBaseConfig();
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        
        // When
        dataBaseConfig.closePreparedStatement(preparedStatement);
        
        // Then
        verify(preparedStatement, times(1)).close();
    }

    @Test
    public void testCloseResultSet() throws SQLException {
        // Given
        DataBaseConfig dataBaseConfig = new DataBaseConfig();
        ResultSet resultSet = mock(ResultSet.class);
        
        // When
        dataBaseConfig.closeResultSet(resultSet);
        
        // Then
        verify(resultSet, times(1)).close();
    }
    @Test
    public void testCloseConnectionWithNull() {
        // Given
        DataBaseConfig dataBaseConfig = new DataBaseConfig();
        
        // When
        dataBaseConfig.closeConnection(null);
        
        // Then
        // No exception should be thrown
    }

    @Test
    public void testClosePreparedStatementWithNull() {
        // Given
        DataBaseConfig dataBaseConfig = new DataBaseConfig();
        
        // When
        dataBaseConfig.closePreparedStatement(null);
        
        // Then
        // No exception should be thrown
    }

    @Test
    public void testCloseResultSetWithNull() {
        // Given
        DataBaseConfig dataBaseConfig = new DataBaseConfig();
        
        // When
        dataBaseConfig.closeResultSet(null);
        
        // Then
        // No exception should be thrown
    }
}
