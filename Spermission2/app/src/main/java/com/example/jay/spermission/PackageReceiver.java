package com.example.jay.spermission;

import android.content.BroadcastReceiver;
import  android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by silogood on 2015-11-19.
 */
public class PackageReceiver extends BroadcastReceiver {







    @Override
    public void onReceive(Context context, Intent intent) {


        // Get application status(Install/ Uninstall)
        boolean applicationStatus = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);
        String toastMessage = null;
        Log.v("XXXX5", "     Intent ?   "+intent);
        // Check if the application is install or uninstall and display the message accordingly
        if(intent.getAction().equals("android.intent.action.PACKAGE_ADDED")){


            String A = intent.getData().toString();
            // Application Install
            Log.v("XXXX5", "     1  "+A);
            toastMessage = "PACKAGE_INSTALL: "+  intent.getData().toString() + getApplicationName(context, intent.getData().toString(), PackageManager.GET_UNINSTALLED_PACKAGES);
            Intent i = new Intent(context,MarketPlay.class);
            i.putExtra("title", A);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            context.startActivity(i);



        }else if(intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")){
            // Application Uninstall
            toastMessage = "PACKAGE_REMOVED: "+  intent.getData().toString() + getApplicationName(context, intent.getData().toString(), PackageManager.GET_UNINSTALLED_PACKAGES);
        }else if(intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")){
            // Application Replaced
            toastMessage = "PACKAGE_REPLACED: "+  intent.getData().toString() + getApplicationName(context, intent.getData().toString(), PackageManager.GET_UNINSTALLED_PACKAGES);
        }

        //Display Toast Message
        if(toastMessage != null){
            Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This method get application name name from application package name
     */
    private String getApplicationName(Context context, String data, int flag) {

        final PackageManager pckManager = context.getPackageManager();
        ApplicationInfo applicationInformation;
        try {
            applicationInformation = pckManager.getApplicationInfo(data, flag);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInformation = null;
        }
        final String applicationName = (String) (applicationInformation != null ? pckManager.getApplicationLabel(applicationInformation) : "(unknown)");

        return applicationName;
    }
}


