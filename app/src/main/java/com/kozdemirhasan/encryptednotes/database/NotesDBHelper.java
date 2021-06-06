package com.kozdemirhasan.encryptednotes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kozdemirhasan.encryptednotes.pojo.Sabitler;


public class NotesDBHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE_NOTLAR = "create table " + Sabitler.TABLO_NOTLAR
            + " (" + Sabitler.KEY_NOT_ID + " integer primary key autoincrement, "
            + Sabitler.ROW_NOT_BASLIK + " text  not null, "
            + Sabitler.ROW_NOT_ICERIK + " text  not null, "
            + Sabitler.ROW_NOT_GRUP + " text not null, "
            + Sabitler.ROW_NOT_TARIH + " longtext  not null);";

    public NotesDBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v("NotesDBHelper..", "Tablolar oluşturuyor…");
        try {
// db.execSQL(“drop table if exists”+Sabitler.TABLO);
            db.execSQL(CREATE_TABLE_NOTLAR);

        } catch (SQLiteException ex) {
            Log.v("Tablo olusturma hatasi ", ex.getMessage());

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("Upgrade islemi", "Tum veriler silinecek !");
/*
* Yenisi geldiğinde eski tablodaki tüm veriler silinecek ve
* tablo yeniden oluşturulacak.
*/
        db.execSQL("drop table if exists " + Sabitler.TABLO_NOTLAR);
        onCreate(db);
    }
}