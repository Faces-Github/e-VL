package Faces_Manifest_Project;

/*
 * @author Paul
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.openmrs.api.context.Context;
//import org.openmrs.util.DatabaseUpdateException;
//import org.openmrs.util.InputRequiredException;
//import org.openmrs.util.OpenmrsUtil;
//import org.simpleframework.xml.*;

/**
 * @author Paul
 */
public class DataAccessClass {

    // Declare internal variables
    private String connURL = "jdbc:mysql://localhost:3306/openmrs";
    private String connUserName = "openmrs";
    private String connPassword = "openmrs";
    private String restOpenMRSURL = "http://localhost:8080/openmrs";
    private String restOpenMRSUserName = "openmrs";
    private String restOpenMRSPassword = "openmrs";
    private String restAmpathURL = "http://eid.ampath.or.ke:85/test/";
    private String restAmpathUserName = "emulwa@kemri-ucsf.org";
    private String restAmpathPassword = "mulwapass";
    private String restAmpathGET = "resultsapi.php";
    private String restAmpathPOST = "request_api.php";
    private String restAmpathAPIKey = "35243eba22";
    
    private Connection connection;
    private static Properties prop = new Properties();
    private static InputStream input = null;

    
     private static void loadProperties()
    {
        try {
 
		input = new FileInputStream("viral_load-runtime.properties");
 
		// load a properties file
		prop.load(input);
 
		// get the property value and print it out
		System.out.println(prop.getProperty("connection.username"));
		System.out.println(prop.getProperty("connection.password"));
		System.out.println(prop.getProperty("connection.url"));
		System.out.println(prop.getProperty("connection.resturl"));
		System.out.println(prop.getProperty("connection.restuser"));
		System.out.println(prop.getProperty("connection.restpass"));
 		System.out.println(prop.getProperty("connection.chaiurl"));
		System.out.println(prop.getProperty("connection.chaiuser"));
		System.out.println(prop.getProperty("connection.chaipass"));
		System.out.println(prop.getProperty("connection.chaiget"));
		System.out.println(prop.getProperty("connection.chaipost"));
		System.out.println(prop.getProperty("connection.chaiapikey"));
                
                // Load OpenMRS properties
//                InputStream inOpenMRS = new FileInputStream("C:\\Users\\admin\\AppData\\Roaming\\OpenMRS\\openmrs-runtime.properties");
//                prop.load(inOpenMRS);
//                Context.startup(prop.getProperty("connection.url"), prop.getProperty("connection.username"), prop.getProperty("connection.password"), prop);
//                Context.startup(prop);
                
//                File propsFile = new File("C:\\Users\\admin\\AppData\\Roaming\\OpenMRS", "openmrs-runtime.properties");
//                File propsFile = new File(OpenmrsUtil.getApplicationDataDirectory(), "openmrs-runtime.properties");
//                Properties props = new Properties();
//                OpenmrsUtil.loadProperties(props, propsFile);
//                Context.startup(prop.getProperty("connection.url"), prop.getProperty("connection.username"), prop.getProperty("connection.password"), prop);
//                Context.startup(prop);
  
	} 
//        catch (InputRequiredException irEx){
//            irEx.printStackTrace();
//        }
//        catch (DatabaseUpdateException dbEx){
//            dbEx.printStackTrace();
//        }
        catch (IOException ioEx) {
		ioEx.printStackTrace();
	} 
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    }
    //accessing the database
    DataAccessClass() throws SQLException {
        
        // Load the connection properties
        loadProperties();
        connURL = prop.getProperty("connection.url");
        connUserName = prop.getProperty("connection.username");
        connPassword = prop.getProperty("connection.password");
        restOpenMRSURL = prop.getProperty("connection.resturl");
        restOpenMRSUserName = prop.getProperty("connection.restuser");
        restOpenMRSPassword = prop.getProperty("connection.restpass");
        restAmpathURL = prop.getProperty("connection.chaiurl");
        restAmpathUserName = prop.getProperty("connection.chaiuser");
        restAmpathPassword = prop.getProperty("connection.chaipass");
        restAmpathGET = prop.getProperty("connection.chaiget");
        restAmpathPOST = prop.getProperty("connection.chaipost");
        restAmpathAPIKey  = prop.getProperty("connection.chaiapikey");
        
        connection = DriverManager.getConnection(connURL, connUserName, connPassword);
    }

    /*
    * return the connection REST URL
    */
    public String getRestURL(int iRestType) {
        
        String restURL = "";
        
        switch (iRestType) {
            case 0:
                restURL = restOpenMRSURL;
                break;
            case 1:
                restURL = restAmpathURL;
        }
        
        return  restURL;
        
    }

    /*
    * return the connection REST User name
    */
    public String getRestUserName(int iRestType) {

        String restUserName = "";
        
        switch (iRestType) {
            case 0:
                restUserName = restOpenMRSUserName;
                break;
            case 1:
                restUserName = restAmpathUserName;
        }
        
        return restUserName;
        
    }

    /*
    * return the connection REST Password
    */
    public String getRestPassword(int iRestType) {
        
        String restPassword = "";
        
        switch (iRestType) {
            case 0:
                restPassword = restOpenMRSPassword;
                break;
            case 1:
                restPassword = restAmpathPassword;
        }
        
        return restPassword;
        
    }

    /*
    * return the REST GET endpoint
    */
    public String getRestGET(int iRestType) {
        
        String restGET = "";
        
        switch (iRestType) {
            case 0:
//                restGET = restOpenMRSGET;
            case 1:
                restGET = restAmpathGET;
        }
        
        return restGET;
        
    }

    /*
    * return the  REST POST endpoint
    */
    public String getRestPOST(int iRestType) {
        
        String restPOST = "";
        
        switch (iRestType) {
            case 0:
//                restPOST = restOpenMRSGET;
            case 1:
                restPOST = restAmpathPOST;
        }
        
        return restPOST;
        
    }

    /*
        Return the AMPATH API Key
    */
    public String getRestAPIKey(){
        return restAmpathAPIKey;
    }
    
    /*
    * return the connection URL
    */
    public String getURL() {
        return connURL;
    }

    /*
    * return the connection Username
    */
    public String getUserName() {
        return connUserName;
    }

    /*
    * return the connection URL
    */
    public String getPassword() {
        return connPassword;
    }

    public List<String> getLocations() throws SQLException {

        List<String>location=new ArrayList<String>();
        ResultSet rs;
        
//        GET ALL Locations order by number of records. the one with many records will be the first one
        String sqlStatement="SELECT name FROM(Select name," +
"count(DISTINCT(e.patient_id)) As pats from location l " +
"INNER JOIN encounter e on e.location_id=l.location_id where l.retired=0 AND e.voided=0 " +
"GROUP BY name order by pats desc,name asc) AS fac LIMIT 1";

        PreparedStatement stmt = connection.prepareStatement(sqlStatement);

        rs = stmt.executeQuery();

        while(rs.next())
        {
            location.add(rs.getString("name"));
        }
        return location;
    }
    //selecting records from the database
    public ResultSet getPatientRecords(String sqlStatement) throws SQLException {

        ResultSet rs;

        PreparedStatement stmt = connection.prepareStatement(sqlStatement);
//        System.out.println(sqlStatement);
         rs = stmt.executeQuery();
//        while(rs.next()){
//            System.out.println("data1: "+rs.getString(1)+": "+rs.getString(2));
//        }
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

    public void uploadResults(String sqlStatement) throws SQLException {
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlStatement);

            stmt.execute();
        } catch (Exception OC) {
            Logger.getLogger(DataAccessClass.class.getName()).log(Level.SEVERE, null, OC);
        }
    }
}
