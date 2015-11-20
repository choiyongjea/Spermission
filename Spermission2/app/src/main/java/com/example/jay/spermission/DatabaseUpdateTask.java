package com.example.jay.spermission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
/**
 * Created by silogood on 2015-11-16.
 */
public class DatabaseUpdateTask extends AsyncTask<Void, Void, Boolean> {

    private Activity activity;
    private SQLiteDatabase database;
    private ProgressDialog progressDialog;

    public DatabaseUpdateTask (Activity activity, SQLiteDatabase database, ProgressDialog progressDialog) {
        this.activity = activity;
        this.database = database;
        this.progressDialog = progressDialog;
    }

    /*
     * doInBackground
     * M�thode de travail dans le thread s�par�
     */
    protected Boolean doInBackground(Void... params) {
        // D�claration de variables avant la boucle
        PackageManager pm = activity.getPackageManager();
        PackageInfo pi;
        String permissionName;
        String applicationLabel;
        String packageName;
        int system;
        int packageVersionCode;
        String packageVersionName;
        Map<String, Integer> permissionIds = new HashMap<String, Integer>();
        Map<String, Application> applicationsToAdd = new HashMap<String, Application>();
        Application currentApplication;
        long applicationId;
        ContentValues values;

        // Vide les tables concern�es dans la base de donn�es
        database.delete("application", null, null);
        database.delete("relation_application_permission", null, null);

        // Obtenir la liste des permissions et les mapper avec leur Id
        Cursor permissionIdsCursor = database.query("permission", new String[] {"id", "name"}, null, null, null, null, null);
        if (permissionIdsCursor.moveToFirst()) {
            while (!permissionIdsCursor.isAfterLast()) {
                permissionIds.put(permissionIdsCursor.getString(1), permissionIdsCursor.getInt(0));
                permissionIdsCursor.moveToNext();
            }
        }
        permissionIdsCursor.close();

        // Liste des applications install�es
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        // Parcourt chaque package du syst�me
        for (ApplicationInfo ai : packages)
        {
            // Incr�mente la barre de progression
            progressDialog.incrementProgressBy(1);

            // R�cup�re le nom du package et si possible le label
            packageName = ai.packageName;
            try {
                applicationLabel = pm.getApplicationLabel(ai).toString();
            } catch (Exception ex) { // application not found
                applicationLabel = packageName;
            }

            // R�cup�re si possible les versions
            try {
                pi = pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
                packageVersionCode = pi.versionCode;
                packageVersionName = pi.versionName;
            } catch (Exception ex) {
                packageVersionCode = 0;
                packageVersionName = "n/a";
                //Log.e("PM", "Error fetching app version");
            }

            if ((ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0)
                system = 1;
            else
                system = 0;

            if (applicationsToAdd.containsKey(packageName)) {
                currentApplication = applicationsToAdd.get(packageName);
            } else {
                currentApplication = new Application(applicationLabel, packageName, packageVersionCode, packageVersionName, system);
            }

            // Essaie d'obtenir les permissions de l'application
            try {
                    pi = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);

                    if (pi.requestedPermissions != null && pi.requestedPermissions.length > 0) {
                        //Parcourt chaque permission
                        for (int i = 0; i < pi.requestedPermissions.length; ++i) {
                            if (pi.requestedPermissions[i].startsWith("android.permission.")) {
                                // si c'est une permission officielle d'android, on r�cup�re son nom
                                permissionName = pi.requestedPermissions[i].substring("android.permission.".length());
                                //Log.d("PERMISSION", "Found permission : " + permissionName);

                                // On r�cup�re l'id de la permission
                                currentApplication.addPermission(permissionIds.get(permissionName));
                            }
                        }
                    }
            } catch (Exception ex) {
                Log.e("UPDATE", ex.toString());
            }

            applicationsToAdd.put(packageName, currentApplication);
        }

        try {
            database.setLockingEnabled(false);

            database.beginTransaction();
            List<Integer> applicationPermissions;
            progressDialog.setProgress(0);
            for (Application a : applicationsToAdd.values()) {
                progressDialog.incrementProgressBy(1);
                values = new ContentValues();
                values.put("label", a.getLabel());
                values.put("name", a.getName());
                values.put("version_code", a.getVersionCode());
                values.put("version_name", a.getVersionName());
                values.put("system", a.isSystem());
                applicationId = database.insert("application", null, values);

                values = new ContentValues();
                values.put("application", applicationId);

                applicationPermissions = a.getPermissions();
                for (Integer p : applicationPermissions) {
                    values.put("permission", p);
                    database.insert("relation_application_permission", null, values);
                }
            }
            database.setTransactionSuccessful();
            database.endTransaction();
        } finally {
            database.setLockingEnabled(true);
        }

        return true;
    }

    /*
     * onPostExecute
     * R�ception du r�sultat dans le thread principal
     */
    protected void onPostExecute(Boolean result) {
        // Fermeture de la barre de progression et appel de la fonction de r�sultat
        progressDialog.dismiss();
        ((ForthTab)activity).databaseUpdated();
    }

}
