package com.mirhoseini.bcg.domain.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mohsen on 06/01/2017.
 */

public class AvatarResponse {

    @SerializedName("avatar_url")
    private String avatarUrl;

    public AvatarResponse() {
    }

    public AvatarResponse(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
