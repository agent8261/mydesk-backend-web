package com.mydesk.shared;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class GenericBuffer implements Serializable
{	
	private static final long serialVersionUID = -2302735393217586476L;
	@PrimaryKey
	private String pBufmsg; // store encoded proto buff message
	private String data; // store encode data
	
	public GenericBuffer() 
	{ pBufmsg = new String("default"); data = new String("value"); }
	
	public GenericBuffer(String _pBufmsg, String _data)
	{  pBufmsg = new String(_pBufmsg); data = new String(_data);  }
		
	public String getPBufMsg()
	{  return pBufmsg;  }
	
	public String getData()
	{  return data;  }
		
	public void setPBufMsg(String _pBufmsg)
	{ pBufmsg = new String(_pBufmsg);  }
	
	public void setData(String _data)
	{  data = new String(_data);  }	
}

