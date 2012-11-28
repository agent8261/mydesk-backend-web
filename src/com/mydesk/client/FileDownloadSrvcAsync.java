package com.mydesk.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FileDownloadSrvcAsync
{

  void getEntries(AsyncCallback<List<FileDirEntry>> callback);

  void getUploadUrl(String filename, AsyncCallback<String> callback);

  void getFileUrl(String filename, AsyncCallback<String> callback);

}
