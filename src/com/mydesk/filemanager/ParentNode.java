package com.mydesk.filemanager;

//import javax.persistence.Id;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class ParentNode
{
  @Id public Long id;
  public String name;
}

