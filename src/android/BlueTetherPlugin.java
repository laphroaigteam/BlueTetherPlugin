package com.ahold.blueteth.cordova.plugin;
// The native Bluetooth API
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
// Cordova-required packages
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BlueTetherPlugin extends CordovaPlugin {
	BluetoothAdapter mBluetoothAdapter = null;
	Class<?> classBluetoothPan = null;
	Constructor<?> BTPanCtor = null;
	Object BTSrvInstance = null;
	Class<?> noparams[] = {};
	Method mIsBTTetheringOn;
	public static Switch toggle ;
	private static final String CONNECT = "connect";
	private CallbackContext connectCallback;
	
	public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        boolean validAction = true;
        if (action.equals(CONNECT)) {

            boolean secure = true;
            connect(args, secure, callbackContext);
        }
		else if (action.equals("setTetheringOn")){
			setTetherOn(args, secure, callbackContext); 
		}
		return validAction;
	}

    public void connect(CordovaArgs args, CallbackContext callbackContext) throws JSONException {
		Context MyContext = getApplicationContext();
		mBluetoothAdapter = getBTAdapter();
		String sClassName = "android.bluetooth.BluetoothPan";
		try {
			classBluetoothPan = Class.forName("android.bluetooth.BluetoothPan");
			mIsBTTetheringOn = classBluetoothPan.getDeclaredMethod("isTetheringOn", noparams);
			BTPanCtor = classBluetoothPan.getDeclaredConstructor(Context.class, BluetoothProfile.ServiceListener.class);
			BTPanCtor.setAccessible(true);

			BTSrvInstance = BTPanCtor.newInstance(MyContext, new BTPanServiceListener(MyContext));
			Thread.sleep(250);

        } catch (ClassNotFoundException e) {
            callbackContext.error("Error encountered: " + e.getMessage());
			return false;
        } catch (Exception e) {
            callbackContext.error("Error encountered: " + e.getMessage());
			return false;
        }
      PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
      callbackContext.sendPluginResult(pluginResult);
      return true;
	}
	
	public void setTetherOn(CordovaArgs args, CallbackContext callbackContext){
		Class<?> classBluetoothPan = Class.forName(sClassName);

		Constructor<?> ctor = classBluetoothPan.getDeclaredConstructor(Context.class, BluetoothProfile.ServiceListener.class);
		ctor.setAccessible(true);
		Object instance = ctor.newInstance(getApplicationContext(), new BTPanServiceListener(getApplicationContext())); 
		// Set Tethering ON
		Class[] paramSet = new Class[1];
		paramSet[0] = boolean.class;

		Method setTetheringOn = classBluetoothPan.getDeclaredMethod("setBluetoothTethering", paramSet);

		setTetheringOn.invoke(instance,true);

	} catch (ClassNotFoundException e) {
		callbackContext.error("Unhandled action in Bluetoothtether: "+e.getMessage());
	} catch (Exception e) {
		callbackContext.error("Unhandled action in Bluetoothtether: "+e.getMessage());
		return true;
	}
	return false;
	callbackContext.error("Unhandled action in Bluetoothtether: " + action);
	}
	
   @SuppressLint("NewApi")
    private BluetoothAdapter getBTAdapter() {
            BluetoothManager bm = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
            return bm.getAdapter();
    }

    public  static void changeToggleState(boolean state, final CallbackContext callbackContext) {
        try{
            if(state){
                toggle.setChecked(BTPanServiceListener.state);
            }else {
                toggle.setChecked(BTPanServiceListener.state);
            }
        }catch (Exception e){
            callbackContext.error("Error encountered: " + e.getMessage());
        }
    }	
	
}