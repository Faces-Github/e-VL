package manifetproject2;

/*
 * @author Paul
 */
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatientDetails {

    private String patientid;
    private String patientNames;
    private String facility;
    private Date sample_date;
    private boolean shipped;
    private String vResult;
    private String logResult;
    private Date dateRun;

    PatientDetails() {
    }

    public PatientDetails(String patientid, String patientNames, String facility, Date sample_date, boolean shipped, String vResult, String logResult, Date dateRun) {
        this.patientid = patientid;
        this.patientNames = patientNames;
        this.facility = facility;
        this.sample_date = sample_date;
        this.shipped = shipped;
        this.vResult = vResult;
        this.logResult = logResult;
        this.dateRun = dateRun;

    }

    public String getpatientid() {
        return patientid;
    }

    public void setpatientid(String patientid) {
        try {
            this.patientid = patientid;
        } catch (Exception pi) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pi);
        }
    }

    public String getpatientNames() {
        return patientNames;
    }

    public void setpatientNames(String patientNames) {
        try {
            this.patientNames = patientNames;
        } catch (Exception pd) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pd);
        }
    }

    public String getfacility() {
        return facility;
    }

    public void setfacility(String facility) {
        try {
            this.facility = facility;
        } catch (Exception pd) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pd);
        }
    }

    public Date getsample_date() {
        return sample_date;
    }

    public void setsample_date(Date sample_date) {
        try {
            this.sample_date = sample_date;

        } catch (Exception pd) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pd);
        }
    }

    public boolean isshipped() {
        return shipped;
    }

    public void setshipped(boolean shipped) {
        try {
            this.shipped = shipped;
        } catch (Exception pd) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pd);
        }
    }

    public String getvResult() {
        return vResult;
    }

    public void setvResult(String vResult) {
        try {
            this.vResult = vResult;

        } catch (Exception pd) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pd);
        }
    }

    public String getlogResult() {
        return logResult;
    }

    public void setlogResult(String logResult) {
        try {
            this.logResult = logResult;
        } catch (Exception pd) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pd);
        }
    }

    public Date getdateRun() {
        return dateRun;
    }

    public void setdateRun(Date dateRun) {
        try {
            this.dateRun = dateRun;

        } catch (Exception pd) {
            Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, pd);
        }
    }
}
