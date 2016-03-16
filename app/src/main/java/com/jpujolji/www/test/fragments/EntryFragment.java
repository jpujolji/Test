/*
 * Copyright (c) 2016. Jorge Pujol - Todos los derechos reservados.
 * Escrito por Jorge Pujol <jpujolji@gmail.com>, marzo 2016.
 */

package com.jpujolji.www.test.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jpujolji.www.test.DetailEntryActivity;
import com.jpujolji.www.test.R;
import com.jpujolji.www.test.adapters.EntryListAdapter;
import com.jpujolji.www.test.database.Database;
import com.jpujolji.www.test.models.Entry;

import java.util.List;

public class EntryFragment extends Fragment implements EntryListAdapter.EntryListInterface {

    RecyclerView rvEntries;
    View rootView;
    List<Entry> entries;

    EntryListAdapter mAdapter;

    public static EntryFragment newInstance() {
        return new EntryFragment();
    }

    public EntryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_entry, container, false);
        rvEntries = (RecyclerView) rootView.findViewById(R.id.rvEntries);

        rvEntries.setHasFixedSize(true);
        if (getResources().getBoolean(R.bool.isTablet)) {
            rvEntries.setLayoutManager(new GridLayoutManager(rootView.getContext(), 2));
        } else {
            rvEntries.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        }
        rvEntries.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (mAdapter != null) {
                    mAdapter.setLockedAnimations(true);
                }
            }
        });

        return rootView;
    }

    public void setLIst(int idCategory) {
        Database database = new Database(rootView.getContext());
        try {
            database.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        entries = database.getEntries(idCategory);

        mAdapter = new EntryListAdapter(getContext(), entries, this);
        rvEntries.setAdapter(mAdapter);
        rvEntries.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onEntryClick(View view, int position) {
        Intent intent = new Intent(rootView.getContext(), DetailEntryActivity.class);
        intent.putExtra(DetailEntryActivity.ID_ENTRY, entries.get(position).id);

        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(),
                new Pair<>(view.findViewById(R.id.tvTitle),
                        DetailEntryActivity.VIEW_NAME),
                new Pair<>(view.findViewById(R.id.tvArtist),
                        DetailEntryActivity.VIEW_ARTIST),
                new Pair<>(view.findViewById(R.id.ivImage),
                        DetailEntryActivity.VIEW_IMAGE));
        ActivityCompat.startActivity(getActivity(), intent, activityOptions.toBundle());
    }
}