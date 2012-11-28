package com.mydesk.server;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class BlobServer extends HttpServlet
{
  private static final long serialVersionUID = -848796755245678979L;
  private BlobstoreService blobSrvc = BlobstoreServiceFactory.getBlobstoreService();
  
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException
  {
    DebugConsole.println("\nStart Blob Server");
    BlobKey blobKey = new BlobKey(request.getParameter("blob-key"));
    blobSrvc.serve(blobKey, response);
    DebugConsole.println("Blob Server- doGet\n");
    DebugConsole.printRequest(request);
  }
}
