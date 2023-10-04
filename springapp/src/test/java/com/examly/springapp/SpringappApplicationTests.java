package com.examly.springapp;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SpringappApplicationTests {

    private static final String DB_URL = "jdbc:mysql://localhost/LTISDETJDBC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    private static Connection connection;
    private static Statement statement;
    private static ByteArrayOutputStream outputStream;

    @BeforeClass
    public static void setUp() throws SQLException {
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        statement = connection.createStatement();

        // Redirect console output to capture it
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterClass
    public static void tearDown() throws SQLException {
        statement.close();
        connection.close();

        // Restore the original console output
        System.setOut(System.out);
    }

    @Test
    public void Week1_Day6_testInsertHotel() {
        // Prepare input for inserting a hotel
        String input = "1\n1\nTest Hotel\nTest Location\n4.5\ntrue\ntrue\nfalse\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        SpringappApplication.main(new String[]{});

        // Verify that the output contains the inserted hotel's information
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("Hotel record inserted successfully."));
        assertTrue(consoleOutput.contains("ID: 1, HotelName: Test Hotel, Location: Test Location, Rating: 4.5, Pool: true, Gym: true, Spa: false"));
    }

    @Test
    public void Week2_Day1_testUpdateHotel() {
        // Prepare input for updating a hotel
        String input = "2\n1\nUpdated Hotel\nUpdated Location\n4.8\nfalse\nfalse\ntrue\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        SpringappApplication.main(new String[]{});

        // Verify that the output contains the updated hotel's information
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("Hotel record updated successfully."));
        assertTrue(consoleOutput.contains("ID: 1, HotelName: Updated Hotel, Location: Updated Location, Rating: 4.8, Pool: false, Gym: false, Spa: true"));
    }


    @Test
    public void Week2_Day1_testDisplayAllHotels() {
        // Insert some test hotels for display
        try {
            statement.executeUpdate("INSERT INTO hotels(id, hotelname, location, rating, pool, gym, spa) VALUES (3, 'Hotel 1', 'Location 1', 4.2, true, false, true)");
            statement.executeUpdate("INSERT INTO hotels(id, hotelname, location, rating, pool, gym, spa) VALUES (4, 'Hotel 2', 'Location 2', 3.9, false, true, false)");
            statement.executeUpdate("INSERT INTO hotels(id, hotelname, location, rating, pool, gym, spa) VALUES (5, 'Hotel 3', 'Location 3', 4.8, true, true, true)");
        } catch (SQLException e) {
            fail("Failed to insert test hotels for display.");
        }

        // Prepare input for displaying hotels
        String input = "3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        SpringappApplication.main(new String[]{});

        // Verify that the output contains the inserted hotel records
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("ID: 3, HotelName: Hotel 1, Location: Location 1, Rating: 4.2, Pool: true, Gym: false, Spa: true"));
        assertTrue(consoleOutput.contains("ID: 4, HotelName: Hotel 2, Location: Location 2, Rating: 3.9, Pool: false, Gym: true, Spa: false"));
        assertTrue(consoleOutput.contains("ID: 5, HotelName: Hotel 3, Location: Location 3, Rating: 4.8, Pool: true, Gym: true, Spa: true"));
    }

    @Test
    public void Week2_Day1_testDeleteHotel() {
        // Insert a hotel first for testing deletion
        try {
            statement.executeUpdate("INSERT INTO hotels(id, hotelname, location, rating, pool, gym, spa) VALUES (2, 'Test Hotel', 'Test Location', 4.5, true, true, false)");
        } catch (SQLException e) {
            fail("Failed to insert a test hotel.");
        }

        // Prepare input for deleting the test hotel
        String input = "4\n2\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        SpringappApplication.main(new String[]{});

        // Verify that the output contains the deletion confirmation message
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("Hotel record with ID 2 deleted successfully."));
    }

    @Test
    public void Week2_Day1_testInvalidOperation() {
        // Prepare input for an invalid operation
        String input = "5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        SpringappApplication.main(new String[]{});

        // Verify that the output contains an error message
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("Invalid operation number. Please try again."));
    }


}
