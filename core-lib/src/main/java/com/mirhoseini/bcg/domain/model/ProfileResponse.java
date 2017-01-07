package com.mirhoseini.bcg.domain.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mohsen on 06/01/2017.
 */

public class ProfileResponse {

    @SerializedName("email")
    private String email;
    @SerializedName("avatar_url")
    private String avatarUrl;

    public ProfileResponse() {
    }

    public ProfileResponse(String email, String avatarUrl) {
        this.email = email;
        this.avatarUrl = avatarUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
