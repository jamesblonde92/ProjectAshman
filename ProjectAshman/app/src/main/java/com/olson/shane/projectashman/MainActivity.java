package com.olson.shane.projectashman;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnLongClickListener {


    Button button_up;
    Button button_right;
    Button button_down;
    Button button_left;
    Maze displayMaze;
    TextView textView_cakes;


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.button_up:
                displayMaze.setUIDirection("up");
                break;
            case R.id.button_right:
                displayMaze.setUIDirection("right");
                break;
            case R.id.button_down:
                displayMaze.setUIDirection("down");
                break;
            case R.id.button_left:
                displayMaze.setUIDirection("left");
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button_up = (Button) findViewById(R.id.button_up);
        button_right = (Button) findViewById(R.id.button_right);
        button_down = (Button) findViewById(R.id.button_down);
        button_left = (Button) findViewById(R.id.button_left);
        displayMaze = (Maze) findViewById(R.id.display_maze);
        textView_cakes = (TextView) findViewById(R.id.textView_cakes);

        button_up.setOnTouchListener(this);
        button_right.setOnTouchListener(this);
        button_down.setOnTouchListener(this);
        button_left.setOnTouchListener(this);
        textView_cakes.setOnLongClickListener(this);

        setUpCallbacks();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        displayMaze.setIsGamePaused(false);
        super.onResume();
    }

    @Override
    protected void onPause() {
        displayMaze.setIsGamePaused(true);
        super.onPause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this, "Project Ashman made by Shane Olson \nCreated as the final project of Paul Schimpf's Android Development class.", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setCakeNumber(int num){
        textView_cakes.setText("Cakes Remaining: " + num);
    }


    public void setUpCallbacks(){
        Maze g=(Maze)findViewById(R.id.display_maze);
        g.setObserver(new TheObserver() {
            public void callback(int num) {
                // here you call something inside your activity, for instance
                setCakeNumber(num);
            }
        });
    }


    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.textView_cakes:
                displayMaze.cheatRemoveCakes();
                break;
        }
        return true;
    }
}
