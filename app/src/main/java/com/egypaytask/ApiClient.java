package com.egypaytask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {


        Retrofit retrofit = null;

        ApiService RETROFIT_CLIENT= null ;


        ApiService getInstance() {
                //if REST_CLIENT is null then set-up again.
                if (RETROFIT_CLIENT == null) {
                        setupRestClient();
                }
                return RETROFIT_CLIENT;
        }

        private void setupRestClient() {
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                 retrofit =new  Retrofit.Builder()
                        .baseUrl("http://101.amrbdr.com/")
                        .client(getOkHttp())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                         .addConverterFactory(GsonConverterFactory.create(gson))

                         .build();
                RETROFIT_CLIENT = retrofit.create(ApiService.class);
        }


        private OkHttpClient getOkHttp(){
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                return new OkHttpClient.Builder()
                        .readTimeout(30, TimeUnit.SECONDS)
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .addInterceptor(interceptor)
                        .addInterceptor(chain -> {
                                Request chainRequest = chain.request();
                                Request authenticatedRequest = chainRequest.newBuilder().build();
                                return chain.proceed(authenticatedRequest);
                        })
                        .build();
        }



}