package com.mydesk.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("filedownload")
public interface FileDownloadSrvc extends RemoteService
{
  List<FileDirEntry> getEntries();
  
  // Returns a String that
  String getUploadUrl(String filename);
  String getFileUrl(String filename);
}
