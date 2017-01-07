package com.mirhoseini.bcg.domain.client;

import com.mirhoseini.bcg.domain.model.AvatarResponse;
import com.mirhoseini.bcg.domain.model.LoginResponse;
import com.mirhoseini.bcg.domain.model.ProfileResponse;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Mohsen on 06/01/2017.
 */

public interface Api {
    String EMAIL = "email";
    String PASSWORD = "password";
    String TOKEN = "token";
    String USER_ID = "userid";
    String AVATAR = "avatar";

    // http://91.109.18.74/session/new/
    @POST("sessions/new")
    @FormUrlEncoded
    Observable<Response<LoginResponse>> login(
            @Field(EMAIL) String email,
            @Field(PASSWORD) String password);

    // http://91.109.18.74/users/:userid
    @GET("users/{userid}")
    Observable<Response<ProfileResponse>> getProfile(
            @Header(TOKEN) String token,
            @Path(USER_ID) int userId);

    // http://91.109.18.74/users/:userid/avatar
    @POST("users/{userid}/avatar")
    @FormUrlEncoded
    Observable<Response<AvatarResponse>> setAvatar(
            @Header(TOKEN) String token,
            @Path(USER_ID) int userId,
            @Field(AVATAR) String avatarBase64);
}
