package com.insidion.anl.o3;

import com.insidion.anl.o3.Simulations.DataInsertSimulation;
import com.insidion.anl.o3.Simulations.DataRequestSimulation;
import com.insidion.anl.o3.Simulations.Simulation;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by mitchell on 6/2/2014.
 */
public class Controller {
    static Logger log = Logger.getLogger(Controller.class.getName());
    Connection connection = null;

    public Controller() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/Opdracht3", "anl", "anl");
        log.info("Database conection initialized");
    }

    public void startSimulations() {
        for(int i = 0; i < 3; i++) {
            new Thread(new DataInsertSimulation(connection, this)).start();
            new Thread(new DataRequestSimulation(connection, this)).start();
        }
    }

    public void simulationFinished(Simulation simulation) {
        log.info(simulation.getClass().getSimpleName() + " ended, Average: " + simulation.getAverageResult());

    }

}
