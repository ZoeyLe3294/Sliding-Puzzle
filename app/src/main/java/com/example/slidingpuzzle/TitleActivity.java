package com.example.slidingpuzzle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class TitleActivity extends AppCompatActivity {
    private EditText levels;
    private Switch timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        init();
    }
    private void confirm(View view){
        Intent intent = new Intent(TitleActivity.this,MainActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putInt("level", Integer.parseInt(levels.getText().toString()));
        mBundle.putString("timer", timer.getText().toString());

        intent.putExtra("setUp", mBundle);
    }
    private void init(){
        levels = (EditText) findViewById(R.id.level);
        timer = (Switch) findViewById(R.id.timer);
    }
}
