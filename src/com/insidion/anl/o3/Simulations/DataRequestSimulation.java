package com.insidion.anl.o3.Simulations;

import com.insidion.anl.o3.Controller;
import com.insidion.anl.o3.Stopwatch;

import java.sql.Connection;

import static java.lang.Thread.sleep;

/**
 * Created by mitchell on 6/2/2014.
 */
public class DataRequestSimulation extends Simulation {

    public DataRequestSimulation(Connection con, Controller controller) {
        super(con, controller);
    }


    @Override
    protected void runSimulation() {
        try {
            sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
