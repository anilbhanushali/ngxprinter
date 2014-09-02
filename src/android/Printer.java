package org.anil.thermalprinter;

import com.printer.ZQPrinter;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.http.client.protocol.ClientContext;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Printer extends CordovaPlugin {
	public static final String ACTION_CONNECT_PRINTER = "connect";
	public static final String ACTION_PRINT_TEXT = "printtext";
	public static final String ACTION_PRINT_IMAGE="printimage";
	private static final String TAG = "Test";
    private ZQPrinter PrinterService = null;   
    private boolean conn = false;
    private Context ctx ;
    public Printer()
    {
    	Log.e(TAG, "+++ ON Contructor +++");
    	PrinterService = new ZQPrinter();
    	
    }
    
    
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		try {
			ctx = cordova.getActivity().getApplicationContext();
			
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
	            
	            boolean result  = PrintImage(macid,file);
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
	
	private boolean PrintImage(String macid,String file){
				
				InputStream input;
				String path="";
				boolean result;
				try{
					input = ctx.getAssets().open("www/"+file);
					int size = input.available();
		             byte[] buffer = new byte[size];
		             input.read(buffer);
		             input.close();
		             
		             String strpngFile = ctx.getApplicationContext().getFilesDir().getAbsolutePath() + "/"+file;
		             File fileobj = new File(strpngFile);
		     		if (!fileobj.exists())
		     		{
		     			try {
		 					FileOutputStream fs = new FileOutputStream(strpngFile);
		 		            fs.write(buffer, 0, size);
		 				} catch (IOException e) {
		 					// TODO Auto-generated catch block
		 					e.printStackTrace();
		 					result = false;
		 				}
		     		}
		     		//////
		     		boolean returnvalue=false;
		     		Bitmap bm = BitmapFactory.decodeFile(strpngFile);
		     		returnvalue = Connect(macid);
					if(returnvalue)
					{
			     		PrinterService.LineFeed(1);
						PrinterService.PrintBitmap1D76(bm, 0);
						//PrinterService.SetImage(1, strpngFile, 1, 1, 50);
						//PrinterService.LineFeed(1);
						//PrinterService.PrintImage1B2A(strpngFile, 1);
						//PrinterService.LineFeed(1);
						//PrinterService.PrintImage1D76(strpngFile,1);
						
						if(PrinterService.GetStatus() == ZQPrinter.AB_SUCCESS){
							result = true;
						}else{
							result = false;
						}
		     		}else
		     		{
						result = false;
					}
		             
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					result = false;
				}
				return result;
				
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