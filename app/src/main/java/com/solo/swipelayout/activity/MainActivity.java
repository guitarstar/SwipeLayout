package com.solo.swipelayout.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.solo.swipelayout.R;

public class MainActivity extends AppCompatActivity {
    private Button btnTestInListView, btnTestInRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTestInListView = (Button) findViewById(R.id.btnTestInListView);
        btnTestInRecycleView = (Button) findViewById(R.id.btnTestInRecycleView);

        btnTestInListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TestListViewActivity.class));
            }
        });

        btnTestInRecycleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TestRcyActivity.class));
            }
        });
    }
}
