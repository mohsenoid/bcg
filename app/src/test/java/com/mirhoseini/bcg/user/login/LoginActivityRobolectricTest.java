package com.mirhoseini.bcg.user.login;


import com.mirhoseini.bcg.test.support.ShadowSnackbar;
import com.mirhoseini.bcg.user.login.LoginActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.mirhoseini.bcg.test.support.Assert.assertAlertDialogIsShown;
import static com.mirhoseini.bcg.test.support.Assert.assertSnackbarIsShown;
import static com.mirhoseini.bcg.test.support.Assert.assertToastIsShown;
import static org.junit.Assert.assertNotNull;


/**
 * Created by Mohsen on 08/01/2017.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, shadows = {ShadowSnackbar.class})
public class LoginActivityRobolectricTest {

    private static final String TEST_TEXT = "This is a test text.";

    private LoginActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.setupActivity(LoginActivity.class);

        assertNotNull(activity);
    }

    @Test
    public void testShowOfflineMessage() throws Exception {
        activity.showOfflineMessage(false);
        assertSnackbarIsShown(R.string.offline_message);

        activity.showOfflineMessage(true);
        assertAlertDialogIsShown(R.string.utils__no_connection_title, R.string.utils__no_connection);
    }

    @Test
    public void testShowMessage() throws Exception {
        activity.showMessage(TEST_TEXT);

        assertToastIsShown(TEST_TEXT);
    }
}
