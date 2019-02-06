package com.example.rpl2016_11.crud.API;

import com.example.rpl2016_11.crud.model.Value;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RegisterAPI {
    @FormUrlEncoded
    @POST("insert.php")
    Call<Value> daftar(@Field("id") String id,
                       @Field("nama") String nama,
                       @Field("jurusan") String jurusan,
                       @Field("jk") String jk,
                       @Field("pasword")String pasword);

    @GET("view.php")
    Call<Value> view();

    @FormUrlEncoded
    @POST("search.php")
    Call<Value> search(@Field("search")String search);

    @FormUrlEncoded
    @POST("delete.php")
    Call<Void> hapus(@Field("id")int id);

    @FormUrlEncoded
    @POST("update.php")
    Call<Void> ubah (@Field("id") int id,
                      @Field("nama") String nama,
                      @Field("jurusan") String jurusan,
                      @Field("jk") String jk,
                      @Field("pasword")String pasword);

}
