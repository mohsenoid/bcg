package com.mirhoseini.bcg.user;

import com.mirhoseini.bcg.base.BaseInteractor;
import com.mirhoseini.bcg.domain.model.AvatarResponse;
import com.mirhoseini.bcg.domain.model.LoginResponse;
import com.mirhoseini.bcg.domain.model.ProfileResponse;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by Mohsen on 06/01/2017.
 */

public interface UserInteractor extends BaseInteractor {

    Observable<Response<LoginResponse>> login(String email, String password);

    Observable<Response<ProfileResponse>> getProfile(String token, int userId);

    Observable<Response<AvatarResponse>> setAvatar(String token, int userId, String avatarBase64);

}
