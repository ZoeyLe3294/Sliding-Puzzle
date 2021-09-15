package com.example.slidingpuzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;

public class ImageActivity extends Activity {
    TableLayout myTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        init();

        int rowNumber = 6;
        int colNumber = 3;
        Collections.shuffle(SetupActivity.arrayName);
        for (int i=1;i<=rowNumber;i++){
            TableRow tableRow = new TableRow(this);
            for (int j=1;j<=colNumber;j++){
                ImageView imageView = new ImageView(this);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(250,250);
                int index = colNumber*(i-1)+j-1;
                int idImage = getResources().getIdentifier(SetupActivity.arrayName.get(index), "drawable", getPackageName());
                imageView.setImageResource(idImage);
                imageView.setLayoutParams(layoutParams);
                //add imageview in tablerow
                tableRow.addView(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.putExtra("imageName", SetupActivity.arrayName.get((index)));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
            //add tablerow in table
            myTable.addView(tableRow);
        }
    }

    private void init(){
        myTable = (TableLayout) findViewById(R.id.table);
    }
}
