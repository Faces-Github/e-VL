/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Faces_Manifest_Project;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 *
 * @author admin
 */
public class Global {
    
    // This class is used to declare global variables and functions
    public static String threadError = "";
    public static String threadOutput = "";

    // Variables for VL Results Import
    public static int resultOption = 0;     // 0 = AMPATH; 1 = MS Excel
    public static SimpleDateFormat VL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    public static File dataFile = null;
    public static String importFacility;
    public static String vlImportStartDate;
    public static String vlImportEndDate;
    public static String vlShippingStartDate;
     public static String vlShippingEndDate;
    public static boolean cancelImport = true;
    public static boolean noInternetConnection = false;
    public static boolean uploadShippingManifest = false;
    public static Pattern patt_FACES_ID = Pattern.compile("^[0-9]{5}(SGO|STR|SSE|SKG|SNG|SNA|SMS|SDH|SRO|SNS|SKG|SKI|SON|SKU|SMD|STO|SKD|SNA|SGO|SME|MKR|MLW|MMC|MMH|MND|MNR|MOG|MOL|MOT|MSR|MWO|MMT|MNY|NCR|NMI|RDH|RLW|RMN|RON|ROT|RYN|SDH|SNG|SND|SRE|SRI|SWK|SUG|SSK|SSN|SYO|STA|SUG|KRB|KPP|KLM|KTU|KDH)-[0-9]$");

}
