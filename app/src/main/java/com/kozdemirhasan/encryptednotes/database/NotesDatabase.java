package com.kozdemirhasan.encryptednotes.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;


import com.kozdemirhasan.encryptednotes.pojo.Crypt;
import com.kozdemirhasan.encryptednotes.pojo.Not;
import com.kozdemirhasan.encryptednotes.pojo.Sabitler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.widget.Toast.*;

public class NotesDatabase {

    private SQLiteDatabase db;
    private final Context context;
    private final NotesDBHelper dbhelper;


    //constructer
    public NotesDatabase(Context c) {
        context = c;
//Dphelper opjesiyle yeni veritabanı oluşturuluyor.
        dbhelper = new NotesDBHelper(context, Sabitler.DATABASE_NAME_NOTES, null,
                Sabitler.DATABASE_VERSION_NOTES);

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


    public void tumNotlariSil() {
        db.delete(Sabitler.TABLO_NOTLAR, null, null);
    }


    //grubun tüm notları al getir
    public ArrayList<Not> grubunNotlariniGetir(String grupAdi) {
        ArrayList<Not> notlar = new ArrayList<Not>();
        Cursor c;
        try {
            // new String[]{Sabitler.KEY_NOT_ID, Sabitler.ROW_NOT_BASLIK, Sabitler.ROW_NOT_TARIH, Sabitler.ROW_NOT_GRUP}
            c = db.query(Sabitler.TABLO_NOTLAR, new String[]{Sabitler.KEY_NOT_ID, Sabitler.ROW_NOT_BASLIK,
                            Sabitler.ROW_NOT_TARIH, Sabitler.ROW_NOT_GRUP},
                    Sabitler.ROW_NOT_GRUP + "=? ",
                    new String[]{grupAdi},
                    null, null, Sabitler.ROW_NOT_TARIH + " desc");


        } catch (Exception ex) {
            ex.printStackTrace();
            c = null;
        }

//Curson tipinde gelen notları teker teker dolaşıyoruz
        if (c != null) {
            while (c.moveToNext()) {
                int id = c.getInt(c.getColumnIndex(Sabitler.KEY_NOT_ID));
                String baslik = c.getString(c.getColumnIndex(Sabitler.ROW_NOT_BASLIK));
                String grup = c.getString(c.getColumnIndex(Sabitler.ROW_NOT_GRUP));
                //  String icerik = c.getString(c.getColumnIndex(Sabitler.ROW_NOT_ICERIK));

                DateFormat dateFormat = DateFormat.getDateTimeInstance();
                long trhfark = new Date().getTime() - c.getLong(c.getColumnIndex(Sabitler.ROW_NOT_TARIH));
                String tarih = null;
                long birgun = 24 * 60 * 60 * 1000;
                if (trhfark <= 0) {
                    trhfark = trhfark * (-1);
                }

                if (trhfark < birgun) {
                    SimpleDateFormat bicimAyniGun = new SimpleDateFormat("HH:mm");
                    tarih = bicimAyniGun.format(new Date(c.getLong(c
                            .getColumnIndex(Sabitler.ROW_NOT_TARIH))).getTime());
                } else {
                    SimpleDateFormat bicim = new SimpleDateFormat("dd MMM yy");
                    tarih = bicim.format(new Date(c.getLong(c
                            .getColumnIndex(Sabitler.ROW_NOT_TARIH))).getTime());
                }
                Not not = new Not();
                not.set_id(id);
                not.setKonu(baslik);
                not.setGrup(grup);
                not.setKayittarihi(tarih);
                notlar.add(not);
            }
        }
        return notlar;
    }


    //tüm gruplari al getir, kriptolu metin
    public ArrayList<String> tumGruplariGetir() throws Exception {
        ArrayList<String> gruplar = new ArrayList<String>();
        Cursor c = tumGruplariAlGetir();

//Curson tipinde gelen notları teker teker dolaşıyoruz
        if (c != null) {
            while (c.moveToNext()) {
                String grup = c.getString(c.getColumnIndex(Sabitler.ROW_NOT_GRUP));
                gruplar.add(grup);//kriptolu halde grup adları
            }
        }

        Crypt crypt = new Crypt();
        ArrayList<String> gruplarYeni = new ArrayList<String>();
        for(String grp:gruplar){
            gruplarYeni.add( crypt.decrypt(grp,Sabitler.loginPassword));//kriptolu grup adlarını çözümlüyoruz
        }
        Collections.sort(gruplarYeni);//çözümlediğimiz grup adlarını a-z sıralıyoruz

        ArrayList<String> gruplarSon = new ArrayList<>();
        for(String grp:gruplarYeni){
            gruplarSon.add( crypt.encrypt(grp,Sabitler.loginPassword));//a-z sıralanmış grup adları tekrar kriptolanıp yeni gruba atıyoruz
        }


        return gruplarSon;
    }

    public Cursor tumGruplariAlGetir() {
        try {
            //Sabitler.ROW_USER_ID + " = ? ", new String[]{String.valueOf(idKullanici)}
            Cursor c = db.query(true, Sabitler.TABLO_NOTLAR,
                    new String[]{Sabitler.ROW_NOT_GRUP},
                    null, null, null, null
                    , null, null);
            return c;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //başlangıçta tüm notların id ve tarihlerini al getir
    public List<Not> tumKayitlar() {
        List<Not> notlar = new ArrayList<Not>();
        Cursor c = tumNotlar();

//Curson tipinde gelen notları teker teker dolaşıyoruz
        if (c != null) {
            while (c.moveToNext()) {
                int id = c.getInt(c.getColumnIndex(Sabitler.KEY_NOT_ID));

                long tarihLong = c.getLong(c.getColumnIndex(Sabitler.ROW_NOT_TARIH));
                Not gecici = new Not(id, tarihLong);
//Veritabanındaki tüm notları birer birer ArrayList’e kaydediyoruz.
                notlar.add(gecici);
            }
        }


        return notlar;

    }

    public Cursor tumNotlar() {
        try {
            //Sabitler.ROW_USER_ID + " = ? ", new String[]{String.valueOf(idKullanici)}
            Cursor c = db.query(Sabitler.TABLO_NOTLAR, null, null, null, null, null
                    , null, null);
            return c;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }


    }


    /*
     * Veritabanına not eklediğimiz method.
     * insert Yapısı:
     * —-db.insert(String table, String nullColumnHack, ContentValues icerikDegerleri)
     */
    public long notEkle(String konu, String icerik, String grup) throws Exception {
        try {
            ContentValues yeniDegerler = new ContentValues();
            Crypt crypt = new Crypt();
            yeniDegerler.put(Sabitler.ROW_NOT_BASLIK, crypt.encrypt(konu, Sabitler.loginPassword));
            yeniDegerler.put(Sabitler.ROW_NOT_ICERIK, crypt.encrypt(icerik, Sabitler.loginPassword));
            yeniDegerler.put(Sabitler.ROW_NOT_GRUP, crypt.encrypt(grup, Sabitler.loginPassword));
            yeniDegerler.put(Sabitler.ROW_NOT_TARIH, System.currentTimeMillis());
            return db.insert(Sabitler.TABLO_NOTLAR, null, yeniDegerler);

        } catch (SQLiteException ex) {

            Log.v("error in adding",
                    ex.getMessage());

            return -1;
        }
    }

    public void notGuncelle(int id, String konu, String icerik, String grup) {

        ContentValues guncelDegerler = new ContentValues();
        String[] idArray = {String.valueOf(id)};

        guncelDegerler.put(Sabitler.ROW_NOT_BASLIK, konu);
        guncelDegerler.put(Sabitler.ROW_NOT_ICERIK, icerik);
        guncelDegerler.put(Sabitler.ROW_NOT_GRUP, grup);
        guncelDegerler.put(Sabitler.ROW_NOT_TARIH, System.currentTimeMillis());
        db.update(Sabitler.TABLO_NOTLAR, guncelDegerler, Sabitler.KEY_NOT_ID + " =? ", idArray);


    }

    public int tumTarihGuncelle(long tarih) {
        ContentValues guncelDegerler = new ContentValues();
        guncelDegerler.put(Sabitler.ROW_NOT_TARIH, tarih);
        try {
            db.update(Sabitler.TABLO_NOTLAR, guncelDegerler, null, null);
            return 1;
        } catch (Exception ex) {
            return -1;
        }

    }

    public void idIleNotSil(int id) {
        db.delete(Sabitler.TABLO_NOTLAR, Sabitler.KEY_NOT_ID + " =" + id, null);
    }

    public void eskileriSil(List<Integer> idler) {
        for (int i = 0; i < idler.size(); i++) {
            String[] idArray = {String.valueOf(idler.get(i))};
            db.delete(Sabitler.TABLO_NOTLAR, Sabitler.KEY_NOT_ID + " = ?", idArray);
        }
    }


    //id ile not getir
    public Not notGetir(String id) {

        Cursor c;
        try {
            //Sabitler.ROW_USER_ID + " = ? ", new String[]{String.valueOf(idKullanici)}
            c = db.query(Sabitler.TABLO_NOTLAR, null, Sabitler.KEY_NOT_ID + " = ? ",
                    new String[]{id}, null
                    , null, null);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

//Curson tipinde gelen notları teker teker dolaşıyoruz

        if (c.moveToNext()) {
            int idx = c.getInt(c.getColumnIndex(Sabitler.KEY_NOT_ID));
            String baslik = c.getString(c.getColumnIndex(Sabitler.ROW_NOT_BASLIK));
            String icerik = c.getString(c.getColumnIndex(Sabitler.ROW_NOT_ICERIK));
            String grup = c.getString(c.getColumnIndex(Sabitler.ROW_NOT_GRUP));
            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            long trhfark = new Date().getTime() - c.getLong(c.getColumnIndex(Sabitler.ROW_NOT_TARIH));
            String tarih;
            if (trhfark < 24 * 60 * 60 * 1000) {
                SimpleDateFormat bicimAyniGun = new SimpleDateFormat("HH:mm:ss");
                tarih = bicimAyniGun.format(new Date(c.getLong(c
                        .getColumnIndex(Sabitler.ROW_NOT_TARIH))).getTime());
            } else {
                SimpleDateFormat bicim = new SimpleDateFormat("dd-MM-yyyy");
                tarih = bicim.format(new Date(c.getLong(c
                        .getColumnIndex(Sabitler.ROW_NOT_TARIH))).getTime());
            }

            Not not = new Not(idx, baslik, icerik, tarih, grup);

            return not;

        } else {
            makeText(null, "An error occurred", LENGTH_SHORT).show();
            return null;
        }


    }


    //parola değiştiriken tüm notların şifrelemesinin değişmesi için tüm notları al getir
    public List<Not> butunNotlar() {
        List<Not> notlar = new ArrayList<Not>();
        Cursor c = tumNotlar2();

//Curson tipinde gelen notları teker teker dolaşıyoruz
        if (c != null) {
            while (c.moveToNext()) {
                int id = c.getInt(c.getColumnIndex(Sabitler.KEY_NOT_ID));
                String grup = c.getString(c.getColumnIndex(Sabitler.ROW_NOT_GRUP));
                String baslik = c.getString(c.getColumnIndex(Sabitler.ROW_NOT_BASLIK));
                String icerik = c.getString(c.getColumnIndex(Sabitler.ROW_NOT_ICERIK));
                long tarihLong = c.getLong(c.getColumnIndex(Sabitler.ROW_NOT_TARIH));

                Not gecici = new Not(id, grup, baslik, icerik, tarihLong);

//Veritabanındaki tüm notları birer birer ArrayList’e kaydediyoruz.
                notlar.add(gecici);
            }
        }

        return notlar;

    }

    public Cursor tumNotlar2() {
        try {
            //Sabitler.ROW_USER_ID + " = ? ", new String[]{String.valueOf(idKullanici)}
            Cursor c = db.query(Sabitler.TABLO_NOTLAR, null, null, null, null, null
                    , null, null);
            return c;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void notlariYenidenYaz(Not not) {
        ContentValues yeniDegerler = new ContentValues();

        String[] idArray = {String.valueOf(not.get_id())};
        yeniDegerler.put(Sabitler.ROW_NOT_GRUP, not.getGrup());
        yeniDegerler.put(Sabitler.ROW_NOT_BASLIK, not.getKonu());
        yeniDegerler.put(Sabitler.ROW_NOT_ICERIK, not.getIcerik());
        yeniDegerler.put(Sabitler.ROW_NOT_TARIH, not.getTrh());
        db.update(Sabitler.TABLO_NOTLAR, yeniDegerler, Sabitler.KEY_NOT_ID + " =?", idArray);
    }

}