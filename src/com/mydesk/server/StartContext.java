package com.mydesk.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.mydesk.filemanager.FileMngr;

// Used to start the file manager before client request
public class StartContext implements ServletContextListener
{
  static
  {  
    FileMngr.init();
  }
  @Override
  public void contextDestroyed(ServletContextEvent arg0)
  {}
  @Override
  public void contextInitialized(ServletContextEvent arg0)
  { }
}
