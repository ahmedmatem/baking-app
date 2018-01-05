package com.example.android.bakingapp;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.android.bakingapp.recyclerviewactions.RecyclerViewActions;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {

    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityTestRule = new ActivityTestRule<>(RecipeListActivity.class);

    @Rule
    public IntentsTestRule<RecipeDetailActivity> mIntentsTestRule =
            new IntentsTestRule<RecipeDetailActivity>(RecipeDetailActivity.class);

    @Test
    public void clickExploreButton_LaunchRecipeDetailActivity() {
        /*
        skip
        java.lang.IllegalStateException: No view holder at position: 0
        by sleep test for 10 sec - (time need to load recipes)
         */
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.recipe_list))
                .perform(android.support.test.espresso.contrib.RecyclerViewActions
                .actionOnItemAtPosition(0, RecyclerViewActions.clickChildViewWithId(
                        R.id.btn_action_explore)));

        intended(hasComponent(RecipeDetailActivity.class.getName()));
    }
}
