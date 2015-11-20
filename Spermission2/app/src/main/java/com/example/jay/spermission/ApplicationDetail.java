package com.example.jay.spermission;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by silogood on 2015-11-16.
 */
public class ApplicationDetail extends Activity {

    private ListView permissionList; // Composant graphique g�rant la liste de permissions
    private ImageButton manageButton; // Bouton pemrettant d'ouvrir l'application manager
    private String packageName;
    private Context context;
    /*
     * onCreate :
     * Ex�cut� � la cr�ation de l'activit�. R�cup�re
     * les informations sur l'application re�ue par
     * l'Intent et les inscrits dans les composants
     * graphiques
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cr�ation de l'interface graphique et r�cup�ration de l'Intent
        setContentView(R.layout.application_detail);

        this.context = this;
        Intent thisIntent = getIntent();
        String applicationId = Long.toString(thisIntent.getExtras().getLong("applicationId"));


       // R�cup�ration des donn�es
        Cursor data = Tools.database.database.query("application", new String[]{"label", "name", "version_code", "version_name", "system"}, "id = ?", new String[]{applicationId}, null, null, null);
        if (data.getCount() == 1) {
            data.moveToFirst();

          packageName = data.getString(1);


            // Affichage du nom de l'application, du package et de la version
            ((TextView)findViewById(R.id.application_detail_label)).setText(data.getString(0));
            ((TextView)findViewById(R.id.application_detail_name)).setText(data.getString(1));
            ((TextView)findViewById(R.id.application_detail_version)).setText(data.getString(2) + " / " + data.getString(3));

            if (data.getInt(4) == 1)
                ((TextView)findViewById(R.id.application_detail_system)).setVisibility(View.VISIBLE);
            else
                ((TextView)findViewById(R.id.application_detail_system)).setVisibility(View.GONE);


            manageButton = (ImageButton)findViewById(R.id.application_detail_manage_button);
            manageButton.setImageResource(R.drawable.ic_menu_manage);
            manageButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= 9) {
                        try {
                            Intent i = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                          i.addCategory(Intent.CATEGORY_DEFAULT);
                           i.setData(Uri.parse("package:" + packageName));
                          startActivity(i);
                       } catch (ActivityNotFoundException anfe) {
                        Intent i = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                           i.addCategory(Intent.CATEGORY_DEFAULT);
                          startActivity(i);
                    }
        } else {
                      Toast.makeText(ApplicationDetail.this, context.getText(R.string.application_detail_manager_unavailable), Toast.LENGTH_LONG).show();
                  }

               }
           });

            // R�cup�ration du nombre de permissions utilis�es et affichage
            data = Tools.database.database.rawQuery("SELECT Count(*) AS number " +
                    "FROM relation_application_permission " +
                    "WHERE application = ?;", new String[]{applicationId});
            data.moveToFirst();
            ((TextView)findViewById(R.id.application_detail_permission_count)).setText(data.getString(0));

            // R�cup�ration des permissions et cr�ation de la liste
            Cursor permissionListCursor = Tools.database.database.rawQuery("SELECT permission.id AS _id, permission.name AS name FROM relation_application_permission INNER JOIN permission ON relation_application_permission.permission = permission.id WHERE relation_application_permission.application = ? ORDER BY permission.name COLLATE NOCASE ASC;", new String[] {applicationId});
            startManagingCursor(permissionListCursor);

            //ddddddddddddddd
            PackageManager mPm;
            mPm = getPackageManager();
            PermissionInfo pinfo = null;
            ArrayList<HashMap<String, String>>mapList = new ArrayList<HashMap<String,String>>();
            while(permissionListCursor.moveToNext()) {

                String name1 = permissionListCursor.getString(permissionListCursor.getColumnIndex("name"));


                try {
                    pinfo = mPm.getPermissionInfo("android.permission."+name1, PackageManager.GET_META_DATA);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                HashMap<String, String> Map = new HashMap();


                CharSequence label = pinfo.loadLabel(mPm);
                CharSequence desc = pinfo.loadDescription(mPm);
                Map.put("NAME1", name1);
                Map.put("NAME", (label == null) ? pinfo.name : label.toString());
                Map.put("DESCRIPTION", (desc == null) ? "" : desc.toString());
                Map.put("SECURITYLEVEL", String.valueOf(pinfo.protectionLevel));

                mapList.add(Map);

            }
            SimpleAdapter adapter = new SimpleAdapter(this, mapList, R.layout.permission_list_item, new String[]{"NAME1","NAME","DESCRIPTION"}, new int[]{R.id.listviewpermissiontext, R.id.text1, R.id.text2});
            permissionList = (ListView)findViewById(R.id.application_detail_permission_list);
            permissionList.setAdapter(adapter);
            //ddddddddddddd

//            ListAdapter permissionAdapter = new SimpleCursorAdapter(this, R.layout.permission_list_item, permissionListCursor, new String[] {"_id"}, new int[]{R.id.listviewpermissiontext});
//            permissionList = (ListView)findViewById(R.id.application_detail_permission_list);
//            permissionList.setAdapter(permissionAdapter);

            // Evenement au clic sur la liste
            permissionList.setOnItemClickListener(new OnItemClickListener() {
               public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    // Ouverture de l'activite d�tail de la permission selectionnee
                    Intent intent = new Intent(getBaseContext() , PermissionDetail.class);
                    intent.putExtra("permissionId",id);
                    startActivity(intent);
                }
            });


        } else {
            // Application non trouv�e dans la base de donnees
            ((TextView)findViewById(R.id.application_detail_label)).setText(getString(R.string.application_detail_nodata));
        }
        // Fermeture de l'acc�s a la base de donnees
       data.close();
    }




    }
