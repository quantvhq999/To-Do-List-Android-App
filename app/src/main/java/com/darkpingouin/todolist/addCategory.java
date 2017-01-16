package com.darkpingouin.todolist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import petrov.kristiyan.colorpicker.ColorPicker;

public class addCategory extends AppCompatActivity {

    int finalColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        finalColor = Color.parseColor("#DFDFDF");
    }

    public void setColor(View v) {
        ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.show();
        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position, int color) {
                System.out.println("Chosen" + color);
                finalColor = color;
                ((View) findViewById(R.id.color)).setBackgroundColor(color);
            }

            @Override
            public void onCancel() {
                System.out.println("CANCEL");
            }
        });
    }

    public void added(View v)
    {
        String name;

        name = ((TextView) findViewById(R.id.catName)).getText().toString();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("name", name);
        returnIntent.putExtra("color", String.valueOf(finalColor));
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}