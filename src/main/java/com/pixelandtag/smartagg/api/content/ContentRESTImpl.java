package com.pixelandtag.smartagg.api.content;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.pixelandtag.smartagg.ejb.content.ContentEJBI;

@Stateless
public class ContentRESTImpl  implements ContentRESTI {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@EJB
	private ContentEJBI contentEJB;
	
	@Override
	public Response get(@Context HttpHeaders headers, @PathParam("contentid")  Long contentid, @Context HttpServletRequest req) throws Exception{
		
		Response response = null;
		JSONObject jsob = new JSONObject();
		
		try{
			
			logger.info("::::: contentid = "+contentid);
			if(contentid==null)
				throw new ContentException("contentid not provided");
			
			
		}catch(ContentException exp){
			logger.error(exp.getMessage(), exp);
			jsob.put("message", exp.getMessage());
			jsob.put("success", false);
			response = Response.status(Response.Status.NO_CONTENT)
					.entity(jsob.toString()).build();
		}catch(Exception exp){
			logger.error(exp.getMessage(), exp);
			jsob.put("message", "Problem occurred");
			jsob.put("success", false);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(jsob.toString()).build();
		}
		
		return response;
	}

}
