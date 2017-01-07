package com.mirhoseini.bcg.user.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mirhoseini.bcg.ApplicationComponent;
import com.mirhoseini.bcg.BcgApplication;
import com.mirhoseini.bcg.R;
import com.mirhoseini.bcg.base.BaseActivity;
import com.mirhoseini.bcg.user.profile.ProfileException;
import com.mirhoseini.bcg.util.StateManager;
import com.mirhoseini.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import timber.log.Timber;

/**
 * Created by Mohsen on 07/01/2017.
 */

public class LoginActivity extends BaseActivity implements LoginView {

    public static final int LOGIN_IME_ACTION_ID = 1;

    // inject objects via Dagger
    @Inject
    Context context;
    @Inject
    Resources resources;
    @Inject
    StateManager stateManager;
    @Inject
    LoginPresenter presenter;

    // inject views via ButterKnife
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.login_container)
    ViewGroup loginContainer;

    private AppLoginModule module;
    private AlertDialog internetDialog;

    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        return intent;
    }

    @OnEditorAction(R.id.password)
    boolean onPasswordAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == LOGIN_IME_ACTION_ID || id == EditorInfo.IME_NULL) {
            attemptLogin(textView);
            return true;
        }
        return false;
    }

    @OnClick(R.id.login)
    void onLoginClick(View view) {
        attemptLogin(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        module.bind(this);
        presenter.bind(this);

        // inject views using ButterKnife
        ButterKnife.bind(this);

        setupToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mayDismissOfflineMessageDialog();
    }

    private void mayDismissOfflineMessageDialog() {
        if (null != internetDialog)
            internetDialog.dismiss();
    }

    @Override
    protected void injectDependencies(BcgApplication application, ApplicationComponent component) {
        application
                .getLoginSubComponent()
                .inject(this);

        module = application.getLoginModule();
    }

    @Override
    protected void releaseSubComponents(BcgApplication application) {
        application.releaseLoginSubComponent();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.logo);
        getSupportActionBar().setTitle(R.string.title_activity_login);
    }

    private void attemptLogin(View view) {
        Timber.d("Attempt to login by: %s", view.toString());

        // Reset errors
        email.setError(null);
        password.setError(null);

        String emailValue = this.email.getText().toString().trim();
        String passwordValue = this.password.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password
        if (!presenter.isPasswordValid(passwordValue)) {
            this.password.setError(getString(R.string.error_invalid_password));
            focusView = this.password;
            cancel = true;
        }

        // Check for a valid email address
        if (!presenter.isEmailValid(emailValue)) {
            this.email.setError(getString(R.string.error_invalid_email));
            focusView = this.email;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // hide keyboard
            Utils.hideKeyboard(this);

            presenter.login(emailValue, passwordValue);
        }
    }

    @Override
    public void showProgress() {
        showAnimatedProgress(true);
    }

    @Override
    public void hideProgress() {
        showAnimatedProgress(false);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showAnimatedProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            loginContainer.setVisibility(show ? View.GONE : View.VISIBLE);
            loginContainer.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginContainer.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progress.setVisibility(show ? View.VISIBLE : View.GONE);
            progress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progress.setVisibility(show ? View.VISIBLE : View.GONE);
            loginContainer.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void userLoggedIn() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void showRetryMessage(Throwable throwable) {
        Timber.e(throwable, "Retry error!");

        Snackbar.make(loginContainer, resources.getString(R.string.retry_message), Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, this::attemptLogin)
                .show();
    }

    @Override
    public void showInvalidUsernamePassword(LoginException throwable) {
        showMessage("Invalid Username and Password!!");
    }

    @Override
    public void showInvalidProfile(ProfileException throwable) {
        showMessage("Unable to load your profile!!");
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
    protected void onDestroy() {
        super.onDestroy();

        module.unbind();
        presenter.unbind();
    }

}