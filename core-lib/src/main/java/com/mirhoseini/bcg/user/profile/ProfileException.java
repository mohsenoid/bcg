package com.mirhoseini.bcg.user.profile;

import com.mirhoseini.bcg.domain.model.ProfileResponse;

/**
 * Created by Mohsen on 06/01/2017.
 */

public class ProfileException extends Exception {
    private final ProfileResponse ProfileResponse;

    // TODO: 07/01/2017 Profile Response can contain more information about error cause
    public ProfileException(ProfileResponse ProfileResponse) {

        this.ProfileResponse = ProfileResponse;
    }

    public com.mirhoseini.bcg.domain.model.ProfileResponse getProfileResponse() {
        return ProfileResponse;
    }
}
