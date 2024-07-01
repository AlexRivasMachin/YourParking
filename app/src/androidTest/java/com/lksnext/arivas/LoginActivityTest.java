package com.lksnext.arivas;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.lksnext.arivas.view.activity.LoginActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Before
    public void setUp() {
        ActivityScenario.launch(LoginActivity.class);
    }

    @Test
    public void testLoginSuccess() {
        Espresso.onView(ViewMatchers.withId(R.id.NombreUsuariolayout))
                .perform(ViewActions.typeText(" alex17623@gmail.com "), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.ContraseñaInputLayout))
                .perform(ViewActions.typeText("7654321"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.iniciarSesion))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.mainFragment))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testLoginFailure() {
        // Reemplaza con tus propios ID de vista
        Espresso.onView(ViewMatchers.withId(R.id.NombreUsuariolayout))
                .perform(ViewActions.typeText("wrong@example.com"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.ContraseñaInputLayout))
                .perform(ViewActions.typeText("wrongpassword"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.iniciarSesion))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.mainFragment))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
