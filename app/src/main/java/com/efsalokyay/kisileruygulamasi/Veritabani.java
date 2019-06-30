package com.efsalokyay.kisileruygulamasi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Veritabani extends SQLiteOpenHelper {

    public Veritabani(Context context) {
        super(context, "rehber.sqlite", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE kisiler ( kisi_id INTEGER PRIMARY KEY AUTOINCREMENT, kisi_ad TEXT, kisi_tel TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS kisiler");
        onCreate(db);
    }
}
