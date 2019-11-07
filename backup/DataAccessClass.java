package manifetproject2;

/*
 * @author Paul
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Paul
 */
public class DataAccessClass {

    //change your mysql database connection here
    private String StrUrl = "jdbc:mysql://localhost/openmrs";
    private String StrUid = "root";
    private String StrPwd = "test123";
    private Connection connection;

    //accessing the database
    DataAccessClass() throws SQLException {
        connection = DriverManager.getConnection(StrUrl, StrUid, StrPwd);
    }

    //selecting records from the database
    public ResultSet getPatientRecords(String sqlStatement) throws SQLException {

        ResultSet rs;

        PreparedStatement stmt = connection.prepareStatement(sqlStatement);

        rs = stmt.executeQuery();

        return rs;
    }

    //updating shipped records into the database
    public void updatePatientRecords(String sqlStatement) throws SQLException {
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlStatement);

            stmt.execute();
        } catch (Exception OC) {
            Logger.getLogger(DataAccessClass.class.getName()).log(Level.SEVERE, null, OC);
        }
    }

//    uploading Viral results to OpenMRS database
           
    public void uploadResults(String sqlStatement) throws SQLException {
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlStatement);

            stmt.execute();
        } catch (Exception OC) {
            Logger.getLogger(DataAccessClass.class.getName()).log(Level.SEVERE, null, OC);
        }
    }
}
