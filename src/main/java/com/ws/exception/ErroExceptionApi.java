package com.ws.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ErroExceptionApi extends Exception implements
		ExceptionMapper<ErroExceptionApi> {
		 	
	    private static final long serialVersionUID = 1L;
	    
	    public ErroExceptionApi() {
	        super("No File found with given name !!");
	    }
	 	    	 
	    public ErroExceptionApi(String string) {
	        super(string);
	    }
	 
	    @Override
	    public Response toResponse(ErroExceptionApi exception) 
	    {
	        return Response.status(404).entity(exception.getMessage())
	                                    .type("text/json").build();
	    }
	}
	
