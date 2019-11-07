package Faces_Manifest_Project;

/*
 * @author Paul
 */
import static Faces_Manifest_Project.Global.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatientDetails {

    private String patientid;
    private String patientUUID;
    private String patientNames;
    private String facility;
    private Date sample_date;
    private boolean shipped;
    private String vResult;
    private String logResult;
    private Date dateRun;
    private String comments;
    private boolean imported;
    private String providerUUID;
    private String encounterUUID;
    private String facilityUUID;
    private String mflCode;
    private String dateStartedART;
    private String currentRegimen = "";
    private int regimenLine;
    private String gender;
    private String birthDate;
    private int age;
    private String pregnant = "No";
    private String breastFeeding = "No";
    
    // Edwin 19 Jul 2016
    private String sampleType;
    private String dateCurrentRegimen;
    private int justificationCode1;
    private int justificationCode2;
    
    // Geofrey 5th Sept 2019
    
    private int justificationCode;
    private String dateTimeCollection;
    private String dateTimeSeparation;
    private String rejection;
    private String rejectionReason;
    
    PatientDetails() {
    
    }

    public PatientDetails(String patientid, String patientNames, String patientUUID, String facility, Date sample_date, boolean shipped, String vResult, String logResult, Date dateRun, String comments,boolean imported, String providerUUID) {
        
        this.patientid = patientid;
        this.patientUUID = patientUUID;
        this.patientNames = patientNames;
        this.facility = facility;
        this.sample_date = sample_date;
        this.shipped = shipped;
        this.vResult = vResult;
        this.logResult = logResult;
        this.dateRun = dateRun;
        this.comments=comments;
        this.imported=imported;
        this.providerUUID = providerUUID;
        
    }

    public String getPatientID() {
        return patientid;
    }

    public void setPatientID(String patientid) {
        try {
            this.patientid = patientid.toUpperCase();
        } catch (Exception pi) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pi);
        }
    }

    public String getPatientUUID() {
        return this.patientUUID;
    }

    public void setPatientUUID(String patientUUID) {
        try {
            this.patientUUID = patientUUID;
        } catch (Exception pi) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pi);
        }
    }

    public String getProviderUUID() {
        return this.providerUUID;
    }

    public void setProviderUUID(String providerUUID) {
        try {
            this.providerUUID = providerUUID;
        } catch (Exception pi) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pi);
        }
    }

    public String getEncounterUUID() {
        return this.encounterUUID;
    }

    public void setEncounterUUID(String encounterUUID) {
        try {
            this.encounterUUID = encounterUUID;
        } catch (Exception pi) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pi);
        }
    }

    public String getFacilityUUID() {
        return this.facilityUUID;
    }

    public void setFacilityUUID(String facilityUUID) {
        try {
            this.facilityUUID = facilityUUID;
        } catch (Exception pi) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pi);
        }
    }

    public String getPatientNames() {
        return patientNames;
    }

    public void setPatientNames(String patientNames) {
        try {
            this.patientNames = patientNames;
        } catch (Exception pd) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pd);
        }
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        try {
            this.facility = facility;
        } catch (Exception pd) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pd);
        }
    }

    public Date getSampleDate() {
        return sample_date;
    }

    public void setSampleDate(Date sample_date) {
        try {
            this.sample_date = sample_date;

        } catch (Exception pd) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pd);
        }
    }

    public boolean isShipped() {
        return shipped;
    }

    public void setShipped(boolean shipped) {
        try {
            this.shipped = shipped;
        } catch (Exception pd) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pd);
        }
    }

    public String getVLResult() {
        return vResult;
    }

    public void setVLResult(String vResult) {
        try {
            this.vResult = vResult;

        } catch (Exception pd) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pd);
        }
    }

    public String getLogResult() {
        return logResult;
    }

    public void setLogResult(String logResult) {
        try {
            this.logResult = logResult;
        } catch (Exception pd) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pd);
        }
    }

    public Date getDateRun() {
        return dateRun;
    }

    public void setDateRun(Date dateRun) {
        try {
            this.dateRun = dateRun;

        } catch (Exception pd) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pd);
        }
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        try {
            this.comments = comments;
        } catch (Exception pd) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pd);
        }
    }
    
        public boolean isImported() {
        return imported;
    }

    public void setImported(boolean imported) {
        try {
            this.imported = imported;
        } catch (Exception pd) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pd);
        }
    }

    public String getMFLCode() {
        return mflCode;
    }

    public void setMFLCode(String mfl) {
        try {
            this.mflCode = mfl;
        } catch (Exception pi) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pi);
        }
    }
    
    public String getDateStartedART() {
        return dateStartedART;
    }

    public void setDateStartedART(String startART) {
        try {
            this.dateStartedART = startART;
        } catch (Exception pi) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pi);
        }
    }
    
    public String getCurrentRegimen() {
        return currentRegimen;
    }

    public void setCurrentRegimen(String currRegimen) {
        try {
            this.currentRegimen = currRegimen;
        } catch (Exception pi) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pi);
        }
    }
    
    public int getRegimenLine() {
        return regimenLine;
    }

    public void setRegimenLine(int regLine) {
        try {
            this.regimenLine = regLine;
        } catch (Exception pi) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pi);
        }
    }
    
    public String getGender() {
        return gender;
    }

    public void setGender(String sex) {
        try {
            this.gender = sex;
        } catch (Exception pi) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pi);
        }
    }
    
    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String dob) {
        try {
            this.birthDate = dob;
        } catch (Exception pi) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pi);
        }
    }
    
    public int getAge() {
        return age;
    }

    public void setAge(int patAge) {
        try {
            this.age = patAge;
        } catch (Exception pi) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pi);
        }
    }
    
    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sample_type) {
        try {
            this.sampleType = sample_type;

        } catch (Exception pd) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pd);
        }
    }

    public String getPregnancyStatus() {
        return pregnant;
    }

    public void setPregnancyStatus(String pregnant_state) {
        try {
            this.pregnant = pregnant_state;

        } catch (Exception pd) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pd);
        }
    }

    public String getBreastFeeding() {
        return breastFeeding;
    }

    public void setBreastFeeding(String bFeeding) {
        try {
            this.breastFeeding = bFeeding;

        } catch (Exception pd) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pd);
        }
    }

    public String getCurrentRegimenDate() {
        return dateCurrentRegimen;
    }

    public void setCurrentRegimenDate(String dateCurrReg) {
        try {
            this.dateCurrentRegimen = dateCurrReg;

        } catch (Exception pd) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pd);
        }
    }

    public int getJustificationCode() {
        return justificationCode;
    }

    public void setJustificationCode(int justificationCode) {
        this.justificationCode = justificationCode;
    }

    public int getJustificationCode1() {
        return justificationCode1;
    }

    public void setJustificationCode1(int justCode1) {
        try {
            this.justificationCode1 = justCode1;

        } catch (Exception pd) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pd);
        }
    }

    public int getJustificationCode2() {
        return justificationCode2;
    }

    public void setJustificationCode2(int justCode2) {
        try {
            this.justificationCode2 = justCode2;

        } catch (Exception pd) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pd);
        }
    }

    public String getDateTimeCollection() {
        return dateTimeCollection;
    }

    public void setDateTimeCollection(String dateTimeCollection) {
        this.dateTimeCollection = dateTimeCollection;
    }

    public String getDateTimeSeparation() {
        return dateTimeSeparation;
    }

    public void setDateTimeSeparation(String dateTimeSeparation) {
        this.dateTimeSeparation = dateTimeSeparation;
    }

    public String getRejection() {
        return rejection;
    }

    public void setRejection(String rejection) {
        this.rejection = rejection;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public void correctCheckDigit(){
    
//        // Modify the Patient ID to have the correct check digit
//        if (patt_FACES_ID.matcher(patientid).matches()) {
//
//            // Get the new check digit
//            patientid = patientid.substring(0, 8) + "-" + getCheckDigit(patientid.substring(0,8));
//
//            // Indicate in the comments
//            comments = "Check Digit derived.";
//            
//        }
//        else {
//            // Log the IDs that have a problem so that they can be corrected
//            
//        }
        
        if(patientid.length() > 7){
            
            // Get the new check digit
            patientid = patientid.substring(0, 8) + "-" + getCheckDigit(patientid.substring(0,8));

            // Indicate in the comments
            comments = "Check Digit derived.";
            
        }
    }
    
    private int getCheckDigit(String idWithoutCheckdigit){

        // The purpose of check digits is simple. 
        // Any time identifiers (typically number +/- letters) are being manually entered via keyboard, 
        // there will be errors. Inadvertent keystrokes or fatigue can cause digits to be rearranged, dropped, or inserted. 

        // Check digits help to reduce the likelihood of errors by introducing a final digit that is calculated from the prior digits. 
        // Using the proper algorithm, the final digit can always be calculated. Therefore, when a number is entered into the system 
        // (manually or otherwise), the computer can instantly verify that the final digit matches the digit predicted by the check digit 
        // algorithm. If the two do not match, the number is refused. The end result is fewer data entry errors.

        // allowable characters within identifier
        String validChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVYWXZ_";

        // this will be a running total
        int sum = 0;
        int weight;

        try {
            
            // remove leading or trailing whitespace, convert to uppercase
            idWithoutCheckdigit = idWithoutCheckdigit.trim().toUpperCase();

            // loop through digits from right to left
            for (int iDigitCount = 0; iDigitCount < idWithoutCheckdigit.length(); iDigitCount++) {

                //set ch to "current" character to be processed
                char ch = idWithoutCheckdigit.charAt(idWithoutCheckdigit.length() - iDigitCount - 1);

                // throw exception for invalid characters
                if (validChars.indexOf(ch) == -1){
                    throw new IndexOutOfBoundsException("\"" + ch + "\" is an invalid character");
                }

                // our "digit" is calculated using ASCII value - 48
                int digit = (int)ch - 48;

                // weight will be the current digit's contribution to
                // the running total
                if (iDigitCount % 2 == 0) {

                      // for alternating digits starting with the rightmost, we
                      // use our formula this is the same as multiplying x 2 and
                      // adding digits together for values 0 to 9.  Using the
                      // following formula allows us to gracefully calculate a
                      // weight for non-numeric "digits" as well (from their
                      // ASCII value - 48).
                      weight = (2 * digit) - (int) (digit / 5) * 9;

                    } else {

                      // even-positioned digits just contribute their ascii
                      // value minus 48
                      weight = digit;

                    }

                // keep a running total of weights
                sum += weight;

            }

            // avoid sum less than 10 (if characters below "0" allowed,
            // this could happen)
            sum = Math.abs(sum) + 10;

        }
        catch(IndexOutOfBoundsException iobError){
            iobError.printStackTrace();
        }            
        catch(Exception exError){
            exError.printStackTrace();
        }
        
        // check digit is amount needed to reach next number
        // divisible by ten
        return (10 - (sum % 10)) % 10;

    }
    

    void getComments(String dyufutu) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
