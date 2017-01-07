package com.mirhoseini.bcg.user.profile;

import com.mirhoseini.bcg.base.BaseView;

/**
 * Created by Mohsen on 06/01/2017.
 */

interface ProfileView extends BaseView {

    void showAvatarProgress();

    void hideAvatarProgress();

    void updateProfile();

    void showRetryMessage(Throwable throwable);

    void showAvatarError(AvatarException avatarException);

    void showAvatarSizeError();
}
