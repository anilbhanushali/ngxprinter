package com.anil.printerplugin;
 
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import android.app.Activity;
import android.content.Intent;

public class Printer extends CordovaPlugin {
	public static final String ACTION_CONNECT_PRINTER = "connect";

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		try {
		    if (ACTION_CONNECT_PRINTER.equals(action)) { 
	             JSONObject arg_object = args.getJSONObject(0);

	             //printer intent
	             Intent printIntent = new Intent(Intent.ACTION_EDIT)
			        .setType("vnd.android.cursor.item/event")
			        .putExtra("macaddress", arg_object.getString("macaddress"));
		 
		       this.cordova.getActivity().startActivity(printIntent);
		       callbackContext.success();
		       return true;
		    }
		    callbackContext.error("Invalid action");
		    return false;
		} catch(Exception e) {
		    System.err.println("Exception: " + e.getMessage());
		    callbackContext.error(e.getMessage());
		    return false;
		}	 
	}
}