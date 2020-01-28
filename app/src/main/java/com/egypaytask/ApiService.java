package com.egypaytask;

import java.lang.reflect.Array;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/")
    Observable<Array> sendData(@Body Object o);
}
