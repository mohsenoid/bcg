package com.mirhoseini.bcg.user.login;

import com.mirhoseini.bcg.domain.client.Api;
import com.mirhoseini.bcg.domain.model.LoginResponse;
import com.mirhoseini.bcg.domain.model.ProfileResponse;
import com.mirhoseini.bcg.user.UserInteractor;
import com.mirhoseini.bcg.user.UserInteractorImpl;
import com.mirhoseini.bcg.user.model.ProfileModel;
import com.mirhoseini.bcg.user.profile.ProfileException;
import com.mirhoseini.bcg.util.SchedulerProvider;
import com.mirhoseini.bcg.util.StateManager;

import org.junit.Before;
import org.junit.Test;

import retrofit2.Response;
import rx.Observable;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by Mohsen on 08/01/2017.
 */

public class LoginPresenterTest {

    private static final int TEST_USER_ID = 1234;
    private static final String TEST_TOKEN = "abcd1234";
    private static final String TEST_EMAIL = "msn.mirhoseini@gmail.com";
    private static final String TEST_AVATAR = "http://mirhoseini.com/wp-content/uploads/2017/01/avatar.jpg";
    private static final String TEST_PASSWORD = "********";

    private Api api;
    private SchedulerProvider scheduler;
    private UserInteractor interactor;
    private StateManager stateManager;
    private LoginPresenter presenter;

    private LoginView view;

    private ProfileModel expectedResult;

    @Before
    public void setUp() {
        api = mock(Api.class);
        scheduler = mock(SchedulerProvider.class);
        stateManager = mock(StateManager.class);
        view = mock(LoginView.class);

        // Set up the stub we want to return in the mock
        LoginResponse loginResponse = new LoginResponse(TEST_USER_ID, TEST_TOKEN);
        Response<LoginResponse> loginResult = Response.success(loginResponse);

        ProfileResponse profileResponse = new ProfileResponse(TEST_EMAIL, TEST_AVATAR);
        Response<ProfileResponse> profileResult = Response.success(profileResponse);

        expectedResult = ProfileModel.create(TEST_EMAIL, TEST_AVATAR);

        // mock scheduler to run immediately
        when(scheduler.mainThread())
                .thenReturn(Schedulers.immediate());
        when(scheduler.backgroundThread())
                .thenReturn(Schedulers.immediate());

        // mock state manager
        when(stateManager.isConnect())
                .thenReturn(true);

        // mock api result with expected result
        when(api.login(eq(TEST_EMAIL), eq(TEST_PASSWORD)))
                .thenReturn(Observable.just(loginResult));
        when(api.getProfile(eq(TEST_TOKEN), eq(TEST_USER_ID)))
                .thenReturn(Observable.just(profileResult));

        // create a real interactor and presenter using mocked api, scheduler and stateManager
        interactor = new UserInteractorImpl(api, scheduler);
        presenter = new LoginPresenterImpl(scheduler, interactor, stateManager);
        presenter.bind(view);
    }

    @Test
    public void testLogin() {
        presenter.login(TEST_EMAIL, TEST_PASSWORD);

        // check if save methods called with expected result
        verify(stateManager, times(1)).saveUserToken(TEST_TOKEN);
        verify(stateManager, times(1)).saveUserId(TEST_USER_ID);
        verify(stateManager, times(1)).saveUserProfile(expectedResult);

        // test view functionality during login
        verify(view, times(1)).showProgress();
        verify(view, times(1)).hideProgress();
        verify(view, times(1)).userLoggedIn();

        verify(view, times(0)).showInvalidProfile(any(ProfileException.class));
        verify(view, times(0)).showRetryMessage(any(Throwable.class));

    }

    private static final String TEST_VALID_EMAIL = "test.test_test@test.test";
    private static final String TEST_INVALID_EMAIL = "test.test_test.test";


    @Test
    public void testIsEmailValid() {
        assertTrue(presenter.isEmailValid(TEST_VALID_EMAIL));
        assertFalse(presenter.isEmailValid(TEST_INVALID_EMAIL));
    }

    private static final String TEST_VALID_PASSWORD = "test";
    private static final String TEST_INVALID_PASSWORD = "t";

    @Test
    public void testIsPasswordValid() {
        assertTrue(presenter.isPasswordValid(TEST_VALID_PASSWORD));
        assertFalse(presenter.isPasswordValid(TEST_INVALID_PASSWORD));
    }

}
