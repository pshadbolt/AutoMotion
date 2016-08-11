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
public class GarageTestManual {

    @Rule
    public ActivityTestRule<GarageActivity> mActivityRule = new ActivityTestRule<>(GarageActivity.class);

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

    private void addRemoveVehicle(String make, String model, String year, String style, String engine, String transmission, String mileage1, String mileage2) {
        onView(withId(R.id.fab_add_vehicle)).perform(click());
        onView(withText("Manual Entry")).perform(click());

        onView(withId(R.id.spinner1)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(make))).perform(click());
        onView(withId(R.id.spinner2)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(model))).perform(click());
        onView(withId(R.id.spinner3)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(year))).perform(click());
        onView(withId(R.id.spinner4)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(style))).perform(click());
        if (engine != null) {
            onView(withId(R.id.spinner5)).perform(click());
            onData(allOf(is(instanceOf(String.class)), is(engine))).perform(click());
        }
        if (transmission != null) {
            onView(withId(R.id.spinner6)).perform(click());
            onData(allOf(is(instanceOf(String.class)), is(transmission))).perform(click());
        }
        onView(withId(R.id.confirm)).perform(click());
        onView(withId(R.id.editText1)).perform(typeText(mileage1), pressImeActionButton());
        onView(withId(R.id.editText2)).perform(typeText(mileage2), pressImeActionButton());
        onView(withId(R.id.confirm)).perform(click());
    }

    @Test
    public void addRemoveTest1() {
        addRemoveVehicle("Chevrolet", "Cruze", "2011", "Eco", "1.4", "MANUAL", "61386", "12000");
        onData(hasToString(startsWith("2011,Chevrolet,Cruze,Eco"))).inAdapterView(withId(R.id.listView)).perform(longClick());
    }

    @Test
    public void addRemoveTest2() {
        addRemoveVehicle("Toyota", "Corolla", "2001", "CE", "1.8", "AUTOMATIC", "114500", "10000");
        onData(hasToString(startsWith("2001,Toyota,Corolla,CE"))).inAdapterView(withId(R.id.listView)).perform(longClick());
    }

    @Test
    public void addRemoveTest3() {
        addRemoveVehicle("Ford", "F-150", "2014", "Limited", "3.5", "AUTOMATIC", "45000", "20000");
        onData(hasToString(startsWith("2014,Ford,F-150,Limited"))).inAdapterView(withId(R.id.listView)).perform(longClick());
    }

    @Test
    public void addRemoveTest4() {
        addRemoveVehicle("Tesla", "Model X", "2016", "90D", null, "DIRECT_DRIVE", "45000", "20000");
        onData(hasToString(startsWith("2016,Tesla,Model X,90D"))).inAdapterView(withId(R.id.listView)).perform(longClick());
    }

    @Test
    public void addRemoveTest5() {
        addRemoveVehicle("Chevrolet", "Astro", "2005", "Base", "4.3", "AUTOMATIC", "10000", "1000");
        onData(hasToString(startsWith("2016,Tesla,Model X,90D"))).inAdapterView(withId(R.id.listView)).perform(longClick());
    }
}

