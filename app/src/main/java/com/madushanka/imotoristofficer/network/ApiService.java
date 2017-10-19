package com.madushanka.imotoristofficer.network;





import com.madushanka.imotoristofficer.entities.AccessToken;
import com.madushanka.imotoristofficer.entities.Motorist;
import com.madushanka.imotoristofficer.entities.Offence;
import com.madushanka.imotoristofficer.entities.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface ApiService {

    @POST("register")
    @FormUrlEncoded
    Call<AccessToken> register(@Field("name") String name, @Field("email") String email, @Field("password") String password);

    @POST("login")
    @FormUrlEncoded
    Call<AccessToken> login(@Field("username") String username, @Field("password") String password);

    @POST("logout")
    Call<AccessToken> logout();

    @GET("logged-in-user")
    Call<User> user();


    @GET("offences")
    Call<List<Offence>> offences();

    @POST("get-user")
    @FormUrlEncoded
    Call<Motorist> motorist(@Field("license_no") String license_no);

    @POST("refresh")
    @FormUrlEncoded
    Call<AccessToken> refresh(@Field("refresh_token") String refreshToken);


}
