package com.cky.rx.network;


import com.cky.rx.model.BookDetailResult;
import com.cky.rx.model.SearchBookResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by cuikangyuan on 16/5/12.
 */
public interface IteBooksApi {

    @GET("search/{query}")
    Observable<SearchBookResult> getBooksByCategory(@Path("query") String query);

    @GET("search/{query}/page/{number}")
    Observable<SearchBookResult> getBooksByPage(@Path("query") String query, @Path("number") String number);

    @GET("book/{id}")
    Observable<BookDetailResult> getBookDetail(@Path("id") String id);
}
