/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Faces_Manifest_Project;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Edwin Mulwa
 */
public class VLResultData {
    
    // Declare variables
    private int LabID;
    private String PatientID;
    private int MFLCode;
    private String DateCollected;
    private String DateReceived;
    private String DateTested;
    private String FinalResultCps;
    private String FinalResultLog;
    private String DateDispatched;
    
    // Constructor
    public void VLResultData(){
        
    }
    
    // Define Getters and Setters
    public int getLabID() {
        return LabID;
    }

    public void setLabID(int LabID) {
        try {
            LabID = LabID;
        } catch (Exception pi) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pi);
        }
    }

    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String patientID) {
        try {
            PatientID = patientID;
        } catch (Exception pi) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pi);
        }
    }

    public int getMFLCode() {
        return MFLCode;
    }

    public void setMFLCode(int mflCode) {
        try {
            MFLCode = mflCode;
        } catch (Exception pi) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pi);
        }
    }

    public String getDateCollected() {
        return DateCollected;
    }

    public void setDateCollected(String dateCollected) {
        try {
            DateCollected = dateCollected;
        } catch (Exception pi) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pi);
        }
    }

    public String getDateReceived() {
        return DateReceived;
    }

    public void setDateReceived(String dateReceived) {
        try {
            DateReceived = dateReceived;
        } catch (Exception pi) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pi);
        }
    }

    public String getDateTested() {
        return DateTested;
    }

    public void setDateTested(String dateTested) {
        try {
            DateTested = dateTested;
        } catch (Exception pi) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pi);
        }
    }

    public String getFinalResultLog() {
        return FinalResultLog;
    }

    public void setResult(String finalResultLog) {
        try {
            FinalResultLog = finalResultLog;
        } catch (Exception pi) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pi);
        }
    }

    public String getFinalResultCps() {
        return FinalResultCps;
    }

    public void setFinalResultCps(String finalResultCps) {
        try {
            FinalResultCps = finalResultCps;
        } catch (Exception pi) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pi);
        }
    }

    public String getDateDispatched() {
        return DateDispatched;
    }

    public void setDateDispatched(String dateDispatch) {
        try {
            DateDispatched = dateDispatch;
        } catch (Exception pi) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pi);
        }
    }

}
