package com.mydesk.server;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

/*
 * Contains function used when outputting debug messages from the server
 */
public class DebugConsole
{
  // Set false to prevent debut output to the console
  static final boolean DebugOut = false;
  
  static public void print(String str)
  {
    if(DebugOut)
    {
      System.out.println(str);
    }
  }
  static public void println(String str)
  {
    if(DebugOut)
    {
      System.out.println(str);
    }
  }
  
  static public void printRequest(HttpServletRequest request) 
    throws IOException
  {
    if(DebugOut)
    {
      BufferedReader reader = request.getReader();    
      while(reader.ready())
      {
        String line = reader.readLine();
        println(line);
      }
      println("\nRequest Done\n");
    }
  }  
}
