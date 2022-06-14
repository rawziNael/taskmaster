package com.example.taskmaster;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.taskmaster", appContext.getPackageName());
    }

    @Test
    public void testTheMainActivity(){
        onView(withId(R.id.myTasks)).check(matches(withText("My Tasks")));
    }

    @Test
    public void testTheSettingActivity(){
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Settings")).perform(click());
        onView(withId(R.id.settingsTitle)).check(matches(withText("Settings")));
    }

    @Test
    public void testTheAddTaskActivity(){
        onView(withId(R.id.addTaskButton)).perform(click());
        onView(withId(R.id.addATask)).check(matches(withText("Add Task")));
    }

    @Test
    public void testAddNewTask(){
        onView(withId(R.id.addTaskButton)).perform(click());

        onView(withId(R.id.newTaskName)).perform(typeText("New Task"),closeSoftKeyboard());
        onView(withId(R.id.newTaskBody)).perform(typeText("Task Details"),closeSoftKeyboard());

        onView(withId(R.id.newTaskSubmit)).perform(click());

        pressBack();

        onView(ViewMatchers.withId(R.id.List_tasks)).check(matches(isDisplayed()));
    }

    @Test
    public void testChangeUserName(){
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Settings")).perform(click());
        onView(withId(R.id.usernameInput)).perform(typeText("Ahmad") ,closeSoftKeyboard());
        onView(withId(R.id.usernameSaveButton)).perform(click());
        onView(withId(R.id.userTasksLabel)).check(matches(withText("Ahmad's Tasks"))) ;
    }
}