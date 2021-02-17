package sg.edu.iss.mindmatters.activities.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

import sg.edu.iss.mindmatters.R;

public class OnboardingActivity extends TutorialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addFragment(
                new Step.Builder()
                        .setTitle(getString(R.string.onboarding_resources))
                        .setBackgroundColor(Color.parseColor("#96E0D9"))
                        .setDrawable(R.drawable.resource)
                        .build());
        addFragment(
                new Step.Builder()
                        .setTitle(getString(R.string.onboarding_take_quiz))
                        .setBackgroundColor(Color.parseColor("#96E0D9"))
                        .setDrawable(R.drawable.takequiz)
                        .build());
        addFragment(
                new Step.Builder()
                        .setTitle(getString(R.string.onboarding_get_help))
                        .setBackgroundColor(Color.parseColor("#96E0D9"))
                        .setDrawable(R.drawable.gethelp)
                        .build());
        addFragment(
                new Step.Builder()

                        .setTitle(getString(R.string.onboarding_daily_quiz))
                        .setBackgroundColor(Color.parseColor("#96E0D9"))
                        .setDrawable(R.drawable.daily)
                        .build());

        addFragment(
                new Step.Builder()
                        .setTitle(getString(R.string.onboarding_dashboard))
                        .setBackgroundColor(Color.parseColor("#96E0D9"))
                        .setDrawable(R.drawable.dashboard)
                        .build());

        finishTutorial();
    }

    public void finishTutorial() {

        SharedPreferences preferences =
                getSharedPreferences("my_preferences", MODE_PRIVATE);

        // Set onboarding_complete to true
        preferences.edit()
                .putBoolean("onboarding_complete",true).commit();

    }

}
