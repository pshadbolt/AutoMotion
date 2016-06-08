package com.ssj.prototype.prototype;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ssj.prototype.prototype.ui.GarageActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringStartsWith.startsWith;

@RunWith(AndroidJUnit4.class)
public class GarageTest {

    @Rule
    public ActivityTestRule<GarageActivity> mActivityRule = new ActivityTestRule<>(GarageActivity.class);

    @Test
    public void addRemoveManualChevroletCruzeTest() {
        onView(withId(R.id.fab_add_vehicle)).perform(click());
        onView(withText("Manual Entry")).perform(click());

        onView(withId(R.id.spinner1)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Chevrolet"))).perform(click());
        onView(withId(R.id.spinner2)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Cruze"))).perform(click());
        onView(withId(R.id.spinner3)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("2011"))).perform(click());
        onView(withId(R.id.spinner4)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Eco 4dr Sedan (1.4L 4cyl Turbo 6M)"))).perform(click());
        onView(withId(R.id.spinner5)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("4ITCG1.4"))).perform(click());
        onView(withId(R.id.spinner6)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("MANUAL"))).perform(click());
        onView(withId(R.id.confirm)).perform(click());
        onView(withId(R.id.editText1)).perform(typeText("61386"), pressImeActionButton());
        onView(withId(R.id.editText2)).perform(typeText("12000"), pressImeActionButton());
        onView(withId(R.id.confirm)).perform(click());

        onData(hasToString(startsWith("2011,Chevrolet,Cruze,Eco 4dr Sedan (1.4L 4cyl Turbo 6M"))).inAdapterView(withId(R.id.listView)).perform(longClick());
    }

    @Test
    public void addRemoveManualToyotaCorollaTest() {
        onView(withId(R.id.fab_add_vehicle)).perform(click());
        onView(withText("Manual Entry")).perform(click());

        onView(withId(R.id.spinner1)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Toyota"))).perform(click());
        onView(withId(R.id.spinner2)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Corolla"))).perform(click());
        onView(withId(R.id.spinner3)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("2001"))).perform(click());
        onView(withId(R.id.spinner4)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("CE 4dr Sedan (1.8L 4cyl 3A)"))).perform(click());
        onView(withId(R.id.spinner5)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("4INAG1.8"))).perform(click());
        onView(withId(R.id.spinner6)).check(matches(not(isEnabled())));
        onView(withId(R.id.confirm)).perform(click());
        onView(withId(R.id.editText1)).perform(typeText("114500"), pressImeActionButton());
        onView(withId(R.id.editText2)).perform(typeText("10000"), pressImeActionButton());
        onView(withId(R.id.confirm)).perform(click());

        onData(hasToString(startsWith("2001,Toyota,Corolla,CE 4dr Sedan (1.8L 4cyl 3A)"))).inAdapterView(withId(R.id.listView)).perform(longClick());
    }

    @Test
    public void addRemoveVINChevroletCruzeTest() {
        onView(withId(R.id.fab_add_vehicle)).perform(click());
        onView(withText("Search VIN")).perform(click());

        onView(withId(R.id.editText_vin)).perform(typeText("1G1PK5S9XB7170032"));
        onView(withText("SUBMIT")).perform(click());

        onView(withId(R.id.confirm)).perform(click());
        onView(withId(R.id.editText1)).perform(typeText("99999"), pressImeActionButton());
        onView(withId(R.id.editText2)).perform(typeText("5000"), pressImeActionButton());
        onView(withId(R.id.confirm)).perform(click());

        onData(hasToString(startsWith("2011,Chevrolet,Cruze,Eco 4dr Sedan (1.4L 4cyl Turbo 6M"))).inAdapterView(withId(R.id.listView)).perform(longClick());
    }

    @Test
    public void addRemoveVINTest1() {
        onView(withId(R.id.fab_add_vehicle)).perform(click());
        onView(withText("Search VIN")).perform(click());

        onView(withId(R.id.editText_vin)).perform(typeText("1FTFW1ET5EFB88620"));
        onView(withText("SUBMIT")).perform(click());

        onView(withId(R.id.confirm)).perform(click());
        onView(withId(R.id.editText1)).perform(typeText("99999"), pressImeActionButton());
        onView(withId(R.id.editText2)).perform(typeText("5000"), pressImeActionButton());
        onView(withId(R.id.confirm)).perform(click());

        //onData(hasToString(startsWith("2011,Chevrolet,Cruze,Eco 4dr Sedan (1.4L 4cyl Turbo 6M"))).inAdapterView(withId(R.id.listView)).perform(longClick());
    }

    @Test
    public void addRemoveVINTest2() {
        onView(withId(R.id.fab_add_vehicle)).perform(click());
        onView(withText("Search VIN")).perform(click());

        onView(withId(R.id.editText_vin)).perform(typeText("1FMEU51K37UA16277"));
        onView(withText("SUBMIT")).perform(click());

        onView(withId(R.id.confirm)).perform(click());
        onView(withId(R.id.editText1)).perform(typeText("99999"), pressImeActionButton());
        onView(withId(R.id.editText2)).perform(typeText("5000"), pressImeActionButton());
        onView(withId(R.id.confirm)).perform(click());

        //onData(hasToString(startsWith("2011,Chevrolet,Cruze,Eco 4dr Sedan (1.4L 4cyl Turbo 6M"))).inAdapterView(withId(R.id.listView)).perform(longClick());
    }

    @Test
    public void addRemoveVINTest3() {
        onView(withId(R.id.fab_add_vehicle)).perform(click());
        onView(withText("Search VIN")).perform(click());

        onView(withId(R.id.editText_vin)).perform(typeText("1GMDU03158D119110"));
        onView(withText("SUBMIT")).perform(click());

        onView(withId(R.id.confirm)).perform(click());
        onView(withId(R.id.editText1)).perform(typeText("99999"), pressImeActionButton());
        onView(withId(R.id.editText2)).perform(typeText("5000"), pressImeActionButton());
        onView(withId(R.id.confirm)).perform(click());

        //onData(hasToString(startsWith("2011,Chevrolet,Cruze,Eco 4dr Sedan (1.4L 4cyl Turbo 6M"))).inAdapterView(withId(R.id.listView)).perform(longClick());
    }

    @Test
    public void addRemoveVINTest4() {
        onView(withId(R.id.fab_add_vehicle)).perform(click());
        onView(withText("Search VIN")).perform(click());

        onView(withId(R.id.editText_vin)).perform(typeText("JH4DC53015S802892"));
        onView(withText("SUBMIT")).perform(click());

        onView(withId(R.id.confirm)).perform(click());
        onView(withId(R.id.editText1)).perform(typeText("99999"), pressImeActionButton());
        onView(withId(R.id.editText2)).perform(typeText("5000"), pressImeActionButton());
        onView(withId(R.id.confirm)).perform(click());

        //onData(hasToString(startsWith("2011,Chevrolet,Cruze,Eco 4dr Sedan (1.4L 4cyl Turbo 6M"))).inAdapterView(withId(R.id.listView)).perform(longClick());
    }

    @Test
    public void addRemoveVINTest5() {
        onView(withId(R.id.fab_add_vehicle)).perform(click());
        onView(withText("Search VIN")).perform(click());

        onView(withId(R.id.editText_vin)).perform(typeText("WVWAAE63B75P043304"));
        onView(withText("SUBMIT")).perform(click());

        onView(withId(R.id.confirm)).perform(click());
        onView(withId(R.id.editText1)).perform(typeText("99999"), pressImeActionButton());
        onView(withId(R.id.editText2)).perform(typeText("5000"), pressImeActionButton());
        onView(withId(R.id.confirm)).perform(click());

        //onData(hasToString(startsWith("2011,Chevrolet,Cruze,Eco 4dr Sedan (1.4L 4cyl Turbo 6M"))).inAdapterView(withId(R.id.listView)).perform(longClick());
    }
}

