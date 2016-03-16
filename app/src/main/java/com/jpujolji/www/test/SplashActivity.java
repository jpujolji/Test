/*
 * Copyright (c) 2016. Jorge Pujol - Todos los derechos reservados.
 * Escrito por Jorge Pujol <jpujolji@gmail.com>, marzo 2016.
 */

package com.jpujolji.www.test;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jpujolji.www.test.database.Database;
import com.jpujolji.www.test.interfaces.HttpInterface;
import com.jpujolji.www.test.models.Entry;
import com.jpujolji.www.test.network.HttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity implements HttpInterface {

    HttpClient httpClient;
    Utils utils;
    ProgressDialog progressDialog;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        httpClient = new HttpClient(this, SplashActivity.this);
        utils = new Utils();

        database = new Database(SplashActivity.this);
        try {
            database = database.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        progressDialog = new ProgressDialog(SplashActivity.this);
        progressDialog.setTitle("Cargando...");
        progressDialog.setMessage("Descargando datos, espere");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                getData();
            }
        }.start();
    }

    void getData() {
        if (utils.checkNetwork(SplashActivity.this)) {
            httpClient.httpRequest();
            progressDialog.show();
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);

            builder.setMessage("No se detectó conexión a Internet")
                    .setCancelable(false)
                    .setTitle("Error de conexión");

            builder.setNegativeButton("Opciones",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent myIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);

                            startActivity(myIntent);
                            dialog.cancel();
                        }
                    });

            builder.setPositiveButton("Continuar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void onProgress(long progress) {

    }

    @Override
    public void onSuccess(JSONObject response) {

        JSONObject jsonFeed = response.optJSONObject("feed");
        JSONArray jsonEntries = jsonFeed.optJSONArray("entry");
        List<Entry> entries = new ArrayList<>();
        for (int x = 0; x < jsonEntries.length(); x++) {
            JSONObject jsonEntry = jsonEntries.optJSONObject(x);
            entries.add(Entry.parseObject(jsonEntry));
        }
        database.insertEntries(entries);
        progressDialog.dismiss();
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    @Override
    public void onFailed(JSONObject errorResponse) {
        progressDialog.dismiss();
    }
}
