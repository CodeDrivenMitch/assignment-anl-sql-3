package com.insidion.anl.o3.Simulations;

import com.insidion.anl.o3.Controller;
import com.insidion.anl.o3.Stopwatch;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by mitchell on 6/2/2014.
 */
public abstract class Simulation implements Runnable {
    private List<Long> results = new ArrayList<Long>(600);
    private Controller controller;
    private Stopwatch stopwatch;
    private int runs = 0;

    protected Connection connection;


    public Simulation(Connection con, Controller controller) {
        this.connection = con;
        this.controller = controller;
        this.stopwatch = new Stopwatch();
    }

    protected void addResult(long result) {
        this.results.add(result);
    }

    public List<Long> getResults() {
        return this.results;
    }

    public int getAverageResult() {
        long total = 0;
        int number = 0;

        for (long res : this.getResults()) {
            total += res;
            number++;
        }

        return (int) (total / number);
    }

    protected abstract void runSimulation();

    @Override
    public void run() {
        while (runs < 600) {
            stopwatch.start();

            runSimulation();

            addResult(stopwatch.stopAndResult());
            runs++;
        }

        controller.simulationFinished(this);
    }

    protected boolean ShouldWeDoIt(double chance) {
        return chance > (new Random().nextDouble());
    }
}
