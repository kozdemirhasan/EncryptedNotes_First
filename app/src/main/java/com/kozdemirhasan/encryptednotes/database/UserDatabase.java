package com.kozdemirhasan.encryptednotes.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.kozdemirhasan.encryptednotes.pojo.Crypt;
import com.kozdemirhasan.encryptednotes.pojo.Kullanici;
import com.kozdemirhasan.encryptednotes.pojo.MD5;
import com.kozdemirhasan.encryptednotes.pojo.Sabitler;

import static android.widget.Toast.makeText;

public class UserDatabase {

    private SQLiteDatabase db;
    private final Context context;
    private final UserDBHelper dbhelper;

    //constructer
    public UserDatabase(Context c) {
        context = c;
//Dphelper opjesiyle yeni veritabanı oluşturuluyor.
        dbhelper = new UserDBHelper(context, Sabitler.DATABASE_NAME_USER, null,
                Sabitler.DATABASE_VERSION_USER);
    }

    /*
     * Veritabanını operasyonlara kapatmak
     * için kullandığımız method.
     */
    public void kapat() {
        db.close();
    }

    /*
     * Veritabanını yazma ve okuma için açtığımız method
     * **!**
     * ->yazmak için aç, yazma operasyonu değilse exception ver catch bloğunda okumak için aç
     */
    public void ac() throws SQLiteException {
        try {
            db = dbhelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            Log.v("db exception caught", ex.getMessage());
            db = dbhelper.getReadableDatabase();
        }
    }

    public int kullaniciVarmiKontrolEt() {
        int x = 0;
        try {
            Cursor c = kullaniciGetir();
            if (c.moveToFirst()) {
                do {
                    x = 1;
                } while (c.moveToNext());
            }

        } catch (SQLiteException ex) {
            return 0;
        } catch (Exception ex) {
            return 0;
        }

        return x;
    }

    public Cursor kullaniciGetir() {
        Cursor c = db.query(Sabitler.TABLO_KULLANICI, null, null, null, null, null,
                null, null);
        return c;
    }

    public long kullaniciKayit(String password, String fakePassword) {
        int x;
        try {
            ContentValues yeniDegerler = new ContentValues();
            yeniDegerler.put(Sabitler.ROW_USER_PASSWORD, password);
            yeniDegerler.put(Sabitler.ROW_USER_FAKEPASSWORD, fakePassword);
            yeniDegerler.put(Sabitler.ROW_USER_GUN, 20);
            yeniDegerler.put(Sabitler.ROW_USER_GUN_DURUM, 0);
            yeniDegerler.put(Sabitler.ROW_USER_TEXTSIZE, 18);
            db.insert(Sabitler.TABLO_KULLANICI, null, yeniDegerler);
            x = 1;
        } catch (SQLiteException ex) {

            Log.v("ekleme isleminde hata !",
                    ex.getMessage());
            //  Toast.makeText(this.context, "Kullanıcı adı daha önce kayıtlı\nBaşka bir kullanıcı adı ile kayıt yapınız", Toast.LENGTH_LONG).show();
            x = -1;

        }
        return x;
    }

    public boolean fakePasswordKonrolEt(String fakePassword) {
        Boolean durum = false;
        Cursor c = db.query(Sabitler.TABLO_KULLANICI, null,
                Sabitler.ROW_USER_FAKEPASSWORD + " = ? ",
                new String[]{fakePassword}, null, null, null);
        //Kullanıcı ismi yoksa hata veriliyor.
        if (c.getCount() < 1) {
            c.close();
            return durum = false;
        } else {
            // c.moveToFirst();
            c.close();
            return durum = true;
        }
    }


    public boolean passwordKonrolEt(String password) {
        Boolean durum = false;
        Cursor c = db.query(Sabitler.TABLO_KULLANICI, null,
                Sabitler.ROW_USER_PASSWORD + " = ? ",
                new String[]{password}, null, null, null);
        //Kullanıcı ismi yoksa hata veriliyor.
        if (c.getCount() < 1) {
            c.close();
            return durum = false;
        } else {
            // c.moveToFirst();
            c.close();
            return durum = true;
        }
    }


    public int parolaDegistir(String eskiParola, String yeniParola) {
        try {
            Crypt crypt = new Crypt();
            //  String[] idArray = {String.valueOf(MD5.md5Sifrele(crypt.encrypt(eskiParola, eskiParola)))};

            ContentValues guncelDegerler = new ContentValues();
            guncelDegerler.put(Sabitler.ROW_USER_PASSWORD, MD5.md5Sifrele(crypt.encrypt(yeniParola, yeniParola)));

            //      return db.update(Sabitler.TABLO_KULLANICI, guncelDegerler, Sabitler.ROW_USER_PASSWORD + "=?", idArray);
            return db.update(Sabitler.TABLO_KULLANICI, guncelDegerler, null, null);


        } catch (SQLiteException ex) {
            return -1;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;

        }

    }

    public int fakeParolaDegistir(String eskiParola, String yeniParola) {
        try {
            Crypt crypt = new Crypt();
           // String[] idArray = {String.valueOf(MD5.md5Sifrele(crypt.encrypt(eskiParola, eskiParola)))};
            ContentValues guncelDegerler = new ContentValues();
            guncelDegerler.put(Sabitler.ROW_USER_FAKEPASSWORD, MD5.md5Sifrele(crypt.encrypt(yeniParola, yeniParola)));

           // return db.update(Sabitler.TABLO_KULLANICI, guncelDegerler, Sabitler.ROW_USER_FAKEPASSWORD + "=?", idArray);
            return db.update(Sabitler.TABLO_KULLANICI, guncelDegerler, null,null);

        } catch (SQLiteException ex) {
            return -1;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }


    public int gunSayisiSetEt(int gun) {

        try {
            ContentValues guncelDegerler = new ContentValues();
            guncelDegerler.put(Sabitler.ROW_USER_GUN, gun);

            return db.update(Sabitler.TABLO_KULLANICI, guncelDegerler, null, null);

        } catch (SQLiteException ex) {
            return -1;

        }
    }

    public int textSizeSetEt(int textSize) {

        try {
            ContentValues guncelDegerler = new ContentValues();
            guncelDegerler.put(Sabitler.ROW_USER_TEXTSIZE, textSize);

            return db.update(Sabitler.TABLO_KULLANICI, guncelDegerler, null, null);

        } catch (SQLiteException ex) {
            return -1;

        }
    }


    public int silmeDurumTrueYap() {
        try {

            ContentValues guncelDegerler = new ContentValues();
            guncelDegerler.put(Sabitler.ROW_USER_GUN_DURUM, 1);

            return db.update(Sabitler.TABLO_KULLANICI, guncelDegerler, null, null);

        } catch (SQLiteException ex) {
            return -1;

        }
    }

    public int silmeDurumFalseYap() {
        try {

            ContentValues guncelDegerler = new ContentValues();
            guncelDegerler.put(Sabitler.ROW_USER_GUN_DURUM, 0);

            return db.update(Sabitler.TABLO_KULLANICI, guncelDegerler, null, null);

        } catch (SQLiteException ex) {
            return -1;

        }
    }

    public Kullanici ayarlar() {
       // HashMap<String, Integer> ayarlar = new HashMap<String, Integer>();
        Kullanici ayarlar = new Kullanici();
        Cursor c = null;
        try {
       /*     //Sabitler.ROW_USER_ID + " = ? ", new String[]{String.valueOf(idKullanici)}
            c = db.query(Sabitler.TABLO_KULLANICI, new String[]{Sabitler.ROW_USER_GUN,
                            Sabitler.ROW_USER_GUN_DURUM, Sabitler.ROW_USER_TEXTSIZE},
                    null, null, null, null, null);
*/
            c = db.query(Sabitler.TABLO_KULLANICI, null,
                    null, null, null, null, null);

        } catch (Exception ex) {
            ex.printStackTrace();

        }
        if (c.moveToNext()) {
            do {
               // ayarlar.put("GUNSAYISI", c.getInt(c.getColumnIndex(Sabitler.ROW_USER_GUN)));
              //  ayarlar.put("SILMEDURUM", c.getInt(c.getColumnIndex(Sabitler.ROW_USER_GUN_DURUM)));
               // ayarlar.put("TEXTSIZE", c.getInt(c.getColumnIndex(Sabitler.ROW_USER_TEXTSIZE)));
                ayarlar.setKullaniciId(c.getInt(c.getColumnIndex(Sabitler.KEY_USER_ID)));
                ayarlar.setFakePassword(c.getString(c.getColumnIndex(Sabitler.ROW_USER_PASSWORD)));
                ayarlar.setFakePassword(c.getString(c.getColumnIndex(Sabitler.ROW_USER_FAKEPASSWORD)));
                ayarlar.setSilmeDurum(c.getInt(c.getColumnIndex(Sabitler.ROW_USER_GUN_DURUM)));
                ayarlar.setSilmeGun(c.getInt(c.getColumnIndex(Sabitler.ROW_USER_GUN)));
                ayarlar.setMetinBoyutu(c.getInt(c.getColumnIndex(Sabitler.ROW_USER_TEXTSIZE)));

            } while (c.moveToNext());
        }
        return ayarlar;
    }


}