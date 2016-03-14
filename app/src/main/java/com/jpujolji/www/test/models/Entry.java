/*
 * Copyright (c) 2016. Jorge Pujol - Todos los derechos reservados.
 * Escrito por Jorge Pujol <jpujolji@gmail.com>, marzo 2016.
 */

package com.jpujolji.www.test.models;

import org.json.JSONObject;

public class Entry {

    public String title, summary, idCategory, image, artist;
    public int id;

    public Category category;

    public Entry(String mTitle, String mSummary, Category mCategory, String mImage, String mArtist) {
        title = mTitle;
        summary = mSummary;
        category = mCategory;
        image = mImage;
        artist = mArtist;
    }

    public Entry(int mId, String mTitle, String mSummary, String mImage, String mArtist) {
        id = mId;
        title = mTitle;
        summary = mSummary;
        image = mImage;
        artist = mArtist;
    }

    public static Entry parseObject(JSONObject jsonObject) {

        Category category = new Category(jsonObject.optJSONObject("category").optJSONObject("attributes").optInt("im:id"),
                jsonObject.optJSONObject("category").optJSONObject("attributes").optString("label"));

        Entry entry = new Entry(jsonObject.optJSONObject("im:name").optString("label"),
                jsonObject.optJSONObject("summary").optString("label"), category,
                jsonObject.optJSONArray("im:image").optJSONObject(2).optString("label"),
                jsonObject.optJSONObject("im:artist").optString("label"));
        return entry;
    }
}