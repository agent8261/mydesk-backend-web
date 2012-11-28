package com.mydesk.client;

import java.io.Serializable;
import java.util.Date;

public class FileDirEntry implements Serializable
{
  private static final long serialVersionUID = -6585161221017837931L;
  
  public String filename;
  public Long id;  
  public Date lastUpdated;
  public long size; 
}
