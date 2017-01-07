package com.mirhoseini.bcg.user.profile;

import com.mirhoseini.bcg.domain.model.AvatarResponse;

/**
 * Created by Mohsen on 06/01/2017.
 */

class AvatarException extends Throwable {
    private final AvatarResponse avatarResponse;

    // TODO: 07/01/2017 Avatar Response can contain more information about error cause
    AvatarException(AvatarResponse avatarResponse) {

        this.avatarResponse = avatarResponse;

    }

    public AvatarResponse getAvatarResponse() {
        return avatarResponse;
    }
}
