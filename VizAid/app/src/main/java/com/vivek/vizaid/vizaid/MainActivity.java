/**
 * Created by Vivek on 30-01-2017.
 */

package com.vivek.vizaid.vizaid;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.support.v4.view.GestureDetectorCompat;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.support.v4.view.GestureDetectorCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.widget.Toast;

import android.support.v7.widget.Toolbar;

import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends ActionBarActivity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private GestureDetectorCompat gestureDetector;
    private TextToSpeech t1;
    private TextToSpeech t2;
    private int flag=0;


    private static final int REQUEST_APP_SETTINGS = 168;

    private static final String[] requiredPermissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getString(R.string.subscription_key).startsWith("Please")) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.add_subscription_key_tip_title))
                    .setMessage(getString(R.string.add_subscription_key_tip))
                    .setCancelable(false)
                    .show();
        }


        if (Build.VERSION.SDK_INT > 22 && !hasPermissions(requiredPermissions)) {

            goToSettings();
        }



        flag=0;
        if (hasPermissions(requiredPermissions)){
        t2=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t2.setLanguage(Locale.UK);
                    String toSpeak = "Welcome to Viz Aid. Swipe Down for tips.";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        String utteranceId=this.hashCode() + "";
                        t2.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                    }else{
                        HashMap<String, String> map = new HashMap<>();
                        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
                        t2.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, map);
                    }
                }
            }
        });
        }

        this.gestureDetector = new GestureDetectorCompat(this, this);
        gestureDetector.setOnDoubleTapListener(this);

    }



    public void activityRecognize(View v) {
        Intent intent = new Intent(this, RecognizeActivity.class);
        startActivity(intent);
    }

    ///////// GESTURE METHODS //////////
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        //buckysMessage.setText("onSingleTapConfirmed");
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Intent intent;
        intent = new Intent(MainActivity.this, com.vivek.vizaid.vizaid.DescribeActivity.class);
        startActivityForResult(intent,0);
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        //buckysMessage.setText("onDoubleTapEvent");
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {

        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = false;
        try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight();
                    } else {
                        onSwipeLeft();
                    }
                }
                result = true;
            }
            else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    onSwipeBottom();
                } else {
                    onSwipeTop();
                }
            }
            result = true;

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }


    public void onSwipeRight() {
        Intent intent;
        intent = new Intent(MainActivity.this, com.vivek.vizaid.vizaid.RecognizeActivity.class);
        startActivityForResult(intent,0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setContentView(R.layout.activity_main);

        if (requestCode == REQUEST_APP_SETTINGS) {
            if (hasPermissions(requiredPermissions)) {
                Toast.makeText(this, "Please grant permissions", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please grant permissions", Toast.LENGTH_LONG).show();
            }
        }


        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                    String toSpeak = "Home Screen";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        String utteranceId=this.hashCode() + "";
                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                    }else{
                        HashMap<String, String> map = new HashMap<>();
                        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, map);
                    }
                }
            }
        });
    }
    public void onSwipeLeft() {
        Intent intent;
        intent = new Intent(MainActivity.this, com.vivek.vizaid.vizaid.EmotionActivity.class);
        startActivityForResult(intent,0);
    }

    public void onSwipeTop() {
        Intent intent;
        intent = new Intent(MainActivity.this, com.vivek.vizaid.vizaid.ui.IdentificationActivity.class);
        startActivity(intent);
    }

    public void onSwipeBottom() {
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                    String toSpeak = "Swipe right for reading text. Swipe left for recognizing emotions. Double tap to describe image.";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        String utteranceId=this.hashCode() + "";
                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                    }else{
                        HashMap<String, String> map = new HashMap<>();
                        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, map);
                    }
                }
            }
        });
    }

    ///////// GESTURE METHODS //////////

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    public void onResume() {
        super.onResume();  // Always call the superclass method firs
        if(flag==1){
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                    String toSpeak = "Home Page.";

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        String utteranceId=this.hashCode() + "";
                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                    }else{
                        HashMap<String, String> map = new HashMap<>();
                        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, map);
                    }
                }
                }
        });
        }
    }
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
    }
    protected void onStop() {
        // call the superclass method first
        super.onStop();
    }

    public  void onBackPressed() {

        super.onBackPressed();
        onPause();
    }


    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(myAppSettings, REQUEST_APP_SETTINGS);
    }

    public boolean hasPermissions(@NonNull String... permissions) {
        for (String permission : permissions)
            if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(permission))
                return false;
        return true;
    }

}
