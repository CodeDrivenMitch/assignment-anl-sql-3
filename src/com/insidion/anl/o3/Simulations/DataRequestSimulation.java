package com.insidion.anl.o3.Simulations;

import com.insidion.anl.o3.Controller;
import com.insidion.anl.o3.Stopwatch;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import static java.lang.Thread.sleep;

public class DataRequestSimulation extends Simulation {

    public DataRequestSimulation(Connection con, Controller controller) {
        super(con, controller);
    }


    @Override
    protected void runSimulation() {
        try {
            Statement st = connection.createStatement();
            st.execute("SELECT studentnummer, voornaam, achternaam FROM student");
            ResultSet rs = st.getResultSet();

            if(rs.getFetchSize() == 0) return;
            for(int i = 0 ; i < (new Random()).nextInt(rs.getFetchSize()); i++ ) {
                rs.next();
            }

            st.execute("SELECT * FROM student WHERE voornaam="+rs.getString(2)+" AND achternaam="+rs.getString(3) + " LIMIT 1");
            rs = st.getResultSet();
            rs.next();

            st.execute("SELECT module_id FROM klas_studenten WHERE student_id="+rs.getString(1));
            rs = st.getResultSet();

            while(rs.next()) {
                st.execute("SELECT * FROM modules WHERE modulecode="+rs.getString(1));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void prepareStatements() {

    }
}
