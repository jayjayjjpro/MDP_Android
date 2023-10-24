package com.example.mdptest;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Path;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mdptest.databinding.FragmentSecondBinding;

import java.nio.charset.Charset;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.example.mdptest.DBHelper;

public class SecondFragment extends Fragment {

    // Wayne
    private FragmentSecondBinding binding;

    private static final int SNAP_GRID_INTERVAL = 40;

    private static final int ANIMATOR_DURATION = 1000;

    private boolean isObstacle1LongClicked = false;
    private boolean isObstacle2LongClicked = false;
    private boolean isObstacle3LongClicked = false;
    private boolean isObstacle4LongClicked = false;
    private boolean isObstacle5LongClicked = false;
    private boolean isObstacle6LongClicked = false;
    private boolean isObstacle7LongClicked = false;
    private boolean isObstacle8LongClicked = false;

    private String lastReceivedMessage = null;

    private boolean isDragging = false;


    private String[] checkUpdate =new String[8];
    private int checki = 0;
    private int checkOBStr = 0;

    Button sendObstaclesButton;

    ImageButton resetObstacles, putObstacles, startbtn, carGenerator,alertButton,musicButton;

    ImageButton forwardButton, turnLeftButton, turnRightButton,reverseButton, leftReverseButton, rightReverseButton;

    ImageView image1, image2, image3, image4, image5, zombieimage;
    FrameLayout obstacle1, obstacle2, obstacle3, obstacle4, obstacle5, obstacle6,obstacle7,obstacle8;
    float maxWidth;
    float maxHeight;
    int countOs = 0;
    ImageView car;
    Map<Integer, FrameLayout> obstacles;
    Map<Integer, ImageView> images;


    DBHelper dbHelper;

    Map<Integer, Integer> resources = new HashMap<Integer, Integer>() {{

        put(1, R.drawable.pic_1);
        put(2, R.drawable.pic_2);
        put(3, R.drawable.pic_3);
        put(4, R.drawable.pic_4);
        put(5, R.drawable.pic_5);
        put(6, R.drawable.pic_6);
        put(7, R.drawable.pic_7);
        put(8, R.drawable.pic_8);

    }};


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        FrameLayout frameLayout1 = getView().findViewById(R.id.combo1);
        savedInstanceState.putFloat("test",frameLayout1.getX());
        savedInstanceState.putFloat("testY",frameLayout1.getY());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the DBHelper with the fragment's context
        dbHelper = new DBHelper(requireContext());
    }
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void onViewCreated(@NonNull View f, Bundle savedInstanceState) {
        super.onViewCreated(f, savedInstanceState);


        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        // Register Broadcast Receiver for incoming message
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(myReceiver, new IntentFilter("IncomingMsg"));

        obstacle1 = getActivity().findViewById(R.id.combo1);
        obstacle2 = getActivity().findViewById(R.id.combo2);
        obstacle3 = getActivity().findViewById(R.id.combo3);
        obstacle4 = getActivity().findViewById(R.id.combo4);
        obstacle5 = getActivity().findViewById(R.id.combo5);
        obstacle6 = getActivity().findViewById(R.id.combo6);
        obstacle7 = getActivity().findViewById(R.id.combo7);
        obstacle8 = getActivity().findViewById(R.id.combo8);

        obstacles = new HashMap<Integer, FrameLayout>() {{
            put(1, obstacle1);
            put(2, obstacle2);
            put(3, obstacle3);
            put(4, obstacle4);
            put(5, obstacle5);
            put(6, obstacle6);
            put(7, obstacle7);
            put(8, obstacle8);
        }};


        image1 = getActivity().findViewById(R.id.obstacle1);
        image2 = getActivity().findViewById(R.id.obstacle2);
        image3 = getActivity().findViewById(R.id.obstacle3);
        image4 = getActivity().findViewById(R.id.obstacle4);
        image5 = getActivity().findViewById(R.id.obstacle5);

        images = new HashMap<Integer, ImageView>(){{
            put(1, image1);
            put(2, image2);
            put(3, image3);
            put(4, image4);
            put(5, image5);
        }};

        while(checki < 8){
            checkUpdate[checki] = "";
            checki++;
        }

        obstacle1.setOnLongClickListener(view -> {
            isObstacle1LongClicked = true;
            isDragging = true;
            // Test if returning true instead might fix the image flying off
            return false;
        });

        obstacle2.setOnLongClickListener(view -> {
            isObstacle2LongClicked = true;
            isDragging = true;
            return false;
        });

        obstacle3.setOnLongClickListener(view -> {
            isObstacle3LongClicked = true;
            isDragging = true;
            return false;
        });

        obstacle4.setOnLongClickListener(view -> {
            isObstacle4LongClicked = true;
            isDragging = true;
            return false;
        });

        obstacle5.setOnLongClickListener(view -> {
            isObstacle5LongClicked = true;
            isDragging = true;
            return false;
        });

        obstacle6.setOnLongClickListener(view -> {
            isObstacle6LongClicked = true;
            isDragging = true;
            return false;
        });

        obstacle7.setOnLongClickListener(view -> {
            isObstacle7LongClicked = true;
            isDragging = true;
            return false;
        });

        obstacle8.setOnLongClickListener(view -> {
            isObstacle8LongClicked = true;
            isDragging = true;
            return false;
        });

        //set the max height and width according to the grid map;
        maxWidth = SNAP_GRID_INTERVAL*19;
        maxHeight = SNAP_GRID_INTERVAL*22;

        obstacle1.setOnTouchListener(new View.OnTouchListener() {
            int x = 0;
            int y = 0;
            int dx = 0;
            int dy = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isObstacle1LongClicked) {
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        obstacle1.getChildAt(1).setPivotX(obstacle1.getWidth()/2.0f);
                        obstacle1.getChildAt(1).setPivotY(obstacle1.getHeight()/2.0f);

                        obstacle1.getChildAt(1).setRotation((obstacle1.getChildAt(1).getRotation() + 90) % 360);

                    }
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = (int) event.getX();
                        y = (int) event.getY();
                        isDragging = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        dx = (int) event.getX() - x;
                        dy = (int) event.getY() - y;

                        obstacle1.setX(obstacle1.getX() + dx);
                        obstacle1.setY(obstacle1.getY() + dy);
                        break;
                    case MotionEvent.ACTION_UP:
                        int snapToX = ((int) ((obstacle1.getX() + SNAP_GRID_INTERVAL / 2) / SNAP_GRID_INTERVAL)) * SNAP_GRID_INTERVAL;
                        int snapToY = ((int) ((obstacle1.getY() + SNAP_GRID_INTERVAL / 2) / SNAP_GRID_INTERVAL)) * SNAP_GRID_INTERVAL;
                        obstacle1.setX(snapToX);
                        obstacle1.setY(snapToY);
                        isObstacle1LongClicked = false;

                        //this will send obstacle info once I lift my finger?
                        sendObstaclesOneTime(1);
                        Log.d("SecondFragment", "TEST: Insert first data");
                        dbHelper.insertData(1,(int)obstacles.get(1).getX()/SNAP_GRID_INTERVAL, 19-(int)obstacles.get(1).getY()/SNAP_GRID_INTERVAL,getViewOrientation(obstacle1));
                        isDragging = false;
                        break;
                    default:
                        break;
                }

                //Set boundary for obstacles' movement;
                if(obstacle1.getX() > maxWidth){
                    obstacle1.setX(maxWidth);
                }
                else if(obstacle1.getX() < 0){
                    obstacle1.setX(0);
                }
                if(obstacle1.getY() > maxHeight){
                    obstacle1.setY(maxHeight);
                }
                else if(obstacle1.getY() < 0){
                    obstacle1.setY(0);
                }

                return false;
            }
        });

        obstacle2.setOnTouchListener(new View.OnTouchListener() {
            int x = 0;
            int y = 0;
            int dx = 0;
            int dy = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isObstacle2LongClicked) {
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        obstacle2.getChildAt(1).setPivotX(obstacle2.getWidth()/2.0f);
                        obstacle2.getChildAt(1).setPivotY(obstacle2.getHeight()/2.0f);

                        obstacle2.getChildAt(1).setRotation((obstacle2.getChildAt(1).getRotation() + 90) % 360);

                    }
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = (int) event.getX();
                        y = (int) event.getY();
                        isDragging = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        dx = (int) event.getX() - x;
                        dy = (int) event.getY() - y;

                        obstacle2.setX(obstacle2.getX() + dx);
                        obstacle2.setY(obstacle2.getY() + dy);
                        break;
                    case MotionEvent.ACTION_UP:
                        int snapToX = ((int) ((obstacle2.getX() + SNAP_GRID_INTERVAL / 2) / SNAP_GRID_INTERVAL)) * SNAP_GRID_INTERVAL;
                        int snapToY = ((int) ((obstacle2.getY() + SNAP_GRID_INTERVAL / 2) / SNAP_GRID_INTERVAL)) * SNAP_GRID_INTERVAL;
                        obstacle2.setX(snapToX);
                        obstacle2.setY(snapToY);
                        isObstacle2LongClicked = false;

                        sendObstaclesOneTime(2);
                        dbHelper.insertData(2,(int)obstacles.get(2).getX()/SNAP_GRID_INTERVAL, 19-(int)obstacles.get(2).getY()/SNAP_GRID_INTERVAL,getViewOrientation(obstacle2));
                        isDragging = false;
                        break;
                    default:
                        break;
                }

                if(obstacle2.getX() > maxWidth){
                    obstacle2.setX(maxWidth);
                }
                else if(obstacle2.getX() < 0){
                    obstacle2.setX(0);
                }
                if(obstacle2.getY() > maxHeight){
                    obstacle2.setY(maxHeight);
                }
                else if(obstacle2.getY() < 0){
                    obstacle2.setY(0);
                }

                return false;
            }
        });

        obstacle3.setOnTouchListener(new View.OnTouchListener() {
            int x = 0;
            int y = 0;
            int dx = 0;
            int dy = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isObstacle3LongClicked) {
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        obstacle3.getChildAt(1).setPivotX(obstacle3.getWidth()/2.0f);
                        obstacle3.getChildAt(1).setPivotY(obstacle3.getHeight()/2.0f);

                        obstacle3.getChildAt(1).setRotation((obstacle3.getChildAt(1).getRotation() + 90) % 360);

                    }
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = (int) event.getX();
                        y = (int) event.getY();
                        isDragging = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        dx = (int) event.getX() - x;
                        dy = (int) event.getY() - y;

                        obstacle3.setX(obstacle3.getX() + dx);
                        obstacle3.setY(obstacle3.getY() + dy);
                        break;
                    case MotionEvent.ACTION_UP:
                        int snapToX = ((int) ((obstacle3.getX() + SNAP_GRID_INTERVAL / 2) / SNAP_GRID_INTERVAL)) * SNAP_GRID_INTERVAL;
                        int snapToY = ((int) ((obstacle3.getY() + SNAP_GRID_INTERVAL / 2) / SNAP_GRID_INTERVAL)) * SNAP_GRID_INTERVAL;
                        obstacle3.setX(snapToX);
                        obstacle3.setY(snapToY);
                        isObstacle3LongClicked = false;

                        sendObstaclesOneTime(3);
                        dbHelper.insertData(3,(int)obstacles.get(3).getX()/SNAP_GRID_INTERVAL, 19-(int)obstacles.get(3).getY()/SNAP_GRID_INTERVAL,getViewOrientation(obstacle3));
                        isDragging = false;
                        break;
                    default:
                        break;
                }

                if(obstacle3.getX() > maxWidth){
                    obstacle3.setX(maxWidth);
                }
                else if(obstacle3.getX() < 0){
                    obstacle3.setX(0);
                }
                if(obstacle3.getY() > maxHeight){
                    obstacle3.setY(maxHeight);
                }
                else if(obstacle3.getY() < 0){
                    obstacle3.setY(0);
                }

                return false;
            }
        });

        obstacle4.setOnTouchListener(new View.OnTouchListener() {
            int x = 0;
            int y = 0;
            int dx = 0;
            int dy = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isObstacle4LongClicked) {
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        obstacle4.getChildAt(1).setPivotX(obstacle4.getWidth()/2.0f);
                        obstacle4.getChildAt(1).setPivotY(obstacle4.getHeight()/2.0f);

                        obstacle4.getChildAt(1).setRotation((obstacle4.getChildAt(1).getRotation() + 90) % 360);

                    }
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = (int) event.getX();
                        y = (int) event.getY();
                        isDragging = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        dx = (int) event.getX() - x;
                        dy = (int) event.getY() - y;

                        obstacle4.setX(obstacle4.getX() + dx);
                        obstacle4.setY(obstacle4.getY() + dy);
                        break;
                    case MotionEvent.ACTION_UP:
                        int snapToX = ((int) ((obstacle4.getX() + SNAP_GRID_INTERVAL / 2) / SNAP_GRID_INTERVAL)) * SNAP_GRID_INTERVAL;
                        int snapToY = ((int) ((obstacle4.getY() + SNAP_GRID_INTERVAL / 2) / SNAP_GRID_INTERVAL)) * SNAP_GRID_INTERVAL;
                        obstacle4.setX(snapToX);
                        obstacle4.setY(snapToY);
                        isObstacle4LongClicked = false;

                        sendObstaclesOneTime(4);
                        dbHelper.insertData(4,(int)obstacles.get(4).getX()/SNAP_GRID_INTERVAL, 19-(int)obstacles.get(4).getY()/SNAP_GRID_INTERVAL,getViewOrientation(obstacle4));
                        isDragging = false;
                        break;
                    default:
                        break;
                }

                if(obstacle4.getX() > maxWidth){
                    obstacle4.setX(maxWidth);
                }
                else if(obstacle4.getX() < 0){
                    obstacle4.setX(0);
                }
                if(obstacle4.getY() > maxHeight){
                    obstacle4.setY(maxHeight);
                }
                else if(obstacle4.getY() < 0){
                    obstacle4.setY(0);
                }

                return false;
            }
        });

        obstacle5.setOnTouchListener(new View.OnTouchListener() {
            int x = 0;
            int y = 0;
            int dx = 0;
            int dy = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isObstacle5LongClicked) {
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        obstacle5.getChildAt(1).setPivotX(obstacle5.getWidth()/2.0f);
                        obstacle5.getChildAt(1).setPivotY(obstacle5.getHeight()/2.0f);

                        obstacle5.getChildAt(1).setRotation((obstacle5.getChildAt(1).getRotation() + 90) % 360);

                    }
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = (int) event.getX();
                        y = (int) event.getY();
                        isDragging = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        dx = (int) event.getX() - x;
                        dy = (int) event.getY() - y;

                        obstacle5.setX(obstacle5.getX() + dx);
                        obstacle5.setY(obstacle5.getY() + dy);
                        break;
                    case MotionEvent.ACTION_UP:
                        int snapToX = ((int) ((obstacle5.getX() + SNAP_GRID_INTERVAL / 2) / SNAP_GRID_INTERVAL)) * SNAP_GRID_INTERVAL;
                        int snapToY = ((int) ((obstacle5.getY() + SNAP_GRID_INTERVAL / 2) / SNAP_GRID_INTERVAL)) * SNAP_GRID_INTERVAL;
                        obstacle5.setX(snapToX);
                        obstacle5.setY(snapToY);
                        isObstacle5LongClicked = false;

                        sendObstaclesOneTime(5);
                        dbHelper.insertData(5,(int)obstacles.get(5).getX()/SNAP_GRID_INTERVAL, 19-(int)obstacles.get(5).getY()/SNAP_GRID_INTERVAL,getViewOrientation(obstacle5));
                        isDragging = false;
                        break;
                    default:
                        break;
                }

                if(obstacle5.getX() > maxWidth){
                    obstacle5.setX(maxWidth);
                }
                else if(obstacle5.getX() < 0){
                    obstacle5.setX(0);
                }
                if(obstacle5.getY() > maxHeight){
                    obstacle5.setY(maxHeight);
                }
                else if(obstacle5.getY() < 0){
                    obstacle5.setY(0);
                }

                return false;
            }
        });

        obstacle6.setOnTouchListener(new View.OnTouchListener() {
            int x = 0;
            int y = 0;
            int dx = 0;
            int dy = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isObstacle6LongClicked) {
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        obstacle6.getChildAt(1).setPivotX(obstacle6.getWidth()/2.0f);
                        obstacle6.getChildAt(1).setPivotY(obstacle6.getHeight()/2.0f);

                        obstacle6.getChildAt(1).setRotation((obstacle6.getChildAt(1).getRotation() + 90) % 360);

                    }
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = (int) event.getX();
                        y = (int) event.getY();
                        isDragging = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        dx = (int) event.getX() - x;
                        dy = (int) event.getY() - y;

                        obstacle6.setX(obstacle6.getX() + dx);
                        obstacle6.setY(obstacle6.getY() + dy);
                        break;
                    case MotionEvent.ACTION_UP:
                        int snapToX = ((int) ((obstacle6.getX() + SNAP_GRID_INTERVAL / 2) / SNAP_GRID_INTERVAL)) * SNAP_GRID_INTERVAL;
                        int snapToY = ((int) ((obstacle6.getY() + SNAP_GRID_INTERVAL / 2) / SNAP_GRID_INTERVAL)) * SNAP_GRID_INTERVAL;
                        obstacle6.setX(snapToX);
                        obstacle6.setY(snapToY);
                        isObstacle6LongClicked = false;

                        sendObstaclesOneTime(6);
                        dbHelper.insertData(6,(int)obstacles.get(6).getX()/SNAP_GRID_INTERVAL, 19-(int)obstacles.get(6).getY()/SNAP_GRID_INTERVAL,getViewOrientation(obstacle6));
                        isDragging = false;
                        break;
                    default:
                        break;
                }

                if(obstacle6.getX() > maxWidth){
                    obstacle6.setX(maxWidth);
                }
                else if(obstacle6.getX() < 0){
                    obstacle6.setX(0);
                }
                if(obstacle6.getY() > maxHeight){
                    obstacle6.setY(maxHeight);
                }
                else if(obstacle6.getY() < 0){
                    obstacle6.setY(0);
                }

                return false;
            }
        });

        obstacle7.setOnTouchListener(new View.OnTouchListener() {
            int x = 0;
            int y = 0;
            int dx = 0;
            int dy = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isObstacle7LongClicked) {
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        obstacle7.getChildAt(1).setPivotX(obstacle7.getWidth()/2.0f);
                        obstacle7.getChildAt(1).setPivotY(obstacle7.getHeight()/2.0f);

                        obstacle7.getChildAt(1).setRotation((obstacle7.getChildAt(1).getRotation() + 90) % 360);

                    }
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = (int) event.getX();
                        y = (int) event.getY();
                        isDragging = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        dx = (int) event.getX() - x;
                        dy = (int) event.getY() - y;

                        obstacle7.setX(obstacle7.getX() + dx);
                        obstacle7.setY(obstacle7.getY() + dy);
                        break;
                    case MotionEvent.ACTION_UP:
                        int snapToX = ((int) ((obstacle7.getX() + SNAP_GRID_INTERVAL / 2) / SNAP_GRID_INTERVAL)) * SNAP_GRID_INTERVAL;
                        int snapToY = ((int) ((obstacle7.getY() + SNAP_GRID_INTERVAL / 2) / SNAP_GRID_INTERVAL)) * SNAP_GRID_INTERVAL;
                        obstacle7.setX(snapToX);
                        obstacle7.setY(snapToY);
                        isObstacle7LongClicked = false;

                        sendObstaclesOneTime(7);
                        dbHelper.insertData(7,(int)obstacles.get(7).getX()/SNAP_GRID_INTERVAL, 19-(int)obstacles.get(7).getY()/SNAP_GRID_INTERVAL,getViewOrientation(obstacle7));
                        isDragging = false;
                        break;
                    default:
                        break;
                }

                if(obstacle7.getX() > maxWidth){
                    obstacle7.setX(maxWidth);
                }
                else if(obstacle7.getX() < 0){
                    obstacle7.setX(0);
                }
                if(obstacle7.getY() > maxHeight){
                    obstacle7.setY(maxHeight);
                }
                else if(obstacle7.getY() < 0){
                    obstacle7.setY(0);
                }

                return false;
            }
        });

        obstacle8.setOnTouchListener(new View.OnTouchListener() {
            int x = 0;
            int y = 0;
            int dx = 0;
            int dy = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isObstacle8LongClicked) {
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        obstacle8.getChildAt(1).setPivotX(obstacle8.getWidth()/2.0f);
                        obstacle8.getChildAt(1).setPivotY(obstacle8.getHeight()/2.0f);

                        obstacle8.getChildAt(1).setRotation((obstacle8.getChildAt(1).getRotation() + 90) % 360);

                    }
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = (int) event.getX();
                        y = (int) event.getY();
                        isDragging = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        dx = (int) event.getX() - x;
                        dy = (int) event.getY() - y;

                        obstacle8.setX(obstacle8.getX() + dx);
                        obstacle8.setY(obstacle8.getY() + dy);
                        break;
                    case MotionEvent.ACTION_UP:
                        int snapToX = ((int) ((obstacle8.getX() + SNAP_GRID_INTERVAL / 2) / SNAP_GRID_INTERVAL)) * SNAP_GRID_INTERVAL;
                        int snapToY = ((int) ((obstacle8.getY() + SNAP_GRID_INTERVAL / 2) / SNAP_GRID_INTERVAL)) * SNAP_GRID_INTERVAL;
                        obstacle8.setX(snapToX);
                        obstacle8.setY(snapToY);
                        isObstacle8LongClicked = false;


                        sendObstaclesOneTime(8);
                        dbHelper.insertData(8,(int)obstacles.get(8).getX()/SNAP_GRID_INTERVAL, 19-(int)obstacles.get(8).getY()/SNAP_GRID_INTERVAL,getViewOrientation(obstacle8));
                        isDragging = false;
                        break;
                    default:
                        break;
                }

                if(obstacle8.getX() > maxWidth){
                    obstacle8.setX(maxWidth);
                }
                else if(obstacle8.getX() < 0){
                    obstacle8.setX(0);
                }
                if(obstacle8.getY() > maxHeight){
                    obstacle8.setY(maxHeight);
                }
                else if(obstacle8.getY() < 0){
                    obstacle8.setY(0);
                }

                return false;
            }
        });

        // Buttons and Objects initialization
        sendObstaclesButton = getActivity().findViewById(R.id.sendObstaclesButton);
        resetObstacles = getActivity().findViewById(R.id.resetObstaclesButton);
        car = getActivity().findViewById(R.id.car);
        forwardButton = getActivity().findViewById(R.id.forwardButton);
        turnRightButton = getActivity().findViewById(R.id.turnRightButton);
        turnLeftButton = getActivity().findViewById(R.id.turnLeftButton);
        reverseButton = getActivity().findViewById(R.id.reverseButton);
        leftReverseButton = getActivity().findViewById(R.id.leftReverseButton);
        rightReverseButton = getActivity().findViewById(R.id.rightReverseButton);
        putObstacles = getActivity().findViewById(R.id.setObstaclesBtn);
        startbtn = getActivity().findViewById(R.id.startbtn);
        carGenerator = getActivity().findViewById(R.id.car_generator);
        alertButton = getActivity().findViewById(R.id.alertButton);
        musicButton = getActivity().findViewById(R.id.musicButton);
        zombieimage = getActivity().findViewById(R.id.combo_image);

        //set ImageButton resource
        forwardButton.setImageResource(R.drawable.forward);
        turnLeftButton.setImageResource(R.drawable.turn_left);
        turnRightButton.setImageResource(R.drawable.turn_right);
        reverseButton.setImageResource(R.drawable.reverse);
        leftReverseButton.setImageResource(R.drawable.left_reverse);
        rightReverseButton.setImageResource(R.drawable.right_reverse);

        // Set button click events
        sendObstaclesButton.setOnClickListener(view -> sendObstaclesEvent());
        resetObstacles.setOnClickListener(view -> resetObstaclesEvent());
        car.setOnClickListener(view -> carClickEvent());
        forwardButton.setOnClickListener(view -> forwardButtonEvent());
        reverseButton.setOnClickListener(view -> reverseButtonEvent());
        turnRightButton.setOnClickListener(view -> turnRightButtonEvent());
        turnLeftButton.setOnClickListener(view -> turnLeftButtonEvent());
        leftReverseButton.setOnClickListener(view -> LeftReverseButton());
        rightReverseButton.setOnClickListener(view -> RightReverseButton());
        putObstacles.setOnClickListener(view -> putObstaclesButtonEvent());
        startbtn.setOnClickListener(view -> startbtnEvent());
        carGenerator.setOnClickListener(view -> carGeneratorEvent());
        alertButton.setOnClickListener(view -> alertButtonEvent());
        musicButton.setOnClickListener(view -> musicButtonEvent());



    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void carGeneratorEvent(){
        car.setImageResource(R.drawable.car);
        updateRobotPosition(0, 0, 'N');
    }

    private void alertButtonEvent(){
        Random random = new Random();
        int max = 8;
        int rannum = random.nextInt(max) + 1;
        zombieimage.setImageResource(resources.get(rannum));
        if(rannum == 5){
            zombieimage.setX(13*SNAP_GRID_INTERVAL);
            zombieimage.setY(10*SNAP_GRID_INTERVAL);
        }
        else{
            zombieimage.setX((float)8.5*SNAP_GRID_INTERVAL);
            zombieimage.setY(13*SNAP_GRID_INTERVAL);
        }
    }
    private void musicButtonEvent(){
        zombieimage.setImageResource(0);
    }
    private void startbtnEvent(){
        StringBuilder strBuilder =  new StringBuilder();
        strBuilder.append("Start");
        Toast.makeText(getActivity(), strBuilder.toString(), Toast.LENGTH_LONG).show();
        byte[] bytes = strBuilder.toString().getBytes(Charset.defaultCharset());
        BluetoothService.writeMsg(bytes);
    }

    private void sendObstaclesEvent() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("beginImgRec:");

        while(checkOBStr < 8){

            if(!getObstacleString(checkOBStr + 1).equals("outside")){
                stringBuilder.append(checkOBStr + 1).append(",")
                        .append(getObstacleString(checkOBStr + 1))
                        .append(":");
            }
            checkOBStr++;
        }
        checkOBStr = 0;
//        //TODO: get things to be loop;
        Log.d("Sending Obstacles",stringBuilder.toString());
        Toast.makeText(getActivity(), stringBuilder.toString(), Toast.LENGTH_LONG).show();

        byte[] bytes = stringBuilder.toString().getBytes(Charset.defaultCharset());
        BluetoothService.writeMsg(bytes);
    }

    private void sendObstaclesOneTime(int obstacleNum){
        StringBuilder strBuilder =  new StringBuilder();

        if(obstacles.get(obstacleNum).getY() > 19*SNAP_GRID_INTERVAL){
            return;
        }
        else{
            strBuilder.append(obstacleNum).append(',')
                    .append((int)obstacles.get(obstacleNum).getX()/SNAP_GRID_INTERVAL).append(',')
                    .append(19-(int)obstacles.get(obstacleNum).getY()/SNAP_GRID_INTERVAL)
                    .append(',')
                    .append(getViewOrientation(obstacles.get(obstacleNum)));
        }
        Toast.makeText(getActivity(), strBuilder.toString(), Toast.LENGTH_LONG).show();

        byte[] bytes = strBuilder.toString().getBytes(Charset.defaultCharset());
        BluetoothService.writeMsg(bytes);
    }

    private void resetObstaclesEvent(){
        countOs = 0;

        while(countOs < 8){
            obstacles.get(countOs+1).setX((float)(2.5*(1 + countOs) - 1.5)*SNAP_GRID_INTERVAL);
            obstacles.get(countOs+1).setY(21*SNAP_GRID_INTERVAL);

            //rotate the yellow line to the initial state;
            obstacles.get(countOs+1).getChildAt(1).setRotation((360 - obstacles.get(countOs+1).getRotation()) % 360);
            //images.get(countOs+1).setImageResource(resources.get("o" + (countOs+1)));

            //clear the content for the TextView Box
            TextView obstacleID = (TextView)obstacles.get(countOs+1).getChildAt(2);
            obstacleID.setText(Integer.toString(countOs + 1));

            countOs++;
        }
        dbHelper.Clean();

        Log.d("Action","Reset the positions of obstacles");
    }

    //This is used for send obstacles information one time;
    private String getObstacleString(int obstacleNum) {

        FrameLayout obstacle = obstacles.get(obstacleNum);

        if(obstacle.getY() > 19*SNAP_GRID_INTERVAL){
            //here is the message for RPI and algo to receive;
            return "outside";
        }
        else{
            return
                    getViewOrientation(obstacle) +
                            ',' +
                            ((int) obstacle.getX() / SNAP_GRID_INTERVAL) +
                            ',' +
                            (19 - ((int) obstacle.getY() / SNAP_GRID_INTERVAL));
        }
    }

    //This is used to get the yellow line(image) direction;
    private String getViewOrientation(FrameLayout obstacle) {
        switch (((int) ((obstacle.getChildAt(1).getRotation() / 90) % 4 + 4) % 4)) {
            case 0:
                return "n";
            case 1:
                return "e";
            case 2:
                return "s";
            case 3:
                return "w";
            default:
                // Shouldn't reach this case
                return "x";
        }
    }

    //This can reset the car position and also can be used to test some methods;
    private void carClickEvent() {
        //for testing
        updateRobotPosition(0, 0, 'N');


        if(!dbHelper.checkEmpty()){
            List<Obstacle> dataList = dbHelper.getAllData();

            // Now, you have a list of Obstacle objects containing the retrieved data
            for (Obstacle data : dataList) {
                int id = data.getId();
                int x = data.getX();
                int y = data.getY();
                String direction = data.getDirection();

                // Do something with the retrieved data, such as displaying it in your app
                obstacles.get(id).setX(x*SNAP_GRID_INTERVAL);
                obstacles.get(id).setY((19-y)*SNAP_GRID_INTERVAL);
                Log.d("OBx", x +","+y);
                obstacles.get(id).getChildAt(1).setPivotX(obstacles.get(id).getWidth()/2.0f);
                obstacles.get(id).getChildAt(1).setPivotY(obstacles.get(id).getHeight()/2.0f);
                switch(direction){
                    case "n":
                        obstacles.get(id).getChildAt(1).setRotation(0);
                        break;
                    case "e":
                        obstacles.get(id).getChildAt(1).setRotation(90);
                        break;
                    case "s":
                        obstacles.get(id).getChildAt(1).setRotation(180);
                        break;
                    case "w":
                        obstacles.get(id).getChildAt(1).setRotation(270);
                        break;
                }
                // For example, you can log the data:
                Log.d("ObstacleData", "ID: " + id + ", X: " + x + ", Y: " + y + ", Direction: " + direction);
            }
        }

        //Toast.makeText(getActivity(), "sent successfully!",Toast.LENGTH_SHORT).show();

    }

    private void forwardButtonEvent() {
        //byte[] bytes = commands.get("forward").getBytes(Charset.defaultCharset());
        byte[] bytes = String.valueOf('W').getBytes();
        BluetoothService.writeMsg(bytes);

        int orientation = (int) car.getRotation();
        ObjectAnimator animator;
        switch (((orientation / 90) % 4 + 4) % 4) {
            case 0:
                animator = ObjectAnimator.ofFloat(car, "y", car.getY() - SNAP_GRID_INTERVAL);
                animator.setDuration(ANIMATOR_DURATION);
                animator.start();
                break;
            case 1:
                animator = ObjectAnimator.ofFloat(car, "x", car.getX() + SNAP_GRID_INTERVAL);
                animator.setDuration(ANIMATOR_DURATION);
                animator.start();
                break;
            case 2:
                animator = ObjectAnimator.ofFloat(car, "y", car.getY() + SNAP_GRID_INTERVAL);
                animator.setDuration(ANIMATOR_DURATION);
                animator.start();
                break;
            case 3:
                animator = ObjectAnimator.ofFloat(car, "x", car.getX() - SNAP_GRID_INTERVAL);
                animator.setDuration(ANIMATOR_DURATION);
                animator.start();
                break;
            default:
                // Shouldn't reach this case
                break;
        }
    }

    private void reverseButtonEvent() {
        //byte[] bytes = commands.get("reverse").getBytes(Charset.defaultCharset());
        byte[] bytes = String.valueOf('S').getBytes();
        BluetoothService.writeMsg(bytes);

        int orientation = (int) car.getRotation();
        ObjectAnimator animator;
        switch (((orientation / 90) % 4 + 4) % 4) {
            case 0:
                animator = ObjectAnimator.ofFloat(car, "y", car.getY() + SNAP_GRID_INTERVAL);
                animator.setDuration(ANIMATOR_DURATION);
                animator.start();
                break;
            case 1:
                animator = ObjectAnimator.ofFloat(car, "x", car.getX() - SNAP_GRID_INTERVAL);
                animator.setDuration(ANIMATOR_DURATION);
                animator.start();
                break;
            case 2:
                animator = ObjectAnimator.ofFloat(car, "y", car.getY() - SNAP_GRID_INTERVAL);
                animator.setDuration(ANIMATOR_DURATION);
                animator.start();
                break;
            case 3:
                animator = ObjectAnimator.ofFloat(car, "x", car.getX() + SNAP_GRID_INTERVAL);
                animator.setDuration(ANIMATOR_DURATION);
                animator.start();
                break;
            default:
                // Shouldn't reach this case
                break;
        }
    }

    private void turnRightButtonEvent() {
        //byte[] bytes = commands.get("turnRight").getBytes(Charset.defaultCharset());
        byte[] bytes = String.valueOf('E').getBytes();
        BluetoothService.writeMsg(bytes);

        int orientation = (int) car.getRotation();

        ObjectAnimator animatorX;
        ObjectAnimator animatorY;
        ObjectAnimator animatorArc;
        ObjectAnimator rotateAnimator;
        AnimatorSet animatorSet = new AnimatorSet();
        Path path = new Path();

        switch (((orientation / 90) % 4 + 4) % 4) {
            case 0:
                animatorY = ObjectAnimator.ofFloat(car, "y", car.getY() - SNAP_GRID_INTERVAL);
                animatorY.setDuration(ANIMATOR_DURATION);

                path.arcTo(car.getX(),
                        car.getY() - SNAP_GRID_INTERVAL * 3,
                        car.getX() + SNAP_GRID_INTERVAL * 4,
                        car.getY() + SNAP_GRID_INTERVAL,
                        180f,
                        90f,
                        true);

                animatorArc = ObjectAnimator.ofFloat(car, View.X, View.Y, path);
                animatorArc.setDuration(ANIMATOR_DURATION);
                animatorArc.setStartDelay(ANIMATOR_DURATION);

                rotateAnimator = ObjectAnimator.ofFloat(car, "rotation", orientation, orientation + 90);
                rotateAnimator.setDuration(ANIMATOR_DURATION);
                rotateAnimator.setStartDelay(ANIMATOR_DURATION);

                animatorX = ObjectAnimator.ofFloat(car, "x", car.getX() + SNAP_GRID_INTERVAL * 3);
                animatorX.setDuration(ANIMATOR_DURATION);
                animatorX.setStartDelay(ANIMATOR_DURATION * 2);

                animatorSet.playTogether(animatorY, animatorArc, rotateAnimator, animatorX);
                animatorSet.start();
                break;
            case 1:
                animatorX = ObjectAnimator.ofFloat(car, "x", car.getX() + SNAP_GRID_INTERVAL);
                animatorX.setDuration(ANIMATOR_DURATION);

                path.arcTo(car.getX() - SNAP_GRID_INTERVAL,
                        car.getY(),
                        car.getX() + SNAP_GRID_INTERVAL * 3,
                        car.getY() + SNAP_GRID_INTERVAL * 4,
                        270f,
                        90f,
                        true);

                animatorArc = ObjectAnimator.ofFloat(car, View.X, View.Y, path);
                animatorArc.setDuration(ANIMATOR_DURATION);
                animatorArc.setStartDelay(ANIMATOR_DURATION);

                rotateAnimator = ObjectAnimator.ofFloat(car, "rotation", orientation, orientation + 90);
                rotateAnimator.setDuration(ANIMATOR_DURATION);
                rotateAnimator.setStartDelay(ANIMATOR_DURATION);

                animatorY = ObjectAnimator.ofFloat(car, "y", car.getY() + SNAP_GRID_INTERVAL * 3);
                animatorY.setDuration(ANIMATOR_DURATION);
                animatorY.setStartDelay(ANIMATOR_DURATION * 2);

                animatorSet.playTogether(animatorY, animatorArc, rotateAnimator, animatorX);
                animatorSet.start();
                break;
            case 2:
                animatorY = ObjectAnimator.ofFloat(car, "y", car.getY() + SNAP_GRID_INTERVAL);
                animatorY.setDuration(ANIMATOR_DURATION);

                path.arcTo(car.getX() - SNAP_GRID_INTERVAL * 4,
                        car.getY() + SNAP_GRID_INTERVAL,
                        car.getX(),
                        car.getY() + SNAP_GRID_INTERVAL * 3,
                        0f,
                        90f,
                        true);

                animatorArc = ObjectAnimator.ofFloat(car, View.X, View.Y, path);
                animatorArc.setDuration(ANIMATOR_DURATION);
                animatorArc.setStartDelay(ANIMATOR_DURATION);

                rotateAnimator = ObjectAnimator.ofFloat(car, "rotation", orientation, orientation + 90);
                rotateAnimator.setDuration(ANIMATOR_DURATION);
                rotateAnimator.setStartDelay(ANIMATOR_DURATION);

                animatorX = ObjectAnimator.ofFloat(car, "x", car.getX() - SNAP_GRID_INTERVAL * 3);
                animatorX.setDuration(ANIMATOR_DURATION);
                animatorX.setStartDelay(ANIMATOR_DURATION * 2);

                animatorSet.playTogether(animatorY, animatorArc, rotateAnimator, animatorX);
                animatorSet.start();
                break;
            case 3:
                animatorX = ObjectAnimator.ofFloat(car, "x", car.getX() - SNAP_GRID_INTERVAL);
                animatorX.setDuration(ANIMATOR_DURATION);

                path.arcTo(car.getX() - SNAP_GRID_INTERVAL * 3,
                        car.getY() - SNAP_GRID_INTERVAL * 4,
                        car.getX() + SNAP_GRID_INTERVAL,
                        car.getY(),
                        90f,
                        90f,
                        true);

                animatorArc = ObjectAnimator.ofFloat(car, View.X, View.Y, path);
                animatorArc.setDuration(ANIMATOR_DURATION);
                animatorArc.setStartDelay(ANIMATOR_DURATION);

                rotateAnimator = ObjectAnimator.ofFloat(car, "rotation", orientation, orientation + 90);
                rotateAnimator.setDuration(ANIMATOR_DURATION);
                rotateAnimator.setStartDelay(ANIMATOR_DURATION);

                animatorY = ObjectAnimator.ofFloat(car, "y", car.getY() - SNAP_GRID_INTERVAL * 3);
                animatorY.setDuration(ANIMATOR_DURATION);
                animatorY.setStartDelay(ANIMATOR_DURATION * 2);

                animatorSet.playTogether(animatorY, animatorArc, rotateAnimator, animatorX);
                animatorSet.start();
                break;
            default:
                // Shouldn't reach this case
                break;
        }
    }

    private void turnLeftButtonEvent() {
        //byte[] bytes = commands.get("turnLeft").getBytes(Charset.defaultCharset());
        byte[] bytes = String.valueOf('Q').getBytes();
        BluetoothService.writeMsg(bytes);

        int orientation = (int) car.getRotation();

        ObjectAnimator animatorX;
        ObjectAnimator animatorY;
        ObjectAnimator animatorArc;
        ObjectAnimator rotateAnimator;
        AnimatorSet animatorSet = new AnimatorSet();
        Path path = new Path();

        switch (((orientation / 90) % 4 + 4) % 4) {
            case 0:
                animatorY = ObjectAnimator.ofFloat(car, "y", car.getY() - SNAP_GRID_INTERVAL);
                animatorY.setDuration(ANIMATOR_DURATION);

                path.arcTo(car.getX() - SNAP_GRID_INTERVAL * 4,
                        car.getY() - SNAP_GRID_INTERVAL * 3,
                        car.getX(),
                        car.getY() + SNAP_GRID_INTERVAL,
                        0f,
                        -90f,
                        true);

                animatorArc = ObjectAnimator.ofFloat(car, View.X, View.Y, path);
                animatorArc.setDuration(ANIMATOR_DURATION);
                animatorArc.setStartDelay(ANIMATOR_DURATION);

                rotateAnimator = ObjectAnimator.ofFloat(car, "rotation", orientation, orientation - 90);
                rotateAnimator.setDuration(ANIMATOR_DURATION);
                rotateAnimator.setStartDelay(ANIMATOR_DURATION);

                animatorX = ObjectAnimator.ofFloat(car, "x", car.getX() - SNAP_GRID_INTERVAL * 3);
                animatorX.setDuration(ANIMATOR_DURATION);
                animatorX.setStartDelay(ANIMATOR_DURATION * 2);

                animatorSet.playTogether(animatorY, animatorArc, rotateAnimator, animatorX);
                animatorSet.start();
                break;
            case 1:
                animatorX = ObjectAnimator.ofFloat(car, "x", car.getX() + SNAP_GRID_INTERVAL);
                animatorX.setDuration(ANIMATOR_DURATION);

                path.arcTo(car.getX() - SNAP_GRID_INTERVAL,
                        car.getY() - SNAP_GRID_INTERVAL * 4,
                        car.getX() + SNAP_GRID_INTERVAL * 3,
                        car.getY(),
                        90f,
                        -90f,
                        true);

                animatorArc = ObjectAnimator.ofFloat(car, View.X, View.Y, path);
                animatorArc.setDuration(ANIMATOR_DURATION);
                animatorArc.setStartDelay(ANIMATOR_DURATION);

                rotateAnimator = ObjectAnimator.ofFloat(car, "rotation", orientation, orientation - 90);
                rotateAnimator.setDuration(ANIMATOR_DURATION);
                rotateAnimator.setStartDelay(ANIMATOR_DURATION);

                animatorY = ObjectAnimator.ofFloat(car, "y", car.getY() - SNAP_GRID_INTERVAL * 3);
                animatorY.setDuration(ANIMATOR_DURATION);
                animatorY.setStartDelay(ANIMATOR_DURATION * 2);

                animatorSet.playTogether(animatorY, animatorArc, rotateAnimator, animatorX);
                animatorSet.start();
                break;
            case 2:
                animatorY = ObjectAnimator.ofFloat(car, "y", car.getY() + SNAP_GRID_INTERVAL);
                animatorY.setDuration(ANIMATOR_DURATION);

                path.arcTo(car.getX(),
                        car.getY() + SNAP_GRID_INTERVAL,
                        car.getX() + SNAP_GRID_INTERVAL * 4,
                        car.getY() + SNAP_GRID_INTERVAL * 3,
                        180f,
                        -90f,
                        true);

                animatorArc = ObjectAnimator.ofFloat(car, View.X, View.Y, path);
                animatorArc.setDuration(ANIMATOR_DURATION);
                animatorArc.setStartDelay(ANIMATOR_DURATION);

                rotateAnimator = ObjectAnimator.ofFloat(car, "rotation", orientation, orientation - 90);
                rotateAnimator.setDuration(ANIMATOR_DURATION);
                rotateAnimator.setStartDelay(ANIMATOR_DURATION);

                animatorX = ObjectAnimator.ofFloat(car, "x", car.getX() + SNAP_GRID_INTERVAL * 3);
                animatorX.setDuration(ANIMATOR_DURATION);
                animatorX.setStartDelay(ANIMATOR_DURATION * 2);

                animatorSet.playTogether(animatorY, animatorArc, rotateAnimator, animatorX);
                animatorSet.start();
                break;
            case 3:
                animatorX = ObjectAnimator.ofFloat(car, "x", car.getX() - SNAP_GRID_INTERVAL);
                animatorX.setDuration(ANIMATOR_DURATION);

                path.arcTo(car.getX() - SNAP_GRID_INTERVAL * 3,
                        car.getY(),
                        car.getX() + SNAP_GRID_INTERVAL,
                        car.getY() + SNAP_GRID_INTERVAL * 4,
                        270f,
                        -90f,
                        true);

                animatorArc = ObjectAnimator.ofFloat(car, View.X, View.Y, path);
                animatorArc.setDuration(ANIMATOR_DURATION);
                animatorArc.setStartDelay(ANIMATOR_DURATION);

                rotateAnimator = ObjectAnimator.ofFloat(car, "rotation", orientation, orientation - 90);
                rotateAnimator.setDuration(ANIMATOR_DURATION);
                rotateAnimator.setStartDelay(ANIMATOR_DURATION);

                animatorY = ObjectAnimator.ofFloat(car, "y", car.getY() + SNAP_GRID_INTERVAL * 3);
                animatorY.setDuration(ANIMATOR_DURATION);
                animatorY.setStartDelay(ANIMATOR_DURATION * 2);

                animatorSet.playTogether(animatorY, animatorArc, rotateAnimator, animatorX);
                animatorSet.start();
                break;
            default:
                // Shouldn't reach this case
                break;
        }
    }

    private void LeftReverseButton(){
        byte[] bytes = String.valueOf('A').getBytes();
        BluetoothService.writeMsg(bytes);

        int orientation = (int) car.getRotation();

        ObjectAnimator animatorX;
        ObjectAnimator animatorY;
        ObjectAnimator animatorArc;
        ObjectAnimator rotateAnimator;
        AnimatorSet animatorSet = new AnimatorSet();
        Path path = new Path();

        switch (((orientation / 90) % 4 + 4) % 4) {
            case 0:
                animatorY = ObjectAnimator.ofFloat(car, "y", car.getY() + SNAP_GRID_INTERVAL);
                animatorY.setDuration(ANIMATOR_DURATION);

                path.arcTo(car.getX()- SNAP_GRID_INTERVAL * 4,
                        car.getY() - SNAP_GRID_INTERVAL,
                        car.getX(),
                        car.getY() + SNAP_GRID_INTERVAL * 3,
                        0f,
                        90f,
                        true);

                animatorArc = ObjectAnimator.ofFloat(car, View.X, View.Y, path);
                animatorArc.setDuration(ANIMATOR_DURATION);
                animatorArc.setStartDelay(ANIMATOR_DURATION);

                rotateAnimator = ObjectAnimator.ofFloat(car, "rotation", orientation, orientation + 90);
                rotateAnimator.setDuration(ANIMATOR_DURATION);
                rotateAnimator.setStartDelay(ANIMATOR_DURATION);

                animatorX = ObjectAnimator.ofFloat(car, "x", car.getX() - SNAP_GRID_INTERVAL * 3);
                animatorX.setDuration(ANIMATOR_DURATION);
                animatorX.setStartDelay(ANIMATOR_DURATION * 2);

                animatorSet.playTogether(animatorY, animatorArc, rotateAnimator, animatorX);
                animatorSet.start();

                break;
            case 1:
                animatorX = ObjectAnimator.ofFloat(car, "x", car.getX() - SNAP_GRID_INTERVAL);
                animatorX.setDuration(ANIMATOR_DURATION);

                path.arcTo(car.getX() - SNAP_GRID_INTERVAL * 3,
                        car.getY() - SNAP_GRID_INTERVAL * 4,
                        car.getX() + SNAP_GRID_INTERVAL,
                        car.getY(),
                        90f,
                        90f,
                        true);

                animatorArc = ObjectAnimator.ofFloat(car, View.X, View.Y, path);
                animatorArc.setDuration(ANIMATOR_DURATION);
                animatorArc.setStartDelay(ANIMATOR_DURATION);

                rotateAnimator = ObjectAnimator.ofFloat(car, "rotation", orientation, orientation + 90);
                rotateAnimator.setDuration(ANIMATOR_DURATION);
                rotateAnimator.setStartDelay(ANIMATOR_DURATION);

                animatorY = ObjectAnimator.ofFloat(car, "y", car.getY() - SNAP_GRID_INTERVAL * 3);
                animatorY.setDuration(ANIMATOR_DURATION);
                animatorY.setStartDelay(ANIMATOR_DURATION * 2);

                animatorSet.playTogether(animatorY, animatorArc, rotateAnimator, animatorX);
                animatorSet.start();
                break;
            case 2:
                animatorY = ObjectAnimator.ofFloat(car, "y", car.getY() - SNAP_GRID_INTERVAL);
                animatorY.setDuration(ANIMATOR_DURATION);

                path.arcTo(car.getX(),
                        car.getY() - SNAP_GRID_INTERVAL * 3,
                        car.getX() + SNAP_GRID_INTERVAL * 4,
                        car.getY() - SNAP_GRID_INTERVAL,
                        180f,
                        90f,
                        true);

                animatorArc = ObjectAnimator.ofFloat(car, View.X, View.Y, path);
                animatorArc.setDuration(ANIMATOR_DURATION);
                animatorArc.setStartDelay(ANIMATOR_DURATION);

                rotateAnimator = ObjectAnimator.ofFloat(car, "rotation", orientation, orientation + 90);
                rotateAnimator.setDuration(ANIMATOR_DURATION);
                rotateAnimator.setStartDelay(ANIMATOR_DURATION);

                animatorX = ObjectAnimator.ofFloat(car, "x", car.getX() + SNAP_GRID_INTERVAL * 3);
                animatorX.setDuration(ANIMATOR_DURATION);
                animatorX.setStartDelay(ANIMATOR_DURATION * 2);

                animatorSet.playTogether(animatorY, animatorArc, rotateAnimator, animatorX);
                animatorSet.start();
                break;
            case 3:
                animatorX = ObjectAnimator.ofFloat(car, "x", car.getX() + SNAP_GRID_INTERVAL);
                animatorX.setDuration(ANIMATOR_DURATION);

                path.arcTo(car.getX() - SNAP_GRID_INTERVAL,
                        car.getY(),
                        car.getX() + SNAP_GRID_INTERVAL * 3,
                        car.getY() + SNAP_GRID_INTERVAL * 4,
                        270f,
                        90f,
                        true);

                animatorArc = ObjectAnimator.ofFloat(car, View.X, View.Y, path);
                animatorArc.setDuration(ANIMATOR_DURATION);
                animatorArc.setStartDelay(ANIMATOR_DURATION);

                rotateAnimator = ObjectAnimator.ofFloat(car, "rotation", orientation, orientation + 90);
                rotateAnimator.setDuration(ANIMATOR_DURATION);
                rotateAnimator.setStartDelay(ANIMATOR_DURATION);

                animatorY = ObjectAnimator.ofFloat(car, "y", car.getY() + SNAP_GRID_INTERVAL * 3);
                animatorY.setDuration(ANIMATOR_DURATION);
                animatorY.setStartDelay(ANIMATOR_DURATION * 2);

                animatorSet.playTogether(animatorY, animatorArc, rotateAnimator, animatorX);
                animatorSet.start();
                break;
            default:
                // Shouldn't reach this case
                break;
        }

    }

    private void RightReverseButton(){
        byte[] bytes = String.valueOf('D').getBytes();
        BluetoothService.writeMsg(bytes);

        int orientation = (int) car.getRotation();

        ObjectAnimator animatorX;
        ObjectAnimator animatorY;
        ObjectAnimator animatorArc;
        ObjectAnimator rotateAnimator;
        AnimatorSet animatorSet = new AnimatorSet();
        Path path = new Path();

        switch (((orientation / 90) % 4 + 4) % 4) {
            case 0:
                animatorY = ObjectAnimator.ofFloat(car, "y", car.getY() + SNAP_GRID_INTERVAL);
                animatorY.setDuration(ANIMATOR_DURATION);

                path.arcTo(car.getX(),
                        car.getY() - SNAP_GRID_INTERVAL,
                        car.getX() + SNAP_GRID_INTERVAL * 4,
                        car.getY() + SNAP_GRID_INTERVAL * 3,
                        180f,
                        -90f,
                        true);

                animatorArc = ObjectAnimator.ofFloat(car, View.X, View.Y, path);
                animatorArc.setDuration(ANIMATOR_DURATION);
                animatorArc.setStartDelay(ANIMATOR_DURATION);

                rotateAnimator = ObjectAnimator.ofFloat(car, "rotation", orientation, orientation - 90);
                rotateAnimator.setDuration(ANIMATOR_DURATION);
                rotateAnimator.setStartDelay(ANIMATOR_DURATION);

                animatorX = ObjectAnimator.ofFloat(car, "x", car.getX() + SNAP_GRID_INTERVAL * 3);
                animatorX.setDuration(ANIMATOR_DURATION);
                animatorX.setStartDelay(ANIMATOR_DURATION * 2);

                animatorSet.playTogether(animatorY, animatorArc, rotateAnimator, animatorX);
                animatorSet.start();
                break;
            case 1:
                animatorX = ObjectAnimator.ofFloat(car, "x", car.getX() - SNAP_GRID_INTERVAL);
                animatorX.setDuration(ANIMATOR_DURATION);

                path.arcTo(car.getX() - SNAP_GRID_INTERVAL * 3,
                        car.getY(),
                        car.getX() + SNAP_GRID_INTERVAL,
                        car.getY() + SNAP_GRID_INTERVAL * 4,
                        270f,
                        -90f,
                        true);

                animatorArc = ObjectAnimator.ofFloat(car, View.X, View.Y, path);
                animatorArc.setDuration(ANIMATOR_DURATION);
                animatorArc.setStartDelay(ANIMATOR_DURATION);

                rotateAnimator = ObjectAnimator.ofFloat(car, "rotation", orientation, orientation - 90);
                rotateAnimator.setDuration(ANIMATOR_DURATION);
                rotateAnimator.setStartDelay(ANIMATOR_DURATION);

                animatorY = ObjectAnimator.ofFloat(car, "y", car.getY() + SNAP_GRID_INTERVAL * 3);
                animatorY.setDuration(ANIMATOR_DURATION);
                animatorY.setStartDelay(ANIMATOR_DURATION * 2);

                animatorSet.playTogether(animatorY, animatorArc, rotateAnimator, animatorX);
                animatorSet.start();
                break;
            case 2:
                animatorY = ObjectAnimator.ofFloat(car, "y", car.getY() - SNAP_GRID_INTERVAL);
                animatorY.setDuration(ANIMATOR_DURATION);

                path.arcTo(car.getX() - SNAP_GRID_INTERVAL * 4,
                        car.getY() - SNAP_GRID_INTERVAL * 3,
                        car.getX(),
                        car.getY() - SNAP_GRID_INTERVAL,
                        0f,
                        -90f,
                        true);

                animatorArc = ObjectAnimator.ofFloat(car, View.X, View.Y, path);
                animatorArc.setDuration(ANIMATOR_DURATION);
                animatorArc.setStartDelay(ANIMATOR_DURATION);

                rotateAnimator = ObjectAnimator.ofFloat(car, "rotation", orientation, orientation - 90);
                rotateAnimator.setDuration(ANIMATOR_DURATION);
                rotateAnimator.setStartDelay(ANIMATOR_DURATION);

                animatorX = ObjectAnimator.ofFloat(car, "x", car.getX() - SNAP_GRID_INTERVAL * 3);
                animatorX.setDuration(ANIMATOR_DURATION);
                animatorX.setStartDelay(ANIMATOR_DURATION * 2);

                animatorSet.playTogether(animatorY, animatorArc, rotateAnimator, animatorX);
                animatorSet.start();
                break;
            case 3:
                animatorX = ObjectAnimator.ofFloat(car, "x", car.getX() + SNAP_GRID_INTERVAL);
                animatorX.setDuration(ANIMATOR_DURATION);

                path.arcTo(car.getX() - SNAP_GRID_INTERVAL,
                        car.getY() - SNAP_GRID_INTERVAL * 4,
                        car.getX() + SNAP_GRID_INTERVAL * 3,
                        car.getY(),
                        90f,
                        -90f,
                        true);

                animatorArc = ObjectAnimator.ofFloat(car, View.X, View.Y, path);
                animatorArc.setDuration(ANIMATOR_DURATION);
                animatorArc.setStartDelay(ANIMATOR_DURATION);

                rotateAnimator = ObjectAnimator.ofFloat(car, "rotation", orientation, orientation - 90);
                rotateAnimator.setDuration(ANIMATOR_DURATION);
                rotateAnimator.setStartDelay(ANIMATOR_DURATION);

                animatorY = ObjectAnimator.ofFloat(car, "y", car.getY() - SNAP_GRID_INTERVAL * 3);
                animatorY.setDuration(ANIMATOR_DURATION);
                animatorY.setStartDelay(ANIMATOR_DURATION * 2);

                animatorSet.playTogether(animatorY, animatorArc, rotateAnimator, animatorX);
                animatorSet.start();
                break;
            default:
                // Shouldn't reach this case
                break;
        }
    }

    //set the target ID text in the white color;
    private void setObstacleID(int obstacleNumber, String imageID){
        TextView obstacleID = (TextView)obstacles.get(obstacleNumber).getChildAt(2);
        obstacleID.setText(imageID);
    }

    private void updateRobotPosition(int x, int y, char direction) {

        switch (direction) {
            case 'N':
                car.setRotation(0);

                car.setX(x * SNAP_GRID_INTERVAL);
                car.setY((20 - y) * SNAP_GRID_INTERVAL - car.getHeight());
                break;
            case 'E':
                car.setRotation(90);

                car.setX(x * SNAP_GRID_INTERVAL);
                car.setY((20 - y) * SNAP_GRID_INTERVAL);
                break;
            case 'S':
                car.setRotation(180);

                car.setX(x * SNAP_GRID_INTERVAL + car.getWidth());
                car.setY((20 - y) * SNAP_GRID_INTERVAL);
                break;
            case 'W':
                car.setRotation(270);

                car.setX(x * SNAP_GRID_INTERVAL + car.getWidth());
                car.setY((20 - y) * SNAP_GRID_INTERVAL - car.getHeight());
                break;
            default:
                // Shouldn't reach this case
                break;
        }
    }

    //set a Dialog caller
    public void showMyDialog() {
        PutObstaclesDialog dialogFragment = new PutObstaclesDialog();
        dialogFragment.setTargetFragment(this, 0); // Set the target fragment
        dialogFragment.show(getFragmentManager(), "MyDialogFragment");
    }

    // Receive values from the dialog and update the FrameLayout position
    public void applyValues(String obsnum, String xValue, String yValue) {
        // Parse the values and update the FrameLayout position
        // For example:
        float x = Float.parseFloat(xValue);
        float y = Float.parseFloat(yValue);
        int obnum = Integer.parseInt(obsnum);
        obstacles.get(obnum).setX(x*SNAP_GRID_INTERVAL);
        obstacles.get(obnum).setY((19-y)*SNAP_GRID_INTERVAL);

        sendObstaclesOneTime(obnum);
    }

    private void putObstaclesButtonEvent(){
        showMyDialog();
    }

    //Broadcast Receiver for incoming message
    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Message", "Receiving Message!");

            String message = intent.getStringExtra("receivingMsg").trim();

            Log.d("msg", message);

            if(!message.equals(lastReceivedMessage)){
                String command = message.substring(0, message.indexOf(','));

                String[] parts = message.split(",");

                switch (command) {
                    case "ROBOT":

                        int x = Integer.parseInt(parts[1]);
                        int y = Integer.parseInt(parts[2]);
                        char direction = parts[3].charAt(0);

                        Log.d("ROBOT", "(x: " + x + ") (y: " + y + ") (direction: " + direction + ")");



                        updateRobotPosition(x, y, direction);
                        break;

                    //car status receiver part and corresponding textview box for this;
                    case "STATUS":
                        String status = message.substring((message.indexOf(',') + 1));
                        break;

                    case "TARGET":

                        int obstacleNumber = Integer.parseInt(parts[1]);
                        String imageID = parts[2];

                        //int obstacleNumber = Character.getNumericValue(message.charAt(7));

                        //String imageID = message.substring(9);

                        if(!checkUpdate[obstacleNumber - 1].equals(imageID)){
                            Log.d("TARGET", "(obstacleNumber: " + obstacleNumber + ") (targetId: " + imageID + ")");

                            //setObstacleImage(obstacleNumber,targetId);
                            //update the obstacle with updating textview;
                            setObstacleID(obstacleNumber,imageID);

                            StringBuilder stringBack = new StringBuilder();
                            stringBack.append("Target ")
                                    .append(obstacleNumber)
                                    .append(" updated successfully");

                            byte[] bytes = stringBack.toString().getBytes(Charset.defaultCharset());
                            BluetoothService.writeMsg(bytes);

                            stringBack.setLength(0);

                            checkUpdate[obstacleNumber - 1] = imageID;
                        }

                        break;
                    default:
                        break;
                }
            }
            lastReceivedMessage = message;

        }
    };
}