package ch.epfl.sweng.SDP.auth;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static junit.framework.TestCase.assertTrue;

@RunWith(JUnit4.class)
public class LoginActivityTest {

    @Rule
    public final ActivityTestRule<LoginActivity> activityRule =
            new ActivityTestRule<>(LoginActivity.class);

    Intent mockIntent;
    IdpResponse mockIdpResponse = Mockito.mock(IdpResponse.class);
    LoginActivity loginActivity;

    /**
     * Initializes the mock objects.
     */
    @Before
    public void init(){
        mockIntent = Mockito.mock(Intent.class);
        mockIdpResponse = Mockito.mock(IdpResponse.class);
        loginActivity = activityRule.getActivity();
    }

    @Test
    public void testSuccessfulLogin(){
        Mockito.when(mockIntent.getParcelableExtra(ExtraConstants.IDP_RESPONSE))
                .thenReturn(mockIdpResponse);
        Mockito.when(mockIdpResponse.isNewUser()).thenReturn(true);
        loginActivity.onActivityResult(42, -1, mockIntent);
        assertTrue(loginActivity.isFinishing());
    }

    @Test
    public void testExistingUser(){
        getDefaultSharedPreferences(activityRule.getActivity()).edit()
                .putBoolean("hasAccount", true).apply();
        Mockito.when(mockIntent.getParcelableExtra(ExtraConstants.IDP_RESPONSE))
                .thenReturn(mockIdpResponse);
        Mockito.when(mockIdpResponse.isNewUser()).thenReturn(false);
        loginActivity.onActivityResult(42, -1, mockIntent);
        assertTrue(loginActivity.isFinishing());
    }

    //@Test
    //public void testNoInternetConnection(){
    //    Mockito.when(mockIntent.getParcelableExtra(ExtraConstants.IDP_RESPONSE))
    //          .thenReturn(mockIdpResponse);
    //    Mockito.when(mockIdpResponse.getError()).thenReturn(mockError);
    //    Mockito.when(mockError.getErrorCode()).thenReturn(1);
    //    loginActivity.onActivityResult(42, 0, mockIntent);
    //    assertEquals(((TextView)loginActivity.findViewById(R.id.error_message))
    //          .getText(), "No Internet connection");
    //}

    @Test
    public void testFailedLoginNullResponse(){
        mockIntent = Mockito.mock(Intent.class);
        mockIdpResponse = null;
        Mockito.when(mockIntent.getParcelableExtra(ExtraConstants.IDP_RESPONSE))
                .thenReturn(mockIdpResponse);
        loginActivity.onActivityResult(42, 0, mockIntent);
        assertTrue(loginActivity.isFinishing());
    }
}
