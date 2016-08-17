package com.ssj.prototype.prototype;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ssj.prototype.prototype.ui.GarageActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringStartsWith.startsWith;

@RunWith(AndroidJUnit4.class)
public class GarageTestVIN {

    @Rule
    public ActivityTestRule<GarageActivity> mActivityRule = new ActivityTestRule<>(GarageActivity.class);

    private void addRemoveVIN(String VIN, String mileage1, String mileage2) {
        onView(withId(R.id.fab_add_vehicle)).perform(click());
        onView(withText("Search VIN")).perform(click());

        onView(withId(R.id.editText_vin)).perform(typeText(VIN));
        onView(withText(R.string.button_submit)).perform(click());

        onView(withId(R.id.confirm)).perform(click());
        onView(withId(R.id.editText1)).perform(typeText(mileage1), pressImeActionButton());
        onView(withId(R.id.editText2)).perform(typeText(mileage2), pressImeActionButton());
        onView(withId(R.id.confirm)).perform(click());
    }

    @Before
    public void removeAllVehicles() {
        while (true) {
            try {
                onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(longClick());
                onView(withText("Delete Vehicle")).perform(click());
            } catch (Exception e) {
                return;
            }
        }
    }

    @Test
    public void addRemoveVINTest1() {

        onView(withId(R.id.fab_add_vehicle)).perform(click());
        onView(withText("Search VIN")).perform(click());

        onView(withId(R.id.editText_vin)).perform(typeText("1FTFW1ET5EFB88620"));
        onView(withText(R.string.button_submit)).perform(click());

        onView(withId(R.id.spinner4)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Limited"))).perform(click());
        onView(withId(R.id.spinner5)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("3.5"))).perform(click());
        onView(withId(R.id.spinner6)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("AUTOMATIC"))).perform(click());
        onView(withId(R.id.confirm)).perform(click());

        onView(withId(R.id.editText1)).perform(typeText("99999"), pressImeActionButton());
        onView(withId(R.id.editText2)).perform(typeText("5000"), pressImeActionButton());
        onView(withId(R.id.confirm)).perform(click());

        onData(hasToString(startsWith("2014,Ford,F-150,Limited"))).inAdapterView(withId(R.id.listView)).perform(longClick());
        onView(withText("Delete Vehicle")).perform(click());
    }

    @Test
    public void addRemoveVINTest2() {
        addRemoveVIN("1FMEU51K37UA16277", "99999", "5000");
        onData(hasToString(startsWith("2007,Ford,Explorer Sport Trac,XLT"))).inAdapterView(withId(R.id.listView)).perform(longClick());
        onView(withText("Delete Vehicle")).perform(click());
    }

    //@Test
    public void addRemoveVINTest3() {
        addRemoveVIN("1GMDU03158D119110", "99999", "5000");
        //onData(hasToString(startsWith("2011,Chevrolet,Cruze,Eco 4dr Sedan (1.4L 4cyl Turbo 6M"))).inAdapterView(withId(R.id.listView)).perform(longClick());
    }

    @Test
    public void addRemoveVINTest4() {
        addRemoveVIN("JH4DC53015S802892", "99999", "5000");
        onData(hasToString(startsWith("2005,Acura,RSX,Type-S"))).inAdapterView(withId(R.id.listView)).perform(longClick());
        onView(withText("Delete Vehicle")).perform(click());
    }

    //@Test
    public void addRemoveVINTest5() {
        addRemoveVIN("WVWAAE63B75P043304", "99999", "5000");
        //onData(hasToString(startsWith("2011,Chevrolet,Cruze,Eco 4dr Sedan (1.4L 4cyl Turbo 6M"))).inAdapterView(withId(R.id.listView)).perform(longClick());
    }

    @Test
    public void addRemoveVINTest6() {
        addRemoveVIN("1G1PK5S9XB7170032", "99999", "5000");
        onData(hasToString(startsWith("2011,Chevrolet,Cruze,Eco"))).inAdapterView(withId(R.id.listView)).perform(longClick());
        onView(withText("Delete Vehicle")).perform(click());
    }

    @Test
    public void addRemoveVINTest7() {
        addRemoveVIN("2T1BR12E21C840463", "99999", "5000");
        onData(hasToString(startsWith("2001,Toyota,Corolla"))).inAdapterView(withId(R.id.listView)).perform(longClick());
        onView(withText("Delete Vehicle")).perform(click());
    }

    @Test
    public void addRemoveVINTestFail1() {
        onView(withId(R.id.fab_add_vehicle)).perform(click());
        onView(withText(R.string.fab_action_search_vin)).perform(click());

        onView(withId(R.id.editText_vin)).perform(typeText("AAAA121212"));
        onView(withText(R.string.button_submit)).perform(click());

        onView(withId(R.id.editText_vin)).perform(typeText("AT1BR12E21C840463"));
        onView(withText(R.string.button_submit)).perform(click());
        onView(withText("CANCEL")).perform(click());
    }
}

