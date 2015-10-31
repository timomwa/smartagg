package com.pixelandtag.smartagg.android.gcm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestPush {
	
	final static private String deviceId = "3F267DAA9E1EB853";// /01
    final static private String apiId = "AIzaSyBx_qTrOvK7fUIMuRPUuiJ963u3ZpgOoEk";// "778162576633";//YOUR_API_ID";
    final static private String sendUrl = "https://gcm-http.googleapis.com/gcm/send";//"https://android.googleapis.com/gcm/send";

    public static void main(String[] args) {
        URL url;
        HttpsURLConnection urlConnection;
        OutputStream os = null;
        InputStream is = null;;
        try {
            url = new URL(sendUrl);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(3000);
            urlConnection.setReadTimeout(3000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("User-Agent", "Android Push tester");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Authorization", "key="+apiId);
            JSONObject message = new JSONObject();
            JSONArray regIds = new JSONArray();
            JSONObject data = new JSONObject();
            regIds.put(deviceId);
            message.put("registration_ids", regIds);
            //message.put("collapse_key", value)
            data.put("something", "value");
            message.put("data", data);
            urlConnection.setDoOutput(true);
            os = urlConnection.getOutputStream();
            os.write(message.toString().getBytes());
            os.flush();
            int status = urlConnection.getResponseCode();
            is = urlConnection.getInputStream();
            byte[] response = new byte[4096];
            is.read(response);
            String responseText = String.valueOf(response); 
            System.out.println("responseText  :: "+responseText);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            os = null;
            is = null;
        }
    }

}
