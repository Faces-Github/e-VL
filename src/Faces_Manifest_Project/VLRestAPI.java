/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Faces_Manifest_Project;

/**
 *
 * @author admin
 */

import static Faces_Manifest_Project.Global.noInternetConnection;
import static Faces_Manifest_Project.Global.uploadShippingManifest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
// import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class VLRestAPI {
    
    String openUserName = null;
    String openPassword = null;
    String openURLBase = null;
    
    public VLRestAPI(){
        
    }
    
    /**
     * HTTP POST
     * @param URLPath
     * @param input
     * @return
     * @throws Exception
     */
    //public int getRequestPost(String URLPath, StringEntity input, ArrayList<NameValuePair> params) throws Exception {
    public int getRequestPost(String URLPath, StringEntity input, String params, String apiKey) throws Exception {
        
        /*
            The return value can take the following values:
                0 = Unsuccessful
                1 = Successful
                2 = Unsuccesful due to internet connection. Terminate the outside loop!
        */
        
        String URL = openURLBase + URLPath;
        String responseString = new String();
        String connStatusMessage = "";
        int response =  0;
        HttpContext localContext = new BasicHttpContext();
        HttpClient httpclient = HttpClientBuilder.create().build();
        
        try {
            HttpPost httpPost = new HttpPost(URL);
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(openUserName, openPassword);
            BasicScheme scheme = new BasicScheme();
//            Header authorizationHeader = scheme.authenticate(credentials, httpPost);
            Header authorizationHeader = scheme.authenticate(credentials, httpPost, localContext);
            httpPost.setHeader(authorizationHeader);
            
            // Edwin: 26Nov18
            httpPost.addHeader("Content-Type", "application/json");
            //httpPost.addHeader("apikey", apiKey);
            //StringEntity newParams = new StringEntity(params);
            
            // Edwin: 26Nov18
            
            // If the input parameter is NULL, use the other
            if(input != null){
                httpPost.setEntity(input);
            }
            else{
                //httpPost.setEntity(new UrlEncodedFormEntity(params));
                httpPost.addHeader("apikey", apiKey);
                StringEntity newParams = new StringEntity(params);
                httpPost.setEntity(newParams);   // Edwin: 26Nov18
            }
                

            HttpResponse responseRequest = httpclient.execute(httpPost);

            if (responseRequest.getStatusLine().getStatusCode() != 204 && responseRequest.getStatusLine().getStatusCode() != 201 && responseRequest.getStatusLine().getStatusCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + responseRequest.getStatusLine().getStatusCode() + "\n"
                            + responseRequest.getStatusLine().getReasonPhrase());
            }

            // Get the response Body
            HttpEntity responseEntity = responseRequest.getEntity();
            if(responseEntity != null) {
                responseString = EntityUtils.toString(responseEntity);
                System.out.println(responseString);
            }

            httpclient.getConnectionManager().shutdown();

            if(responseString.contains("FAIL")){
                return response;
            }

            // Set success status of method
            response = 1;
        } 
        catch (RuntimeException rEx){
            System.out.println(rEx.getMessage());
        }
        catch (UnknownHostException exNoWeb) {
            if(!noInternetConnection){
                
                connStatusMessage = "No internet connection detected. Upload process will stop.\n\nPlease resolve then try again.";

                if (!uploadShippingManifest){
                    connStatusMessage += "\n\nThe Shipping Manifest text file will still be generated for later use.";
                }

                JOptionPane.showMessageDialog(null, connStatusMessage);                
                
                noInternetConnection = true;
            }
//            Logger.getLogger(VLRestAPI.class.getName()).log(Level.SEVERE, null, exNoWeb);
            response = 2;
        }
        catch (IOException eIO) {
            eIO.printStackTrace();
        }
        catch(Exception exError){
            exError.getCause();
            exError.printStackTrace();
        }            

        finally {
            httpclient.getConnectionManager().shutdown();
        }
        return response;
    }
    
    /**
     * HTTP GET
     * @param URLPath
     * @return
     * @throws Exception
     */
    //public String getRequestGet(String URLPath) throws Exception {
    public String getRequestGet(String URLPath, String params, String apiKey) throws Exception {
        
        String URL = openURLBase + URLPath;
        String response =  "";
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpContext localContext = new BasicHttpContext();  // Edwin: 28Nov18
        
        try {
            HttpPost httpPost = new HttpPost(URL);  // Edwin: 28Nov18
            //HttpGet httpGet = new HttpGet(URL);

            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(openUserName, openPassword);
            BasicScheme scheme = new BasicScheme();
            Header authorizationHeader = scheme.authenticate(credentials, httpPost, localContext);
//            Header authorizationHeader = scheme.authenticate(credentials, httpGet);
            httpPost.setHeader(authorizationHeader);  // Edwin: 28Nov18
//            httpGet.setHeader(authorizationHeader);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            // Edwin: 26Nov18
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("apikey", apiKey);
            StringEntity newParams = new StringEntity(params);
            httpPost.setEntity(newParams);   // Edwin: 26Nov18
            
            HttpResponse responseRequest = httpclient.execute(httpPost);

            if (responseRequest.getStatusLine().getStatusCode() != 204 && responseRequest.getStatusLine().getStatusCode() != 201 && responseRequest.getStatusLine().getStatusCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + responseRequest.getStatusLine().getStatusCode() + "\n"
                            + responseRequest.getStatusLine().getReasonPhrase());
            }

            // Get the response Body
            HttpEntity responseEntity = responseRequest.getEntity();
            if(responseEntity != null) {
                response = EntityUtils.toString(responseEntity);
                System.out.println(response);
            }

            httpclient.getConnectionManager().shutdown();

            if(response.contains("FAIL")){
                return response;
            }
            
            // Edwin: 26Nov18
            //System.out.println("Executing request: " + httpGet.getRequestLine());
            //System.out.println(response);
//            response = httpclient.execute(httpGet,responseHandler);


        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return response;
    }


    public void setUsername(String username) {
            openUserName = username;
    }


    public void setPassword(String password) {
            openPassword = password;
    }


    public void setURLBase(String uRLBase) {
            openURLBase = uRLBase;
    }

}
