package com.mirhoseini.bcg.user.profile;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mirhoseini.bcg.BR;
import com.mirhoseini.bcg.BcgApplication;
import com.mirhoseini.bcg.R;
import com.mirhoseini.bcg.base.BaseFragment;
import com.mirhoseini.bcg.user.model.ProfileModel;
import com.mirhoseini.bcg.util.AppConstants;
import com.mirhoseini.bcg.util.StateManager;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Created by Mohsen on 07/01/2017.
 */

public class ProfileFragment extends BaseFragment implements ProfileView {

    @Inject
    Context context;
    @Inject
    Resources resources;
    @Inject
    StateManager stateManager;
    @Inject
    ProfilePresenter presenter;

    @BindView(R.id.progress)
    ProgressBar progress;

    AppProfileModule module;
    ProfileModel profile;

    PublishSubject<Void> notifyPickImage = PublishSubject.create();
    PublishSubject<String> notifyMessage = PublishSubject.create();
    PublishSubject<Boolean> notifyOffline = PublishSubject.create();
    PublishSubject<Void> notifyLogout = PublishSubject.create();

    View view;
    ViewDataBinding binding;
    private Bitmap avatarBitmap;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @OnClick(R.id.avatar)
    public void avatarClick() {
        notifyPickImage.onNext(null);
    }

    @OnClick(R.id.logout)
    public void logoutClick() {
        stateManager.clearUser();
        updateProfile();
        notifyLogout.onNext(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (null == view || null != savedInstanceState) {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_profile, container, false);

            // inject views using ButterKnife
            ButterKnife.bind(this, view);
        }

        if (stateManager.isLoggedIn()) {
            updateProfile();
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        module.bind(this);
        presenter.bind(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        updateProfile();
    }

    @Override
    protected void injectDependencies(BcgApplication application) {
        application
                .getProfileSubComponent()
                .inject(this);

        module = application.getProfileModule();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        module.unbind();
        presenter.unbind();
    }

    public void setAvatar(Bitmap avatarBitmap) {
        this.avatarBitmap = avatarBitmap;
        sendAvatar();
    }

    private void sendAvatar() {
        if (null != avatarBitmap)
            presenter.setAvatar(convertBitmapToByteArray(avatarBitmap));
    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, AppConstants.AVATAR_THUMBNAIL_SIZE, AppConstants.AVATAR_THUMBNAIL_SIZE);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void showAvatarProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAvatarProgress() {
        progress.setVisibility(View.GONE);

    }

    @Override
    public void updateProfile() {
        profile = stateManager.loadUserProfile();

        if (null != profile) {
            binding = DataBindingUtil.bind(view);
            binding.setVariable(BR.profile, profile);
            binding.executePendingBindings();
        }
    }

    @Override
    public void showRetryMessage(Throwable throwable) {
        Timber.e(throwable, "Retry error!");

        Snackbar.make(view, resources.getString(R.string.retry_message), Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, view -> sendAvatar())
                .show();
    }

    @Override
    public void showAvatarError(AvatarException avatarException) {
        showMessage("Avatar file error!!\n" + avatarException.getMessage());
    }

    @Override
    public void showAvatarSizeError() {
        showMessage("Avatar image must be smaller than 1MB!!");
    }

    @Override
    public void showMessage(String message) {
        notifyMessage.onNext(message);
    }

    @Override
    public void showOfflineMessage(boolean isCritical) {
        notifyOffline.onNext(isCritical);
    }

    public Observable<Void> pickImageObservable() {
        return notifyPickImage.asObservable();
    }

    public Observable<String> messageObservable() {
        return notifyMessage.asObservable();
    }

    public Observable<Boolean> offlineObservable() {
        return notifyOffline.asObservable();
    }

    public Observable<Void> logoutObservable() {
        return notifyLogout.asObservable();
    }

}
