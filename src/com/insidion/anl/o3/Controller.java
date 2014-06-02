package com.insidion.anl.o3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by mitchell on 6/2/2014.
 */
public class Controller {
    Connection connection = null;

    public Controller() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/Opdracht3", "anl", "anl");
    }
}
