package com.mirhoseini.bcg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.mirhoseini.bcg.base.BaseActivity;
import com.mirhoseini.bcg.user.login.LoginActivity;
import com.mirhoseini.bcg.user.profile.ProfileFragment;
import com.mirhoseini.bcg.util.ImagePicker;
import com.mirhoseini.bcg.util.StateManager;
import com.mirhoseini.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by Mohsen on 07/01/2017.
 */

public class MainActivity extends BaseActivity {

    public static final String TAG_PROFILE_FRAGMENT = "profile_fragment";
    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    private static final int LOGIN_REQUEST_CODE = 2;

    // inject objects via Dagger
    @Inject
    Context context;
    @Inject
    StateManager stateManager;

    // inject views via ButterKnife
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    ProfileFragment profileFragment;
    CompositeSubscription subscriptions;
    private AlertDialog internetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // inject views using ButterKnife
        ButterKnife.bind(this);

        setupToolbar();

        if (null == savedInstanceState) {
            createFragments();
            attachFragments();
        } else {
            findFragments();
        }

        binding();

        Timber.d("Main Activity Created");
    }

    private void binding() {
        if (null == subscriptions || subscriptions.isUnsubscribed())
            subscriptions = new CompositeSubscription();

        subscriptions.addAll(
                profileFragment.pickImageObservable()
                        .subscribe(aVoid -> pickImage()),
                profileFragment.messageObservable()
                        .subscribe(this::showMessage),
                profileFragment.offlineObservable()
                        .subscribe(this::showOfflineMessage),
                profileFragment.logoutObservable()
                        .subscribe(aVoid -> checkUserStateAndLogin())
        );
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.logo);
        getSupportActionBar().setTitle(R.string.app_name);
    }

    private void createFragments() {
        profileFragment = ProfileFragment.newInstance();
    }

    private void attachFragments() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.profile_container, profileFragment, TAG_PROFILE_FRAGMENT);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void findFragments() {
        profileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentByTag(TAG_PROFILE_FRAGMENT);
    }

    @Override
    protected void injectDependencies(BcgApplication application, ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mayDismissOfflineMessageDialog();

        checkUserStateAndLogin();
    }

    private void mayDismissOfflineMessageDialog() {
        if (null != internetDialog)
            internetDialog.dismiss();
    }

    private void checkUserStateAndLogin() {
        if (!stateManager.isLoggedIn()) {
            startActivityForResult(LoginActivity.newIntent(this), LOGIN_REQUEST_CODE);
        }
    }

    public void showMessage(String message) {
        Timber.d("Showing Message: %s", message);

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void showOfflineMessage(boolean isCritical) {
        Timber.d("Showing Offline Message");

        if (isCritical) {
            internetDialog = Utils.showNoInternetConnectionDialog(this, isCritical);
        } else {
            Snackbar.make(toolbar, R.string.offline_message, Snackbar.LENGTH_LONG)
                    .setAction(R.string.go_online, v -> startActivity(new Intent(
                            Settings.ACTION_WIFI_SETTINGS)))
                    .setActionTextColor(Color.GREEN)
                    .show();
        }
    }

    @Override
    protected void releaseSubComponents(BcgApplication application) {
        if (null != subscriptions && !subscriptions.isUnsubscribed())
            subscriptions.unsubscribe();

        application.releaseProfileSubComponent();
    }

    private void pickImage() {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap avatar;
                    if (data.getExtras() == null) {
                        avatar = ImagePicker.getImageFromResult(this, data);
                    } else {
                        avatar = (Bitmap) data.getExtras().get("data");
                    }

                    profileFragment.setAvatar(avatar);
                }
                break;
            case LOGIN_REQUEST_CODE:
                if (resultCode != Activity.RESULT_OK)
                    finish();
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}
