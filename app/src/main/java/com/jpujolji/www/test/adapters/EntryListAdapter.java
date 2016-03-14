/*
 * Copyright (c) 2016. Jorge Pujol - Todos los derechos reservados.
 * Escrito por Jorge Pujol <jpujolji@gmail.com>, marzo 2016.
 */

package com.jpujolji.www.test.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpujolji.www.test.R;
import com.jpujolji.www.test.models.Entry;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EntryListAdapter extends RecyclerView.Adapter<EntryListAdapter.MyViewHolder> {

    List<Entry> mEntries;
    EntryListInterface mListInterface;
    Context mContext;

    public EntryListAdapter(Context context, List<Entry> entries, EntryListInterface clientListInterface) {
        mContext = context;
        mEntries = entries;
        mListInterface = clientListInterface;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View rootView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list_entry, viewGroup, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Entry entry = mEntries.get(position);
        holder.tvName.setText(entry.title);
        holder.tvArtist.setText(entry.artist);
        Picasso.with(mContext).load(entry.image).into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return mEntries.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvName, tvArtist;
        public ImageView ivImage;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvTitle);
            tvArtist = (TextView) itemView.findViewById(R.id.tvArtist);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListInterface.onEntryClick(itemView, getAdapterPosition());
        }
    }

    public interface EntryListInterface {
        void onEntryClick(View view, int position);
    }
}
