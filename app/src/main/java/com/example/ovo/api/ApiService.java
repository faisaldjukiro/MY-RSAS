package com.example.ovo.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("cekpeg.php")
    Call<CheckKdPegawaiResponse> checkKdPegawai(
            @Field("kd_peg") String kdPegawai
    );
    @FormUrlEncoded
    @POST("registrasi.php")
    Call<UpdatePasswordResponse> updatePassword(
            @Field("kd_peg") String kdPegawai,
            @Field("password") String password
    );

//    @Headers({
//            "Content-Type: application/json"
//    })
    @FormUrlEncoded
    @POST("jasasatu.php")
    Call<JasaSatuResponse> getData(
            @Header("token") String token,
            @Field("kd_peg") String kdPeg
    );
}