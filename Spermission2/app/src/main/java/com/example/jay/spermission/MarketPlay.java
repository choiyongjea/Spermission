package com.example.jay.spermission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by silogood on 2015-11-21.
 */

public class MarketPlay extends Activity {
    private Context context;



    private PackageManager mPm;

    private static final String NAME = "Name";
    private static final String DESCRIPTION = "Description";
    private static final String PACKAGENAME = "PackageName";
    private static final String SECURITYLEVEL = "Securitylevel";
    private static final String TAG = "Permissions";
    private List<Map<String, String>> mGroupData;
    private List<List<Map<String, String>>> mChildData;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
        mPm = getPackageManager();
        Intent i = getIntent();
        String title = i.getStringExtra("title");
        String PackageName = title.substring(8);


        mGroupData = new ArrayList<Map<String, String>>();
        mChildData = new ArrayList<List<Map<String, String>>>();
        String permissionName;
        String applicationLabel;
        String packageName;
        PackageInfo pi = null;
        applicationLabel = PackageName;
        int packageVersionCode;
        String packageVersionName;
        int system;

        try {
            pi = mPm.getPackageInfo(PackageName, PackageManager.GET_META_DATA);

            packageVersionCode = pi.versionCode;
            packageVersionName = pi.versionName;
            Log.v("XXXX6", "code:     " + packageVersionCode);
            Log.v("XXXX7", "name:     " + packageVersionName);
        } catch (Exception ex) {
            packageVersionCode = 0;
            packageVersionName = "n/a";
            //Log.e("PM", "Error fetching app version");
        }
///////////////////////////////////////////////////////////////////////////////////////////////////////////
        try {
            pi = mPm.getPackageInfo(PackageName, PackageManager.GET_PERMISSIONS);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        Map<String, String> curGroupMap = new HashMap<String, String>();
        int count = 0;
        try {
            for (String key : pi.requestedPermissions) {
                if (key.startsWith("android.permission.")) count++;
            }
            curGroupMap.put(NAME, applicationLabel + "(" + count + ")");
            Log.v("XXXX7", "count :     " +count);
        } catch (NullPointerException e) {
            curGroupMap.put(NAME, applicationLabel + "(" + 0 + ")");
            Log.v("XXXX7", "///count :     " + count);
        }
        curGroupMap.put(DESCRIPTION, packageVersionName);  Log.v("XXXX7", "Description  :     " + packageVersionName);
        curGroupMap.put(PACKAGENAME, PackageName); Log.v("XXXX7", "packageName  :     " + PackageName);

        mChildData.clear();
        mGroupData.clear();

        mGroupData.add(curGroupMap);
        Log.v("XXXX7", "1  " );

        Log.d("AAA", PackageName);
        List<Map<String, String>> children = new ArrayList<Map<String, String>>();
        try {
            for (String key : pi.requestedPermissions) {
                try {

                    if (!(key.startsWith("android.permission."))) continue;
                    Log.d("BBB", key);
                    PermissionInfo pinfo =
                            mPm.getPermissionInfo(key, PackageManager.GET_META_DATA);
                    Map<String, String> curChildMap = new HashMap<String, String>();


                    CharSequence label = pinfo.loadLabel(mPm);
                    CharSequence desc = pinfo.loadDescription(mPm);
                    curChildMap.put(NAME, (label == null) ? pinfo.name : label.toString());
                    curChildMap.put(DESCRIPTION, (desc == null) ? "" : desc.toString() + "(" + key + ")");
                    curChildMap.put(SECURITYLEVEL, String.valueOf(pinfo.protectionLevel));
                    children.add(curChildMap);

                } catch (PackageManager.NameNotFoundException e) {
                    Log.i(TAG, "Ignoring unknown permission ");
                    continue;
                }
            }
        } catch (NullPointerException e) {

        }
        mChildData.add(children);


    }

}








