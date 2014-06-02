package com.insidion.anl.o3.Simulations;

import com.insidion.anl.o3.Controller;
import com.insidion.anl.o3.Simulations.enums.InsertType;
import org.apache.log4j.Logger;

import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by mitchell on 6/2/2014.
 */
public class DataInsertSimulation extends Simulation {
    static Logger log = Logger.getLogger(DataInsertSimulation.class.getName());
    private Map<InsertType, PreparedStatement> statementMap;
    private Random rand = new Random();

    public DataInsertSimulation(Connection con, Controller controller) {
        super(con, controller);
        this.statementMap = new HashMap<InsertType, PreparedStatement>();
    }

    @Override
    protected void runSimulation() {

        try {
            connection.setAutoCommit(false);

            /**
             * Insert new Address
             */
            PreparedStatement address = statementMap.get(InsertType.Adres);

            address.setString(1, "sluiskreek");
            address.setInt(2, 36);
            address.setString(3, "3079CN");
            address.setString(4, "Rotterdam");
            address.setString(5, "0104792055");
            address.executeUpdate();

            long returnedKeyAddress = getReturnedKey(address);

            /**
             * Insert new Student
             */

            PreparedStatement student = statementMap.get(InsertType.Student);
            student.setString(1, generateRandomString(7));
            student.setString(2, "Mitchell");
            student.setString(3, "");
            student.setString(4, "Herrijgers");
            student.setString(5, "man");
            student.setLong(6, returnedKeyAddress);
            student.executeUpdate();
            ResultSet returnedKeys = student.getGeneratedKeys();
            String returnedKeyStudent = "";
            if(returnedKeys.next()) {
                returnedKeyStudent = returnedKeys.getString(1);
                returnedKeys.close();
            }

            if(getNumberOfKlas() == 0 || shouldWeDoIt(1/30)) {
                createNewKlas();
            }

            PreparedStatement klasStudent = statementMap.get(InsertType.KlasStudenten);
            klasStudent.setString(1, getRandomKlas());
            klasStudent.setString(2, returnedKeyStudent);

            if(getNumberOfModules() == 0 || shouldWeDoIt(1/30)) {
                createNewModule();
            }

            if(shouldWeDoIt(1/30)) {
                String module = createNewModule();
                Statement st = connection.createStatement();
                st.execute("SELECT klascode FROM klas");
                ResultSet rs = st.getResultSet();
                PreparedStatement ps = this.statementMap.get(InsertType.KlasModule);
                while(rs.next()) {
                    if(shouldWeDoIt(0.15)) {
                        ps.setString(1, rs.getString(1));
                        ps.setString(2, module);
                        ps.executeUpdate();
                    }
                }
            }


            connection.commit();

        } catch (SQLException e) {
            log.warn("Exception occurred during execution!", e);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void prepareStatements() throws SQLException {
        this.statementMap.put(InsertType.Adres, connection.prepareStatement("INSERT INTO adresgegevens(straat, huisnummer, postcode, woonplaats, telefoon_nummer)" +
                "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS));
        this.statementMap.put(InsertType.Klas, connection.prepareStatement("INSERT INTO klas(klascode, start_datum, eind_datum)" +
                "VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS));
        this.statementMap.put(InsertType.KlasStudenten, connection.prepareStatement("INSERT INTO klas_studenten( klas_id, student_id)" +
                "VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS));
        this.statementMap.put(InsertType.Student, connection.prepareStatement("INSERT INTO student(studentnummer, voornaam, tussenvoegsel, achternaam, geslacht, adres_id)" +
                "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS));
        this.statementMap.put(InsertType.Module, connection.prepareStatement("INSERT INTO modules (modulecode, modulebeheerder, start_datum, eind_datum)" +
                "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS));
        this.statementMap.put(InsertType.KlasModule, connection.prepareStatement("INSERT INTO klas_modules (klas_id, module_id) " +
                "VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS));
    }

    private String generateRandomString(int length) {
        StringBuffer buffer = new StringBuffer();

        for(int i = 0; i < length; i++) {
            buffer.append(rand.nextInt(9));
        }

        return buffer.toString();
    }

    private String createNewModule() throws SQLException, ParseException {
        PreparedStatement module = statementMap.get(InsertType.Module);
        module.setString(1, generateRandomString(4));
        module.setString(2, "HERRM");
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
        module.setDate(3, new Date(formatter.parse("17-June-2014 02:10:15").getTime()));
        module.setDate(4, new Date(formatter.parse("17-June-2015 02:10:15").getTime()));
        module.executeUpdate();
        ResultSet returnedKeys = module.getGeneratedKeys();
        returnedKeys.next();
        return returnedKeys.getString(1);

    }

    private void createRelationStudentModule() {

    }

    private void createNewKlas() throws SQLException, ParseException {
        PreparedStatement klas = statementMap.get(InsertType.Klas);
        klas.setString(1, generateRandomString(5));
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
        klas.setDate(2, new Date(formatter.parse("17-June-2014 02:10:15").getTime()));
        klas.setDate(3, new Date(formatter.parse("17-June-2015 02:10:15").getTime()));
        klas.executeUpdate();
        klas.close();
    }

    private int getNumberOfKlas() throws SQLException {
        int number = 0;
        Statement st = connection.createStatement();
        st.execute("SELECT COUNT(*) FROM klas");
        ResultSet rs = st.getResultSet();
        if(rs.next()) {
            number = rs.getInt(1);
        }
        return number;
    }

    private int getNumberOfModules() throws SQLException {
        int number = 0;
        Statement st = connection.createStatement();
        st.execute("SELECT COUNT(*) FROM klas");
        ResultSet rs = st.getResultSet();
        if(rs.next()) {
            number = rs.getInt(1);
        }
        return number;
    }

    private String getRandomKlas() throws SQLException {
        Statement st = connection.createStatement();
        st.execute("SELECT klascode FROM klas");
        ResultSet rs = st.getResultSet();
        List<String> list = new LinkedList<String>();

        while(rs.next()) {
            list.add(rs.getString(1));
        }
        return list.get(rand.nextInt(list.size()));
    }

    private long getReturnedKey(PreparedStatement st) throws SQLException {
        ResultSet returnedKeys = st.getGeneratedKeys();
        long returnedKey = 0;
        if(returnedKeys.next()) {
            returnedKey = returnedKeys.getLong(1);
            returnedKeys.close();
        }
        return returnedKey;

    }
}
