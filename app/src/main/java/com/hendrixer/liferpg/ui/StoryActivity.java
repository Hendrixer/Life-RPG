package com.hendrixer.liferpg.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.Window;

import com.hendrixer.liferpg.R;
import com.hendrixer.liferpg.model.Page;


public class StoryActivity extends Activity {

    public static final String TAG = StoryActivity.class.getSimpleName();
//    private Window window = getWindow();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Transition transition = new Explode();

        transition.setStartDelay(250);
        transition.setDuration(700);

        getWindow().setEnterTransition(transition);
        getWindow().setExitTransition(transition);
        setContentView(R.layout.activity_story);

        Intent intent = getIntent();
        String name = intent.getStringExtra(getString(R.string.name_extra));

        if(name == null){
            name = "friend";
        }

        Log.d(TAG, name);


    }
}
