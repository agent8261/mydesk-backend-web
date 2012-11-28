package com.mydesk.server;

import org.apache.commons.codec.binary.Base64;

import com.mydesk.filemanager.FileMngr;
import com.mydesk.server.ProtocolBufferTransport.DummyMessage;
import com.mydesk.server.ProtocolBufferTransport.GenericProperty;
import com.mydesk.server.ProtocolBufferTransport.GenericPropertyList;
import com.mydesk.server.ProtocolBufferTransport.GenericTransport;
import com.mydesk.server.ProtocolBufferTransport.ProtocolBufferScalarType;
import com.mydesk.shared.GenericBuffer;


/*
 * Helper class for GenericEndpoint. Building the client library seems
 * to work better if there are fewer imports in the annotated class.
 * The plugin does not bring in any libraries that I am using, when 
 * building the client library.
 */
public class EndPointHelper
{
//===========================================================================
  static public GenericBuffer getTest()
  { 
  // Create the thing that we are transporting.
    DummyMessage dummy = DummyMessage.newBuilder().setMsg("Hello World!")
        .build();

    // Start a GenericPropertyList which will contain "meta-data" about the
    // thing we are sending.
    GenericPropertyList.Builder property_list_builder = GenericPropertyList
        .newBuilder();

    // Make some properties and add them to the property list.
    // NOTE: You have the following scalar value types available: STRING, BOOL,
    // BYTES, DOUBLE, FLOAT, INT32, INT64, SINT32, SINT64, UINT32, UINT64,
    // SFIXED32, SFIXED64, FIXED32, and FIXED64.
    for(int i=0; i<5; i++)
    {
      GenericProperty property = GenericProperty.newBuilder()
          .setPropertyName("MyPropertyName" + i)
          .setStringValue("MyPropertyStringValue" + i)
          .setScalarType(ProtocolBufferScalarType.STRING).build();

      property_list_builder.addProperty(property);
    }

    // Create a GenericTransport.
    GenericTransport transport = GenericTransport.newBuilder()
        .setTypeName("DummyMessage").setByteData(dummy.toByteString())
        .setPropertyList(property_list_builder.build()).build();
    
    // encode the byte[] in base64 and store in string
    String encodedData = new String(Base64.encodeBase64URLSafeString(transport.toByteArray()));
    DebugConsole.println("Get Test Encoded Data" + encodedData);
    
    return new GenericBuffer("example", encodedData);
  }
  
  //===========================================================================  
  static public GenericBuffer getFileUrl(String fileName)
  {
    GenericBuffer buff;
    buff = new GenericBuffer(fileName, FileMngr.getFileUrl(fileName));
    return buff;
  }
}
