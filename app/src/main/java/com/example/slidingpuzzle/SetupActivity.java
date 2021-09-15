package com.example.slidingpuzzle;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SetupActivity extends AppCompatActivity{
    private EditText levels;
    private Switch timer;
    private ImageView imageView;
    public static ArrayList<String> arrayName;
    private Uri imageUri;
    Bundle mBundle = new Bundle();
    int idImage;
    String imageName;
    int REQUEST_CODE_IMAGE = 123;
    int SELECT_CODE_IMAGE = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        init();
    }
    public void confirm(View view){
        Intent intent = new Intent(SetupActivity.this,MainActivity.class);
        if(levels.getText().toString().matches("")){
            mBundle.putInt("level", 3);
        }else {mBundle.putInt("level", Integer.parseInt(levels.getText().toString()));}
        mBundle.putString("timer", timer.getText().toString());

        intent.putExtra("setUp", mBundle);
        startActivity(intent);
    }
    public void selectImage(View view){
        startActivityForResult(new Intent(SetupActivity.this,ImageActivity.class),REQUEST_CODE_IMAGE);
    }
    public void openGallery(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Title"), SELECT_CODE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE_IMAGE && resultCode==RESULT_OK && data!=null){
            imageName = data.getStringExtra("imageName");
            idImage = getResources().getIdentifier(imageName, "drawable",getPackageName());
            imageView.setImageResource(idImage);
            mBundle.putString("imageUri", imageName);
        }else if (requestCode==SELECT_CODE_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            mBundle.putString("imageUri", imageUri.toString());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init(){
        levels = (EditText) findViewById(R.id.level);
        timer = (Switch) findViewById(R.id.timer);
        imageView = (ImageView) findViewById(R.id.imageView);
        String[] listName = getResources().getStringArray(R.array.list_name);
        arrayName = new ArrayList<>(Arrays.asList(listName));
        Random randomID = new Random();
        imageName = arrayName.get(randomID.nextInt(arrayName.size()));
        idImage = getResources().getIdentifier(imageName, "drawable", getPackageName());
        mBundle.putString("imageUri", imageName);
        imageView.setImageResource(idImage);
    }
}
