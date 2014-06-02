package com.insidion.anl.o3;

import java.sql.SQLException;

public class Main {


    public static void main(String[] args) throws ClassNotFoundException, SQLException {
	    // Load Postgres JDBC Driver
        Class.forName("org.postgresql.Driver");

        // Make connection
        Controller controller = new Controller();
        controller.startSimulations();
    }
}
