package com.example.teststock.controllers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.teststock.R;

import java.util.ArrayList;
import java.util.List;

public class SplashscreenActivity extends AppCompatActivity{

    private ObjectAnimator fadeIn, fadeOut;
    private ImageView splashScreenImage;
    private List<Drawable> pictureList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        pictureList.add(ContextCompat.getDrawable(this, R.drawable.femme_parfaite_splashscreen));
        pictureList.add(ContextCompat.getDrawable(this, R.drawable.maman_nolan));
        Integer random = (int)(Math.random() * pictureList.size());

        splashScreenImage = findViewById(R.id.activitySplashscreen_imageView);
        splashScreenImage.setImageDrawable(pictureList.get(random));
        splashScreenImage.setImageAlpha(0);

        fadeIn = ObjectAnimator.ofInt(splashScreenImage, "imageAlpha", 0, 255);
        fadeIn.setDuration(1000);
        fadeOut = ObjectAnimator.ofInt(splashScreenImage, "imageAlpha", 255, 0);
        fadeOut.setDuration(1000);

        fadeIn.addListener(new AnimatorListenerAdapter(){
            @Override
            public void onAnimationEnd(Animator animation){
                super.onAnimationEnd(animation);
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                fadeOut.start();
            }
        });

        fadeOut.addListener(new AnimatorListenerAdapter(){
            @Override
            public void onAnimationEnd(Animator animation){
                super.onAnimationEnd(animation);
                Intent intent = new Intent(SplashscreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        fadeIn.start();
    }
}