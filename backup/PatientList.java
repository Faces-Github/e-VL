package manifetproject2;

/*
 * @author Paul
 */
//import Faces_Manifest_Project.DataAccessClass;
//import Faces_Manifest_Project.PatientDetails;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;

public class PatientList {

    PatientDetails patientinfo=new PatientDetails();
    List<PatientDetails> viralList = new ArrayList<>();
    DataAccessClass dao;
    File sfile = new File("c:\\test.csv");
    String sample_type = "Frozen Plasma";
    String VlResult = "";
    //String missing = "Null";
    //int excelColumn;
    
//    java.sql.Date sqlDate;// = new java.sql.Date(values[3].getTime());

    PatientList() throws SQLException {
        try {
            dao = new DataAccessClass();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<PatientDetails> getPatientinfo() throws SQLException {
        try {
            // Get the patients list
            GetPatients();
        } catch (Exception pl) {
            Logger.getLogger(PatientList.class.getName()).log(Level.SEVERE, null, pl);
        }
        //Return the list
        return viralList;

    }

    //generating Viral Load records
    public void GetPatients() throws SQLException {
        try {
            // patientinfo.setpatientid(null);
            ResultSet rs;
            String sqlStatement = "select pi.identifier as PatientID,"
                    + "concat(pn.given_name,' ',pn.middle_name,' ',pn.family_name) as Patient_Names,"
                    + "l.name as Facility,"
                    + "e.encounter_datetime as sample_date,"
                    + " max(if(o.concept_id=7468,cn.name,null)) as shipped "
                    + " from encounter e "
                    + "join location l on l.location_id=e.location_id "
                    + "join patient_identifier pi on pi.patient_id=e.patient_id and pi.identifier_type=3 "
                    + "join obs o on o.encounter_id=e.encounter_id and o.concept_id in (7468)"
                    + "join concept_name cn on cn.concept_id=o.value_coded and cn.concept_name_type='Fully_specified' "
                    + "join encounter_type et on et.encounter_type_id=e.encounter_type "
                    + "join person_name pn on pn.person_id=e.patient_id "
                    + "where encounter_type=35 "
                    + "group by e.encounter_id "
                    + "having shipped='NO'";

            rs = dao.getPatientRecords(sqlStatement);

            // Clear the existing list
            viralList.clear();

            while (rs.next()) {
                

                patientinfo.setpatientid(rs.getString("PatientID"));
                patientinfo.setpatientNames(rs.getString("patient_Names"));
                patientinfo.setfacility(rs.getString("Facility"));
                patientinfo.setsample_date(rs.getDate("sample_date"));
                patientinfo.setshipped(rs.getBoolean("shipped"));

                viralList.add(patientinfo);

            }
        } catch (Exception pl) {
            Logger.getLogger(PatientList.class.getName()).log(Level.SEVERE, null, pl);
        }

    }

    //updating shipped records
    public void SaveManifest() throws SQLException {
        try {
            for (PatientDetails patient : viralList) {
                if (patient.isshipped()) {

                    String sql_update = "update obs o \n";
                    sql_update += "join patient_identifier pi on pi.patient_id=o.person_id \n";
                    sql_update += "set o.value_coded=1065 \n";
                    sql_update += "where o.concept_id=7468 and pi.identifier='" + patient.getpatientid() + "' and obs_datetime='" + patient.getsample_date() + "'";

                    dao.updatePatientRecords(sql_update);
                }

            }
        } catch (Exception sm) {
            Logger.getLogger(PatientList.class.getName()).log(Level.SEVERE, null, sm);
        }

    }

    public void generateManifest() throws IOException {

        try {
            // Open File Writer
            FileWriter excel = new FileWriter(sfile);

            // Insert columns into file
            excel.write("Patient ID, Names,Facility,Date,Sample_Type,Result\n");

            for (PatientDetails patient : viralList) {

                if (patient.isshipped()) {

                    excel.write(patient.getpatientid().toString() + "," + patient.getpatientNames().toString() + "," + patient.getfacility() + "," + patient.getsample_date() + "," + sample_type + "," + VlResult + "\n");
                }
            }

            // Close writer
            excel.close();

        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public List<PatientDetails> getExcelRecords() throws SQLException, IOException, ParseException {

        try {
            
            // Declare variables for storing column indexes
            int iPatientID_Col = 0, iResult_Col = 0, iResultLog_Col = 0, iResultDate_Col = 0;
            
            viralList.clear();

            JFileChooser excelFile = new JFileChooser();
            excelFile.showOpenDialog(null);
            File dataFile = excelFile.getSelectedFile();
            InputStream input = new BufferedInputStream(new FileInputStream(dataFile));
            POIFSFileSystem fs = new POIFSFileSystem(input);
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);

            Iterator rows = sheet.rowIterator();
            
            // Read header row to determine row headings
            HSSFRow row1 = (HSSFRow) rows.next();
            
            for(int iCount = 0; iCount < row1.getLastCellNum(); iCount++){
                switch (row1.getCell(iCount).getStringCellValue()) {
                    
                    case "SAMPLE ID":
                        iPatientID_Col = iCount;
                        break;
                    case "RESULT (Copies/ml)":
                        iResult_Col = iCount;
                        break;
                    case "RESULT (Log Copies/ml)":
                        iResultLog_Col = iCount;
                        break;
                     case "RUN DATE":
                        iResultDate_Col = iCount;
                        break;
                        
                }
            }
//           rows.next();
           
            while (rows.hasNext()) {
                row1 = (HSSFRow) rows.next();

                Cell sampleId = row1.getCell(iPatientID_Col);
                Cell viralResult = row1.getCell(iResult_Col);
                Cell resultLog = row1.getCell(iResultLog_Col);
                Cell runDate = row1.getCell(iResultDate_Col);

                patientinfo = new PatientDetails();


                if (sampleId != null) {
                    switch (sampleId.getCellType()) {

                        case Cell.CELL_TYPE_NUMERIC:
                            //System.out.print(sampleId.getNumericCellValue() + "\t");
                            patientinfo.setpatientid(Double.toString(sampleId.getNumericCellValue()));

                            break;
                        case Cell.CELL_TYPE_STRING:
                            //System.out.print(sampleId.getStringCellValue() + "\t");
                            patientinfo.setpatientid(sampleId.getStringCellValue());
                            break;
                    }

                }

                if (viralResult != null) {
                    switch (viralResult.getCellType()) {

                        case Cell.CELL_TYPE_NUMERIC:
                            //System.out.print(vResult.getNumericCellValue() + "\t");
                            patientinfo.setvResult(Double.toString(viralResult.getNumericCellValue()));
                            break;
                        case Cell.CELL_TYPE_STRING:
                            // System.out.print(vResult.getStringCellValue() + "\t");
                            patientinfo.setvResult(viralResult.getStringCellValue());
                            break;
                        default:
                            patientinfo.setvResult("");
                            break;
                    }

                }

                if (resultLog != null) {
                    switch (resultLog.getCellType()) {

                        case Cell.CELL_TYPE_NUMERIC:
                            // System.out.print(resultLog.getNumericCellValue() + "\t");
                            patientinfo.setlogResult(Double.toString(resultLog.getNumericCellValue()));
                            break;
                        case Cell.CELL_TYPE_STRING:
                            //System.out.print(resultLog.getStringCellValue() + "\t");
                            patientinfo.setlogResult(resultLog.getStringCellValue());
                            break;
                        default:
                            patientinfo.setlogResult("");
                            break;


                    }

                }
                if (runDate != null) {

                    DataFormatter df = new DataFormatter();
                    String sRunDate = df.formatCellValue(runDate);
                    DateFormat formatter = new SimpleDateFormat("MM/dd/yy hh:mm"); //format 
                    Date dtRunDate = formatter.parse(sRunDate);
                    java.sql.Date dtSQLDate = new java.sql.Date(dtRunDate.getTime());

                    patientinfo.setdateRun(dtSQLDate);

                    viralList.add(patientinfo);

                }

            }
        } catch (Exception eX) {
            System.out.println(eX);
        }


        return viralList;

    }

    public void SaveExcelData() throws SQLException {
        try {

            while (viralList.size() > 0) {

                PatientDetails patient = viralList.get(0);

                //for (PatientDetails patient : viralList) {
                //query to insert viral load results to the Database
                String sqlResult = "insert into obs (person_id,obs_datetime,location_id,concept_id,value_text,uuid,encounter_id,creator,date_created) "
                        + "select pi.patient_id,MAX(e.encounter_datetime),mid(max(concat(e.encounter_datetime,e.location_id)),20),7470," + patient.getvResult() + ",uuid(),mid(max(concat(e.encounter_datetime,e.encounter_id)),20),e.creator,e.date_created "
                        + "from patient_identifier pi join encounter e on e.patient_id=pi.patient_id and pi.identifier='" + patient.getpatientid() + "' "
                        + "join location l on l.location_id=e.location_id "
                        + " where e.encounter_datetime<='" + patient.getdateRun() + "' and e.encounter_type=35";

                //method inserting results to the database
                dao.uploadResults(sqlResult);


                //query to insert viral load results to the Database
                sqlResult = "insert into obs (person_id,obs_datetime,location_id,concept_id,value_text,uuid,encounter_id,creator,date_created) "
                        + "select pi.patient_id,MAX(e.encounter_datetime),mid(max(concat(e.encounter_datetime,e.location_id)),20),7469," + patient.getlogResult() + ",uuid(),mid(max(concat(e.encounter_datetime,e.encounter_id)),20),e.creator,e.date_created "
                        + "from patient_identifier pi join encounter e on e.patient_id=pi.patient_id and pi.identifier='" + patient.getpatientid() + "' "
                        + "join location l on l.location_id=e.location_id "
                        + " where e.encounter_datetime<='" + patient.getdateRun() + "'and e.encounter_type=35";

                //method inserting results to the database
                dao.uploadResults(sqlResult);

                viralList.remove(patient);
                //}
            }
        } catch (SQLException ex) {
            Logger.getLogger(PatientList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<PatientDetails> getActiveList() {
        return viralList;
    }
}
