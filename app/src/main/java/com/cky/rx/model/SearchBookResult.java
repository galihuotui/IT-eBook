package com.cky.rx.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cuikangyuan on 16/5/8.
 */
public class SearchBookResult {

    public @SerializedName("Error") String Error;
    public @SerializedName("Time") String Time;
    public @SerializedName("Total") String Total;
    public @SerializedName("Page") String Page;
    public @SerializedName("Books") List<BookItem> Books;
}
