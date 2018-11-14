package ch.epfl.sweng.SDP.home;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.SDP.game.LoadingScreenActivity.disableLoadingAnimations;
import static ch.epfl.sweng.SDP.game.LoadingScreenActivity.setOnTest;
import static ch.epfl.sweng.SDP.home.HomeActivity.disableBackgroundAnimation;

import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import ch.epfl.sweng.SDP.R;
import ch.epfl.sweng.SDP.auth.Account;
import ch.epfl.sweng.SDP.game.LoadingScreenActivity;
import ch.epfl.sweng.SDP.localDatabase.LocalDbHandlerForAccount;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {

    @Rule
    public final ActivityTestRule<HomeActivity> mActivityRule =
            new ActivityTestRule<HomeActivity>(HomeActivity.class) {
                @Override
                protected void beforeActivityLaunched() {
                    disableBackgroundAnimation();
                    disableLoadingAnimations();
                    setOnTest();
                }
            };

    @Test
    public void testLocalDb() {
        LocalDbHandlerForAccount localDbHandler = new LocalDbHandlerForAccount(
                mActivityRule.getActivity(), null, 1);
        localDbHandler.saveAccount(Account.getInstance(mActivityRule.getActivity()));
        localDbHandler.retrieveAccount(Account.getInstance(mActivityRule.getActivity()));
    }

    @Test
    public void testDrawButtonOpensWaitingPageActivity() {
        Intents.init();
        onView(ViewMatchers.withId(R.id.drawButton)).perform(click());
        intended(hasComponent(LoadingScreenActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testClickOnLeagueImageOpensLeaguesActivity() {
        Intents.init();
        HomeActivity.disableBackgroundAnimation();
        onView(ViewMatchers.withId(R.id.leagueImage)).perform(click());
        intended(hasComponent(LeaguesActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testClickOnLeaderboardButtonOpensLeaderboardActivity() {
        HomeActivity.disableBackgroundAnimation();
        Intents.init();
        onView(ViewMatchers.withId(R.id.leaderboardButton)).perform(click());
        intended(hasComponent(LeaderboardActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testTrophiesButtonIsClickable() {
        onView(withId(R.id.trophiesButton)).check(matches(isClickable()));
    }

    @Test
    public void testStarsButtonIsClickable() {
        onView(withId(R.id.starsButton)).check(matches(isClickable()));
    }

    @Test
    public void testLeagueImageIsVisible() {
        onView(withId(R.id.leagueImage)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void testUsernameOpensPopUp() {
        onView(withId(R.id.usernameButton)).perform(click());
        onView(withId(R.id.usernamePopUp)).check(matches(isDisplayed()));
    }

    @Test
    public void testCrossClosesPopUp() {
        openAndClosePopUp(R.id.crossText);
    }

    @Test
    public void testCanSignOutAccount() {
        openAndClosePopUp(R.id.signOutButton);
    }

    private void openAndClosePopUp(int view) {
        onView(withId(R.id.usernameButton)).perform(click());
        onView(withId(view)).perform(click());
        onView(withId(R.id.usernamePopUp)).check(doesNotExist());
    }
}