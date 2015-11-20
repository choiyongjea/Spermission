package com.example.jay.spermission;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;




/**
 * Created by silogood on 2015-11-16.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper{


    private static final String DATABASE_NAME = "application_permission.db";
    private static final int DATABASE_VERSION = 3;

    // Cr�ations des tables
    private static final String APPLICATION_TABLE_CREATE =
            "CREATE TABLE application (" +
                    "id INTEGER PRIMARY KEY, " +
                    "label TEXT , " +
                    "name TEXT, " +
                    "version_code INTEGER, " +
                    "version_name TEXT, " +
                    "system INTEGER);";

    private static final String PERMISSION_TABLE_CREATE =
            "CREATE TABLE permission (" +
                    "id INTEGER PRIMARY KEY, " +
                    "name TEXT);";

    private static final String CATEGORY_TABLE_CREATE =
            "CREATE TABLE category (" +
                    "id INTEGER PRIMARY KEY, " +
                    "name TEXT);";

    private static final String RELATION_APPLICATION_PERMISSION_TABLE_CREATE =
            "CREATE TABLE relation_application_permission (" +
                    "application INTEGER, " +
                    "permission INTEGER, " +
                    "FOREIGN KEY(application) REFERENCES application(id)," +
                    "FOREIGN KEY(permission) REFERENCES permission(id));";

    private static final String RELATION_CATEGORY_PERMISSION =
            "CREATE TABLE relation_category_permission ( " +
                    "category INTEGER, " +
                    "permission INTEGER, " +
                    "FOREIGN KEY (category) REFERENCES category(id)," +
                    "FOREIGN KEY (permission) REFERENCES permission(id));";

    // Suppression des tables
    private static final String APPLICATION_TABLE_DELETE = "DROP TABLE application;";
    private static final String PERMISSION_TABLE_DELETE = "DROP TABLE permission;";
    private static final String RELATION_APPLICATION_PERMISSION_TABLE_DELETE = "DROP TABLE relation_application_permission;";
    private static final String CATEGORY_TABLE_DELETE = "DROP TABLE category;";
    private static final String RELATION_CATEGORY_PERMISSION_DELETE = "DROP TABLE relation_category_permission";

    // Donn�es de la base
    private static final String[] permissionList = {"ACCESS_ALL_DOWNLOADS",
            "ACCESS_ASSISTED_GPS",
            "ACCESS_BLUETOOTH_PRINTER",
            "ACCESS_BLUETOOTH_SHARE",
            "ACCESS_BROWSER",
            "ACCESS_CACHE_FILESYSTEM",
            "ACCESS_CELL_ID",
            "ACCESS_CHECKIN_PROPERTIES",
            "ACCESS_COARSE_LOCATION",
            "ACCESS_COARSE_UPDATES",
            "ACCESS_DEV_STORAGE",
            "ACCESS_DOWNLOAD_DATA",
            "ACCESS_DOWNLOAD_MANAGER",
            "ACCESS_DOWNLOAD_MANAGER_ADVANCED",
            "ACCESS_DRM",
            "ACCESS_FINE_LOCATION",
            "ACCESS_FM_RECEIVER",
            "ACCESS_GPS",
            "ACCESS_LGDRM",
            "ACCESS_LOCATION",
            "ACCESS_LOCATION_EXTRA_COMMANDS",
            "ACCESS_MOCK_LOCATION",
            "ACCESS_MTP",
            "ACCESS_NETWORK_STATE",
            "ACCESS_NETWORK_LOCATION",
            "ACCESS_OBEX",
            "ACCESS_SURFACE_FLINGER",
            "ACCESS_UPLOAD_DATA",
            "ACCESS_UPLOAD_MANAGER",
            "ACCESS_WIFI_STATE",
            "ACCESS_WIMAX_STATE",
            "ACCOUNT_MANAGER",
            "ADD_SYSTEM_SERVICE",
            "ASEC_ACCESS",
            "ASEC_CREATE",
            "ASEC_DESTROY",
            "ASEC_MOUNT_UNMOUNT",
            "AUTHENTICATE_ACCOUNTS",
            "BACKUP",
            "BACKUP_DATA",
            "BATTERY_STATS",
            "BIND_APPWIDGET",
            "BIND_DEVICE_ADMIN",
            "BIND_INPUT_METHOD",
            "BIND_REMOTEVIEWS",
            "BIND_WALLPAPER",
            "BLUETOOTH",
            "BLUETOOTH_ADMIN",
            "BOOT_COMPLETED",
            "BRICK",
            "BROADCAST_PACKAGE_REMOVED",
            "BROADCAST_SMS",
            "BROADCAST_STICKY",
            "BROADCAST_WAP_PUSH",
            "CALL",
            "CALL_PHONE",
            "CALL_PRIVILEGED",
            "CAMERA",
            "CHANGE_BACKGROUND_DATA_SETTING",
            "CHANGE_COMPONENT_ENABLED_STATE",
            "CHANGE_CONFIGURATION",
            "CHANGE_NETWORK_STATE",
            "CHANGE_WIFI_MULTICAST_STATE",
            "CHANGE_WIFI_STATE",
            "CHANGE_WIMAX_STATE",
            "CLEAR_APP_CACHE",
            "CLEAR_APP_USER_DATA",
            "CONFIRM_FULL_BACKUP",
            "CONNECTIVITY_INTERNAL",
            "CONTROL_LOCATION_UPDATES",
            "DELETE_CACHE_FILES",
            "DELETE_PACKAGES",
            "DELETE_SMS",
            "DEVICE_POWER",
            "DIAGNOSTIC",
            "DISABLE_KEYGUARD",
            "DOWNLOAD_WITHOUT_NOTIFICATION",
            "DUMP",
            "EXPAND_STATUS_BAR",
            "FACTORY_TEST",
            "FLASHLIGHT",
            "FORCE_BACK",
            "FORCE_STOP_PACKAGES",
            "FOTA_UPDATE",
            "FULLSCREEN",
            "GET_ACCOUNTS",
            "GET_PACKAGE_SIZE",
            "GET_TASKS",
            "GLOBAL_SEARCH",
            "HARDWARE_TEST",
            "HTC_FOTA_UPDATE",
            "INJECT_AUDIO_VOLUME_SETTINGS",
            "INJECT_EVENTS",
            "INSTALL_DRM",
            "INSTALL_LOCATION_PROVIDER",
            "INSTALL_PACKAGES",
            "INTERNAL_SYSTEM_WINDOW",
            "INTERNET",
            "KILL_BACKGROUND_PROCESSES",
            "LOCATION",
            "MANAGE_ACCOUNTS",
            "MANAGE_APP_TOKENS",
            "MANAGE_USB",
            "MASTER_CLEAR",
            "MODIFY_AUDIO_SETTINGS",
            "MODIFY_NETWORK_ACCOUNTING",
            "MODIFY_PHONE_STATE",
            "MOUNT_FORMAT_FILESYSTEMS",
            "MOUNT_UNMOUNT_FILESYSTEMS",
            "MOVE_PACKAGE",
            "NFC",
            "PACKAGE_USAGE_STATS",
            "PERFORM_CDMA_PROVISIONING",
            "PERSISTENT_ACTIVITY",
            "PROCESS_OUTGOING_CALLS",
            "QUERY_DRM",
            "RAISED_THREAD_PRIORITY",
            "READ_CALENDAR",
            "READ_CALL_STATE",
            "READ_CONTACTS",
            "READ_DIARY",
            "READ_EXTERNAL_STORAGE",
            "READ_FRAME_BUFFER",
            "READ_GMAIL",
            "READ_HISTORY_BOOKMARKS",
            "READ_INPUT_STATE",
            "READ_LOGS",
            "READ_MMS",
            "READ_NOTES",
            "READ_OWNER_DATA",
            "READ_PHONE_STATE",
            "READ_POLICIES",
            "READ_PROFILE",
            "READ_SETTINGS",
            "READ_SMS",
            "READ_SOCIAL_STREAM",
            "READ_SYNC_SETTINGS",
            "READ_SYNC_STATS",
            "READ_TASKS",
            "READ_USER_DICTIONARY",
            "REBOOT",
            "RECEIVE_BOOT_COMPLETE",
            "RECEIVE_BOOT_COMPLETED",
            "RECEIVE_EMAIL_NOTIFICATION",
            "RECEIVE_MMS",
            "RECEIVE_SHUTDOWN",
            "RECEIVE_SMS",
            "RECEIVE_WAP_PUSH",
            "RECORD_AUDIO",
            "REORDER_TASKS",
            "RESTART_PACKAGES",
            "RUN_INSTRUMENTATION",
            "SCREEN",
            "SECRET_CODE",
            "SEND",
            "SEND_DOWNLOAD_COMPLETED_INTENTS",
            "SEND_SMS",
            "SEND_SMS_NO_CONFIRMATION",
            "SEND_UPLOAD_COMPLETED_INTENTS",
            "SET_ACTIVITY_WATCHER",
            "SET_ALARM",
            "SET_ALWAYS_FINISH",
            "SET_ANIMATION_SCALE",
            "SET_DEBUG_APP",
            "SET_ORIENTATION",
            "SET_POINTER_SPEED",
            "SET_PREFERRED_APPLICATIONS",
            "SET_PROCESS_LIMIT",
            "SET_TIME",
            "SET_TIME_ZONE",
            "SET_WALLPAPER",
            "SET_WALLPAPER_COMPONENT",
            "SET_WALLPAPER_HINTS",
            "SHUTDOWN",
            "SIGNAL_PERSISTENT_PROCESSES",
            "STATUS_BAR",
            "STATUS_BAR_SERVICE",
            "STOP_APP_SWITCHES",
            "SUBSCRIBED_FEEDS_READ",
            "SUBSCRIBED_FEEDS_WRITE",
            "SYSTEM_ALERT_WINDOW",
            "UPDATE_DEVICE_STATS",
            "USE_CREDENTIALS",
            "USE_SIP",
            "VIBRATE",
            "WAKE_LOCK",
            "WIFI_LOCK",
            "WRITE_APN_SETTINGS",
            "WRITE_CALENDAR",
            "WRITE_CDMA_CARRIER_SETTINGS",
            "WRITE_CONTACTS",
            "WRITE_EXTERNAL_STORAGE",
            "WRITE_GSERVICES",
            "WRITE_HISTORY_BOOKMARKS",
            "WRITE_MEDIA_STORAGE",
            "WRITE_NOTES",
            "WRITE_OWNER_DATA",
            "WRITE_POLICIES",
            "WRITE_PROFILE",
            "WRITE_SECURE_SETTINGS",
            "WRITE_SETTINGS",
            "WRITE_SMS",
            "WRITE_SOCIAL_STREAM",
            "WRITE_SYNC_SETTINGS",
            "WRITE_SYNC_STATS",
            "WRITE_TASKS",
            "WRITE_USER_DICTIONARY"};

    // Permissions de localisation
    private static final String[] permissionLocation = {"ACCESS_ASSISTED_GPS",
            "ACCESS_COARSE_LOCATION",
            "ACCESS_COARSE_UPDATES",
            "ACCESS_FINE_LOCATION",
            "ACCESS_GPS",
            "ACCESS_LOCATION",
            "ACCESS_LOCATION_EXTRA_COMMANDS",
            "ACCESS_NETWORK_LOCATION",
            "LOCATION"};

    // Permissions de l'identit�
    private static final String[] permissionPhoneIdentity = {"AUTHENTICATE_ACCOUNTS",
            "GET_ACCOUNTS",
            "MANAGE_ACCOUNTS",
            "MANAGE_APP_TOKENS",
            "READ_OWNER_DATA",
            "READ_PROFILE",
            "USE_CREDENTIALS",
            "WRITE_OWNER_DATA",
            "WRITE_PROFILE"};

    // Permissions des messages
    private static final String[] permissionMessages = {"BROADCAST_SMS",
            "DELETE_SMS",
            "READ_SMS",
            "READ_MMS",
            "RECEIVE_EMAIL_NOTIFICATION",
            "RECEIVE_MMS",
            "RECEIVE_SMS",
            "RECEIVE_WAP_PUSH",
            "WRITE_SMS"};

    // Permissions des contacts
    private static final String[] permissionContacts = {"READ_CONTACTS",
            "WRITE_CONTACTS"};

    // Permissions du calendrier
    private static final String[] permissionCalendar = {"READ_CALENDAR",
            "READ_DIARY",
            "WRITE_CALENDAR"};

    // Permissions des acc�s payants
    private static final String[] permissionPaying = {"CALL",
            "CALL_PHONE",
            "CALL_PRIVILEGED",
            "SEND_SMS",
            "SEND_SMS_NO_CONFIRMATION"};

    // Permissions du syst�me
    private static final String[] permissionSystem = {"ACCESS_MTP",
            "BLUETOOTH_ADMIN",
            "BRICK",
            "CHANGE_CONFIGURATION",
            "CHANGE_NETWORK_STATE",
            "CHANGE_WIFI_STATE",
            "CHANGE_WIMAX_STATE",
            "CLEAR_APP_USER_DATA",
            "CONFIRM_FULL_BACKUP",
            "DELETE_CACHE_FILES",
            "DELETE_PACKAGES",
            "DEVICE_POWER",
            "DIAGNOSTIC",
            "DISABLE_KEYGUARD",
            "DUMP",
            "FACTORY_TEST",
            "FORCE_STOP_PACKAGES",
            "GET_TASKS",
            "INSTALL_PACKAGES",
            "INTERNAL_SYSTEM_WINDOW",
            "KILL_BACKGROUND_PROCESSES",
            "MANAGE_USB",
            "MODIFY_AUDIO_SETTINGS",
            "MODIFY_NETWORK_ACCOUNTING",
            "MODIFY_PHONE_STATE",
            "READ_FRAME_BUFFER",
            "READ_LOGS",
            "READ_SETTINGS",
            "READ_SYNC_SETTINGS",
            "READ_SYNC_STATS",
            "READ_NETWORK_USAGE_HISTORY",
            "READ_USER_DICTIONARY",
            "REBOOT",
            "SET_POINTER_SPEED",
            "SET_PROCESS_LIMIT",
            "SET_TIME",
            "SET_TIME_ZONE",
            "SHUTDOWN",
            "WAKE_LOCK",
            "WRITE_APN_SETTINGS",
            "WRITE_MEDIA_STORAGE",
            "WRITE_SECURE_SETTINGS",
            "WRITE_SETTINGS",
            "WRITE_SYNC_SETTINGS"};

    /*
	 * DatabaseOpenHelper :
	 * Cr�ation ou ouverture d'une base de donn�es pour l'application
	 */
    DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
	 * createMinimalDb :
	 * Cr�ations des tables minimales � la base de donn�es
	 */
    private void createMinimalDb(SQLiteDatabase db) {
        db.execSQL(APPLICATION_TABLE_CREATE);
        db.execSQL(PERMISSION_TABLE_CREATE);
        db.execSQL(RELATION_APPLICATION_PERMISSION_TABLE_CREATE);
        db.execSQL(CATEGORY_TABLE_CREATE);
        db.execSQL(RELATION_CATEGORY_PERMISSION);
    }

    /*
	 * fillCategory :
	 * Ajout d'une cat�gorie et lien avec ses permissions
	 * dans la base de donn�es
	 */
    private void fillCategory(SQLiteDatabase db, String[] list, String categoryName) {
        // Identifiant de la permission
        int permissionId;

        // Ajout de la categorie � la base de donn�es
        ContentValues values = new ContentValues();
        values.put("name", categoryName);
        long categoryId = db.insert("category", null, values);

        // Parcourt la liste des permissions de la cat�gorie
        int count = list.length;
        for (int i = 0; i < count; ++i) {
            // R�cup�re l'id de la permission
            Cursor cursor = db.query("permission", new String[]{"id"}, "name = ?", new String[]{list[i]}, null, null, null);
            if (cursor.getCount() == 1) {
                cursor.moveToFirst();
                permissionId = cursor.getInt(0);

                // Fait le lien entre la cat�gorie et la permission
                values = new ContentValues();
                values.put("category", categoryId);
                values.put("permission", permissionId);
                db.insert("relation_category_permission", null, values);
            }
            cursor.close();
        }
    }

    /*
	 * fillDb :
	 * Ajout des informations de permission
	 * dans la base de donn�es
	 */
    private void fillDb(SQLiteDatabase db) {
        ContentValues values;

        // Parcourt toutes les permissions
        int count = permissionList.length;
        for (int i = 0; i < count; ++i) {
            // ajoute chacune d'elles � la base de donn�es
            values = new ContentValues();
            values.put("name", permissionList[i]);
            db.insert("permission", null, values);
        }

        // Ajout des cat�gories � la base de donn�es
        // LOCATION
        fillCategory(db, permissionLocation, "LOCATION");

        // PHONE_IDENTITY
        fillCategory(db, permissionPhoneIdentity, "PHONE_IDENTITY");

        // MESSAGES
        fillCategory(db, permissionMessages, "MESSAGES");

        // CONTACTS
        fillCategory(db, permissionContacts, "CONTACTS");

        // CALENDAR
        fillCategory(db, permissionCalendar, "CALENDAR");

        // PAYING
        fillCategory(db, permissionPaying, "PAYING");

        // SYSTEM
        fillCategory(db, permissionSystem, "SYSTEM");
    }

    /*
	 * onCreate
	 * Appel� en cas de cr�ation d'une base de donn�es
	 */
    @Override
    public void onCreate(SQLiteDatabase db) {
        createMinimalDb(db);
        fillDb(db);
    }

    /*
	 * onUpgrade
	 * Appel� en cas de changement de version de la base de donn�es
	 */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Supprime les tables
        db.execSQL(RELATION_APPLICATION_PERMISSION_TABLE_DELETE);
        db.execSQL(RELATION_CATEGORY_PERMISSION_DELETE);
        db.execSQL(CATEGORY_TABLE_DELETE);
        db.execSQL(APPLICATION_TABLE_DELETE);
        db.execSQL(PERMISSION_TABLE_DELETE);

        // Cr釪 une nouvelle base de donn�es
        createMinimalDb(db);
        fillDb(db);
    }



}
