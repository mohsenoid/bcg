package com.mirhoseini.bcg.util;

import com.mirhoseini.bcg.user.model.ProfileModel;

/**
 * Created by Mohsen on 06/01/2017.
 */

public interface StateManager {

    boolean isConnect();

    boolean saveUserToken(String token);

    String loadUserToken();

    boolean saveUserId(int userId);

    int loadUserId();

    boolean isLoggedIn();

    boolean saveUserProfile(ProfileModel profile);

    ProfileModel loadUserProfile();

    boolean updateAvatarUrl(String avatarUrl);

    void clearUser();

}
