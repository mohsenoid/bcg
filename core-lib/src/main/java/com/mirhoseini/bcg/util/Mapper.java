package com.mirhoseini.bcg.util;

import com.mirhoseini.bcg.domain.model.ProfileResponse;
import com.mirhoseini.bcg.user.model.ProfileModel;

/**
 * Created by Mohsen on 06/01/2017.
 */

public class Mapper {

    public static ProfileModel mapLoginResponseToUserModel(ProfileResponse profileResponse) {
        return ProfileModel.create(profileResponse.getEmail(), profileResponse.getAvatarUrl());
    }

}
