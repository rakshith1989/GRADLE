

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import ca.cgi.com.removeNameSpace.RemoveNameSpaceUtil;

import com.ibm.wbiserver.datahandler.xml.XMLDataHandler;
import com.ibm.websphere.bo.BOXMLDocument;
import com.ibm.websphere.bo.BOXMLSerializer;
import com.ibm.websphere.sca.ServiceManager;
import commonj.connector.runtime.DataHandler;
import commonj.connector.runtime.DataHandlerException;
import commonj.sdo.DataObject;

public class RetrieveVoiceProfileCustomHTTPHandler implements DataHandler{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5044126777006893718L;
	XMLDataHandler xmlDH = new XMLDataHandler();
	@Override
	public void setBindingContext(Map context) {

		System.out.println("Inside setBindingContext");
		xmlDH.setBindingContext(context);
		
		
	}

	@Override
	public Object transform(Object source, Class arg1, Object arg2)
			throws DataHandlerException {
		// TODO Auto-generated method stub
		
		System.out.println("Inside transform");
		int bufferSize = 2000 * 4;
		String inputStr = null;
		BOXMLSerializer boXMLSerializer = (BOXMLSerializer) ServiceManager.INSTANCE
				.locateService("com/ibm/websphere/bo/BOXMLSerializer");
        if (source instanceof InputStream) {
            InputStream is = (InputStream)source;
            try {
				byte[] inputs = new byte[is.available()];
				is.read(inputs);
				inputStr = new String(inputs);
				is.reset();
			} catch (IOException e) {
				e.printStackTrace();
			}
            
        } else if (source instanceof byte[]) {
            inputStr = new String((byte[])source);
        } else if (source instanceof Reader) {
        	
			try {
	            char[] chbf = new char[bufferSize];
	            ((Reader)source).read(chbf);
	            inputStr = new String(chbf);
	            ((Reader)source).reset();
			} catch (IOException e) {
				e.printStackTrace();
			}
            
        } else if (source instanceof String) {
            inputStr = (String)source;
        }

        System.out.println("inputStr:"+inputStr);
        	Object returnedObject = xmlDH.transform(source, arg1, arg2);
        	
        	 if (returnedObject instanceof InputStream) {
                 InputStream is = (InputStream)returnedObject;
                 try {
     				byte[] inputs = new byte[is.available()];
     				is.read(inputs);
     				inputStr = new String(inputs);
     				is.reset();
     			} catch (IOException e) {
     				e.printStackTrace();
     			}
                 
             } else if (returnedObject instanceof byte[]) {
                 inputStr = new String((byte[])returnedObject);
             } else if (returnedObject instanceof Reader) {
             	
     			try {
     	            char[] chbf = new char[bufferSize];
     	            ((Reader)returnedObject).read(chbf);
     	            inputStr = new String(chbf);
     	            ((Reader)returnedObject).reset();
     			} catch (IOException e) {
     				e.printStackTrace();
     			}
                 
             } else if (returnedObject instanceof String) {
                 inputStr = (String)returnedObject;
             }
        	 
        	
        	
        	BOXMLDocument document = null;
			try {
				document = boXMLSerializer.readXMLDocument(new ByteArrayInputStream
				        (inputStr.getBytes("UTF-8")));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("returnedObject:"+inputStr);
			returnedObject = document.getDataObject();

        	return returnedObject; 
        
	}

	@Override
	public void transformInto(Object source, Object target, Object options)
			throws DataHandlerException {
		System.out.println("Inside transformInto");
		String requestDetailXmlTempModified = "";
		String targetXmlStr = null;
		String convertedStr = "";
		String convertedStrTmp;
		if(source instanceof DataObject)
		{
			System.out.println("Instance of DataObject 111");
			DataObject maxFlowObject = (DataObject)source;
//			  convertedStr = convertDataObjectToString(maxFlowObject);
			  
			  convertedStr = RemoveNameSpaceUtil.removeNameSpaceString(maxFlowObject,"customerProfileQueryRequest", "custProfileQueryRequest.xsd", true);
			  System.out.println("ConvertedStr - " + convertedStr);

			  convertedStr = convertedStr.replace("<customerProfileQueryRequest>","<customerProfileQueryRequest  xsi:noNamespaceSchemaLocation='custProfileQueryRequest.xsd' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>");

			}
			System.out.println("Final request sent to retrieveVoiceProfile:"+convertedStr);
		
	
		
        try {
               	
        if (target instanceof OutputStream) {
        	System.out.println("Entered OutPutStream Check");
        	OutputStream outStream = (OutputStream)target;
        	
            try {
            	outStream.flush();
            	byte[] sourceXmlBytes = convertedStr.getBytes();
            	//new byte[((InputStream)source).available()];
				//byte[] inputs = new byte[is.available()];
            	outStream.write(sourceXmlBytes);
				targetXmlStr = new String(sourceXmlBytes);
				//is.reset();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }
        else if (target instanceof StringWriter){
    		System.out.println("Target is StringWriter Instance maxflow");
    		((StringWriter) target).flush();
    		//StringWriter stringWriter = new StringWriter();
    		//stringWriter.flush();
    		//stringWriter.write(soapStringMsg);
    		((StringWriter) target).write(convertedStr);
    		if(((StringWriter) target).getBuffer() != null ){
    			targetXmlStr=((StringWriter) target).getBuffer().toString();
    		}
    	}
        System.out.println("Target is StringWriter Instance :targetXmlStr for RetrieveVoiceProfile"+targetXmlStr);
        }catch(Exception e) {
        	System.out.println("exception occured in soapStringMsg ");
        	e.printStackTrace();
        }
        
	
		
	}
	public static String convertDataObjectToString(DataObject dataObject) {
		String strDataObject = "";
		BOXMLSerializer boXMLSerializer = (BOXMLSerializer) ServiceManager.INSTANCE
				.locateService("com/ibm/websphere/bo/BOXMLSerializer");
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		if (dataObject != null) {
			try {
				boXMLSerializer.writeDataObject(dataObject, dataObject
						.getType().getURI(), dataObject.getType().getName(),
						outStream);
				strDataObject = outStream.toString("UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return strDataObject;
	}
}
