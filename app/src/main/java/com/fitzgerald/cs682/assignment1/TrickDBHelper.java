package com.fitzgerald.cs682.assignment1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sarah on 4/3/2016.
 */
public class TrickDBHelper extends SQLiteOpenHelper {
    public static final String TRICK_TABLE_NAME = "trick";
    public static final String TRICK_FIELD1 = "dogId";
    public static final String TRICK_FIELD2 = "trickName";
    public static final String TRICK_FIELD3 = "trickResult";
    public static final String ROUTINE_TABLE_NAME = "trickRoutine";
    public static final String ROUTINE_FIELD1 = "id";
    public static final String ROUTINE_FIELD2 = "dogName";
    private static final int DATABASE_VERSION = 10;
    private static final String DATABASE_NAME = "trickRoutine.db";
    private static final String TRICK_TABLE_SPEC = "CREATE TABLE " + TRICK_TABLE_NAME + "(" +
            TRICK_FIELD1 +" INTEGER, " + TRICK_FIELD2 +" TEXT, " + TRICK_FIELD3 + " TEXT," +
            "FOREIGN KEY("+TRICK_FIELD1+") REFERENCES " + ROUTINE_TABLE_NAME +  "("+ROUTINE_FIELD1+"))";
    private static final String ROUTINE_TABLE_SPEC = "CREATE TABLE " + ROUTINE_TABLE_NAME + "(" +
            ROUTINE_FIELD1 +" INTEGER PRIMARY KEY AUTOINCREMENT, " + ROUTINE_FIELD2 + " TEXT)";


    public TrickDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ROUTINE_TABLE_SPEC);
        db.execSQL(TRICK_TABLE_SPEC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TRICK_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ROUTINE_TABLE_NAME);

            db.execSQL(ROUTINE_TABLE_SPEC);
            db.execSQL(TRICK_TABLE_SPEC);
        }

    }
}
