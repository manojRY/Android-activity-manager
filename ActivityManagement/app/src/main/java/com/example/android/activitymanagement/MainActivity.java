package com.example.android.activitymanagement;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final boolean btnCaps = false;
    DatabaseHelper myDB;
    EditText editActivity, editTotalHours;
    Button btnNewActivity, btnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new DatabaseHelper(this);

        LinearLayout addActivityLayout = (LinearLayout) findViewById(R.id.add_activity_layout);
        editActivity = (EditText) findViewById(R.id.activity_edit_text);
        editTotalHours = (EditText) findViewById(R.id.total_hours_edit_text);
        btnNewActivity = (Button) findViewById(R.id.add_activity_button);
        //btnView = (Button) findViewById(R.id.view_activity_button);

        addData();
        //viewActivity();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Cursor activities = myDB.getActivityName();
        if (activities.getCount() == 0){

            Toast toast = new Toast(this);
            Toast.makeText(MainActivity.this, "No activities available", Toast.LENGTH_SHORT).show();
        }

        while (activities.moveToNext()){

            final Button activityButton = new Button(this);
            addActivityLayout.addView(activityButton);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 10, 0, 0);
            activityButton.setText(activities.getString(1));
            activityButton.setLayoutParams(layoutParams);
            activityButton.setGravity(Gravity.CENTER_VERTICAL);
            activityButton.setPadding(8, 0, 0, 0);
            activityButton.setTypeface(Typeface.DEFAULT);
            activityButton.setAllCaps(btnCaps);
            activityButton.setBackgroundColor(Color.parseColor("#64B5F6"));
            activityButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            final String activity = activities.getString(1);
            final String activity_id = activities.getString(0);
            activityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, sub_category.class);
                    intent.putExtra("activityName", activity);
                    intent.putExtra("activityID", activity_id);
                    startActivity(intent);
                }
            });

        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    public void addData(){

        btnNewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isInserted = myDB.insertActivity(editActivity.getText().toString(), editTotalHours.getText().toString());

                addActivity();

                if (isInserted = false)
                    Toast.makeText(MainActivity.this, "Unable to insert new activity", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "New Activity added", Toast.LENGTH_LONG).show();

            }
        });
    }


//    public void viewActivity (){
//
//        btnView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Cursor result = myDB.getData();
//
//                if(result.getCount() == 0){
//
//                    showMessage("Error", "Nothing found");
//                    return;
//                }
//
//                StringBuffer buffer = new StringBuffer();
//                while (result.moveToNext()){
//                    buffer.append("Id :" + result.getString(0) + "\n");
//                    buffer.append("Name :" + result.getString(1) + "\n");
//                    buffer.append("Hours :" + result.getString(2) + "\n\n");
//                }
//
//                showMessage("Data",buffer.toString());
//            }
//        });
//    }
//
//    public void showMessage(String title, String message){
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(true);
//        builder.setTitle(title);
//        builder.setMessage(message);
//        builder.show();
//    }

    public void onPlusClick(View view) {

        EditText activityEditText = (EditText) findViewById(R.id.activity_edit_text);
        EditText totalHoursEditText = (EditText) findViewById(R.id.total_hours_edit_text);
        Button addActivityButton = (Button) findViewById(R.id.add_activity_button);

        Button plusButton = new Button(this);
        //addActivityButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        plusButton.setBackgroundColor(Color.parseColor("#00BCD4"));

        activityEditText.setVisibility(View.VISIBLE);
        totalHoursEditText.setVisibility(View.VISIBLE);
        addActivityButton.setVisibility(View.VISIBLE);

    }


    public void addActivity() {

        LinearLayout addActivityLayout = (LinearLayout) findViewById(R.id.add_activity_layout);
        Button addActivityButton = (Button) findViewById(R.id.add_activity_button);
        EditText activityEditText = (EditText) findViewById(R.id.activity_edit_text);
        EditText totalHoursEditText = (EditText) findViewById(R.id.total_hours_edit_text);

        Editable newActivity = activityEditText.getText();
        Editable totalHours = totalHoursEditText.getText();
        int activityLength = newActivity.length();

        if (activityLength == 0){

            Toast toastMessage = new Toast(this);
            Toast.makeText(MainActivity.this,"Please enter the activity name" , Toast.LENGTH_SHORT).show();
            return;
        }

        final Button addButton = new Button(this);
        addActivityLayout.addView(addButton);
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.setMargins(0, 10, 0, 0);
        addButton.setLayoutParams(buttonLayoutParams);
        addButton.setGravity(Gravity.CENTER_VERTICAL);
        addButton.setPadding(8, 0, 0, 0);
        addActivityButton.setTypeface(Typeface.DEFAULT);
        addButton.setTag(newActivity + "_button");
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String btnName = addButton.getText().toString();

                Intent intent = new Intent(MainActivity.this, sub_category.class);
                intent.putExtra("activityName", btnName);
                startActivity(intent);
            }
        });


        addButton.setText(newActivity);
        addButton.setAllCaps(btnCaps);
        addButton.setBackgroundColor(Color.parseColor("#64B5F6"));
        addButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

//        ProgressBar mainTaskProgressBar = new ProgressBar(this);
//        addActivityLayout.addView(mainTaskProgressBar);
//        mainTaskProgressBar.setMax(100);


        activityEditText.setVisibility(View.GONE);
        addActivityButton.setVisibility(View.GONE);
        totalHoursEditText.setVisibility(View.GONE);

        totalHours.clear();
        newActivity.clear();

    }


}
