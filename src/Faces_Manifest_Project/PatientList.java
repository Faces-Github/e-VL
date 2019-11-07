package Faces_Manifest_Project;

/*
 * @author Paul
 */
import static Faces_Manifest_Project.Global.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.http.entity.StringEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import java.io.BufferedReader;
import java.io.FileReader;
import static java.util.Collections.list;
import org.json.JSONArray;
import org.json.JSONObject;


public class PatientList {

    // Declare internal variables
    PatientDetails patientInfo;
    List<PatientDetails> vlPatientList = new ArrayList<>();
    DataAccessClass dao;
    String defaultSampleType = "Frozen Plasma";
    String viralLoadResult = "";
    String VLResultUUID = "";
    String VLLogCopiesUUID = "";
    String logDate = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date());

    
    /*
        Class Constructor
    */
    public PatientList() throws SQLException {
        try {
            dao = new DataAccessClass();
        } catch (Exception exDataError) {
            System.out.println(exDataError);
        }
    }

    public List<PatientDetails> getShippedManifestList() throws SQLException {
        try {
            // Get the patients list
            GetPatients(true);
        } catch (Exception pl) {
            Logger.getLogger(PatientList.class.getName()).log(Level.SEVERE, null, pl);
        }

        //Return the list
        return vlPatientList;

    }

    public List<PatientDetails> getPatientinfo() throws SQLException {
        try {
            // Get the patients list
            GetPatients(false);
        } catch (Exception pl) {
            Logger.getLogger(PatientList.class.getName()).log(Level.SEVERE, null, pl);
        }

        //Return the list
        return vlPatientList;

    }

    /*
        Get list of patients who have VL requests pending shipment.
        List used when generating manifests for shipping to Lab.
        
        Edwin 11Mar16: Modified to take in a parameter that specifies if the list is for
                        patients with VL pending shipment or already shipped.
                        shipStatus = True: List of shipped VLs
                        shipStatus = False: List of pending VLs
    
    */
    private void GetPatients(boolean shipStatus) throws SQLException {
        
        // Determine what value shipped should take
        String shipStatusText = shipStatus ? "Yes" : "No";
        
        try {
            
            // Instantiate resultset object
            ResultSet rsPatients;
//            System.out.println("checking patients .....");

            // SQL Statement modifed to collect more VL data for the patients
            // Rugut added 03/04/2017 datepicker code
                 Date startDate = (Date) VLMainForm.ShippingStartDate.getDate();
                 Date endDate = (Date) VLMainForm.ShippingEndDate.getDate();
                 DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                 vlShippingStartDate =  df.format(startDate);//txtStartDate.getText();
                 vlShippingEndDate = df.format(endDate);//txtEndDate.getText();
// Rugut added this to close the declartion 
            String sqlStatement = "SELECT  " +
                        "	MAX(pi.identifier) AS PatientID, " +
                        "	COALESCE(CONCAT(pn.given_name,' ',pn.middle_name,' ',pn.family_name),CONCAT(pn.given_name,' ',pn.family_name)) AS Patient_Names, " +
                        "	p.uuid AS PatientUUID, " +
                        "	l.name AS Facility, " +
                        "	osd.sample_date,  " +
                        "	ost.sample_type, " +
                        "	ost.sample_type_name, " +
                        "	oj1.justification_code1, " +
                        "	oj2.justification_code2, " +
                        "       MID(MAX(CONCAT(e.encounter_datetime,IF(o.concept_id IN (1836,5272) AND o.value_coded IN (1065,6847), 'Yes', 'No'))),20) AS Pregnant, " +
                        "       MID(MAX(CONCAT(e.encounter_datetime,IF(o.concept_id = 5632 AND o.value_coded = 1065, 'Yes', 'No'))),20) AS BreastFeeding, " +
                        "	MAX(IF(o.concept_id=7468,cn.name,NULL)) AS Shipped  " +
                        "FROM encounter e  " +
                        "LEFT JOIN  " +
                        "( " +
                        "	SELECT  " +
                        "		o.encounter_id, " +
                        "		MAX(IF(o.concept_id=7467,DATE(o.value_datetime),NULL)) AS sample_date  " +
                        "	FROM obs o " +
                        "	WHERE o.concept_id IN (7467) " +
                        "	GROUP BY 1 " +
                        ") osd ON e.encounter_id = osd.encounter_id " +
                        "LEFT JOIN  " +
                        "( " +
                        "	SELECT  " +
                        "		o.encounter_id, " +
                        "		MAX(IF(o.concept_id=7701,o.value_coded,NULL)) AS sample_type, " +
                        "		MAX(IF(o.concept_id=7701,cn.name,NULL)) AS sample_type_name " +
                        "	FROM obs o " +
                        "	INNER JOIN concept_name cn ON o.value_coded = cn.concept_id " +
                        "	WHERE o.concept_id IN (7701) " +
                        "	GROUP BY 1 " +
                        ") ost ON e.encounter_id = ost.encounter_id " +
                        "LEFT JOIN  " +
                        "( " +
                        "SELECT  o.encounter_id, max(IF(o.concept_id in(8413), case o.value_coded " +
"WHEN 8406 THEN 1 WHEN 8407 THEN 2 WHEN 8408 THEN 3 WHEN 8409 THEN 4 WHEN 8410 THEN 5 WHEN 8412 THEN 6 " +
"ELSE NULL END,null)) AS justification_code1 FROM obs o WHERE o.concept_id IN (8413) GROUP BY 1 " +
                        ") oj1 ON e.encounter_id = oj1.encounter_id " +
                        "LEFT JOIN  " +
                        "( " +
                        "	SELECT  " +
                        "		o.encounter_id, " +
                        "		MAX(IF(o.concept_id=7703,o.value_numeric,NULL)) AS justification_code2 " +
                        "	FROM obs o " +
                        "	WHERE o.concept_id IN (7703) " +
                        "	GROUP BY 1 " +
                        ") oj2 ON e.encounter_id = oj2.encounter_id " +
                        "JOIN person_name pn ON pn.person_id=e.patient_id  " +
                        "JOIN person p ON e.patient_id = p.person_id  " +
                        "JOIN location l ON l.location_id=e.location_id AND l.name = \""+ VLMainForm.cboLocation.getSelectedItem().toString() + "\" " +
                        "JOIN patient_identifier `pi` ON pi.patient_id=e.patient_id AND pi.identifier_type=3 AND pi.voided=0  " +
                        "JOIN obs o ON o.encounter_id=e.encounter_id AND o.concept_id IN (7467,7468,1836,5272,5632,8413) AND o.voided=0  " +
                        "JOIN concept_name cn ON cn.concept_id=o.value_coded AND cn.concept_name_type='Fully_specified'  " +
                        "JOIN encounter_type et ON et.encounter_type_id=e.encounter_type AND e.voided=0  " +
                        "WHERE encounter_type=35  " +
                        "GROUP BY e.encounter_id  " +
                        "HAVING Shipped = '" + shipStatusText + "' and sample_date between '"+ vlShippingStartDate +"' and '"+ vlShippingEndDate +"' ";
            
            
            // Extract list of patients from database
            rsPatients = dao.getPatientRecords(sqlStatement);

            // Clear the existing list
            vlPatientList.clear();

            // Load the patients on the arraylist
            while (rsPatients.next()) {
                patientInfo = new PatientDetails();

                patientInfo.setPatientID(rsPatients.getString("PatientID"));
                patientInfo.setPatientNames(rsPatients.getString("patient_Names"));
                patientInfo.setPatientUUID(rsPatients.getString("PatientUUID"));
                patientInfo.setFacility(rsPatients.getString("Facility"));
                patientInfo.setSampleDate(rsPatients.getDate("sample_date"));
                patientInfo.setSampleType(rsPatients.getString("sample_type_name"));
                patientInfo.setJustificationCode1(rsPatients.getInt("justification_code1"));
                patientInfo.setJustificationCode2(rsPatients.getInt("justification_code2"));
                patientInfo.setPregnancyStatus(rsPatients.getString("Pregnant"));
                patientInfo.setBreastFeeding(rsPatients.getString("BreastFeeding"));
                patientInfo.setShipped(rsPatients.getBoolean("Shipped"));
                patientInfo.setMFLCode(getFacilityMFLCode(patientInfo.getFacility()));
                vlPatientList.add(patientInfo);
            }
        } catch (Exception pl) {
            Logger.getLogger(PatientList.class.getName()).log(Level.SEVERE, null, pl);
        }

    }

    // (Un)Select Shipped flag for patient
    public void SelectAll(boolean selectShipped){
        
        try {
            for (PatientDetails patient : vlPatientList) {
                patient.setShipped(selectShipped);
                }
        } 
        catch (Exception sm) {
            Logger.getLogger(PatientList.class.getName()).log(Level.SEVERE, null, sm);
        }
    }
    
    //updating Shipped records
    public void SaveManifest() throws SQLException {
        try {
            for (PatientDetails patient : vlPatientList) {
                if (patient.isShipped()) {

                    String sql_update = "update obs o \n";
                    sql_update += "join patient_identifier pi on pi.patient_id=o.person_id \n";
                    sql_update += "set o.value_coded=1065 \n";
                    sql_update += "where o.concept_id=7468 and pi.identifier='" + patient.getPatientID() + "' and obs_datetime='" + patient.getSampleDate() + "'";

                    dao.updatePatientRecords(sql_update);
                }

            }
        } catch (Exception sm) {
            Logger.getLogger(PatientList.class.getName()).log(Level.SEVERE, null, sm);
        }

    }

    /*
        Generate the Viral Load Manifest file
    */
    public void generateManifest() throws SQLException, IOException {

        try {
            
            // Open File Writer
            JFileChooser jfChooseManifestFile = new JFileChooser();
            
            jfChooseManifestFile.showSaveDialog(jfChooseManifestFile);
            //jfChooseManifestFile.addChoosableFileFilter(new ExtensionFileFilter(new String[]{".CSV" },"Comma Delimited File (*.CSV)"));
            
            File flManifestFile = jfChooseManifestFile.getSelectedFile();
            
            FileWriter fileWriter = new FileWriter(flManifestFile+".csv");
            
            // Insert columns into file
//            fileWriter.write("Patient ID, Names,Facility,Date,Sample_Type,Result\n");
            fileWriter.write("PatientID, Names, Sex, Birth Date, Sample Type, Collection Date, ART Initiation Date, Current Regimen, Current Regimen Line, Justification, Facility, MFL Code, PMTCT, BreastFeeding\n");

            for (PatientDetails patient : vlPatientList) {

                if (patient.isShipped()) {

                    fileWriter.write("\"" + patient.getPatientID() + "\"," 
                            + "\"" + patient.getPatientNames() + "\"," 
                            + "\"" + patient.getGender() + "\"," 
                            + "\"" + patient.getBirthDate() + "\"," 
                            + "\"" + patient.getSampleType() + "\"," 
                            + "\"" + patient.getSampleDate() + "\"," 
                            + "\"" + patient.getDateStartedART() + "\"," 
                            + "\"" + patient.getCurrentRegimen().replace(",", ";") + "\"," 
                            + "\"" + String.valueOf(patient.getRegimenLine()) + "\"," 
                            + "\"" + patient.getJustificationCode1() + "\"," 
                            + "\"" + patient.getFacility() + "\"," 
                            + "\"" + getPatientMFLCode(patient.getPatientUUID()) + "\"," 
                            + "\"" + patient.getPregnancyStatus() + "\"," 
                            + "\"" + patient.getBreastFeeding() + "\"\n");
                }
            }

            // Close writer
            fileWriter.close();

        } 
        catch (IOException e) {
            System.out.println(e);
        }
        catch (NullPointerException exNull) {
            System.out.println(exNull);
        }
        catch(Exception exError){
            exError.getCause();
            exError.printStackTrace();
        }            

    }

    public List<PatientDetails> getImportRecords() throws SQLException, IOException, ParseException {

        try {

            // Declare variables for storing column indexes
            int iPatientID_Col = 0, iResult_Col = 0, iResultLog_Col = 0, iResultDate_Col = 0;

            // Clear patients list
            System.out.println("Clear existing patients list...");
            vlPatientList.clear();

            // Open the source file with VL results and load patients info into arraylist
//            JFileChooser excelFile = new JFileChooser();
//            excelFile.showOpenDialog(null);
//            dataFile = excelFile.getSelectedFile();
            InputStream input = new BufferedInputStream(new FileInputStream(dataFile));
            POIFSFileSystem fs = new POIFSFileSystem(input);
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);

            // Get a rows iterator
            Iterator rows = sheet.rowIterator();

            // Read header row to determine required data column positions.
            // The row headers may be different from what is being used below and the source file
            // row header labels may need to be changed to work with this code.
            // 
            // For a future release, provide an interface for mapping these required fields to what
            // is in the source file.
            HSSFRow rowPatientRecord = (HSSFRow) rows.next();

            for (int iCount = 0; iCount < rowPatientRecord.getLastCellNum(); iCount++) {
                
                switch (rowPatientRecord.getCell(iCount).getStringCellValue().trim()) {

                    case "Patient CCC No":
                        iPatientID_Col = iCount;
                        break;
                    case "Viral Load Result":
                        iResult_Col = iCount;
                        break;
                    case "Log10 Copies/ml":
                        iResultLog_Col = iCount;
                        break;
                    case "Collection Date":
                        iResultDate_Col = iCount;
                        break;

                }
            }

            // Get and load patient info
            System.out.println("Loading results from selected file..." + dataFile.getName());
            while (rows.hasNext()) {
                
                rowPatientRecord = (HSSFRow) rows.next();

                Cell vlPatientID = rowPatientRecord.getCell(iPatientID_Col);
                Cell vlResult = rowPatientRecord.getCell(iResult_Col);
                Cell vlLogCopies = rowPatientRecord.getCell(iResultLog_Col);
                Cell vlSampleCollectDate = rowPatientRecord.getCell(iResultDate_Col);

                patientInfo = new PatientDetails();

                // Patient ID
                if (vlPatientID != null) {
                    switch (vlPatientID.getCellType()) {

                        case Cell.CELL_TYPE_NUMERIC:
                            //System.out.print(vlPatientID.getNumericCellValue() + "\t");
                            patientInfo.setPatientID(Double.toString(vlPatientID.getNumericCellValue()));
                            patientInfo.correctCheckDigit();
                            
                            break;
                        case Cell.CELL_TYPE_STRING:
                            //System.out.print(vlPatientID.getStringCellValue() + "\t");
                            patientInfo.setPatientID(vlPatientID.getStringCellValue());
                            patientInfo.correctCheckDigit();
                            break;
                    }
                }

                // Viral Load Result
                if (vlResult != null) {
                    switch (vlResult.getCellType()) {

                        case Cell.CELL_TYPE_NUMERIC:
                            //System.out.print(VLResult.getNumericCellValue() + "\t");
                            patientInfo.setVLResult(Double.toString(vlResult.getNumericCellValue()));
                            break;
                        case Cell.CELL_TYPE_STRING:
                            // System.out.print(VLResult.getStringCellValue() + "\t");
                            patientInfo.setVLResult(vlResult.getStringCellValue());
                            break;
                        default:
                            patientInfo.setVLResult("");
                            break;
                    }
                }

                // Viral Load Log Copies
                if (vlLogCopies != null) {
                    switch (vlLogCopies.getCellType()) {

                        case Cell.CELL_TYPE_NUMERIC:
                            // System.out.print(resultLog.getNumericCellValue() + "\t");
                            patientInfo.setLogResult(Double.toString(vlLogCopies.getNumericCellValue()));
                            break;
                        case Cell.CELL_TYPE_STRING:
                            //System.out.print(resultLog.getStringCellValue() + "\t");
                            patientInfo.setLogResult(vlLogCopies.getStringCellValue());
                            break;
                        default:
                            patientInfo.setLogResult("");
                            break;


                    }
                }
                
                // Sample Collection Date
                if (vlSampleCollectDate != null) {

                    DataFormatter df = new DataFormatter();
                    String sRunDate = df.formatCellValue(vlSampleCollectDate);
                    DateFormat formatter = new SimpleDateFormat("MM/dd/yy"); //format 
                    Date dtRunDate = formatter.parse(sRunDate);
                    java.sql.Date dtSQLDate = new java.sql.Date(dtRunDate.getTime());

                    patientInfo.setDateRun(dtSQLDate);

                    // Get the patient sample data
                    getSampleDate(patientInfo.getPatientID(), dtSQLDate.toString());

                    vlPatientList.add(patientInfo);

                }

            }
        } catch (Exception eX) {
            System.out.println(eX);
        }

        // Remove already imported records
        System.out.println("Checking for and removing already imported records...");
        removeImportedRecords();
                
        // Remove Invalid records
        System.out.println("Checking for and removing Invalid records...");
        removeInvalidRecords();
                
        // Display total records
        System.out.println("Total Records For Import: " + vlPatientList.size());
        
        return vlPatientList;

    }

    public List<PatientDetails> getImportManifest() throws SQLException, IOException, ParseException {

        try {

            // Declare variables for storing column indexes
            // PatientID, Names, Sex, Birth Date, Sample Type, Collection Date, ART Initiation Date, Current Regimen, Current Regimen Line, Justification, Facility, MFL Code
            BufferedReader brManifest = null;
            String line = "", csvSplitBy = ",", sampleDate;
            int colPatientID = 0, colPatientNames = 1, colSex = 2, colBirthDate = 3, colSampleType = 4, colCollectDate = 5, 
                    colARTStartDate = 6, colCurrRegimen = 7, colCurrRegimenLine = 8, colJustify = 9, colFacility = 10, colMFLCode = 11, colPMTCT = 12, colBreastFeeding = 13;

            // Clear patients list
            System.out.println("Clear existing patients list...");
            vlPatientList.clear();

            // Open the source file with VL results and load patients info into arraylist

            // Read data from CSV file
            brManifest = new BufferedReader(new FileReader(dataFile));
            
            // Read out the header row
            line = brManifest.readLine();
            
            while ((line = brManifest.readLine()) != null) {
                
                String[] patientData = line.split(csvSplitBy);
                
                patientInfo = new PatientDetails();

                patientInfo.setPatientID(patientData[0].replace("\"", ""));
                patientInfo.setPatientNames(patientData[1].replace("\"", ""));
                patientInfo.setGender(patientData[2].replace("\"", ""));
                patientInfo.setBirthDate(patientData[3].replace("\"", ""));
                patientInfo.setSampleType(patientData[4].replace("\"", ""));
                patientInfo.setSampleDate(VL_DATE_FORMAT.parse(patientData[5].replace("\"", "")));
                getSampleDate(patientInfo.getPatientID(), patientData[5].replace("\"", ""));
                patientInfo.setDateStartedART(patientData[6].replace("\"", ""));
                patientInfo.setCurrentRegimen(patientData[7].replace(";", ",").replace("\"", ""));
                patientInfo.setRegimenLine(Integer.parseInt(patientData[8].replace("\"", "")));
                patientInfo.setJustificationCode1(Integer.parseInt(patientData[9].replace("\"", "")));
                patientInfo.setFacility(patientData[10].replace("\"", ""));
                patientInfo.setMFLCode(patientData[11].replace("\"", ""));
                patientInfo.setPregnancyStatus(patientData[12].replace("\"", ""));
                patientInfo.setBreastFeeding(patientData[13].replace("\"", ""));
                patientInfo.setShipped(true);
                patientInfo.setComments("");

                // Add to collection
                vlPatientList.add(patientInfo);

            }
        } 
        catch (Exception eX) {
            System.out.println(eX);
        }

        // Remove Invalid records
        System.out.println("Checking for and removing Invalid records...");
        removeInvalidRecords();
                
        // Display total records
        System.out.println("Total Records For Import: " + vlPatientList.size());
        
        return vlPatientList;

    }

    /*
     * Get the specified patient's sample date
     */
//    private void getSampleDate(String PatientID, String SampleDate) throws SQLException {
//
//        try {
//
//            ResultSet rs;
//
//            // Updated SQL statement to pick more fields needed in the REST command
//            String sqlStatement = "SELECT pi.patient_id, ppt.uuid AS patient_uuid, (IF(o.concept_id=7467,o.value_datetime,NULL)) AS sample_date, " + 
//                            "(IF(o.concept_id=7701,o.value_coded,NULL)) AS sample_type, MAX(IF(o.concept_id=7701,cn.name,NULL)) AS sample_type_name, " +
//                            "ppr.uuid AS provider_uuid, e.uuid AS encounter_uuid, l.uuid AS location_uuid, l.name AS Facility " +
//                            "FROM patient_identifier pi " +
//                            "JOIN encounter e ON e.patient_id=pi.patient_id AND pi.identifier='" + PatientID + "' " +
//                            "JOIN person ppt ON e.patient_id = ppt.person_id " +
//                            "JOIN `encounter_provider` ep ON e.encounter_id = ep.encounter_id " +
//                            "JOIN person ppr ON ep.provider_id = ppr.person_id " +
//                            "JOIN obs o ON e.encounter_id = o.encounter_id AND o.concept_id IN (7467, 7701) " +
//                            "LEFT OUTER JOIN concept_name cn ON o.value_coded = cn.concept_id " +
//                            "JOIN concept c ON o.concept_id = c.concept_id " +
//                            "JOIN location l ON l.location_id=e.location_id " +
//                            "WHERE e.encounter_type=35 AND o.value_datetime = '" + SampleDate + "' " +
//                            "GROUP BY e.encounter_id, pi.identifier";
//
//            rs = dao.getPatientRecords(sqlStatement);
//            
//            // If no record has been returned, exit
////            if(rs.isBeforeFirst()){
////                patientInfo.setComments("Invalid Patient Record. Corresponding VL request not found.");
////                return;
////            }
//            
//            rs.first();
//
//            patientInfo.setPatientUUID(rs.getString("patient_uuid"));
//            patientInfo.setProviderUUID(rs.getString("provider_uuid"));
//            patientInfo.setEncounterUUID(rs.getString("encounter_uuid"));
//            patientInfo.setFacilityUUID(rs.getString("location_uuid"));
//            patientInfo.setSampleDate(rs.getDate("sample_date"));
//            patientInfo.setSampleType(rs.getInt("sample_type"));
//            patientInfo.setFacility(rs.getString("Facility"));
//
//        } 
//        catch (Exception pl) {
//            patientInfo.setComments("Invalid Patient ID or Missing VL Request Record.");
////            Logger.getLogger(PatientList.class.getName()).log(Level.SEVERE, null, pl);
////            pl.printStackTrace();
//        }
//
//    }

//    private void getSampleData(String PatientID) throws SQLException {
//        try {
//
//            ResultSet rs;
//
//            // Updated SQL statement to pick more fields needed in the REST command
//            String sqlStatement = "SELECT  " +
//                            "	pi.patient_id,  " +
//                            "	e.encounter_id, " +
//                            "	ppt.uuid AS patient_uuid,  " +
//                            "	MAX(IF(o.concept_id = 7467,DATE(o.value_datetime),NULL)) AS sample_date,  " +
//                            "	MAX(IF(o.concept_id = 7701,o.value_coded,NULL)) AS sample_type, " +
//                            "	MAX(IF(o.concept_id = 7701,cn.name,NULL)) AS sample_type_name, " +
//                            "	MAX(IF(o.concept_id = 7702,o.value_numeric,NULL)) AS justification_code1, " +
//                            "	MAX(IF(o.concept_id = 7703,o.value_numeric,NULL)) AS justification_code2, " +
//                            "	ppr.uuid AS provider_uuid,  " +
//                            "	e.uuid AS encounter_uuid,  " +
//                            "	l.uuid AS location_uuid,  " +
//                            "	l.name AS Facility " +
//                            "FROM patient_identifier `pi` " +
//                            "JOIN encounter e ON e.patient_id = pi.patient_id AND pi.identifier = '" + PatientID + "' " +
//                            "JOIN person ppt ON e.patient_id = ppt.person_id " +
//                            "JOIN `encounter_provider` ep ON e.encounter_id = ep.encounter_id " +
//                            "JOIN person ppr ON ep.provider_id = ppr.person_id " +
//                            "JOIN obs o ON e.encounter_id = o.encounter_id AND o.concept_id IN(7467, 7701, 7702, 7703) " +
//                            "LEFT OUTER JOIN concept_name cn ON o.value_coded = cn.concept_id " +
//                            "JOIN concept c ON o.concept_id = c.concept_id " +
//                            "JOIN location l ON l.location_id=e.location_id " +
//                            "WHERE e.encounter_type=35 " +
//                            "GROUP BY e.encounter_id, pi.identifier";
//
//            rs = dao.getPatientRecords(sqlStatement);
//            
//            // If no record has been returned, exit
////            if(rs.isBeforeFirst()){
////                patientInfo.setComments("Invalid Patient Record. Corresponding VL request not found.");
////                return;
////            }
//            
//            rs.first();
//
//            // Modified: 20 July 2016
//            // Added While() to loop through to get the needed record
//            while(rs.getDate("sample_date").toString().equalsIgnoreCase(SampleDate)){
//                
//                patientInfo.setPatientUUID(rs.getString("patient_uuid"));
//                patientInfo.setProviderUUID(rs.getString("provider_uuid"));
//                patientInfo.setEncounterUUID(rs.getString("encounter_uuid"));
//                patientInfo.setFacilityUUID(rs.getString("location_uuid"));
//                patientInfo.setSampleDate(rs.getDate("sample_date"));
//                patientInfo.setSampleType(rs.getString("sample_type_name"));
//                patientInfo.setJustificationCode1(rs.getInt("justification_code1"));
//                patientInfo.setJustificationCode2(rs.getInt("justification_code2"));
//                patientInfo.setFacility(rs.getString("Facility"));
//                
//                break;
//                
//            }
//
//        } 
//        catch (Exception pl) {
//            patientInfo.setComments("Invalid Patient ID or Missing VL Request Record.");
////            Logger.getLogger(PatientList.class.getName()).log(Level.SEVERE, null, pl);
////            pl.printStackTrace();
//        }
//
//    }
    
    private void getSampleDate(String PatientID, String SampleDate) throws SQLException {

        try {

            ResultSet rs;

            // Updated SQL statement to pick more fields needed in the REST command
            String sqlStatement = "SELECT pi.patient_id, ppt.uuid AS patient_uuid, (IF(o.concept_id=7467,o.value_datetime,NULL)) AS sample_date, " + 
                            "ppr.uuid AS provider_uuid, e.uuid AS encounter_uuid, l.uuid AS location_uuid, l.name AS Facility " +
                            "FROM patient_identifier pi " +
                            "JOIN encounter e ON e.patient_id=pi.patient_id AND pi.identifier='" + PatientID + "' " +
                            "JOIN person ppt ON e.patient_id = ppt.person_id " +
                            "JOIN `encounter_provider` ep ON e.encounter_id = ep.encounter_id " +
                            "JOIN person ppr ON ep.provider_id = ppr.person_id " +
                            "JOIN obs o ON e.encounter_id = o.encounter_id AND o.concept_id = 7467 " +
                            "JOIN concept c ON o.concept_id = c.concept_id " +
                            "JOIN location l ON l.location_id=e.location_id " +
                            "WHERE e.encounter_type=35 AND o.value_datetime = '" + SampleDate + "' " +
                            "GROUP BY e.encounter_id, pi.identifier";

            rs = dao.getPatientRecords(sqlStatement);
            
            // If no record has been returned, exit
//            if(rs.isBeforeFirst()){
//                patientInfo.setComments("Invalid Patient Record. Corresponding VL request not found.");
//                return;
//            }
            
            rs.first();

            patientInfo.setPatientUUID(rs.getString("patient_uuid"));
            patientInfo.setProviderUUID(rs.getString("provider_uuid"));
            patientInfo.setEncounterUUID(rs.getString("encounter_uuid"));
            patientInfo.setFacilityUUID(rs.getString("location_uuid"));
            patientInfo.setSampleDate(rs.getDate("sample_date"));
            patientInfo.setFacility(rs.getString("Facility"));

        } 
        catch (Exception pl) {
            patientInfo.setComments("Invalid Patient ID or Missing VL Request Record.");
//            Logger.getLogger(PatientList.class.getName()).log(Level.SEVERE, null, pl);
//            pl.printStackTrace();
        }

    }

    /*
     * Get the Concept UUIDs for the VL results (7470 and 7471)
     */
    private void getVLResultsConceptUUID() throws SQLException {

        // Instantiate Resultset object
        ResultSet rs;

        try {

            // SQL Statement to extract the UUIDs for the VL Results concepts
            String sqlStatement = "SELECT IF(concept_id = 7470,`uuid`,NULL) AS VLLogCopiesConcept, " +
                                    "IF(concept_id = 7471,`uuid`,NULL) AS VLResultConcept " +
                                    "FROM concept " +
                                    "WHERE concept_id IN (7470,7471)";

            // Get the concepts
            rs = dao.getPatientRecords(sqlStatement);

            if(rs.isBeforeFirst()){
                rs.first();
                VLLogCopiesUUID = rs.getString("VLLogCopiesConcept");
                rs.next();
                VLResultUUID = rs.getString("VLResultConcept");
            }
            
            // Close
            rs.close();
            
        } 
        catch (SQLException exSQLError) {
            exSQLError.printStackTrace();
        }
        catch (Exception exError) {
            exError.printStackTrace();
        }
    }

    /*
     * Get the MFL Code for the specified patient
     */
    private String getPatientMFLCode(String patientUUID) throws SQLException {

        // Instantiate Resultset object
        ResultSet rs;
        String mflCode = "";
                
        try {

            // SQL Statement to extract the MFL code for the specified patient
            String sqlStatement = 
                    "SELECT mfl_code FROM(SELECT substring(pid.identifier,1,5) AS mfl_code," +
"                    count(substring(pid.identifier,1,5))  as occurence " +
"                    FROM " +
"                    encounter e " +
"                    INNER JOIN patient_identifier pid ON e.patient_id=pid.patient_id " +
"                    WHERE pid.identifier_type = 9 " +
"                    group by mfl_code " +
"                    Order by occurence DESC " +
"                    LIMIT 1)as mfl ";

//            String sqlStatement = "SELECT LEFT(pid.identifier,5) AS mfl_code " +
//                "FROM patient_identifier pid " +
//                "INNER JOIN person pat ON pid.patient_id = pat.person_id AND pat.uuid = '" + patientUUID + "' " +
//                "WHERE `identifier_type` = 9";
//
            // Get the MFL Code
            rs = dao.getPatientRecords(sqlStatement);
            if(rs.isBeforeFirst()){
                rs.first();
                mflCode = rs.getString("mfl_code");
            }
            
            // Close
            rs.close();
            
        } 
        catch (SQLException exSQLError) {
            exSQLError.printStackTrace();
        }
        catch (Exception exError) {
            exError.printStackTrace();
        }
        
        // Return the MFL Code
        return mflCode;
        
    }

    /*
     * Get the MFL Code for the specified facility
     */
    private String getFacilityMFLCode(String facilityName) throws SQLException {

        // Instantiate Resultset object
        ResultSet rs;
        String mflCode = "";
                
        try {

            // SQL Statement to extract the MFL code for the specified facility
//            String sqlStatement = "SELECT pid.identifier AS mfl_code  " +
//                    "FROM patient_identifier pid " +
//                    "INNER JOIN location l ON pid.location_id = l.location_id AND l.name =\"" + facilityName + "\" " +
//                    "WHERE pid.identifier_type = 9 AND pid.preferred = 1 " +
//                    "ORDER BY pid.date_created";

            // Get the MFL Code
            String sqlStatement = ""
                    + "SELECT mfl_code FROM(SELECT substring(pid.identifier,1,5) AS mfl_code," +
"                    count(substring(pid.identifier,1,5))  as occurence " +
"                    FROM patient_identifier pid " +
"                    INNER JOIN location l ON pid.location_id = l.location_id AND l.name =\"" + facilityName + "\" " +
"                    WHERE pid.identifier_type = 9 " +
"                    group by mfl_code " +
"                    Order by occurence DESC " +
"                    LIMIT 1)as mfl";

            rs = dao.getPatientRecords(sqlStatement);
            if(rs.isBeforeFirst()){
                rs.first();
                mflCode = rs.getString("mfl_code");
            }
            
            
            // Close
            rs.close();
            
        } 
        catch (SQLException exSQLError) {
            exSQLError.printStackTrace();
        }
        catch (Exception exError) {
            exError.printStackTrace();
        }
        
        // Return the MFL Code
        return mflCode;
        
    }

    /*
     * Get the Current Regimen for the specified patient
     */
    private void getPatientRegimen(PatientDetails patient) throws SQLException {

        // Instantiate Resultset object
        ResultSet rs;
                
        try {
                
//            String sqlStatement = "select"
//                    +" pl.person_id,"
//                    +" pl.PatientID,"
//                    +"	pl.gender, "
//                    +"	pl.birthdate, "
//                    +"   DATEDIFF(CURDATE(), pl.birthdate)DIV 365.25 AS age,"  
//                    +"	Date_Started_ART, "
//                    +"	Current_Regimen," 
//                    +"	Current_Regimen_Date, "
//                    +"	IF(( pl.FL_Sub=0 AND pl.SW_Line=0) ,1,0) AS origFirstLine," 
//                    +"	IF(( pl.FL_Sub=1 AND pl.SW_Line=0) OR ( pl.FL_Sub=0 AND pl.SW_Line=0) OR (pl.FL_Sub=1 AND pl.SW_Line=0) ,1,0) AS altFirstLine," 
//                    +"	IF(( pl.SW_Line=1), 2,0) AS origSecondLine"
//                    +" from" 
//                    +" ("
//                    +" select" 
//                    +" e.encounter_id,"
//                    +" o.person_id, "
//                    +" p.gender, "
//                    +" p.birthdate,"
//                    +" DATEDIFF(CURDATE(), p.birthdate)DIV 365.25 AS age,"
//                    +" MAX(IF(pi.identifier_type = 3, pi.identifier, NULL)) AS PatientID,"
//                    +" MID(MIN(CONCAT(e.encounter_datetime,IF(o.concept_id=6746 ,DATE(o.value_datetime),''))),20) AS  Date_Started_ART,"
//                    +"	MAX(IF(o.concept_id IN (6748,6749),1,0)) AS  FL_Sub," 
//                    +" MAX(IF(o.concept_id IN (6751,6782),1,0)) AS  SW_Line "
//                    +" from encounter e"
//                    +" LEFT OUTER JOIN person p ON p.person_id = e.patient_id AND p.voided=0  "
//                    +" LEFT OUTER JOIN obs o ON o.encounter_id=e.encounter_id AND o.voided=0 AND o.concept_id IN (6746,7760)"
//                    +" INNER JOIN patient_identifier `pi` ON pi.patient_id = o.person_id AND o.voided = 0 AND pi.voided=0 AND pi.identifier_type = 3 "
//                    +" WHERE o.concept_id = 6746 AND pi.identifier = '" + patient.getPatientID() + "'"
//                    +" ) pl"
//                    +" INNER JOIN ("
//		    +"	Select "
//                    +"  person_id, "
//                    +"  MID(MAX(CONCAT(e.encounter_datetime,IF(o.concept_id=7760 ,o.value_numeric,NULL))),20) AS  Current_Regimen,"
//		    +"		  MID(MAX(CONCAT(e.encounter_datetime,IF(o.concept_id=7760 ,DATE(obs_datetime),NULL))),20) AS  Current_Regimen_Date "
//		    +"         FROM encounter e"  
//                    +" Inner JOIN obs o ON o.encounter_id=e.encounter_id AND o.voided=0 AND o.concept_id IN (7760)" 
//                    +" group by 1"
//                    +" ) AS oj ON  pl.person_id= oj.person_id";
            
            String sqlStatement="SELECT " +
"e.patient_id AS person_id, " +
"pi.identifier AS PatientID," +
"p.gender AS gender," +
"p.birthdate AS birthdate," +
"DATEDIFF(CURDATE(), p.birthdate)DIV 365.25 AS age," +
"MAX(IF(o.concept_id in(6746),date(o.value_datetime),null)) AS Date_Started_ART, " +
"MAX(IF(o.concept_id in(6739),date(o.value_datetime),null)) AS Current_Regimen_Date, " +
"max(IF(o.concept_id in(7701),CASE o.value_coded " +
"WHEN 7697 THEN 1 WHEN 1000 THEN 2 WHEN 8403 THEN 3 ELSE NULL " +
"END,null)) AS Sample_Type," +
"trim(concat_ws(' ',max(if(o.concept_id in(7467),date(value_datetime),null))," +
"max(if(o.concept_id in(8404),time(value_datetime),null))" +
")) AS Date_Collection," +
"trim(concat_ws(' ',max(if(o.concept_id in(8058),date(value_datetime),null))," +
"max(if(o.concept_id in(8405),time(value_datetime),null))" +
")) AS Date_Separation," +
"max(" +
"IF(o.concept_id in(8436),CASE o.value_coded  " +
"WHEN 8145 THEN 'AF2E' WHEN 6289 THEN 'AF2B' WHEN 8146 THEN 'AF1D'" +
"WHEN 6286 THEN 'AF1B' WHEN 6291 THEN 'AF4B' WHEN 8147 THEN 'AF4C'" +
"WHEN 7644 THEN 'AF2D' WHEN 6296 THEN 'AF2F' WHEN 8411 THEN 'AF1E'" +
"WHEN 8414 THEN 'AF1F' WHEN 6288 THEN 'AF2A' WHEN 6285 THEN 'AF1A'" +
"WHEN 7654 THEN 'AF4A' WHEN 8342 THEN 'AF5X' WHEN 6293 THEN 'AS1A'" +
"WHEN 7501 THEN 'AS1B' WHEN 8422 THEN 'AS1C' WHEN 7643 THEN 'AS2A'" +
"WHEN 8144 THEN 'AS2B' WHEN 7645 THEN 'AS2C' WHEN 7649 THEN 'AS5A'" +
"WHEN 7621 THEN 'AS5B' WHEN 8429 THEN 'AS5C' WHEN 8353 THEN 'AS6X'" +
"WHEN 8433 THEN 'AT2D' WHEN 8434 THEN 'AT2E' WHEN 8435 THEN 'AT2F'" +
"WHEN 8364 THEN 'AT2X' WHEN 8415 THEN 'CF2B' WHEN 8416 THEN 'CF2D'" +
"WHEN 8417 THEN 'CF2A' WHEN 8418 THEN 'CF2F' WHEN 8419 THEN 'CF1A'" +
"WHEN 8420 THEN 'CF1B' WHEN 8365 THEN 'CF1C' WHEN 8421 THEN 'CF5X'" +
"WHEN 8423 THEN 'CS1A' WHEN 8424 THEN 'CS1B' WHEN 8425 THEN 'CS2A'" +
"WHEN 8426 THEN 'CS1C' WHEN 8427 THEN 'CS2D' WHEN 8428 THEN 'CS4X'" +
"WHEN 8430 THEN 'CT1H' WHEN 8431 THEN 'CT2D' WHEN 8432 THEN 'CT3X'" +
"ELSE NULL END,null)) AS Current_Regimen,  " +
"max(IF(o.concept_id in(8413), case o.value_coded " +
"WHEN 8406 THEN 1 WHEN 8407 THEN 2 WHEN 8408 THEN 3 " +
"WHEN 8409 THEN 4 WHEN 8410 THEN 5 WHEN 8412 THEN 6 " +
"ELSE NULL END,null)) AS Justification_Code," +
"max(IF(o.concept_id in(8051),case o.value_coded " +
"WHEN 1065 THEN 'Y' WHEN 1066 THEN 'N' ELSE NULL END,null)" +
") AS Rejection,\n" +
"max(IF(o.concept_id in(8451),case o.value_coded " +
"WHEN 8437 THEN 1  WHEN 8438 THEN 2  WHEN 8439 THEN 3  " +
"WHEN 8440 THEN 4 WHEN 8441 THEN 5  WHEN 8442 THEN 6 " +
"WHEN 8443 THEN 7  WHEN 8444 THEN 8 WHEN 8445 THEN 9  " +
"WHEN 8446 THEN 10 WHEN 8447 THEN 11  WHEN 8448 THEN 12 " +
"WHEN 8449 THEN 13  WHEN 8450 THEN 14 WHEN 7918 THEN 15 " +
"ELSE NULL END,null)) AS Reason," +
"DATE(e.encounter_datetime) AS EncounterDate," +
"DATE(e.date_created) as DateCreated " +
"FROM encounter e   " +
"INNER JOIN patient_identifier pi ON e.patient_id=pi.patient_id " +
"AND pi.identifier IN('"+patient.getPatientID()+"') AND pi.voided=0 " +
"INNER JOIN person p ON e.patient_id=p.person_id " +
"Inner JOIN obs o ON o.encounter_id=e.encounter_id AND o.voided=0 " +
"AND o.concept_id IN (7701,7467,8404,8058,8405,8436,6739,8413,8051,6746,8451)  " +
"group by e.encounter_id " +
"ORDER BY e.encounter_datetime DESC " +
"LIMIT 1";
                  
            // Get the MFL Code
            rs = dao.getPatientRecords(sqlStatement);
            
            if(rs.isBeforeFirst()){
            
                rs.first();
                
                patient.setCurrentRegimen(rs.getString("Current_Regimen")); // Nyabuto 05/09/2019 updated from regimen number to regimen code
                patient.setCurrentRegimenDate(rs.getString("Current_Regimen_Date"));
                patient.setDateStartedART(rs.getString("Date_Started_ART"));
                patient.setBirthDate(rs.getString("birthdate"));
                patient.setGender(rs.getString("gender"));
                patient.setAge(rs.getInt("age"));
                
                patient.setSampleType(rs.getString("Sample_Type"));
                patient.setDateTimeCollection(rs.getString("Date_Collection"));
                patient.setDateTimeSeparation(rs.getString("Date_Separation"));
                patient.setJustificationCode(rs.getInt("Justification_Code"));
                patient.setRejection(rs.getString("Rejection"));
                patient.setRejectionReason(rs.getString("Reason"));
                
//                patient.setRegimenLine(rs.getInt("origFirstLine")); // 28/11/2016 added this to check the value
                
//                if(rs.getInt("origFirstLine") == 1 || rs.getInt("altFirstLine") == 1){
//                    patient.setRegimenLine(1);
//                }
//                else {
//                    if(rs.getInt("origSecondLine") != 0){
//                        patient.setRegimenLine(2);
//                    }
//                }
                
            }
            
            // Close
            rs.close();
            
        } 
        catch (SQLException exSQLError) {
            exSQLError.printStackTrace();
        }
        catch (Exception exError) {
            exError.printStackTrace();
        }
        
    }

    /*
        Encode the provided string into base64 format and return encoded string.
        This code is not needed in this implementation but left here for possible future use
    */
    private String encodeString(String rawString){
        
//        bytesEncoded = Base64.encode(rawString.getBytes());
        String encodedString = Base64.encode(rawString.getBytes());
        
        return encodedString;
        
    }

    /*
        POST VL Requests To AMPATH website
        Edwin Nov 15, 2018: Modified this block to use new key-pair values as specified by CHAI for their upgraded website.
    */
    public void postAMPATHVLRequests(){
        
        VLRestAPI ampath = new VLRestAPI();     // Set up AMPATH REST API
        StringEntity restPOSTString;
        int isUploaded = 0;
        int sampleType = 1;
        int justifyCode = 1;
        String paramString = "";    // Edwin: 26Nov18
        
        try {
            
            // Store the REST database access credentials
            ampath.setPassword(dao.getRestPassword(1));
            ampath.setUsername(dao.getRestUserName(1));
            ampath.setURLBase(dao.getRestURL(1));      // AMPATH REST URL
            
        }
        catch(Exception exError){
            exError.printStackTrace();
        }            
        
        for (PatientDetails patient : vlPatientList) {

            try {

                // If there was no sample date for the patient or the record has not been marked for shipping, skip
                if(patient.getSampleDate() == null || !patient.isShipped()){
                    patient.setImported(false);
                    System.out.println("Record for Patient: " + patient.getPatientID() + " skipped.\n");
                    continue;
                }

                // Get the patient Regimen info
                getPatientRegimen(patient);
                
                // Create the POST parameters
                paramString = "{";
                //paramString += "\"test\":\"2\"" + ",";
                paramString += "\"mflCode\":\"" + patient.getMFLCode() + "\",";
                paramString += "\"patient_identifier\":\"" + patient.getPatientID() + "\",";
                if(patient.getGender().equalsIgnoreCase("M")){                                                 // Edwin: 26Nov18 - Gender 1=M, 2=F, 3=Unknown
                    paramString += "\"sex\":\"1\"" + ",";
                    paramString += "\"pmtct\":\"3\"" + ",";
                }
                else {
                    if(patient.getGender().equalsIgnoreCase("F")){                                                 // Edwin: 26Nov18 - Gender 1=M, 2=F, 3=Unknown
                        paramString += "\"sex\":\"2\"" + ",";

                        // Get Pregnancy Status
                        if(patient.getPregnancyStatus() == "Yes"){
                            paramString += "\"pmtct\":\"1\"" + ",";
                        }
                        else{
                            // Get Breast Feeding Status
                            if(patient.getBreastFeeding() == "Yes"){
                                paramString += "\"pmtct\":\"2\"" + ",";
                            }
                            else {
                                // None of the above
                                paramString += "\"pmtct\":\"3\"" + ",";
                            }
                        }
                    }
                }
                paramString += "\"dob\":\"" + patient.getBirthDate() + "\",";
                paramString += "\"initiation_date\":\"" + patient.getDateStartedART() + "\",";
                paramString += "\"datecollected\":\"" + VL_DATE_FORMAT.format(patient.getSampleDate()) + "\",";
                paramString += "\"prophylaxis\":\"" + patient.getCurrentRegimen() + "\",";
                paramString += "\"justification\":\"" + String.valueOf(patient.getJustificationCode()) + "\",";
                paramString += "\"sampletype\":\"" + String.valueOf(patient.getSampleType()) + "\"}";
                
                // not sent for now 5th sep 2019
//                paramString += "\"datetimecollection\":\"" +patient.getDateTimeCollection()+ "\",";
//                paramString += "\"datetimeseparation\":\"" +patient.getDateTimeSeparation()+ "\",";
//                paramString += "\"rejection\":\"" +patient.getRejection()+ "\",";
//                paramString += "\"rejectionreason\":\"" +patient.getRejectionReason()+ "\"}";
                
                
//                paramString += "\"regimenline\":\"" + String.valueOf(patient.getRegimenLine()) + "\"}";
          

                // System.out.println("Your params are : "+paramString);
                // Execute REST string
                //isUploaded = ampath.getRequestPost(dao.getRestPOST(1) + "?", null, param);
                isUploaded = ampath.getRequestPost(dao.getRestPOST(1) + "?", null, paramString, dao.getRestAPIKey());    // Edwin: 26Nov18

                patient.setImported(isUploaded == 1 ? true : false);

                if(patient.isImported()){
                    patient.setComments("Successfully Uploaded to Lab site!");
                }
                else{
                    patient.setComments("Upload to Lab site Failed!");
                }

                System.out.println("Record for Patient: " + patient.getPatientID() + " processed. (Status: " + patient.getComments() + ")\n");

            } 
            catch (NullPointerException exNull) {
                Logger.getLogger(PatientList.class.getName()).log(Level.SEVERE, null, exNull);
                patient.setComments("Invalid Patient ID.");
                patient.setImported(false);
//                isNullException = true;
            }
            catch(Exception exError){
                patient.setComments("Invalid Patient Record.");
                patient.setImported(false);
                exError.getCause();
                exError.printStackTrace();
            }            

//            // If it was a NullException, continue
//            if(isNullException){
//                continue;
//            }
        }

    }
    
    /*
        Obtain VL results from the AMPATH website
    */
    public List<PatientDetails>  getAMPATHVLResults() throws SQLException, IOException, ParseException {
        
        // Initiliase the result string
        String vlResult = "";
        String vlResultTrimmed = "";
        String facilityMFLCode = "";
        String getRequestParams = "";
        String ampathResult = "";
//        String messageBody="";
//        String vlres="";
//        String patientid="";
//        String facilityname="";
//        int vlresint=0;
//        int countPatients=0;
//        StringBuilder sb = new StringBuilder();
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<VLResultData> vlResultData;
        
        VLRestAPI ampath = new VLRestAPI();     // Set up AMPATH REST API
        
        try {

            String wrongAMPATHIDs = "\nLab Import Date: " + logDate + "\n";
    
            // Clear patients list
            System.out.println("Clear existing patients list...");
            vlPatientList.clear();

            // Store the REST database access credentials
            ampath.setPassword(dao.getRestPassword(1));
            ampath.setUsername(dao.getRestUserName(1));
            ampath.setURLBase(dao.getRestURL(1));      // AMPATH REST URL
            
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            
            // Build the URL parameters needed to extract the results
            // Edwin: 28Nov18
            getRequestParams = "{";
            getRequestParams += "\"test\":\"2\"" + ",";
            getRequestParams += "\"start_date\":\"" + vlImportStartDate + "\",";
            getRequestParams += "\"end_date\":\"" + vlImportEndDate + "\",";
            getRequestParams += "\"facility_code\":\"" + getFacilityMFLCode(importFacility) + "\",";
            getRequestParams += "\"dispatched\":\"true\"" + "}";
            
//            getRequestParams = "orders/resultsapi.php?apikey=" + dao.getRestAPIKey() + "&fcode=" + getFacilityMFLCode(importFacility) + 
//                                "&startDate=" + vlImportStartDate + "&endDate=" + vlImportEndDate;

            // Execute the REST call
            vlResult = ampath.getRequestGet(dao.getRestGET(1) + "?", getRequestParams, dao.getRestAPIKey());
            
            // Edwin: 28Nov18
            // Convert the string into a JSON array and get only the Data Section
            JSONObject joVLResults = new JSONObject(vlResult);

            JSONArray arVLResults = joVLResults.getJSONArray("data");
            
            for(int iVLCount = 0 ; iVLCount < arVLResults.length() ; iVLCount++){
                
                // Check if the patient has results. If not, skip.
                if(arVLResults.getJSONObject(iVLCount).isNull("result")){
                    continue;
                }
                // Check to see if the patient's ID is in the correct format
                if (!patt_FACES_ID.matcher(arVLResults.getJSONObject(iVLCount).getString("patient")).matches()) {
                    
                    // Log error and skip
                    wrongAMPATHIDs = wrongAMPATHIDs +  arVLResults.getJSONObject(iVLCount).getString("patient").toString() + "\r\n";
                    continue;
                    
                }

                // Instantiate new patient object
                patientInfo = new PatientDetails();
                
                // Patient ID
                patientInfo.setPatientID(arVLResults.getJSONObject(iVLCount).getString("patient"));
                patientInfo.correctCheckDigit();
                
                // Viral Load Result
                patientInfo.setVLResult(arVLResults.getJSONObject(iVLCount).getString("result"));
                
              
                // VL Log Copies
//                patientInfo.setLogResult(arVLResults.getJSONObject(iVLCount).getString("log_copies"));
                
                // Sample Collection Date
//                DataFormatter df = new DataFormatter();
//                String sRunDate = vlData.getDateCollected();
//                SimpleDateFormat sdfVLDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
//                Date parsedDate = sdfVLDate.parse(arVLResults.getJSONObject(iVLCount).getString("date_collected"));
                Date parsedDate = VL_DATE_FORMAT.parse(arVLResults.getJSONObject(iVLCount).getString("date_collected"));
                
                //SimpleDateFormat formatter = VL_DATE_FORMAT; //format 
                Date dtRunDate = VL_DATE_FORMAT.parse(VL_DATE_FORMAT.format(parsedDate));
                java.sql.Date dtSQLDate = new java.sql.Date(dtRunDate.getTime());

                patientInfo.setDateRun(dtSQLDate);

                // Get the patient sample data
//                getSampleDate(patientInfo.getPatientID(), dtSQLDate.toString());
                getSampleDate(patientInfo.getPatientID(), arVLResults.getJSONObject(iVLCount).getString("date_collected"));

                vlPatientList.add(patientInfo);
                
            }

            // Edwin: 28Nov18

//            // Trim out sections of the result to remain with a JSON Array
//            vlResultTrimmed = vlResult.substring(9, vlResult.length()-1);   //.replaceAll(":\"", ":\\\\\"").replaceAll("\",", "\\\\\",");
//            
//            // Store the result in a list
//            vlResultData = mapper.readValue(vlResultTrimmed, new TypeReference<List<VLResultData>>(){});
//            
//            // Get patient results and store in list
//            for (VLResultData vlData : vlResultData) {
//                
//                // Check to see if the patient's ID is in the correct format
//                if (!patt_FACES_ID.matcher(vlData.getPatientID()).matches()) {
//                    
//                    // Log error and skip
//                    wrongAMPATHIDs = wrongAMPATHIDs +  vlData.getPatientID().toString() + "\r\n";
//                    continue;
//                    
//                }
//
//                // Instantiate new patient object
//                patientInfo = new PatientDetails();
//                
//                // Patient ID
//                patientInfo.setPatientID(vlData.getPatientID());
//                patientInfo.correctCheckDigit();
//                
//                // Viral Load Result
//                patientInfo.setVLResult(vlData.getFinalResultCps());
//                
//              
//                // VL Log Copies
//                patientInfo.setLogResult(vlData.getFinalResultLog());
//                
//                // Sample Collection Date
////                DataFormatter df = new DataFormatter();
////                String sRunDate = vlData.getDateCollected();
//                SimpleDateFormat sdfVLDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
//                Date parsedDate = sdfVLDate.parse(vlData.getDateCollected());
//                
//                //SimpleDateFormat formatter = VL_DATE_FORMAT; //format 
//                Date dtRunDate = VL_DATE_FORMAT.parse(VL_DATE_FORMAT.format(parsedDate));
//                java.sql.Date dtSQLDate = new java.sql.Date(dtRunDate.getTime());
//
//                patientInfo.setDateRun(dtSQLDate);
//
//                // Get the patient sample data
//                getSampleDate(patientInfo.getPatientID(), dtSQLDate.toString());
//
//                vlPatientList.add(patientInfo);
//                
//                // Add to the string
////                ampathResult += "Patient ID: " + vlData.getPatientID() + ", Result: " + vlData.getFinalResult() + "\n";
//              
////// Rugut Generate Email Body 11/05/2017
////                patientid=vlData.getPatientID();
////                vlres=vlData.getFinalResultCps();
////                facilityname = patientInfo.getFacility();
////                try{
////                vlresint = Integer.parseInt(vlres); 
////                if (vlresint>=1000){
////                    countPatients = countPatients+1;
////                //sb.append(patientid+"    "+vlres +"\n");
////                }
////                }
////                catch (Exception gh)
////                {
////                    
////                }
//                
//            }
          
            // Save the output to a log file
            PrintWriter pwOutputFile = new PrintWriter("Wrong_Lab_IDs" + logDate + ".log");
            pwOutputFile.println(wrongAMPATHIDs);
            pwOutputFile.close();
            
            // Display the data
//            JOptionPane.showMessageDialog(null, ampathResult);
            
        } 
        catch (NullPointerException exNull) {
            Logger.getLogger(PatientList.class.getName()).log(Level.SEVERE, null, exNull);
            exNull.printStackTrace();
        }
        catch(Exception exError){
            Logger.getLogger(PatientList.class.getName()).log(Level.SEVERE, null, exError);
            exError.printStackTrace();
        }            

        // Remove already imported records
        System.out.println("Checking for and removing already imported records...");
        removeImportedRecords();
                
        // Remove Invalid records
        System.out.println("Checking for and removing Invalid records...");
        removeInvalidRecords();
                
        // Display total records
        System.out.println("Total Records For Import: " + vlPatientList.size());
        
        return vlPatientList;

    }
    
    /*
     * Save the results using OpenMRS REST API
     */
    public void saveViralLoadResults(){
        
        // We are saving only Obs records. The Obs payload should be in the following format:
        // {"person":"person_uuid", "obsDatetime": "yyyy-mm-dd","location":"location_uuid", "concept": "concept_uuid", "value": "anyvalue or concept_uuid"}
        
        // Import data
        boolean isNullException = false;
        boolean isVLResultsImported = false;
        boolean isVLLogCopiesImported = false;
        String obsString;
        StringEntity restPOSTString;
        VLRestAPI openMRS = new VLRestAPI();      // Set up the OpenMRS REST API for use
        
        try {
            
            // Reset the database connection
            DataAccessClass dao = new DataAccessClass();

            // Get the Concept UUIDs
            getVLResultsConceptUUID();

            // Store the REST database access credentials
            openMRS.setPassword(dao.getRestPassword(0));
            openMRS.setUsername(dao.getRestUserName(0));
            openMRS.setURLBase(dao.getRestURL(0));      // OpenMRS REST URL

        }
        catch(Exception exError){
            exError.printStackTrace();
        }            
        
                      //Rugute Variable for Email body 11/05/2017
        String messageBody="";
        String vlres="";
        String patientid="";
        String facilityname="";
        int vlresint=0;
        int countPatients=0;
        StringBuilder sb = new StringBuilder();
        
        for (PatientDetails patient : vlPatientList) {

            try {

                // If there was no sample date for the patient, skip
                if(patient.getSampleDate() == null){
                    patient.setImported(false);
                    System.out.println("Record for Patient: " + patient.getPatientID() + " skipped.\n");
                    continue;
                }
                
                // Check if the sample dates from the request and results are the same.
                if (patient.getDateRun().before(patient.getSampleDate())) {

                    patient.setComments("Run Date " + " " + patient.getDateRun() + " is before sample date " + patient.getSampleDate());

                    patient.setImported(false);

                    System.out.println("Record for Patient: " + patient.getPatientID() + " skipped.\n");
                } 
                else {
      
                    
                    // Rugut Generate Email Body 11/05/2017
                patientid=patient.getPatientID();
                vlres=patient.getVLResult();
                facilityname = patient.getFacility();
                try{
                vlresint = Integer.parseInt(vlres); 
                if (vlresint>=1000){
                    countPatients = countPatients+1;
                //sb.append(patientid+"    "+vlres +"\n");
                }
                }
                catch (Exception gh)
                {
                    
                }
                    
                    // Create REST string for Concept 7471: "Viral Load Results"
                    obsString = "{\"person\":\"" + patient.getPatientUUID() + 
                                "\", \"obsDatetime\": \"" + patient.getDateRun() + 
                                "\", \"encounter\": \"" + patient.getEncounterUUID() + 
                                "\", \"location\": \"" + patient.getFacilityUUID() + 
                                "\", \"concept\": \"" + VLResultUUID + 
                                "\", \"value\": \"" + patient.getVLResult() + "\"}";
                    
                    restPOSTString = new StringEntity(obsString);
                    restPOSTString.setContentType("application/json");
                    
                    // Execute REST string
                    isVLResultsImported = (openMRS.getRequestPost("/ws/rest/v1/obs", restPOSTString, null, dao.getRestAPIKey()) == 1 ? true : false);
                    
                    // Create REST string for Concept 7470: "Log Copies"
                    obsString = "{\"person\":\"" + patient.getPatientUUID() + 
                                "\", \"obsDatetime\": \"" + patient.getDateRun() + 
                                "\", \"encounter\": \"" + patient.getEncounterUUID() + 
                                "\", \"location\": \"" + patient.getFacilityUUID() + 
                                "\", \"concept\": \"" + VLLogCopiesUUID + 
                                "\", \"value\": \"" + patient.getLogResult() + "\"}";
                    
                    restPOSTString = new StringEntity(obsString);
                    restPOSTString.setContentType("application/json");

                    // Execute REST string
                    isVLLogCopiesImported = (openMRS.getRequestPost("/ws/rest/v1/obs", restPOSTString, null, dao.getRestAPIKey()) == 1 ? true : false);

                    patient.setImported(isVLResultsImported && isVLLogCopiesImported);

                    if(patient.isImported()){
                        patient.setComments("Successfully Imported!");
                        
                        
                    }
                    else{
                        patient.setComments("Importation Failed!");
                    }
                    
                    System.out.println("Record for Patient: " + patient.getPatientID() + " processed. (Status: " + patient.getComments() + ")\n");
                    
                    //}
                }
            } 
            catch (NullPointerException exNull) {
                Logger.getLogger(PatientList.class.getName()).log(Level.SEVERE, null, exNull);
                patient.setComments("Invalid Patient ID.");
                patient.setImported(false);
                isNullException = true;
            }
            catch(Exception exError){
                patient.setComments("Invalid Patient Record.");
                patient.setImported(false);
                exError.printStackTrace();
            }            

            // If it was a NullException, continue
            if(isNullException){
                continue;
            }
        }
           sb.append("Dear User, \n");
             sb.append(" \n");
             sb.append("Viral Load results have been downloaded from the Testing Lab website and some patients' results have high VL's that need immediate intervention. \n");
             sb.append(" \n");
             sb.append(" Please login into the OpenMRS system and run the \"Patients With High Viral Load\" report to get the list of these patients. Details are as follows: \n");
             sb.append(" \n");
             sb.append("============================================================\n");
             sb.append(" \n");
             sb.append("Sample Collection Dates: "+ vlImportStartDate +" to " + vlImportEndDate + "");
             sb.append(" \n");
             sb.append("Facility: "+ facilityname +" ");
             sb.append(" \n");
             sb.append("No of Patients With High VL: "+ countPatients +"\n");
             sb.append(" \n");
             sb.append("============================================================\n");
             sb.append(" \n");
             sb.append("Regards, \n");
             sb.append("RCTP-FACES SI Team \n");
             sb.append(" \n");
             sb.append("------------------------------------- \n");
             
            //sb.append(countPatients);
            messageBody =sb.toString();
            if (countPatients>0){
            SendEmail.Send("High Viral Load Results Notification",messageBody);
            }

//            for (PatientDetails patient : vlPatientList) {
        for (int iRemove = 0; iRemove < vlPatientList.size(); iRemove++) {

            PatientDetails patient = new PatientDetails();
            patient = vlPatientList.get(iRemove);

            if (patient.isImported()) {

                vlPatientList.remove(patient);

                iRemove=-1;

            }
        }
    }
    
    /*
        Check the database for existing imports and remove the record from the list
    */
    private void removeImportedRecords(){
        
        // Instantiate local objects
        ResultSet rs;
        ArrayList<PatientDetails> plImportedResults = new ArrayList<>();
        String progressMsg = "";
        
        try {
            
            // Get list of imported records for checking
            String sqlStatement = "SELECT " +
                                    "	pi.identifier," +
                                    "	DATE(ol.obs_datetime) AS Log_Date," +
                                    "	ol.Log_Copies," +
                                    "	DATE(ov.obs_datetime) AS Result_Date," +
                                    "	ov.VL_Result " +
                                    "FROM patient_identifier pi " +
                                    "INNER JOIN " +
                                    "(" +
                                    "	SELECT person_id, value_text AS Log_Copies, obs_datetime " +
                                    "	FROM obs " +
                                    "	WHERE concept_id = 7470" +
                                    ") ol ON pi.patient_id = ol.person_id " +
                                    "INNER JOIN " +
                                    "(" +
                                    "	SELECT person_id, value_text AS VL_Result, obs_datetime " +
                                    "	FROM obs " +
                                    "	WHERE concept_id = 7471" +
                                    ") ov ON pi.patient_id = ov.person_id " +
                                    "WHERE pi.identifier_type = 3 AND DATE(ol.obs_datetime) = DATE(ov.obs_datetime)";

            rs = dao.getPatientRecords(sqlStatement);

            // If records are found, load them on the list
            if(rs.isBeforeFirst()){

                // Go to first record then process
                rs.first();
                
                do {
                    
                    // Create new patient
                    PatientDetails patient = new PatientDetails();
                    
                    patient.setPatientID(rs.getString("identifier"));
                    patient.setDateRun(rs.getDate("Log_Date"));
                    patient.setLogResult(rs.getString("Log_Copies"));
                    patient.setVLResult(rs.getString("VL_Result"));
                    
                    // Add to list
                    plImportedResults.add(patient);
                    
                    
                } while (rs.next());
                
                // Close the resultset
                rs.close();
                
                for (int iRemove = 0; iRemove < vlPatientList.size(); iRemove++) {

                    PatientDetails newPatient = vlPatientList.get(iRemove);

                    for (int iCount = 0; iCount < plImportedResults.size(); iCount++) {
                        
                        PatientDetails importedPatient = plImportedResults.get(iCount);
                        
                        // Check if records match and remove from lists
                        // Edwin: 29Nov18 - Modified to not look at Log Copies
                        if(newPatient.getPatientID().equalsIgnoreCase(importedPatient.getPatientID()) && 
                                newPatient.getVLResult().equalsIgnoreCase(importedPatient.getVLResult()) && 
                                newPatient.getDateRun().toString().equalsIgnoreCase(importedPatient.getDateRun().toString())){
//                        if(newPatient.getPatientID().equalsIgnoreCase(importedPatient.getPatientID()) && 
//                                newPatient.getVLResult().equalsIgnoreCase(importedPatient.getVLResult()) && 
//                                newPatient.getLogResult().equalsIgnoreCase(importedPatient.getLogResult()) && 
//                                newPatient.getDateRun().toString().equalsIgnoreCase(importedPatient.getDateRun().toString())){
                            
                            // Remove patients from list
                            vlPatientList.remove(newPatient);
                            plImportedResults.remove(importedPatient);
                            
                            iRemove =- 1;
                            
                            // Craft progress message
                            progressMsg = "(Record=" + iCount + ": Source=" + vlPatientList.size() + ": Imports=" + plImportedResults.size() + ")-->";
                            progressMsg += "Record for Patient: " + newPatient.getPatientID() + " removed from list...\n";
                            
                            System.out.println(progressMsg);
                            break;
                        }
//                        else {
//                            System.out.println("Record for Patient: " + newPatient.getPatientID() + " retained in list...\n");
//                        }
                    }
                    

                    // Check if the patient record exists
//                    String sqlStatement = "SELECT " +
//                                            "	pi.identifier," +
//                                            "	ol.obs_datetime AS Log_Time," +
//                                            "	ol.Log_Copies," +
//                                            "	ov.obs_datetime AS Result_Time," +
//                                            "	ov.VL_Result " +
//                                            "FROM patient_identifier pi " +
//                                            "INNER JOIN " +
//                                            "(" +
//                                            "	SELECT person_id, value_text AS Log_Copies, obs_datetime " +
//                                            "	FROM obs " +
//                                            "	WHERE concept_id = 7470 AND DATE(obs_datetime) = '" + patient.getDateRun() + "'" +
//                                            ") ol ON pi.patient_id = ol.person_id " +
//                                            "INNER JOIN " +
//                                            "(" +
//                                            "	SELECT person_id, value_text AS VL_Result, obs_datetime " +
//                                            "	FROM obs " +
//                                            "	WHERE concept_id = 7471 AND DATE(obs_datetime) = '" + patient.getDateRun() + "'" +
//                                            ") ov ON pi.patient_id = ov.person_id " +
//                                            "WHERE pi.identifier_type = 3 AND pi.identifier = '" + patient.getPatientID() + "' AND DATE(ol.obs_datetime) = DATE(ov.obs_datetime)";
//
//                    rs = dao.getPatientRecords(sqlStatement);
//
//                    // If a record is found, remove from list
//                    if(rs.isBeforeFirst()){
//
//                        System.out.println("Record for Patient: " + patient.getPatientID() + " removed from list...\n");
//                        vlPatientList.remove(patient);
//                        iRemove=-1;
//                    }
//                    else {
//                        System.out.println("Record for Patient: " + patient.getPatientID() + " retained in list...\n");
//
//                    }
//
//                    // Close resultset
//                    rs.close();

                }
            }
            
        }
        catch (SQLException exSQLError) {
            exSQLError.printStackTrace();
        }
        catch (Exception exError) {
            exError.printStackTrace();
        }
    }
    
    /*
        Check the database for existing imports and remove the record from the list
    */
    private void removeInvalidRecords(){
        
        try {
            
            for (int iRemove = 0; iRemove < vlPatientList.size(); iRemove++) {

                PatientDetails newPatient = vlPatientList.get(iRemove);

                // Check if records match and remove from lists
                if(newPatient.getComments().toLowerCase().contains("invalid patient")){

                    // Remove patients from list
                    vlPatientList.remove(newPatient);

                    iRemove =- 1;
                    System.out.println("Record for Invalid Patient: " + newPatient.getPatientID() + " removed from list...\n");
                }
            }
        }
        catch (Exception exError) {
            exError.printStackTrace();
        }
    }
    
    private void runCommandLine(String cmdString){
        
        // Execute specified command line command
        String sReadOutputLine, sGetDhisMetaData = "";
    
        try {
            
            // Initialize the output content holder
            threadError = "";
            threadOutput = "";
            
            Process prGetMetaData = Runtime.getRuntime().exec(cmdString);

            // Start off the process
            startProcess(prGetMetaData);
            
            if(prGetMetaData.waitFor() != 0){
                
                // Restart the process
                runCommandLine(cmdString);
                
            }
//            brReadOutputLine.close();
//
            // Save the output to an XML file
            PrintWriter pwOutputFile = new PrintWriter("VL_Import_REST.log");
            pwOutputFile.println(threadOutput);
            pwOutputFile.close();
//            
        }
        catch(Exception exError){
            exError.printStackTrace();
        }            

    }
    
    private void startProcess(Process prMetaData){

        try {
            
            // any error message?
            StreamGobbler errorGobbler = new 
                StreamGobbler(prMetaData.getErrorStream(), "ERROR");            
            
            // any output?
            StreamGobbler outputGobbler = new 
                StreamGobbler(prMetaData.getInputStream(), "OUTPUT");
                
            // kick them off
            errorGobbler.start();
            outputGobbler.start();
            
            // Insert a delay
            Thread.sleep(10000);
        }
        catch(Exception exError){
            exError.printStackTrace();
        }            
    }
    
    /*
        This is the original method used for importing VL results into OpenMRS.
        This method does a direct database update. OpenMRS Sync module will NOT
        pick these updates so the new method using OpenMRS REST API is preferred.
    */
    public void SaveExcelData() throws SQLException {
        
        // Import data
        boolean isNullException = false;
        PatientDetails patient = null;
        
//            int i=0;

            for (int iImport = 0; iImport < vlPatientList.size(); iImport++) {

                try {

                    patient = new PatientDetails();
                    patient = vlPatientList.get(iImport);


                    if (patient.getDateRun().before(patient.getSampleDate())) {

    //                    JOptionPane.showMessageDialog(null, "date mistmatch");

                        patient.setComments("Run Date " + " " + patient.getDateRun() + " is before sample date " + patient.getSampleDate());

                        patient.setImported(false);
    //                    i++;


                    } else {
    //                    for (PatientDetails patient : vlPatientList) {
                        //query to insert viral load results to the Database
//                        String sqlResult = "insert into obs (person_id,obs_datetime,location_id,concept_id,value_text,uuid,encounter_id,creator,date_created) "
//                                + "select pi.patient_id,MAX(e.encounter_datetime),mid(max(concat(e.encounter_datetime,e.location_id)),20),7471,'" + patient.getVLResult() + "',uuid(),mid(max(concat(e.encounter_datetime,e.encounter_id)),20),e.creator,e.date_created "
//                                + "from patient_identifier pi join encounter e on e.patient_id=pi.patient_id and pi.identifier='" + patient.getPatientID() + "' "
//                                + "join location l on l.location_id=e.location_id "
//                                + " where e.encounter_datetime<='" + patient.getDateRun() + "' and e.encounter_type=35";

                        String sqlResult = "insert into obs (person_id,obs_datetime,location_id,concept_id,value_text,uuid,encounter_id,creator,date_created) "
                                + "select pi.patient_id,o.value_datetime,mid(max(concat(e.encounter_datetime,e.location_id)),20),7471,'" + patient.getVLResult() + "',uuid(),mid(max(concat(e.encounter_datetime,e.encounter_id)),20),e.creator,e.date_created "
                                + "from patient_identifier pi join encounter e on e.patient_id=pi.patient_id and pi.identifier='" + patient.getPatientID() + "' "
                                + "JOIN obs o ON e.encounter_id = o.encounter_id AND o.concept_id = 7467 "
                                + "join location l on l.location_id=e.location_id "
                                + " where o.value_datetime <= '" + patient.getDateRun() + "' and e.encounter_type=35";

                        //method inserting results to the database
                        dao.uploadResults(sqlResult);


                        //query to insert viral load results to the Database
//                        sqlResult = "insert into obs (person_id,obs_datetime,location_id,concept_id,value_text,uuid,encounter_id,creator,date_created) "
//                                + "select pi.patient_id,MAX(e.encounter_datetime),mid(max(concat(e.encounter_datetime,e.location_id)),20),7470,'" + patient.getLogResult() + "',uuid(),mid(max(concat(e.encounter_datetime,e.encounter_id)),20),e.creator,e.date_created "
//                                + "from patient_identifier pi join encounter e on e.patient_id=pi.patient_id and pi.identifier='" + patient.getPatientID() + "' "
//                                + "join location l on l.location_id=e.location_id "
//                                + " where e.encounter_datetime<='" + patient.getDateRun() + "'and e.encounter_type=35";

                        sqlResult = "insert into obs (person_id,obs_datetime,location_id,concept_id,value_text,uuid,encounter_id,creator,date_created) "
                                + "select pi.patient_id,o.value_datetime,mid(max(concat(e.encounter_datetime,e.location_id)),20),7470,'" + patient.getLogResult() + "',uuid(),mid(max(concat(e.encounter_datetime,e.encounter_id)),20),e.creator,e.date_created "
                                + "from patient_identifier pi join encounter e on e.patient_id=pi.patient_id and pi.identifier='" + patient.getPatientID() + "' "
                                + "JOIN obs o ON e.encounter_id = o.encounter_id AND o.concept_id = 7467 "
                                + "join location l on l.location_id=e.location_id "
                                + " where o.value_datetime<='" + patient.getDateRun() + "'and e.encounter_type=35";

                        //method inserting results to the database
                        dao.uploadResults(sqlResult);

                        patient.setImported(true);

                        patient.setComments("Successfully Imported!");
                        //}
                    }
                } 
                catch (NullPointerException exNull) {
                    Logger.getLogger(PatientList.class.getName()).log(Level.SEVERE, null, exNull);
                    patient.setComments("Invalid Patient ID. Data not imported.");
                    patient.setImported(false);
                    isNullException = true;
                }
                catch (SQLException ex) {
                    Logger.getLogger(PatientList.class.getName()).log(Level.SEVERE, null, ex);
                    patient.setComments("VL Request Missing. Data not imported.");
                    patient.setImported(false);
                }

                // If it was a NullException, continue
                if(isNullException){
                    continue;
                }
            }

//            for (PatientDetails patient : vlPatientList) {
            for (int iRemove = 0; iRemove < vlPatientList.size(); iRemove++) {
                
                patient = new PatientDetails();
                patient = vlPatientList.get(iRemove);
                
                if (patient.isImported()) {

                    vlPatientList.remove(patient);
                    
                    iRemove=-1;
                    
                }
            }
            
            // Print out the new VL List with patients who couldn't be imported.
            
    }

    /*
        Review the Patient IDs and generate check digits if necessary.
    */
    public void reviewPatientID(){
    
        // Detect predefined types and enumerate
        int noCheckDigit = 0, wrongFormat = 0, rightFormat = 0;
        String showStats = "";
                
        for (PatientDetails patient : vlPatientList){
            
            // Check if the format is correct
            if(patient.getPatientID().matches("^[0-9]{5}(TG|KTG|KDH|KFH|KHO|KKP|KLH|KLM|KNG|KNH|KPP|KRB|KTM|KTU|KYF|KKD|KND|KBD|KMD|KRD|MAG|MAJ|MAN|MAR|MBA|MDC|MDE|MDH|MDR|MGJ|MGK|MGR|MGT|MKB|MKD|MKM|MKN|MKP|MKR|MKT|MLW|MMC|MMD|MMH|MMM|MMN|MMT|MNB|MND|MNG|MNK|MNN|MNR|MNY|MOA|MOB|MOC|MOD|MOG|MOL|MON|MOR|MOS|MOT|MPM|MRI|MSA|MSB|MSL|MSN|MSR|MSS|MST|MSU|MTL|MTS|MWN|MWO|MYA|NCR|NMI|RBW|RDH|RKL|RKT|RKW|RLW|RMD|RMN|RND|RNG|RNM|ROG|ROM|RON|ROS|ROT|RPW|RRY|RSB|RYN|SAD|SDC|SDH|SGO|SHU|SHN|SJU|SKG|SKD|SKI|SKL|SKR|SKS|SKT|SKU|SLA|SLE|SLK|SLW|SMB|SMC|SMD|SMG|SMS|SMY|SNA|SND|SNG|SNN|SNR|SNS|SNT|SNW|SNY|SOB|SOG|SON|SPE|SPO|SRE|SRI|SRO|SSE|SSK|SSN|STA|STM|STO|STR|STU|SUG|SUS|SVD|SWD|SWK|SYA|SYO|TST)-[0-9]$")){
                
                // Check if the 
                rightFormat += 1;
                continue;
            }
            else {
                // Check if check digit is missing
                if((patient.getPatientID().matches("^[0-9]{5}(TG|KTG|KDH|KFH|KHO|KKP|KLH|KLM|KNG|KNH|KPP|KRB|KTM|KTU|KYF|KKD|KND|KBD|KMD|KRD|MAG|MAJ|MAN|MAR|MBA|MDC|MDE|MDH|MDR|MGJ|MGK|MGR|MGT|MKB|MKD|MKM|MKN|MKP|MKR|MKT|MLW|MMC|MMD|MMH|MMM|MMN|MMT|MNB|MND|MNG|MNK|MNN|MNR|MNY|MOA|MOB|MOC|MOD|MOG|MOL|MON|MOR|MOS|MOT|MPM|MRI|MSA|MSB|MSL|MSN|MSR|MSS|MST|MSU|MTL|MTS|MWN|MWO|MYA|NCR|NMI|RBW|RDH|RKL|RKT|RKW|RLW|RMD|RMN|RND|RNG|RNM|ROG|ROM|RON|ROS|ROT|RPW|RRY|RSB|RYN|SAD|SDC|SDH|SGO|SHU|SHN|SJU|SKG|SKD|SKI|SKL|SKR|SKS|SKT|SKU|SLA|SLE|SLK|SLW|SMB|SMC|SMD|SMG|SMS|SMY|SNA|SND|SNG|SNN|SNR|SNS|SNT|SNW|SNY|SOB|SOG|SON|SPE|SPO|SRE|SRI|SRO|SSE|SSK|SSN|STA|STM|STO|STR|STU|SUG|SUS|SVD|SWD|SWK|SYA|SYO|TST)-$") ||
                        patient.getPatientID().matches("^[0-9]{5}(TG|KTG|KDH|KFH|KHO|KKP|KLH|KLM|KNG|KNH|KPP|KRB|KTM|KTU|KYF|KKD|KND|KBD|KMD|KRD|MAG|MAJ|MAN|MAR|MBA|MDC|MDE|MDH|MDR|MGJ|MGK|MGR|MGT|MKB|MKD|MKM|MKN|MKP|MKR|MKT|MLW|MMC|MMD|MMH|MMM|MMN|MMT|MNB|MND|MNG|MNK|MNN|MNR|MNY|MOA|MOB|MOC|MOD|MOG|MOL|MON|MOR|MOS|MOT|MPM|MRI|MSA|MSB|MSL|MSN|MSR|MSS|MST|MSU|MTL|MTS|MWN|MWO|MYA|NCR|NMI|RBW|RDH|RKL|RKT|RKW|RLW|RMD|RMN|RND|RNG|RNM|ROG|ROM|RON|ROS|ROT|RPW|RRY|RSB|RYN|SAD|SDC|SDH|SGO|SHU|SHN|SJU|SKG|SKD|SKI|SKL|SKR|SKS|SKT|SKU|SLA|SLE|SLK|SLW|SMB|SMC|SMD|SMG|SMS|SMY|SNA|SND|SNG|SNN|SNR|SNS|SNT|SNW|SNY|SOB|SOG|SON|SPE|SPO|SRE|SRI|SRO|SSE|SSK|SSN|STA|STM|STO|STR|STU|SUG|SUS|SVD|SWD|SWK|SYA|SYO|TST)$"))){

                    noCheckDigit += 1;
            
                    // Correct the Check Digits
                    patient.correctCheckDigit();
                    
                }
                else{
                    
                    // Correct length but wrong format
                    wrongFormat += 1;
                    
                }
                
                continue;
            }
        }
        
        // Display the stats
        showStats += "Patient IDs Review Stats:\n\nTotal Patient Records: " + vlPatientList.size() + "\n\nCorrect Format: " + rightFormat;
        showStats += "\n\nMissing Check Digits: " + noCheckDigit + "\n\nWrong Format: " + wrongFormat;
        
        JOptionPane.showMessageDialog(null, showStats, "FACES Viral Load Electronic Module", JOptionPane.INFORMATION_MESSAGE);

    }
    
    public List<PatientDetails> getActiveList() {
        return vlPatientList;
    }
}
