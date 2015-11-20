package com.example.jay.spermission;

import android.content.Context;
import android.content.res.Resources;
/**
 * Created by silogood on 2015-11-16.
 */
public class Tools {
    // Package de l'application
    private final static String packageName = "com.example.jay.spermission";
    // Base de donn�es de l'application
    public static Database database;
    /*
     * getStringResourceByName
     * Permet de r�cup�rer une chaine de caract�res dans les ressources
     * n'importe ou dans l'application
     */
    public static String getStringResourceByName(String name, Resources res, Context context)
    {
        int resId = res.getIdentifier(name, "string", packageName);
        return context.getString(resId);
    }
}
