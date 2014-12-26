package com.hendrixer.liferpg.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.easing.sine.SineEaseIn;
import com.hendrixer.liferpg.R;
import com.hendrixer.liferpg.model.Page;
import com.hendrixer.liferpg.model.Story;


public class StoryActivity extends Activity {

    public static final String TAG = StoryActivity.class.getSimpleName();
    private Story mStory = new Story();
    private ImageView mImageView;
    private TextView mTextView;
    private Button mChoice1;
    private Button mChoice2;
    private String mName;
    private Page mCurrentPage;

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
        mName = intent.getStringExtra(getString(R.string.name_extra));

        if(mName == null){
            mName = "friend";
        }

        Log.d(TAG, mName);

        mImageView = (ImageView) findViewById(R.id.storyImageView);
        mTextView = (TextView) findViewById(R.id.storyTextView);
        mChoice1 = (Button) findViewById(R.id.choiceButton1);
        mChoice2 = (Button) findViewById(R.id.choiceButton2);

        loadPage(0);
    }

    private void loadPage(final int position){
        mCurrentPage = mStory.getPage(position);

        Drawable drawable = getResources().getDrawable(mCurrentPage.getImageId());

        Palette.generateAsync(getBitMapFromDrawable(drawable), new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();

                if (vibrant != null){
                    getWindow().setStatusBarColor(vibrant.getRgb());
                    getWindow().setNavigationBarColor(vibrant.getRgb());
                }

                Palette.Swatch dark = palette.getDarkVibrantSwatch();

                if (dark != null) {
                    mChoice1.setBackgroundColor(dark.getRgb());
                }

                Palette.Swatch muted = palette.getMutedSwatch();

                if (muted != null) {
                    mChoice2.setBackgroundColor(muted.getRgb());
                }
            }
        });

        mImageView.setImageDrawable(drawable);

        if (position > 0){
            mImageView.setVisibility(View.INVISIBLE);
            Animator imageAnimator = setViewInvisible(mImageView, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    mImageView.setVisibility(View.VISIBLE);

                    String pageText = mCurrentPage.getText();

                    pageText = String.format(pageText, mName);
                    mTextView.setText(pageText);

                    if (position > 0)
                        animateView(mTextView, Techniques.SlideInUp, 700);

                }

            });
            imageAnimator.start();
        }

        if (mCurrentPage.getIsFinal()) {
            mChoice1.setVisibility(View.INVISIBLE);

            mChoice2.setText(("PLAY AGAIN"));

            if (position > 0)
                animateView(mChoice2, Techniques.SlideInUp, 700);

//            Animator animator = setViewInvisible(mChoice1);
//            animator.start();
            mChoice2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            mChoice1.setText(mCurrentPage.getChoice1().getText());
            mChoice2.setText(mCurrentPage.getChoice2().getText());

            if (position > 0){
                animateView(mChoice1, Techniques.SlideInUp, 700);
                animateView(mChoice2, Techniques.SlideInUp, 700);
            }

            mChoice1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int nextPage = mCurrentPage.getChoice1().getNextPage();
                    loadPage(nextPage);
                }
            });

            mChoice2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int nextPage = mCurrentPage.getChoice2().getNextPage();
                    loadPage(nextPage);
                }
            });
        }

    }

    public void animateView(View view, Techniques tech, int duration){
        YoYo.with(tech)
                .duration(duration)
                .playOn(view);
    }

    public Animator setViewInvisible(final View view, AnimatorListenerAdapter cb){
        final int cx = Math.round(view.getX() + view.getWidth()) / 2;
        final int cy = Math.round(view.getY() + view.getHeight()) / 2;

//        int initRadius = (float) Math.hypot(view.getWidth(), view.getHeight());

//        view.setVisibility(View.INVISIBLE);
        Animator animator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, (float) Math.hypot(view.getWidth(), view.getHeight()));

        animator.addListener(cb);

        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setStartDelay(200);
        animator.setDuration(700);
        return animator;
    }

    public Bitmap getBitMapFromDrawable(Drawable img){
        Bitmap imageBitmap = ((BitmapDrawable) img).getBitmap();

        return imageBitmap;
    }

}
