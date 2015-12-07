package com.pixelandtag.smartagg.api.content;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;


@Path("v1")
public interface ContentRESTI {

	@GET
	@Produces("application/json")
	@Path("content/{contentid}")
	public Response get(@Context HttpHeaders headers, @PathParam("contentid")  Long contentid, @Context HttpServletRequest req) throws Exception;
	
}
