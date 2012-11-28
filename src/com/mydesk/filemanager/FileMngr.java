package com.mydesk.filemanager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.mydesk.client.FileDirEntry;
import com.mydesk.server.DebugConsole;
import com.mydesk.server.MyDeskURL;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.files.GSFileOptions.GSFileOptionsBuilder;


/* Responsible for Managing the storage and retrieval of file from
 * datastore services
 */
public class FileMngr
{
  static final String matchRegEx = new String("[a-zA-Z_0-9\\.\\-\\_]*");
  static final String entryRoot = new String("mydeskEntryParentId");
  static final String bucketName = new String("mydesk-backend-web");
  static final String cloudPath = new String("/gs/" + bucketName + "/");
  
  static Key<ParentNode> eNodeKey;
	
  private static BlobstoreService blobSrvc = 
      BlobstoreServiceFactory.getBlobstoreService();
   
  
  //===========================================================================
	static public void init()
	{
    // Register classes with objectify
    ObjectifyService.register(ParentNode.class);
	  ObjectifyService.register(DirEntry.class);
    eNodeKey = Key.create(ParentNode.class, entryRoot);
    
    SetParentNode(eNodeKey, entryRoot);
    DirEntry.setParentKey(eNodeKey);
    DebugConsole.println("Initlized FileManager");    
	}
		
  //===========================================================================
	// Parent nodes are used to create separate entity groups
	static void SetParentNode(Key<ParentNode> parentKey, String keyId)
	{
	  Objectify ofy = ObjectifyService.ofy();
	  ParentNode parent = null;
	  try
	  {   parent = ofy.load().key(parentKey).get();  }
	  catch(NotFoundException e)
	  {
	    parent = new ParentNode(); parent.name = keyId;
	    ofy.save().entity(parentKey).now();
	  }
	}
		
  //===========================================================================
  static public void makeCloudMetaData(HttpServletRequest request)
  {       
    DebugConsole.print("Filename: ");
    String p = (String)request.getAttribute("filename");
    DebugConsole.println(p);
    
    Map<String, List<BlobKey>> uploads = blobSrvc.getUploads(request);
    BlobInfoFactory bf = new BlobInfoFactory();
    
    for(Map.Entry<String, List<BlobKey>> entry: uploads.entrySet())
    {
      List<BlobKey> blobs = entry.getValue();
      DebugConsole.println("Key: " + entry.getKey());
      DebugConsole.println("Num blobs: " + entry.getValue().size() + '\n');      
      DebugConsole.println("Blobs Keys:");
      
      for(BlobKey blobkey: blobs)
      {
        DebugConsole.println(blobkey.getKeyString());        
        BlobInfo bInfo = bf.loadBlobInfo(blobkey);
        if(bInfo == null) 
        {  DebugConsole.println("Null Blob");  }
        // Directory is unnecessary with BlobInfo
        DirEntry dirEntry = new DirEntry();
        dirEntry.filename = bInfo.getFilename();
        dirEntry.blobKey = bInfo.getBlobKey().getKeyString();
        dirEntry.size = bInfo.getSize();
        dirEntry.lastUpdated = bInfo.getCreation();
        DebugConsole.println("File: " + dirEntry.filename);
        DebugConsole.println("Blob Key: " + dirEntry.blobKey);
                
        Objectify ofy = ObjectifyService.ofy();
        ofy.save().entity(dirEntry).now();
      }
    }    
  }
  
  private static final int bufferSize = 10240; // 10KB.
  //===========================================================================
  // Store a file into cloud storage
  static public void storeFile(HttpServletRequest request)
  {    
    try
    {
      // Get the file name
      String filename = "TestFile";
      
      // Create the cloud storage object
      FileService fileService = FileServiceFactory.getFileService();
      GSFileOptionsBuilder optionsBuilder = new GSFileOptionsBuilder()
        .setBucket(bucketName).setKey(filename);
      // TODO mime Type
      AppEngineFile writableFile = fileService.createNewGSFile(optionsBuilder.build());
      
      // Store file
      FileWriteChannel writeChannel = fileService.openWriteChannel(writableFile, true);
      OutputStream out = Channels.newOutputStream(writeChannel);      
      ServletInputStream in = request.getInputStream();
      byte [] buffer = new byte [bufferSize];
      int length;
      while((length = in.read(buffer)) > 0)
      { out.write(buffer, 0, length);  }
      writeChannel.closeFinally();      
    }
    catch( IOException e )
    {  e.printStackTrace();  }
  }
  
  static public void storeFile(InputStream in)
  {
    try
    {
      // Get the file name
      String filename = "TestFile";
      
      // Create the cloud storage object
      FileService fileService = FileServiceFactory.getFileService();
      GSFileOptionsBuilder optionsBuilder = new GSFileOptionsBuilder()
        .setBucket(bucketName).setKey(filename);
      // TODO mime Type
      AppEngineFile writableFile = fileService.createNewGSFile(optionsBuilder.build());
      
      // Store file
      FileWriteChannel writeChannel = fileService.openWriteChannel(writableFile, true);
      OutputStream out = Channels.newOutputStream(writeChannel);      
      byte [] buffer = new byte [bufferSize];
      int length;
      while((length = in.read(buffer)) > 0)
      { out.write(buffer, 0, length);  }
      writeChannel.closeFinally();      
    }
    catch( IOException e )
    {  e.printStackTrace();  }    
  }
    
  //===========================================================================
  static public DirEntry getDirEntry(String filename)
  {
    if(filename == "")
    {  return null;  }
    
    Key<DirEntry> key = Key.create(eNodeKey, DirEntry.class, filename);
    DirEntry entry = null;
    try
    {
      Objectify ofy = ObjectifyService.ofy();
      entry = ofy.load().key(key).get();
    }
    catch (NotFoundException e)
    {  return null;  }
    return entry;    
  }
  
  //===========================================================================
  static public String getBlobKey(String filename)
  {
    DirEntry entry = getDirEntry(filename);
    if(entry == null)
    {  return ""; }
    return entry.blobKey;
  }
  
  //===========================================================================
  static public String getFileUrl(String filename)
  {
    String fileUrl = MyDeskURL.getFileServeUrl();
    DebugConsole.println("File Url: " + fileUrl +'\n');
    return fileUrl;
  }

  //===========================================================================
  static public boolean doesFileExist(String filename)
  {
    DirEntry entry = getDirEntry(filename);
    if(entry == null)
    {  return false;  }
    return true;
  }
  
	//===========================================================================
	static public List<FileDirEntry> getAllEntries()
	{
	  Objectify ofy = ObjectifyService.ofy();
	  List<DirEntry> dirEntries = ofy.load().type(DirEntry.class).list();	  
	  List<FileDirEntry> fileEntries = new ArrayList<FileDirEntry>();
	  
	  for(DirEntry entry: dirEntries)
	  {  fileEntries.add(entry.convert());  }
	  return fileEntries;
	}
	
  //=========================================================================== 	
  static public boolean isValidFilename(String _filename)
  {  return _filename.matches(matchRegEx);  }
}
