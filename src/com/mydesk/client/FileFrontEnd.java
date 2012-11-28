package com.mydesk.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.client.ui.TextBox;


/*
 GWT Entry Point class for the Web-Front-End
 Contains the Web-Base UI for uploading and retrieving files
 Used to test backend functionality.
 */
@SuppressWarnings("deprecation")
public class FileFrontEnd implements EntryPoint
{
  private FileDownloadSrvcAsync fileSrvc = GWT.create(FileDownloadSrvc.class);
  private FormPanel formPanel = new FormPanel();
  private FileUpload fileUpload = new FileUpload();
  private List<String> logs = new ArrayList<String>();
  private CellList<String> cellList = new CellList<String>(new TextCell());
  private TextBox fileNameText = new TextBox();
  
  // On Module Load
  @Override
  public void onModuleLoad()
  {
    RootPanel rootPanel = RootPanel.get("MainPanel");
    
    VerticalPanel verticalPanel = new VerticalPanel();
    rootPanel.add(verticalPanel);
    verticalPanel.setWidth("100%");
    
    HorizontalPanel fileSubmitPanel = new HorizontalPanel();
    verticalPanel.add(fileSubmitPanel);
    formPanel.setMethod(FormPanel.METHOD_POST);
    formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
    
    fileSubmitPanel.add(formPanel);
    // Important!! Upload will break if name not set
    fileUpload.setName("fileUploadField");

    formPanel.setWidget(fileUpload);
    fileUpload.setSize("100%", "100%");
    
    Button sendBtn = new Button("Send");
    fileSubmitPanel.add(sendBtn);
    
    HorizontalPanel getFilePanel = new HorizontalPanel();
    verticalPanel.add(getFilePanel);        
    getFilePanel.add(fileNameText);
    
    Button getFileBtn = new Button("New button");
    getFileBtn.setText("Get File");
    getFilePanel.add(getFileBtn);
 
    verticalPanel.add(cellList);
    
    getFileBtn.addClickHandler(new GetBtnClickHndler());
    formPanel.addFormHandler(new UploadFormHdler());
    sendBtn.addClickHandler(new SendBtnClickHndler());
    
    addAndUpdate("Base Url: " + GWT.getHostPageBaseURL());    
  }
  
  void addAndUpdate(String text)
  {
    logs.add(text);
    updateList();
  }
  
  //===========================================================================
  /*The cell list is not automatically updated when new
   strings have been added to the log
   This should be called whenever we need to refresh the log
   with the most up to date information*/
  void updateList()
  {
    cellList.setRowCount(logs.size(), true);
    cellList.setRowData(0, logs);
  }
  
  //===========================================================================
  public class SendBtnClickHndler implements ClickHandler
  {
    @Override
    public void onClick(ClickEvent event)
    { 
      // We have to get the upload url from the server first
      AsyncCallback<String> callback = new UpFileCallback();
      fileSrvc.getUploadUrl(fileUpload.getFilename(), callback);
    }
  }  

  //===========================================================================
  public class GetBtnClickHndler implements ClickHandler
  {
    @Override
    public void onClick(ClickEvent event)
    {
      AsyncCallback<String> callback = new GetFileCallback();
      String fileName = fileNameText.getText();
      fileSrvc.getFileUrl(fileName, callback); 
    }
  }
  
  //===========================================================================
  public class UpFileCallback implements AsyncCallback<String>
  {
    @Override
    public void onFailure(Throwable caught)
    {  Window.alert("failure with blob callback");  }

    @Override
    public void onSuccess(String result)
    {
      logs.add("Up File Callback onSuccess result:");
      logs.add("  " + result.toString());
      updateList();
      // Now set the form panel action to the retrieved URL
      formPanel.setAction(result.toString());
      formPanel.submit();
      formPanel.reset();
    }    
  }
  
  //===========================================================================
  public class GetFileCallback implements AsyncCallback<String>
  {

    @Override
    public void onFailure(Throwable caught)
    {  
      Window.alert("failure with retriving file");
      addAndUpdate(caught.getMessage());
    }

    @Override
    public void onSuccess(String result)
    {
      logs.add("Get File Callback onSuccess result:");
      if(result == "")
      {  addAndUpdate("No File found"); }
      else
      {
        addAndUpdate("  " + result.toString());
        Window.open(result, "Download", "");
      }
      fileNameText.setText("");
    }    
  }
    
  //===========================================================================
  public class UploadFormHdler implements FormHandler
  {
    @Override
    @Deprecated
    public void onSubmit(FormSubmitEvent event){}

    @Override
    @Deprecated //Update the logs with the result of the upload
    public void onSubmitComplete(FormSubmitCompleteEvent event)
    {
      logs.add("Form Submit Result: " + event.getResults());
      updateList();
    }    
  }    
}
