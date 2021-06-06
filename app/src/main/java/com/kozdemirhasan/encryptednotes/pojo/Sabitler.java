package com.kozdemirhasan.encryptednotes.pojo;

import android.os.Parcelable;

/**
 * Created by Casper on 10.01.2018.
 */

public class Sabitler {

    public static final String DATABASE_NAME_USER = "userDB";
    public static final int DATABASE_VERSION_USER = 1;

    public static final String TABLO_KULLANICI = "kullanici_tablosu";
    public static final String KEY_USER_ID = "id";
    public static final String ROW_USER_PASSWORD = "password";
    public static final String ROW_USER_GUN_DURUM = "gundurum";
    public static final String ROW_USER_GUN = "gunsayisi";
    public static final String ROW_USER_TEXTSIZE = "textsize";


    public static final String DATABASE_NAME_NOTES = "notesDB";
    public static final int DATABASE_VERSION_NOTES = 1;

    public static final String TABLO_NOTES_NAME = "notes";
    public static final String KEY_NOT_ID = "id";
    public static final String ROW_NOTE_TITLE = "title";
    public static final String ROW_NOTE_BODY = "body";
    public static final String ROW_NOTE_GROUP = "grup";
    public static final String ROW_NOTE_DATE = "date";

    public static Parcelable state=null;

    public static String loginPassword; //kullanıcının girdiği değer buraya yazılıyor
    public static String PASS_MD5;

    public static int yaziBoyutu = 18;
    public static int lastPosition =-1;


}
