/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Faces_Manifest_Project;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import static Faces_Manifest_Project.Global.threadError;
import static Faces_Manifest_Project.Global.threadOutput;

/**
 *
 * @author admin
 */
public class StreamGobbler extends Thread {
    
    InputStream inStream;
    String thrType;
    
    /**
     *
     * @param iStream
     * @param threadType
     */
    public StreamGobbler(InputStream iStream, String threadType)
    {
        this.inStream = iStream;
        this.thrType = threadType;
    }
    
    /**
     *
     */
    public void run()
    {
        
        String sGetProcessData = "";
        String sReadOutputLine = null;
        
        try
        {
            InputStreamReader isr = new InputStreamReader(inStream);
            BufferedReader br = new BufferedReader(isr);
            while ( (sReadOutputLine = br.readLine()) != null){
                
                switch ( thrType ) {
                    case "ERROR":
                        threadError += sReadOutputLine;
                        break;

                    case "OUTPUT":
                        threadOutput += sReadOutputLine;
                        break;
                }
            }
        }
        catch (Exception exError)
        {
            exError.printStackTrace();  
        }
    }
}
