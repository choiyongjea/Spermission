/*
 * Projet 	: Permission Explorer
 * Auteur 	: Carlo Criniti
 * Date   	: 2011.06.10
 * 
 * Classe CategoryDetail
 * Activit? d'affichage du d?tail d'une categorie
 * avec les applications qu'elle contient
 */

package com.example.jay.spermission;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CategoryDetail extends Activity {
	ListView applicationList; // Composant graphique g?rant la liste de permissions
	
	/*
	 * onCreate :
	 * Ex?cut? ? la cr?ation de l'activit?. R?cup?re
	 * les informations sur la categorie re?ue par
	 * l'Intent et les inscrits dans les composants
	 * graphiques
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {        
    	super.onCreate(savedInstanceState);
    	
    	// Creation de l'interface graphique
        setContentView(R.layout.category_detail);
    	
        // R?cup?ration des donnees de l'intent
        Intent thisIntent = getIntent();
        String categoryId = Long.toString(thisIntent.getExtras().getLong("categoryId"));
        
        // R?cuperation des informations sur la cat?gorie
        Cursor data = Tools.database.database.query("category", new String[]{"name"}, "id = ?", new String[]{categoryId}, null, null, null);
        if (data.getCount() == 1) {
        	data.moveToFirst();
        	
        	// Ajout des informations dans les composants graphiques
        	((TextView)findViewById(R.id.category_detail_name)).setText(data.getString(0));
        	((TextView)findViewById(R.id.category_detail_description)).setText(Tools.getStringResourceByName("category_" + data.getString(0), getResources(), this));
        	
        	// R?cup?re le nombre d'applications qui sont dans cette cat?gorie et l'affiche
        	SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        	boolean hideSystemApp = pref.getBoolean("hide_system_app", false); // Hide system applications
        	
        	String systemAppWhere = "";
        	if (hideSystemApp)
        		systemAppWhere = " AND system = 0";
        	
        	data = Tools.database.database.rawQuery("SELECT Count(DISTINCT application) AS number " +
        											"FROM relation_category_permission " +
        											"INNER JOIN relation_application_permission ON relation_category_permission.permission = relation_application_permission.permission " +
        											"LEFT OUTER JOIN application ON relation_application_permission.application = application.id " +
        											"WHERE category = ?" + systemAppWhere + ";", new String[]{categoryId});
        	data.moveToFirst();
        	((TextView)findViewById(R.id.category_detail_application_count)).setText(data.getString(0));
        	
        	// Pr?ference pour l'affichage des applications, nom ou package
        	boolean applicationName = pref.getBoolean("application_name", true); // Affiche true:label / false:package 
        	
        	String nameField;
        	if (applicationName)
        		nameField = "application.label";
        	else
        		nameField = "application.name";
        	
        	// R?cup?ration des applications de la cat?gorie et affichage dans la liste
        	Cursor applicationListCursor = Tools.database.database.rawQuery("SELECT DISTINCT application.id AS _id, " + nameField + " AS name, application.name AS package " +
        																	"FROM relation_category_permission " +
        																	"INNER JOIN relation_application_permission ON relation_category_permission.permission = relation_application_permission.permission INNER JOIN application ON relation_application_permission.application = application.id " +
        																	"WHERE category = ?" + systemAppWhere + " " +
        																	"ORDER BY " + nameField + " COLLATE NOCASE ASC;", new String[] {categoryId});
        	startManagingCursor(applicationListCursor);

        	List<ApplicationListItem> items = new ArrayList<ApplicationListItem>();
        	
        	PackageManager pm = getPackageManager();
        	try {
    	    	for(applicationListCursor.moveToFirst(); !applicationListCursor.isAfterLast(); applicationListCursor.moveToNext()) {
    				items.add(new ApplicationListItem(applicationListCursor.getLong(0), pm.getApplicationIcon(applicationListCursor.getString(2)), applicationListCursor.getString(1)));
    	    	}
        	} catch (NameNotFoundException e) {
    			e.printStackTrace();
    		}
        	
        	ApplicationListAdapter applicationAdapter = new ApplicationListAdapter(this, items);
        	applicationList = (ListView)findViewById(R.id.category_detail_application_list);
        	applicationList.setAdapter(applicationAdapter);
        	
        	// Evenement au clic sur la liste
        	applicationList.setOnItemClickListener(new OnItemClickListener() {
    			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
    				// Ouverture du d?tail de l'application s?lectionn?e
    				Intent intent = new Intent(getBaseContext() , ApplicationDetail.class);     
    				intent.putExtra("applicationId",id);
    				startActivity(intent); 
    			}
            });
        } else {
        	// La cat?gorie n'est pas trouv?e dans la base de donn?es
        	((TextView)findViewById(R.id.category_detail_name)).setText(getString(R.string.category_detail_nodata));
        }
        // Fermeture de l'acc?s ? la base de donn?es
        data.close();
	}
}
