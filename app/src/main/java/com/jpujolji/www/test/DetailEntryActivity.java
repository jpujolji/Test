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
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.MenuItem;
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
    TextView tvTitle, tvArtist, tvPrice, tvRights, tvLink, tvSummary;
    ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_entry);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = new Database(DetailEntryActivity.this);
        try {
            database.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle bundle = getIntent().getExtras();
        idEntry = bundle.getInt(ID_ENTRY, 0);
        entry = database.getEntry(idEntry);

        getSupportActionBar().setTitle(entry.title);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvArtist = (TextView) findViewById(R.id.tvArtist);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvLink = (TextView) findViewById(R.id.tvLink);
        tvSummary = (TextView) findViewById(R.id.tvSummary);
        tvRights = (TextView) findViewById(R.id.tvRights);
        ivImage = (ImageView) findViewById(R.id.ivImage);

        ViewCompat.setTransitionName(tvTitle, VIEW_NAME);
        ViewCompat.setTransitionName(tvArtist, VIEW_ARTIST);
        ViewCompat.setTransitionName(ivImage, VIEW_IMAGE);

        loadItem();

    }

    private void loadItem() {
        tvTitle.setText(entry.title);
        tvArtist.setText(entry.artist);

        if (entry.price != null) {
            if (!entry.price.isEmpty()) {
                if (Double.parseDouble(entry.price) > 0) {
                    tvPrice.setText(entry.price);
                } else {
                    tvPrice.setText("FREE");
                }
            }
        }

        tvRights.setText(entry.rights);
        tvLink.setText(entry.link);
        tvSummary.setText(entry.summary);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}