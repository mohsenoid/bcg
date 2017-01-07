package com.mirhoseini.bcg.domain.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mohsen on 06/01/2017.
 */

public class LoginResponse {

    @SerializedName("userid")
    private int userId;
    @SerializedName("token")
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(int userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
