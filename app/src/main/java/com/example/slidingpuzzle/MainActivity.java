package com.example.slidingpuzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private static GestureDetectGridView mGridView;

    private static final int COLUMNS = 3;
    public static final int DIMENSIONS = COLUMNS * COLUMNS;

    private static int mColumnWidth, mColumnHeight;

    public static final String up = "up";
    public static final String down = "down";
    public static final String left = "left";
    public static final String right = "right";

    private static String[] tileList;
    private static ArrayList<Drawable> tileImages;
    private Bitmap image;

    Bundle mBundle;
    private int level;
    private String timer, imageString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        getBundle();
        scramble();
        setDimensions();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void getBundle(){
        mBundle = getIntent().getBundleExtra("setUp");
        level = mBundle.getInt("level");
        imageString = mBundle.getString("imageUri");
        int mImageFullId = getResources().getIdentifier(imageString, "drawable", getPackageName());
        if(mImageFullId==0){
            Uri mImageUri = Uri.parse(imageString);
            try {
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                tileImages = splitImage(image, level*level);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable drawable = (BitmapDrawable) getDrawable(mImageFullId);
            image = drawable.getBitmap();
            tileImages = splitImage(image, level*level);
            tileImages.add(getDrawable(getResources().getIdentifier("tile", "drawable", getPackageName())));
        }
    }
    private void init() {
        mGridView = (GestureDetectGridView) findViewById(R.id.grid);
        mGridView.setNumColumns(COLUMNS);
        tileList = new String[DIMENSIONS+1];
        for (int i = 0; i < tileList.length; i++) {
            tileList[i] = String.valueOf(i);
        }
    }
    public void refresh(View view){
        tileList = new String[DIMENSIONS+1];
        for (int i = 0; i < tileList.length; i++) {
            tileList[i] = String.valueOf(i);
        }
        scramble();
        GestureDetectGridView.blankPosition = DIMENSIONS;
        display(getApplicationContext());
    }
    private void scramble() {
        int index;
        String temp;
        Random random = new Random();

        for (int i = tileList.length - 2; i > 0; i--) {
            index = random.nextInt(i + 1);
            if (index!=tileList.length-COLUMNS-1 && i!=tileList.length-COLUMNS-1) {
                temp = tileList[index];
                tileList[index] = tileList[i];
                tileList[i] = temp;
            }
        }
    }

    private void setDimensions() {
        ViewTreeObserver vto = mGridView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int displayWidth = mGridView.getMeasuredWidth();
                int displayHeight = mGridView.getMeasuredHeight();

                int statusbarHeight = getStatusBarHeight(getApplicationContext());
                int requiredHeight = displayHeight - statusbarHeight;

                mColumnWidth = displayWidth / COLUMNS;
                mColumnHeight = requiredHeight / (COLUMNS+1);

                display(getApplicationContext());
            }
        });
    }

    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");

        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    private static void display(Context context) {
        ArrayList<Button> buttons = new ArrayList<>();
        Button button;
        for (String s : tileList) {
            button = new Button(context);
            button.setBackground(tileImages.get(Integer.parseInt(s)));
            buttons.add(button);
        }
        mGridView.setAdapter(new CustomAdapter(buttons, mColumnWidth, mColumnHeight));
//        mGridView.setOnTouchListener(new View.OnTouchListener() {
//            float startX;
//            float startY;
//            int smallimage_Position;
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                    {
//                        startX = event.getX();
//                        startY = event.getY();
//                        smallimage_Position = mGridView.pointToPosition((int) startX,
//                                (int) startY);
//                        return true;
//                    }
//                    case MotionEvent.ACTION_MOVE: {
//                        float endX = event.getX();
//                        float endY = event.getY();
//                        if(endX > startX + mColumnWidth/2 && (endY < startY - mColumnHeight/2 || endY < startY + mColumnHeight/2) ){
//                            startX = endX*200;
//                            startY = endY*200;
//
//
//                        }
//                        return true;
//                    }
//                }
//                return true;
//            }
//        });
    }

    private static void swap(Context context, int currentPosition, int swap) {
        String newPosition = tileList[currentPosition + swap];
        tileList[currentPosition + swap] = tileList[currentPosition];
        tileList[currentPosition] = newPosition;
        display(context);

        if (isSolved()) Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show();
    }

    public static void moveTiles(Context context, String direction, int position) {
        switch (direction) {
            case right:
                swap(context, position, 1);
                break;
            case down:
                swap(context, position, COLUMNS);
                break;
            case left:
                swap(context, position, -1);
                break;
            case up:
                swap(context, position, -COLUMNS);
                break;
        }
    }

    private static boolean isSolved() {
        boolean solved = false;

        for (int i = 0; i < tileList.length; i++) {
            if (tileList[i].equals(String.valueOf(i))) {
                solved = true;
            } else {
                solved = false;
                break;
            }
        }

        return solved;
    }

    private ArrayList<Drawable> splitImage(Bitmap bitmap, int chunkNumbers) {

        //For the number of rows and columns of the grid to be displayed
        int rows,cols;

        //For height and width of the small image chunks
        int chunkHeight,chunkWidth;

        //To store all the small image chunks in bitmap format in this list
        ArrayList<Drawable> chunkedImages = new ArrayList<Drawable>(chunkNumbers);

        //Getting the scaled bitmap of the source image
//        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
//        Bitmap bitmap = drawable.getBitmap();
        Matrix matrix = new Matrix();
        matrix.postRotate(180);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        rows = cols = (int) Math.sqrt(chunkNumbers);
        chunkHeight = bitmap.getHeight()/rows;
        chunkWidth = bitmap.getWidth()/cols;

        //xCoord and yCoord are the pixel positions of the image chunks
        int yCoord = 0;
        for(int x=0; x<rows; x++){
            int xCoord = 0;
            for(int y=0; y<cols; y++){
                chunkedImages.add(new BitmapDrawable(getResources(), Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight)));
                xCoord += chunkWidth;
            }
            yCoord += chunkHeight;
        }
        return chunkedImages;

    }

}