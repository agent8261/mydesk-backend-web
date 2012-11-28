package com.mydesk.server;

/* Separated from File Manager to allow use with Google web toolkit.
 */
public class MyDeskURL
{
  private static final boolean LOCAL_ANDROID_RUN = true;
  public static final String LOCAL_APP_ENGINE_SERVER_URL = "http://127.0.0.1:8888";
  public static final String DEFAULT_ROOT_URL = "http://8261.imlctest.appspot.com";

  static final String serverPath = "/mydesk/server";
  static final String blobParam = "?blob-key=";

  //===========================================================================
  // Return a URL pointing to the BlobServer servlet
  static public String getFileServeUrl()
  {
    String fileUrl;
    if(MyDeskURL.LOCAL_ANDROID_RUN)
    {  
      fileUrl = LOCAL_APP_ENGINE_SERVER_URL + serverPath + 
          blobParam;
    }
    else
    {
      fileUrl = DEFAULT_ROOT_URL + serverPath + blobParam;
    }
    return fileUrl;
  }
}

