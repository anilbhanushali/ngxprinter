package org.betterlife.printerngx;

import com.ngx.*;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.http.client.protocol.ClientContext;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class NGXPrinter extends CordovaPlugin {
	public static final String ACTION_CONNECT_PRINTER = "connect";
	public static final String ACTION_PRINT_TEXT = "printtext";
	public static final String ACTION_SET_IMAGE="setimage";
	public static final String ACTION_PRINT_IMAGE="printimage";
	public static final String ACTION_SHOW_DEVICE="showdevicelist";
	public static final String ACTION_GETSTATUS="getstatus";
	public static final String ACTION_CLEARPRINTER="clearprinters";
	public static final String ACTION_SETFONTSIZE="setfontsize";
	public static final String ACTION_SETFONTSTYLE="setfontstyle";
	
	private static final String TAG = "NGXPLUGIN";
    public static BluetoothPrinter mBtp = BluetoothPrinter.INSTANCE;
    private boolean conn = false;
    private Context ctx ;
    private String mConnectedDeviceName = "";
    Activity activity ;
    private CallbackContext callbackContext;
    private String callbackid = null;
    public NGXPrinter(){
    	Log.e(TAG, "+++ ON Contructor +++");
    }
    
    @Override
    public void pluginInitialize(){
    	super.pluginInitialize();
    	ctx = cordova.getActivity().getApplicationContext();
		activity = (Activity)cordova.getActivity();
    	mBtp.initService(ctx, mHandler);
    }
    
    private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BluetoothPrinter.MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothPrinter.STATE_CONNECTED:
					conn=true;
					Log.i(TAG,"connected to : "+mConnectedDeviceName);
					if(callbackid != null){
						PluginResult result=new PluginResult(PluginResult.Status.OK,"Connected to : "+mConnectedDeviceName);
						result.setKeepCallback(true);
						callbackContext.sendPluginResult(result);
					}
					break;
				case BluetoothPrinter.STATE_CONNECTING:
					Log.i(TAG,"connecting");
					if(callbackid != null){
						PluginResult result=new PluginResult(PluginResult.Status.OK,"Connecting..");
						result.setKeepCallback(true);
						callbackContext.sendPluginResult(result);
					}
					break;
				case BluetoothPrinter.STATE_LISTEN:
				case BluetoothPrinter.STATE_NONE:
					conn=false;
					Log.i(TAG,"Not Connected");
					if(callbackid != null){
						PluginResult result=new PluginResult(PluginResult.Status.OK,"Not Connected");
						result.setKeepCallback(true);
						callbackContext.sendPluginResult(result);
					}
					break;
				}
				break;
			case BluetoothPrinter.MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(
						BluetoothPrinter.DEVICE_NAME);
				break;
			case BluetoothPrinter.MESSAGE_STATUS:
				String status = msg.getData().getString(
						BluetoothPrinter.STATUS_TEXT);
				Log.i(TAG,"Status : "+status);
				if(callbackid != null){
					PluginResult result=new PluginResult(PluginResult.Status.OK,status);
					result.setKeepCallback(true);
					callbackContext.sendPluginResult(result);
				}
				break;
			default:
				break;
			}
		}
    };
    
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException
	{
		
		try {
			
			if(ACTION_GETSTATUS.equals(action)){
				 
				 this.callbackContext = callbackContext;
				 this.callbackid = callbackContext.getCallbackId();
	             PluginResult result=new PluginResult(PluginResult.Status.NO_RESULT,"");
	 			 result.setKeepCallback(true);
	 			 callbackContext.sendPluginResult(result);
	 		     return true;
			}
			else if (ACTION_CONNECT_PRINTER.equals(action)) { 
		    	
		    	//call connect printer method
		    	JSONObject arg_object = args.getJSONObject(0);
	            String macid = "xx";
	            Connect(macid);
	            return true;
		    }
			else if (ACTION_CLEARPRINTER.equals(action)) { 
		    	ClearPrinters();
		    	return true;
		    }
			else if (ACTION_PRINT_TEXT.equals(action)) { 
		    	
		    	//call print text method
		    	 JSONObject arg_object = args.getJSONObject(0);
	             String text = arg_object.getString("text");
	             String macid = "xx";
	             int alignment = arg_object.getInt("alignment");
	             int attribute = arg_object.getInt("attribute");
	             int textsize = arg_object.getInt("textsize");
	             PrintText(macid,text, alignment, attribute, textsize);
	             callbackContext.success();
	             return true;
		    }
			else if(ACTION_SET_IMAGE.equals(action)){
		    	JSONObject arg_object = args.getJSONObject(0);
	            String file = arg_object.getString("file");
	            SetImage(file);
	            callbackContext.success();
	            return true;
	            
		    }
			else if(ACTION_PRINT_IMAGE.equals(action)){
	            PrintImage();
	            callbackContext.success();
	            return true;	            
		    }
			else if(ACTION_SHOW_DEVICE.endsWith(action)){
		    	ShowDeviceList();
		    	return true;
		    }
			else if(ACTION_SETFONTSIZE.equals(action)){
				 JSONObject arg_object = args.getJSONObject(0);
	             int textsize = arg_object.getInt("textsize");
	             SetFontSize(textsize);
	             callbackContext.success();
				return true;
			}
			else if(ACTION_SETFONTSTYLE.equals(action)){
				 JSONObject arg_object = args.getJSONObject(0);
	             int attribute = arg_object.getInt("attribute");
	             SetFontStyle(attribute);
	             callbackContext.success();
				return true;
			}
			
			return false;
			
		    
		} catch(Exception e) {
		    System.err.println("Exception: " + e.getMessage());
		    
		    PluginResult result = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
			callbackContext.sendPluginResult(result);
		    return false;
		}	 
	};
		
	private void PrintText(String macid,String text,int alignment,int attribute,int textsize){
		if(conn){
			mBtp.printText(text);
		}
	}
	
	private void SetFontStyle(int attribute){
		switch(attribute){
		case 0:
			mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_REGULAR);
			break;
		case 1:
			mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
			break;
		default:
			mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_REGULAR);
			break;
		}
	}
	
	private void SetFontSize(int textsize){
		switch(textsize){
		case 0:
			mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_NORMAL);
			break;
		case 1:
			mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_W_H);
			break;
		case 2:
			mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_WIDTH);
			break;
		case 3:
			mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_HEIGHT);
			break;
		default:
			mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_NORMAL);
			break;
		}
	}
	
	private void PrintImage(){
		
		if(conn){
			mBtp.printLogo();
		}		
	}
	
	private void SetImage(String file){
		
		if(conn){

		InputStream input;
		String path="";
		boolean result=true;
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
     		//Bitmap bm = BitmapFactory.decodeFile(strpngFile);
     		//returnvalue = Connect(macid);
			//mBtp.printImage(buffer);
			mBtp.setLogo(strpngFile, true, false, 127);
			//mBtp.printLogo();	
             
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}
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
	
	private void ShowDeviceList(){
		mBtp.showDeviceList(activity);
	}
	
	private void ClearPrinters(){
		mBtp.clearPreferredPrinter();
	}
	private void Connect(String address){
		
		if(!conn && !address.isEmpty())
		{
			mBtp.connectToPrinterUsingMacAddress(address);
		}
	}
	
	@Override
	public void onPause(boolean multitasking) {
		conn=false;
		mBtp.onActivityPause();
		Log.e(TAG, "+++ ON Activity pause +++");
		super.onPause(multitasking);
	}

	@Override
	public void onResume(boolean multitasking) {
		mBtp.onActivityResume();
		DebugLog.logTrace("onResume");
		Log.e(TAG, "+++ ON Activity Resume +++");
		super.onResume(multitasking);
	}
	
	@Override
	public void onDestroy() {
		conn=false;
		mBtp.onActivityDestroy();
		Log.e(TAG, "+++ ON Activity Destroy +++");
		super.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mBtp.onActivityResult(requestCode, resultCode, data,activity);
	}
}