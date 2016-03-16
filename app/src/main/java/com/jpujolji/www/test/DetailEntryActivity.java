/*
 * Copyright (c) 2016. Jorge Pujol - Todos los derechos reservados.
 * Escrito por Jorge Pujol <jpujolji@gmail.com>, marzo 2016.
 */

package com.jpujolji.www.test;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpujolji.www.test.database.Database;
import com.jpujolji.www.test.models.Entry;
import com.squareup.picasso.Picasso;

public class DetailEntryActivity extends AppCompatActivity {

    public static final String ID_ENTRY = "detail:id";
    public static final String VIEW_NAME = "detail:name";
    public static final String VIEW_ARTIST = "detail:artist";
    public static final String VIEW_IMAGE = "detail:image";

    int idEntry;

    Entry entry;
    Database database;
    TextView tvTitle, tvArtist;
    ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_entry);

        database = new Database(DetailEntryActivity.this);
        try {
            database.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle bundle = getIntent().getExtras();
        idEntry = bundle.getInt(ID_ENTRY, 0);
        entry = database.getEntry(idEntry);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvArtist = (TextView) findViewById(R.id.tvArtist);
        ivImage = (ImageView) findViewById(R.id.ivImage);

        ViewCompat.setTransitionName(tvTitle, VIEW_NAME);
        ViewCompat.setTransitionName(tvArtist, VIEW_ARTIST);
        ViewCompat.setTransitionName(ivImage, VIEW_IMAGE);

        loadItem();

    }

    private void loadItem() {
        tvTitle.setText(entry.title);
        tvArtist.setText(entry.artist);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && addTransitionListener()) {
            loadThumbnail();
        } else {
            loadFullSizeImage();
        }
    }

    private void loadThumbnail() {
        Picasso.with(DetailEntryActivity.this)
                .load(entry.image)
                .noFade()
                .into(ivImage);
    }

    private void loadFullSizeImage() {
        Picasso.with(DetailEntryActivity.this)
                .load(entry.image)
                .noFade()
                .noPlaceholder()
                .into(ivImage);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean addTransitionListener() {
        final Transition transition = getWindow().getSharedElementEnterTransition();

        if (transition != null) {
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    loadFullSizeImage();
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionStart(Transition transition) {
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(Transition transition) {
                }

                @Override
                public void onTransitionResume(Transition transition) {
                }
            });
            return true;
        }
        return false;
    }
}
