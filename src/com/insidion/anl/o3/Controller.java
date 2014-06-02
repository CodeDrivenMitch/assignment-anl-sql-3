package com.insidion.anl.o3;

import com.insidion.anl.o3.Simulations.DataInsertSimulation;
import com.insidion.anl.o3.Simulations.DataRequestSimulation;
import com.insidion.anl.o3.Simulations.Simulation;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by mitchell on 6/2/2014.
 */
public class Controller {
    static Logger log = Logger.getLogger(Controller.class.getName());
    Connection connection = null;
    int finishedThreads = 0;
    String[] tables = new String[]{"STUDENT", "DOCENT", "ADRESGEGEVENS", "KLAS", "KLAS_STUDENTEN", "MODULES"};

    public Controller() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5433/Opdracht3", "anl", "anl");
        connection.setAutoCommit(false);
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
        finishedThreads++;

        if(finishedThreads == 5) {
            try {
                log.info("Cleaning up now...");
                cleanUp();
                log.info("Cleaning succeeded!");
            } catch (SQLException e) {
                log.warn("Cleanup failed!", e);
            }
        }

    }

    private void cleanUp() throws SQLException {
        Statement st = connection.createStatement();
        for(String table : tables) {
            st.execute("TRUNCATE TABLE " + table + " CASCADE");
        }
        connection.commit();
    }

}
