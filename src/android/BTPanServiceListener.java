package com.ahold.blueteth.cordova.plugin;

import java.lang.reflect.InvocationTargetException;

import android.bluetooth.BluetoothProfile;
import org.apache.cordova.CallbackContext;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class BTPanServiceListener implements BluetoothProfile.ServiceListener {
    private final Context context;
    public static boolean state = false;
	
	public BTPanServiceListener(final Context context)  {
        this.context = context;
    }

    public void onServiceConnected(final int profile,
                                   final BluetoothProfile proxy
								   ) {
        //Some code must be here or the compiler will optimize away this callback.
        Log.i("MyApp", "BTPan proxy connected");
		CallbackContext callbackContext = null;
        try {
            boolean nowVal = ((Boolean) proxy.getClass().getMethod("isTetheringOn", new Class[0]).invoke(proxy, new Object[0])).booleanValue();
            if (nowVal) {
                proxy.getClass().getMethod("setBluetoothTethering", new Class[]{Boolean.TYPE}).invoke(proxy, new Object[]{Boolean.valueOf(false)});
                Toast.makeText(context, "Turning bluetooth tethering off", Toast.LENGTH_SHORT).show();
                state = false;
            } else {
                proxy.getClass().getMethod("setBluetoothTethering", new Class[]{Boolean.TYPE}).invoke(proxy, new Object[]{Boolean.valueOf(true)});
                Toast.makeText(context, "Turning bluetooth tethering on", Toast.LENGTH_SHORT).show();
                state = true;
            }
            //BlueTetherPlugin.changeToggleState(state, callbackContext);
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            callbackContext.error("Error encountered: " + e.getMessage());
			state = false;
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            callbackContext.error("Error encountered: " + e.getMessage());
			state = false;
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            callbackContext.error("Error encountered: " + e.getMessage());
			state = false;
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            callbackContext.error("Error encountered: " + e.getMessage());
			state = false;
        }
    }

    public void onServiceDisconnected(final int profile) {
    }
}