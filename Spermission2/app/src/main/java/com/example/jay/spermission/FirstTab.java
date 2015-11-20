package com.example.jay.spermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

public class FirstTab extends ExpandableListActivity implements Runnable {
    private static final String TAG = "Permissions";

    private static final String NAME = "Name";
    private static final String DESCRIPTION = "Description";
    private static final String PACKAGENAME = "PackageName";
    private static final String SECURITYLEVEL = "Securitylevel";

    // Installed App Details
    private static final String SCHEME = "package";
    private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
    private static final String APP_PKG_NAME_22 = "pkg";
    private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
    private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";

    private int mDangerousColor;
    private int mDefaultTextColor;

    private static final int PROGRESS_DIALOG = 0;
    private ProgressDialog mProgressDialog;

    private List<Map<String, String>> mGroupData;
    private List<List<Map<String, String>>> mChildData;

    private PackageManager mPm;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPm = getPackageManager();
        Log.i("iver", "XXXXX1" +mPm);
        mDangerousColor = getResources().getColor(R.color.perms_dangerous_grp_color);
        mGroupData = new ArrayList<Map<String, String>>();
        mChildData = new ArrayList<List<Map<String, String>>>();
        mDefaultTextColor = Color.DKGRAY;
        showDialog(PROGRESS_DIALOG);
    };

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                int childPosition, long id) {
        showInstalledAppDetails(this, (String) v.getTag());
        return super.onChildClick(parent, v, groupPosition, childPosition, id);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case PROGRESS_DIALOG:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setMessage(getText(R.string.permission_settings_loading));
                mProgressDialog.setCancelable(false);
                Thread thread = new Thread(this);
                thread.start();
                return mProgressDialog;
            default:
                return super.onCreateDialog(id, null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                showDialog(PROGRESS_DIALOG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            removeDialog(PROGRESS_DIALOG);
            PermissionAdapter mAdapter = new PermissionAdapter(
                    FirstTab.this,mGroupData,
                    R.layout.permissions_expandable_list_item,
                    new String[] { NAME, DESCRIPTION },
                    new int[] { android.R.id.text1, android.R.id.text2 },
                    mChildData,
                    R.layout.permissions_expandable_list_item_child,
                    new String[] { NAME, DESCRIPTION },
                    new int[] { android.R.id.text1, android.R.id.text2 }

            );
           // Log.i("iver", "XXXXX2" + mGroupData);
           // Log.i("iver", "XXXXX3" + mChildData);
            setListAdapter(mAdapter);
        }
    };

    private class PermissionAdapter extends SimpleExpandableListAdapter {
        public PermissionAdapter(Context context, List<? extends Map<String, ?>> groupData,
                                 int groupLayout, String[] groupFrom, int[] groupTo,
                                 List<? extends List<? extends Map<String, ?>>> childData, int childLayout,
                                 String[] childFrom, int[] childTo) {
            super(context, groupData, groupLayout, groupFrom, groupTo, childData,
                    childLayout, childFrom, childTo);

        }

        @Override
        @SuppressWarnings("unchecked")
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            final View v = super.getGroupView(groupPosition, isExpanded, convertView, parent);
            Map<String, String> group = (Map<String, String>) getGroup(groupPosition);
            int secLevel = Integer.parseInt(group.get(SECURITYLEVEL));
            TextView textView = (TextView) v.findViewById(android.R.id.text1);
            if (PermissionInfo.PROTECTION_DANGEROUS == secLevel) {
                textView.setTextColor(mDangerousColor);
            } else {
                textView.setTextColor(mDefaultTextColor);
            }
            return v;
        }

        @Override
        @SuppressWarnings("unchecked")
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {
            final View v = super.getChildView(groupPosition, childPosition, isLastChild,
                    convertView, parent);
            ImageView imageView = (ImageView) v.findViewById(android.R.id.icon);
            Map<String, String> child =
                    (Map<String, String>)getChild(groupPosition, childPosition);
            Drawable icon;
            String packageName = (String)child.get(PACKAGENAME);

            try {
                icon = mPm.getApplicationIcon(packageName);
            } catch (NameNotFoundException e) {
                icon = mPm.getDefaultActivityIcon();
            }
            imageView.setImageDrawable(icon);
            v.setTag(packageName);
            return v;
        }
    }

    public void run() {
        mChildData.clear();
        mGroupData.clear();
        List<PackageInfo> appList =
                mPm.getInstalledPackages(PackageManager.GET_PERMISSIONS);
        Map<String, List<PackageInfo>> permList = new TreeMap<String, List<PackageInfo>>();
        // Loop through all installed packaged to get a list of used permissions and PackageInfos
        for (PackageInfo pi : appList) {
            //Log.i("iver", "XXXXX3" +pi);
            //Log.i("iver", "XXXXX4" +appList);
            // Do not add System Packages
            if (pi.requestedPermissions == null || pi.packageName.equals("android")) {
                continue;
            }
            for (String perms : pi.requestedPermissions) {

                if (!permList.containsKey(perms)) {
                    // First time we get this permission so add it and create a new List
                    permList.put(perms, new ArrayList<PackageInfo>());
                }
                permList.get(perms).add(pi);
            }
        }
        appList.clear();

        Set<String> keys = permList.keySet();
        for (String key : keys) {
            //Log.i("iver", "XXXXX1" + key);
            //Log.i("iver", "XXXXX2" + keys);
            Map<String, String> curGroupMap = new HashMap<String, String>();
            try {
                PermissionInfo pinfo =
                        mPm.getPermissionInfo(key, PackageManager.GET_META_DATA);
                CharSequence label = pinfo.loadLabel(mPm);
                CharSequence desc = pinfo.loadDescription(mPm);
                curGroupMap.put(NAME, (label == null) ? pinfo.name : label.toString());
                curGroupMap.put(DESCRIPTION, (desc == null) ? "" : desc.toString());
                curGroupMap.put(SECURITYLEVEL, String.valueOf(pinfo.protectionLevel));
            } catch (NameNotFoundException e) {
                Log.i(TAG, "Ignoring unknown permission " + key);
                continue;
            }
            mGroupData.add(curGroupMap);
            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
            List<PackageInfo> infos = permList.get(key);
            for (PackageInfo child : infos) {
                Map<String, String> curChildMap = new HashMap<String, String>();
                String appName = (child.applicationInfo == null) ?
                        child.packageName : child.applicationInfo.loadLabel(mPm).toString();
                curChildMap.put(NAME, appName);
                curChildMap.put(DESCRIPTION, child.versionName);
                curChildMap.put(PACKAGENAME, child.packageName);
                children.add(curChildMap);
            }
            mChildData.add(children);
        }
        permList.clear();
        handler.sendEmptyMessage(0);
    }

    public static void showInstalledAppDetails(Context context, String packageName) {
        Intent intent = new Intent();
        final int apiLevel = Build.VERSION.SDK_INT;
        if (apiLevel >= 9) { // above 2.3
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts(SCHEME, packageName, null);
            intent.setData(uri);
        } else { // below 2.3
            final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22
                    : APP_PKG_NAME_21);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName(APP_DETAILS_PACKAGE_NAME,
                    APP_DETAILS_CLASS_NAME);
            intent.putExtra(appPkgName, packageName);
        }
        context.startActivity(intent);
    }
}
