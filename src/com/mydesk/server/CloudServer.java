package com.mydesk.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mydesk.filemanager.FileMngr;

public class CloudServer extends HttpServlet
{  
  private static final long serialVersionUID = -4426624564505942933L;

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
  {
    DebugConsole.println("Start Post Cloud Request");
    FileMngr.storeFile(request);
    DebugConsole.println("End Post Cloud Request");
  }
}
