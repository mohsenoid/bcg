/*
 * Copyright (c) 2016 Karina Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.mirhoseini.bcg.util;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.mirhoseini.appsettings.AppSettings;
import com.mirhoseini.bcg.user.model.ProfileModel;
import com.mirhoseini.utils.Utils;

import javax.inject.Inject;

/**
 * Created by Mohsen on 07/01/2017.
 */

public class StateManagerImpl implements StateManager {

    private Context context;

    @Inject
    StateManagerImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean isConnect() {
        return Utils.isConnected(context);
    }

    @Override
    public boolean saveUserToken(String token) {
        return AppSettings.setValue(context, AppConstants.KEY_USER_TOKEN, token);
    }

    @Override
    public String loadUserToken() {
        return AppSettings.getString(context, AppConstants.KEY_USER_TOKEN);
    }

    @Override
    public boolean saveUserId(int userId) {
        return AppSettings.setValue(context, AppConstants.KEY_USER_ID, userId);
    }

    @Override
    public int loadUserId() {
        return AppSettings.getInt(context, AppConstants.KEY_USER_ID);
    }

    @Override
    public void clearUser() {
        AppSettings.clearValue(context, AppConstants.KEY_USER_TOKEN);
        AppSettings.clearValue(context, AppConstants.KEY_USER_ID);
        AppSettings.clearValue(context, AppConstants.KEY_USER_PROFILE);
    }

    @Override
    public boolean isLoggedIn() {
        return !TextUtils.isEmpty(loadUserToken());
    }

    @Override
    public boolean saveUserProfile(ProfileModel profile) {
        return AppSettings.setValue(context, AppConstants.KEY_USER_PROFILE, ProfileModel.typeAdapter(new Gson()).toJson(profile));
    }

    @Override
    public ProfileModel loadUserProfile() {
        try {
            return ProfileModel.typeAdapter(new Gson()).fromJson(AppSettings.getString(context, AppConstants.KEY_USER_PROFILE));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean updateAvatarUrl(String avatarUrl) {
        ProfileModel profile = loadUserProfile();
        return saveUserProfile(ProfileModel.create(profile.getEmail(), avatarUrl));
    }
}
