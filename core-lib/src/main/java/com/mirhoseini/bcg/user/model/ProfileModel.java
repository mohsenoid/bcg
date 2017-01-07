package com.mirhoseini.bcg.user.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.Serializable;

/**
 * Created by Mohsen on 06/01/2017.
 */

@AutoValue
public abstract class ProfileModel implements Serializable {

    public static ProfileModel create(String email, String avatarUrl) {
        return new AutoValue_ProfileModel(email, avatarUrl);
    }

    public static TypeAdapter<ProfileModel> typeAdapter(Gson gson) {
        return new AutoValue_ProfileModel.GsonTypeAdapter(gson);
    }

    abstract public String getEmail();

    abstract public String getAvatarUrl();

}
