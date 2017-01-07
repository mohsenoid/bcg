package com.mirhoseini.bcg.user.profile;

import com.mirhoseini.bcg.base.BasePresenter;

/**
 * Created by Mohsen on 06/01/2017.
 */

interface ProfilePresenter extends BasePresenter<ProfileView> {

    void setAvatar(byte[] avatarByteArray);

    boolean isAvatarFileSizeValid(byte[] avatar);
}
