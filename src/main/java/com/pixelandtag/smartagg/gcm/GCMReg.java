package com.pixelandtag.smartagg.gcm;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.pixelandtag.smartagg.ejb.device.DeviceEJBI;
import com.pixelandtag.smartagg.entities.Device;


/**
 * Servlet implementation class GCMReg
 */
public class GCMReg extends HttpServlet {
	
	
	@EJB
	private DeviceEJBI deviceEJB;
	
	private static final long serialVersionUID = 1L;
	
	private Logger logger = Logger.getLogger(getClass());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GCMReg() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Enumeration<String> enums = request.getParameterNames();
		
		String paramName = "";
		String value  = "";
		
		Map<String, String> incomingparams = new HashMap<String,String>();
		
		String ip_addr = request.getRemoteAddr();
		
		Enumeration<String> headernames = request.getHeaderNames();
		String headerstr = "\n";
		 while (headernames.hasMoreElements()) { 
			 String headerName = (String) headernames.nextElement();  
		     String headerValue = request.getHeader(headerName);  
		     headerstr += "\n\t\tGCM_REG:HEADER >> "+headerName+ " : "+headerValue;
		     //incomingparams.put(Receiver.HTTP_HEADER_PREFIX+headerName, headerValue);
		 }
		
		 
		 logger.info("GCM_REG:"+headerstr+"\n\n");
		
		
		String params = "\n\n\tGCM_REG::: real ip_addr "+ip_addr+" fake ip address 129.129.129";
		
		while(enums.hasMoreElements()){
			
			paramName = (String) enums.nextElement();
			
			value = request.getParameter(paramName);
			
			incomingparams.put(paramName, value);
						
			params += "\n\tGCM_REG::: "+   paramName +" : "+value;
			
		}
		
		logger.info(params);
		
		try {
			
			final String body = getBody(request);
			
			logger.info("GCM_REG_BODY :::  "+body+"\n\n");
			
			
			String registrationid = request.getParameter("registrationId");
			Device device = deviceEJB.findByRegistrationId(registrationid);
			if(device==null){
				device = new Device();
				device.setRegistrationID(registrationid);
				device = deviceEJB.save(device);
			}
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	
	
	public String getBody(HttpServletRequest request) throws IOException {

	    String body = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader bufferedReader = null;
	    InputStream inputStream = null;
	    
	    try {
	        inputStream = request.getInputStream();
	        if (inputStream != null) {
	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	            char[] charBuffer = new char[128];
	            int bytesRead = -1;
	            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                stringBuilder.append(charBuffer, 0, bytesRead);
	            }
	        } else {
	            stringBuilder.append("");
	        }
	    } catch (IOException ex) {
	        logger.error(ex.getMessage(),ex);
	    } finally {
	    	
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException ex) {
	            	 logger.error(ex.getMessage(),ex);
	            }
	        }
	        
	        try {
	        	inputStream.close();
            } catch (IOException ex) {
            }
	    }

	    body = stringBuilder.toString();
	    return body;
	}

}
