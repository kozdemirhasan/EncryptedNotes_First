package com.kozdemirhasan.encryptednotes.util;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kozdemirhasan.encryptednotes.database.UserDatabase;
import com.kozdemirhasan.encryptednotes.R;
import com.kozdemirhasan.encryptednotes.pojo.Crypt;
import com.kozdemirhasan.encryptednotes.pojo.MD5;
import com.kozdemirhasan.encryptednotes.pojo.Sabitler;

public class KullaniciKayitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.kullanicikayit);
        final EditText etPass = (EditText) findViewById(R.id.etParola);
        final EditText etPassTekrar = (EditText) findViewById(R.id.etParolaTekrar);
      //  final EditText etFakePass = (EditText) findViewById(R.id.etFakeParola);
     //   final EditText etFakePassTekrar = (EditText) findViewById(R.id.etFakeParolaTekrar);
        Button btnKayit = (Button) findViewById(R.id.btnKullaniciKayit);

        btnKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(30);

                String p1 = etPass.getText().toString().trim();
                String p2 = etPassTekrar.getText().toString().trim();
           //     String pf1 = etFakePass.getText().toString().trim();
            //    String pf2 = etFakePassTekrar.getText().toString().trim();

          /*      if (p1.equals("") || p2.equals("") || pf1.equals("") || pf2.equals("")) {
                    Toast.makeText(KullaniciKayitActivity.this,
                            "All fields must be filled", Toast.LENGTH_SHORT).show();
                } else if (!p1.toString().equals(p2.toString())) {
                    Toast.makeText(KullaniciKayitActivity.this,
                            "Parola ile Parola (tekrar) eşit olmalıdır ", Toast.LENGTH_SHORT).show();
                } else if (!pf1.equals(pf2)) {
                    Toast.makeText(KullaniciKayitActivity.this,
                            "Password must be the same as Password (repeat) ", Toast.LENGTH_SHORT).show();
                } else if (p1.toString().equals(pf1.toString())) {
                    Toast.makeText(KullaniciKayitActivity.this,
                            "Password with Fake Password cannot be the same!!!", Toast.LENGTH_SHORT).show();
                } else if (p1.toString().length() < 6 || pf1.toString().length() < 6){
                    Toast.makeText(KullaniciKayitActivity.this,
                            "Passwords must be at least 6 characters long.", Toast.LENGTH_SHORT).show();
                }
                */

                if (!p1.equals(p2)) {
                    Toast.makeText(KullaniciKayitActivity.this,
                            "Password must be the same as Password (repeat) ", Toast.LENGTH_SHORT).show();
                } else if (p1.toString().length() < 6){
                    Toast.makeText(KullaniciKayitActivity.this,
                            "Passwords must be at least 6 characters long.", Toast.LENGTH_SHORT).show();
                } else{
                    //Parolaları vt kaydet
                    UserDatabase db = new UserDatabase(KullaniciKayitActivity.this);
                    db.ac();
                    //parolayı ve fake parolayı parola key i ile aes-256 ya çevirip sonrada md5 ile dönüştürüp vt kaydediyoruz
                    Crypt crypt = new Crypt();
                    long x = 0;
                    try {
                        x = db.kullaniciKayit(MD5.md5Sifrele(crypt.encrypt(p1, p1)), MD5.md5Sifrele(crypt.encrypt("12345", "12345")));//fake pasword olarak 12345 yazıldı
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    db.kapat();
                    if (x == -1) {
                        Toast.makeText(KullaniciKayitActivity.this,
                                "An error occurred!!", Toast.LENGTH_SHORT).show();
                    } else {
                        //ana sayfaya git
                        //Sabitler deki loginPassword değerini set et

                        try {
                            Sabitler.loginPassword = p1;
                            Sabitler.yaziBoyutu = 18;
                            Sabitler.PASS_MD5 = MD5.md5Sifrele(crypt.encrypt(p1, p1));
                          //  Sabitler.FAKE_PASS_MD5 = MD5.md5Sifrele(crypt.encrypt(pf1, pf1));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Intent i = new Intent(KullaniciKayitActivity.this,
                                NotlarActivity.class);
                        startActivity(i);
                        finish();
                    }


                }
            }
        });
    }
}
