package com.example.mdptest;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Path;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.Map;

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

    private boolean isDragging = false;

    private int sequence = 0;

    Button sendObstaclesButton;

    Button resetObstacles;

    ImageButton forwardButton, turnLeftButton, turnRightButton,reverseButton, leftReverseButton, rightReverseButton;

    ImageView image1, image2, image3, image4, image5;
    FrameLayout obstacle1, obstacle2, obstacle3, obstacle4, obstacle5;
    TextView robot_status;
    float maxWidth;
    float maxHeight;
    int countOs = 0;
    ImageView car;

    String dataToSave;

    Map<Integer, FrameLayout> obstacles;
    Map<Integer, ImageView> images;

    Map<String, String> commands = new HashMap<String, String>() {{
        put("forward", "0100");
        put("reverse", "0101");
        put("turnLeft", "0902");
        put("turnRight", "0903");
    }};

    Map<String, Integer> resources = new HashMap<String, Integer>() {{
        put("o1", R.drawable.obstacle1);

        put("o2", R.drawable.obstacle2);

        put("o3", R.drawable.obstacle3);

        put("o4", R.drawable.obstacle4);

        put("o5", R.drawable.obstacle5);

        put("1", R.drawable.number_1);
        put("2", R.drawable.number_2);
        put("3", R.drawable.number_3);
        put("4", R.drawable.number_4);
        put("5", R.drawable.number_5);
        put("6", R.drawable.number_6);
        put("7", R.drawable.number_7);
        put("8", R.drawable.number_8);
        put("9", R.drawable.number_9);

        put("A", R.drawable.alphabet_a);
        put("B", R.drawable.alphabet_b);
        put("C", R.drawable.alphabet_c);
        put("D", R.drawable.alphabet_d);
        put("E", R.drawable.alphabet_e);
        put("F", R.drawable.alphabet_f);
        put("G", R.drawable.alphabet_g);
        put("H", R.drawable.alphabet_h);
        put("S", R.drawable.alphabet_s);
        put("T", R.drawable.alphabet_t);
        put("U", R.drawable.alphabet_u);
        put("V", R.drawable.alphabet_v);
        put("W", R.drawable.alphabet_w);
        put("X", R.drawable.alphabet_x);
        put("Y", R.drawable.alphabet_y);
        put("Z", R.drawable.alphabet_z);

        put("up", R.drawable.arrow_up);
        put("down", R.drawable.arrow_down);
        put("left", R.drawable.arrow_left);
        put("right", R.drawable.arrow_right);

        put("bulls", R.drawable.bullseye);
        put("circle", R.drawable.circle);
    }};

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        if(savedInstanceState != null){
            dataToSave = savedInstanceState.getString("key");
        }
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

        obstacles = new HashMap<Integer, FrameLayout>() {{
            put(1, obstacle1);
            put(2, obstacle2);
            put(3, obstacle3);
            put(4, obstacle4);
            put(5, obstacle5);
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
        robot_status = getActivity().findViewById(R.id.RobotStatus);

        //set ImageButton resource
        forwardButton.setImageResource(R.drawable.forward);
        turnLeftButton.setImageResource(R.drawable.turn_left);
        turnRightButton.setImageResource(R.drawable.turn_right);
        reverseButton.setImageResource(R.drawable.reverse);
        leftReverseButton.setImageResource(R.drawable.left_reverse);
        rightReverseButton.setImageResource(R.drawable.right_reverse);


        //set TextView box scrollable
        robot_status.setMovementMethod(new ScrollingMovementMethod());

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
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("key", dataToSave);
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }

    private void sendObstaclesEvent() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("beginImgRec:1")
                .append(getObstacleString(obstacle1)).append(":2")
                .append(getObstacleString(obstacle2)).append(":3")
                .append(getObstacleString(obstacle3)).append(":4")
                .append(getObstacleString(obstacle4)).append(":5")
                .append(getObstacleString(obstacle5)).append(':');
        //TODO: get things to be loop;
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

        while(countOs < 5){
            obstacles.get(countOs+1).setX(272+countOs*55);
            obstacles.get(countOs+1).setY(873);

            //rotate the yellow line to the initial state;
            obstacles.get(countOs+1).getChildAt(1).setRotation((360 - obstacles.get(countOs+1).getRotation()) % 360);
            images.get(countOs+1).setImageResource(resources.get("o" + (countOs+1)));

            //clear the content for the TextView Box
            TextView obstacleID = (TextView)obstacles.get(countOs+1).getChildAt(2);
            obstacleID.setText("");

            countOs++;
        }

        Log.d("Action","Reset the positions of obstacles");
    }

    //This is used for send obstacles information one time;
    private String getObstacleString(FrameLayout obstacle) {
        if(obstacle.getY() > 19*SNAP_GRID_INTERVAL){
            //here is the message for RPI and algo to receive;
            return
                    //"O," +
                    ',' +
                            getViewOrientation(obstacle) +
                            ',' +
                            -1 +
                            ',' +
                            -1;
        }
        else{
            return
                    //"O," +
                    ',' + getViewOrientation(obstacle) +
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
                return "N";
            case 1:
                return "E";
            case 2:
                return "S";
            case 3:
                return "W";
            default:
                // Shouldn't reach this case
                return "X";
        }
    }

    //This is used to get the car direction;
    private String getImageOrientation(ImageView car) {
        switch (((int) ((car.getRotation() / 90) % 4 + 4) % 4)) {
            case 0:
                return "N";
            case 1:
                return "E";
            case 2:
                return "S";
            case 3:
                return "W";
            default:
                // Shouldn't reach this case
                return "X";
        }
    }

    //This can reset the car position and also can be used to test some methods;
    private void carClickEvent() {
        //for testing
        updateRobotPosition(0, 0, 'N');
//        setObstacleID(1,"12");
//        setObstacleID(2,"22");
//        setObstacleID(3,"45");
//        setObstacleID(4,"101");
//        setObstacleID(5,"20");

        String string = "Car (X: " + (int)car.getX()/SNAP_GRID_INTERVAL + ") " +
                "(Y: " + (20 - ((int)car.getY() + car.getHeight())/SNAP_GRID_INTERVAL) + ") " +
                "Facing: " + getImageOrientation(car);
        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();

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

    //TODO: not set obstacle image, just set the target ID text in the white color;
    private void setObstacleID(int obstacleNumber, String imageID){
        TextView obstacleID = (TextView)obstacles.get(obstacleNumber).getChildAt(2);
        obstacleID.setText(imageID);
        images.get(obstacleNumber).setImageResource(R.drawable.updated_background);

        return;
    }

    //This will be used if images need to be updated;
    private void setObstacleImage(int obstacleNumber, String image) {
        // If input values are invalid, log them and return
        if (!images.containsKey(obstacleNumber) || !resources.containsKey(image)) {
            Log.d("Set Obstacle Image", "obstacleNumber = " + obstacleNumber);
            Log.d("Set Obstacle Image", "image = " + image);
            return;
        }

        images.get(obstacleNumber).setImageResource(resources.get(image));
        images.get(obstacleNumber).setRotation(0);


    }

    //private void updateRobot(int x, int y, char direction, String status){}
    //otherwise maybe get another receiver case to get status;
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

    //This is used to receive the car status information;
    private void updateRobotStatus(String status){
        String newStatus = "RobotStatus: " + status;
        robot_status.setText(newStatus);
    }

    //Broadcast Receiver for incoming message
    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Message", "Receiving Message!");

            String message = intent.getStringExtra("receivingMsg").trim();

            Log.d("msg", message);

            String command = message.substring(0, message.indexOf(','));

            switch (command) {
                case "ROBOT":
                    String[] parts = message.split(",");

                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    char direction = parts[3].charAt(0);

                    Log.d("ROBOT", "(x: " + x + ") (y: " + y + ") (direction: " + direction + ")");



                    updateRobotPosition(x, y, direction);
                    break;

                //car status receiver part and corresponding textview box for this;
                case "STATUS":
                    String status = message.substring((message.indexOf(',') + 1));
                    updateRobotStatus(status);
                    break;

                case "TARGET":
                    int obstacleNumber = Character.getNumericValue(message.charAt(7));
                    //String targetId = message.substring(9);
                    String imageID = message.substring(9);

                    Log.d("TARGET", "(obstacleNumber: " + obstacleNumber + ") (targetId: " + imageID + ")");

                    //setObstacleImage(obstacleNumber,targetId);
                    //update the obstacle with updating textview;
                    setObstacleID(obstacleNumber,imageID);

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Target ")
                            .append(obstacleNumber)
                            .append(" updated successfully");

                    byte[] bytes = stringBuilder.toString().getBytes(Charset.defaultCharset());
                    BluetoothService.writeMsg(bytes);

                    break;
                default:
                    break;
            }
        }
    };
}