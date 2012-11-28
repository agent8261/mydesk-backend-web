package com.mydesk.filemanager;

import java.util.Date;

//import javax.persistence.Id;


import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import com.mydesk.client.FileDirEntry;

@Entity
public class DirEntry
{
  //Parent key for all directory entries
  static private Key<ParentNode> pKey; 
  
  @Parent    private Key<ParentNode> parentKey;
  @Id        public String filename;
  public String blobKey;
  public long size;
  public Date lastUpdated;
  // indexed long sequence number
  
  // Set the key that will be used by every new DirEntry
  static void setParentKey(Key<ParentNode> _pKey)
  {  pKey = _pKey;  }
  
  // Ctor
  public DirEntry()
  { 
    parentKey = pKey;
    filename = null; 
    lastUpdated = null;
    blobKey = null;
    size = 0;  
   }
  
  public FileDirEntry convert()
  {
    FileDirEntry entry = new FileDirEntry();
    entry.filename = filename;
    entry.lastUpdated = lastUpdated;
    entry.size = 0;
    return entry;
  }
}
