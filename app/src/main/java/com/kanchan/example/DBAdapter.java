package com.kanchan.example;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

public class DBAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String TAG = "DBAdapter";
    public static final String DATABASE_NAME = "MyDB";
    public static final String DATABASE_TABLE = "contacts";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_CREATE =
            "create table contacts(_id integer primary key autoincrement, "
                                     + "name text not null, email text not null);";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context=ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

        public void onCreate(SQLiteDatabase db)
        {
            try{
                db.execSQL(DATABASE_CREATE);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.v(TAG, "Upgrading database from version "+ oldVersion + " TO " + newVersion + " , which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }

    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() { DBHelper.close(); }

    public long insertContact(String name, String email)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_EMAIL, email);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
public boolean deleteContact(long rowId)
{
    return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
}

public Cursor getAllContacts()
{
    return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_EMAIL},
            null, null, null, null, null);
}

public Cursor getContact(long rowId) throws SQLException
{
    Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_EMAIL},
            KEY_ROWID + "=" + rowId, null, null, null, null, null);

    if (mCursor != null)
    {
        mCursor.moveToFirst();
    }
    return mCursor;

}

public boolean updateContact(long rowId, String name, String email)
{
    ContentValues args = new ContentValues();
    args.put(KEY_NAME, name);
    args.put(KEY_EMAIL, email);
    return db.update(DATABASE_TABLE, args, KEY_ROWID +"=" + rowId,null) > 0;
}

}
