package com.mydesk.server;

import java.util.List;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mydesk.client.FileDirEntry;
import com.mydesk.client.FileDownloadSrvc;
import com.mydesk.filemanager.FileMngr;

public class FileDownloadSrvcImpl 
      extends RemoteServiceServlet implements FileDownloadSrvc
{
  private static final long serialVersionUID = 6413530336647999629L;
  private static final String uploadUrl = "/mydesk/uploader";
//  private static final String bucketName = "/gs/mydesk-backend-web";
  private BlobstoreService blobstoreSrvc = 
      BlobstoreServiceFactory.getBlobstoreService();
  
  @Override
  public List<FileDirEntry> getEntries()
  {
    List<FileDirEntry> entries = FileMngr.getAllEntries();
    return entries;
  }
  
  @Override
  public String getUploadUrl(String filename)
  {
    String url;
    try
    {  url = blobstoreSrvc.createUploadUrl(uploadUrl);  }
    catch(Throwable e)
    {  url = e.getMessage();  }
    return url;
  }

  @Override
  public String getFileUrl(String filename)
  {
    String fileUrl = MyDeskURL.getFileServeUrl() + FileMngr.getBlobKey(filename);
    DebugConsole.println("RP call name: " + filename + " File Url: " + fileUrl);
    return fileUrl;
  }

}
