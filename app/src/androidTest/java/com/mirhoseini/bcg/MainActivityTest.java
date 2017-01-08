package com.mirhoseini.bcg;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mirhoseini.bcg.domain.client.Api;
import com.mirhoseini.bcg.domain.model.LoginResponse;
import com.mirhoseini.bcg.domain.model.ProfileResponse;
import com.mirhoseini.bcg.user.model.ProfileModel;
import com.mirhoseini.bcg.util.StateManager;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.mirhoseini.bcg.test.support.Matcher.childAtPosition;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * Created by Mohsen on 08/01/2017.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private static final int TEST_USER_ID = 1234;
    private static final String TEST_TOKEN = "abcd1234";
    private static final String TEST_EMAIL = "msn.mirhoseini@gmail.com";
    private static final String TEST_AVATAR = "http://91.109.18.74/images/avatar3.png";
    private static final String TEST_PASSWORD = "********";

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(
            MainActivity.class,
            true,
            // false: do not launch the activity immediately
            false);

    @Inject
    Api api;
    @Inject
    StateManager stateManager;

    ProfileModel expectedResult;

    @Before
    public void setUp() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        BcgTestApplication app = (BcgTestApplication) instrumentation.getTargetContext().getApplicationContext();
        ApplicationTestComponent component = (ApplicationTestComponent) BcgApplication.getComponent();

        // inject dagger
        component.inject(this);

        // clear user data before test
        stateManager.clearUser();

        // Set up the stub we want to return in the mock
        LoginResponse loginResponse = new LoginResponse(TEST_USER_ID, TEST_TOKEN);
        Response<LoginResponse> loginResult = Response.success(loginResponse);

        ProfileResponse profileResponse = new ProfileResponse(TEST_EMAIL, TEST_AVATAR);
        Response<ProfileResponse> profileResult = Response.success(profileResponse);

        expectedResult = ProfileModel.create(TEST_EMAIL, TEST_AVATAR);

        // Setup the mock
        when(api.login(eq(TEST_EMAIL), eq(TEST_PASSWORD)))
                .thenReturn(Observable.just(loginResult));
        when(api.getProfile(eq(TEST_TOKEN), eq(TEST_USER_ID)))
                .thenReturn(Observable.just(profileResult));
    }

    @Test
    public void shouldBeAbleToLogin() {
        // Launch the activity
        mainActivity.launchActivity(new Intent());

        /*
         * This test is recorded using Android Studio "Record Espresso Test" tool
         */

        // enter email
        ViewInteraction email = onView(
                withId(R.id.email));
        email.perform(scrollTo(), replaceText(TEST_EMAIL), closeSoftKeyboard());

        // enter password
        ViewInteraction password = onView(
                withId(R.id.password));
        password.perform(scrollTo(), replaceText(TEST_PASSWORD), closeSoftKeyboard());

        // click login
        ViewInteraction login = onView(
                allOf(withId(R.id.login),
                        withParent(allOf(withId(R.id.email_login_form),
                                withParent(withId(R.id.login_container))))));
        login.perform(scrollTo(), click());

        // check if logged in
        ViewInteraction profileEmail = onView(
                allOf(withText(TEST_EMAIL),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        profileEmail.check(matches(withText(TEST_EMAIL)));

        ViewInteraction logout = onView(
                allOf(withId(R.id.logout), withText("Logout"), isDisplayed()));
        logout.check(matches(withText("Logout")));
    }

}

