package com.example.jay.spermission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
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
public class Database {


    // Contexte de l'application
    private Context context;

    // Barre de progression pour les op�rations longues
    private ProgressDialog progressDialog;

    // Base de donnn�es
    public SQLiteDatabase database;

    /*
     * Database
     * R�cup�ration du contexte du programme et cr�ation
     * ou ouverture de la base de donn�es.
     */
    public Database (Context context)
    {
        this.context = context;
        database = new DatabaseOpenHelper(this.context).getWritableDatabase();
    }

    /*
     * isUpToDate
     * V�rifie si les donn�es de la base sont � jour
     */
    public void isUpToDate()
    {
        // Configuration d'une barre de progression
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage(context.getString(R.string.dialog_uptodate_text));
        progressDialog.setCancelable(false);

        // R�cup�ration du nombre d'applications
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(PackageManager.GET_META_DATA);
        progressDialog.setMax(packages.size());

        // Affichage de la barre de progression
        progressDialog.show();

        // Ex�cution de la t�che parall�le
        new IsUpToDateTask().execute();
    }

    /*
     * Classe IsUpToDateTask
     * Fournit une t�che dans un thread s�par� pour un
     * travail long.
     */
    private class IsUpToDateTask extends AsyncTask<Void, Void, Boolean> {
        /*
         * doInBackground
         * M�thode de travail dans le thread s�par�
         */
        protected Boolean doInBackground(Void... params) {
            // R�cup�ration d'un fournisseur de liste d'applications
            PackageManager pm = context.getPackageManager();
            List<PackageInfo> packages = pm.getInstalledPackages(PackageManager.GET_META_DATA);

            String packageName;			// Nom du package
            String packageVersionCode;	// Code de version du package

            boolean isUpToDate = true;	// La base est, par d�faut, � jour
            Cursor packageChange;		// Acc�s � la base de donn�es

            // Selection du nombre d'applications dans la base de donnees
            packageChange = database.rawQuery("SELECT Count(*) FROM application;", null);
            packageChange.moveToFirst();

            // Si ce n'est pas nul ou ou si ce ne correspond pas au nombre du syst�me
            if (packageChange.getInt(0) != packages.size() || packageChange.getInt(0) == 0) {
                // On ferme l'acc�s et la base n'est pas � jour
                packageChange.close();
                return false;
            }

            // On parcourt chaque application
            for (PackageInfo pi : packages) {
                // On incr�mente la progressbar
                progressDialog.incrementProgressBy(1);

                // R�cup�re les informations sur l'application install�e
                packageName = pi.packageName;
                packageVersionCode = Integer.toString(pi.versionCode);

                // R�cup�re le nombre d'applications dans la base de donn�es correspondant � ces informations
                packageChange = database.query("application", new String[]{"id"}, "name = ? AND version_code = ?", new String[]{packageName, packageVersionCode}, null, null, null);
                if (packageChange.getCount() == 0) {
                    // Aucune application dans la base de donn�es ==> pas � jour
                    isUpToDate = false;
                    packageChange.close();
                    break;
                }
                packageChange.close();
            }

            // Envoi du r�sultat
            return isUpToDate;
        }

        /*
         * onPostExecute
         * R�ception du r�sultat dans le thread principal
         */
        protected void onPostExecute(Boolean result) {
            // Fermeture de la barre de progression et appel de la fonction de fin
            progressDialog.dismiss();
            ((ForthTab)context).isUpToDateResult(result);
        }
    }

    /*
     * updateDatabase
     * M�thode de mise � jour de la base de donn�es
     */
    public void updateDatabase(Activity activity)
    {
        // Configuration d'une barre de progression
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage(context.getString(R.string.dialog_update_text));
        progressDialog.setCancelable(false);

        // R�cup�ration du nombre d'applications sur le syst�me
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        progressDialog.setMax(packages.size());

        // Affichage de la barre de progression
        progressDialog.show();

        // Ex�cution de la t�che dans un thread parall�le
        new DatabaseUpdateTask(activity, database, progressDialog).execute();
    }


}
