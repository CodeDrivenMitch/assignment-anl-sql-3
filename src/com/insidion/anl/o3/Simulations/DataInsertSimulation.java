package com.insidion.anl.o3.Simulations;

import com.insidion.anl.o3.Controller;

import java.sql.Connection;

/**
 * Created by mitchell on 6/2/2014.
 */
public class DataInsertSimulation extends Simulation {

    public DataInsertSimulation(Connection con, Controller controller) {
        super(con, controller);
    }

    @Override
    protected void runSimulation() {

    }
}
