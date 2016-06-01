package com.cky.rx.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cuikangyuan on 16/5/12.
 */
public class BookDetailResult {
    public @SerializedName("Error")String Error;
    public @SerializedName("Time") String Time;
    public @SerializedName("ID") String ID;
    public @SerializedName("Title") String Title;
    public @SerializedName("SubTitle")  String SubTitle;
    public @SerializedName("Description") String Description;
    public @SerializedName("Author") String Author;
    public @SerializedName("ISBN") String ISBN;
    public @SerializedName("Year") String Year;
    public @SerializedName("Page") String Page;
    public @SerializedName("Publisher") String Publisher;
    public @SerializedName("Image") String Image;
    public @SerializedName("Download") String Download;
}
