package org.anil.thermalprinter;

import com.printer.ZQPrinter;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import android.util.Log;
import android.app.Activity;
import android.content.Intent;
import java.io.IOException;

public class Printer extends CordovaPlugin {
	public static final String ACTION_CONNECT_PRINTER = "connect";
	public static final String ACTION_PRINT_TEXT = "printtext";
	public static final String ACTION_PRINT_IMAGE="printimage";
	private static final String TAG = "Test";
    private ZQPrinter PrinterService = null;   
    private boolean conn = false;
    
    public Printer()
    {
    	Log.e(TAG, "+++ ON Contructor +++");
    	PrinterService = new ZQPrinter();
    	
    }
    
    
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		try {
		    if (ACTION_CONNECT_PRINTER.equals(action)) { 
		    	
		    	//call connect printer method
		    	JSONObject arg_object = args.getJSONObject(0);
	             String macid = arg_object.getString("macaddress");
	             Connect(macid);
	            if(conn){
                	callbackContext.success();
                	return true;
             	}else
             	{
             		callbackContext.error("unable to connect");
             		return false;
             	}
	             
		    }
		    if (ACTION_PRINT_TEXT.equals(action)) { 
		    	
		    	//call print text method
		    	 JSONObject arg_object = args.getJSONObject(0);
	             String text = arg_object.getString("text");
	             String macid = arg_object.getString("macaddress");
	             int alignment = arg_object.getInt("alignment");
	             int attribute = arg_object.getInt("attribute");
	             int textsize = arg_object.getInt("textsize");
	             
	             boolean result  = PrintText(macid,text, alignment, attribute, textsize);
	             if(result){
	             	callbackContext.success();
	             	return true;
	             }else{
	             	callbackContext.error("Unable to print");
	             	return false;
	             }
	             
		    }
		    if(ACTION_PRINT_IMAGE.equals(action)){
		    	JSONObject arg_object = args.getJSONObject(0);
	            String file = arg_object.getString("file");
	            String macid = arg_object.getString("macaddress");
	            int width = arg_object.getInt("width");
	            int alignment = arg_object.getInt("alignment");
	            int level = arg_object.getInt("level");
	            
	            boolean result  = PrintImage(macid,file, width, alignment, level);
	             if(result){
	             	callbackContext.success();
	             	return true;
	             }else{
	             	callbackContext.error("Unable to print");
	             	return false;
	             }
		    }
		    callbackContext.error("Invalid action");
		    return false;
		} catch(Exception e) {
		    System.err.println("Exception: " + e.getMessage());
		    callbackContext.error(e.getMessage());
		    return false;
		}	 
	}
	
	private boolean PrintText(String macid,String text,int alignment,int attribute,int textsize){
		//connect to printer
		boolean returnvalue=false;
		returnvalue = Connect(macid);
		if(returnvalue){
			PrinterService.PrintText(text, alignment, attribute, textsize);
			if(PrinterService.GetStatus() == ZQPrinter.AB_SUCCESS){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	private boolean PrintImage(String macid,String file, int width,int alignment,int level){
		//connect to printer
				boolean returnvalue=false;
				returnvalue = Connect(macid);
				if(returnvalue){
					PrinterService.PrintImage(file, width, alignment, level);
					if(PrinterService.GetStatus() == ZQPrinter.AB_SUCCESS){
						return true;
					}else{
						return false;
					}
				}else{
					return false;
				}
	}
	void CheckGC(String FunctionName )
    {
    	long VmfreeMemory =Runtime.getRuntime().freeMemory();
    	long VmmaxMemory=Runtime.getRuntime().maxMemory();
    	long VmtotalMemory=Runtime.getRuntime().totalMemory();
    	long waittime=53;
    	long Memorypercentage=((VmtotalMemory-VmfreeMemory)*100)/VmtotalMemory; 
    	
    	Log.i(TAG,FunctionName+"Before Memorypercentage"+Memorypercentage+"% VmtotalMemory["+VmtotalMemory+"] "+"VmfreeMemory["+VmfreeMemory+"] "+"VmmaxMemory["+VmmaxMemory+"] ");
    	  
    	  //Runtime.getRuntime().gc();
    	  System.runFinalization();
    	  System.gc();
    	  VmfreeMemory =Runtime.getRuntime().freeMemory();
          VmmaxMemory=Runtime.getRuntime().maxMemory();
     	  VmtotalMemory=Runtime.getRuntime().totalMemory();
     	  Memorypercentage=((VmtotalMemory-VmfreeMemory)*100)/VmtotalMemory; 
    	Log.i(TAG,FunctionName+"_After Memorypercentage"+Memorypercentage+"% VmtotalMemory["+VmtotalMemory+"] "+"VmfreeMemory["+VmfreeMemory+"] "+"VmmaxMemory["+VmmaxMemory+"] ");
    }

	private boolean Connect(String address){
		CheckGC("Connect_Start" );
    	int nRet = PrinterService.Connect(address);
        if ( nRet == 0 ) 
        {
        	
            conn = true;
        }
        else
        {
        	Log.e("Test", String.valueOf(nRet));
            
            conn = false;               
        }
        CheckGC("Connect_End" );            
        return true; 
	}
}