package com.mydesk.server;

import com.mydesk.shared.GenericBuffer;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import javax.inject.Named;

/*
 * Cloud Endpoint Class. No work is done in this class. Used
 * only to generate the client library. Specifically we will not
 * import any libraries beyond what is needed to generate the endpoint
 */
@Api(name = "genericendpt")
public class GenericEndpoint 
{
  //===========================================================================
  // Returns a encoded Protocol buffer test message
  @ApiMethod(name ="getTest", httpMethod = "GET")
	public GenericBuffer getTest()
	{ 
    return EndPointHelper.getTest();
	}
  
  //===========================================================================
  // Return the URL for file downloading.
  @ApiMethod(name="getFile", httpMethod="GET")
  public GenericBuffer getFileUrl(@Named("fileName") String fileName)
  {
    return EndPointHelper.getFileUrl(fileName);
  }
}
