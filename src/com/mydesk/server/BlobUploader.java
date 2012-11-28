package com.mydesk.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mydesk.filemanager.FileMngr;

public class BlobUploader extends HttpServlet
{
  private static final long serialVersionUID = 4161069411326751310L;
  
  //===========================================================================  
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    DebugConsole.println("Blob Uploader- doPost\n");
    FileMngr.makeCloudMetaData(request);    
    DebugConsole.printRequest(request);
  }
}
