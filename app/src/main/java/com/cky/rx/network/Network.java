package com.cky.rx.network;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * Created by cuikangyuan on 16/5/7.
 */
public class Network {

    private static IteBooksApi iteBooksApi;

    private static String IT_EBOOKS_BASE_URL = "http://it-ebooks-api.info/v1/";



    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();


    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public static IteBooksApi getIteBooksApi() {
        if (iteBooksApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(IT_EBOOKS_BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();

            iteBooksApi = retrofit.create(IteBooksApi.class);
        }
        return iteBooksApi;
    }




}
