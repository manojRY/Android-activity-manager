package com.nishanth.android.activitymanagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button addBtn;
    TextView coursesTextView;
    EditText courseNameEditText, totalHoursEditText;
    LinearLayout courseLayout;
    Boolean btnCaps = false;

    //inflate


    DatabaseHelper myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new DatabaseHelper(this);
        addBtn = (Button) findViewById(R.id.add_btn);
        coursesTextView = (TextView) findViewById(R.id.courses_text_view);
        courseLayout = (LinearLayout)findViewById(R.id.course_layout);

        coursesTextView.setBackgroundColor(Color.TRANSPARENT);

        addCourseList();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_course, null);

                courseNameEditText = (EditText) view.findViewById(R.id.new_course_edit_text);
                totalHoursEditText = (EditText) view.findViewById(R.id.new_course_edit_text);

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("New Course")
                        .setView(view)
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                addCourseBtnClick();

                            }
                        })

                        .setNegativeButton("cancel", null)
                        .setCancelable(false);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }

        });


    }

    private void addCourseBtnClick (){

        if (courseNameEditText.length() == 0){

            Toast.makeText(MainActivity.this,"Please enter the activity name" , Toast.LENGTH_SHORT).show();
            return;
        }

        if (totalHoursEditText.length() == 0){

            Toast.makeText(MainActivity.this,"Please enter the total hours" , Toast.LENGTH_SHORT).show();
            return;
        }

        final Button courseButton = new Button(this);
        courseLayout.addView(courseButton);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 10, 0, 0);
        courseButton.setText(courseNameEditText.getText());
        courseButton.setLayoutParams(layoutParams);
        courseButton.setGravity(Gravity.CENTER_VERTICAL);
        courseButton.setPadding(8, 0, 0, 0);
        courseButton.setTypeface(Typeface.DEFAULT);
        courseButton.setAllCaps(btnCaps);
        courseButton.setBackground(getResources().getDrawable(R.drawable.shape));
        courseButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        boolean isInserted = myDB.insertActivity(courseNameEditText.getText().toString(), totalHoursEditText.getText().toString());
        if (isInserted = false)
            Toast.makeText(MainActivity.this, "Unable to insert new activity", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(MainActivity.this, "New Activity added", Toast.LENGTH_LONG).show();
    }

    private void addCourseList(){

        final Cursor activities = myDB.getActivityName();

        while (activities.moveToNext()){

            final Button courseButton = new Button(this);
            courseLayout.addView(courseButton);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 10, 0, 0);
            courseButton.setText(activities.getString(1));
            courseButton.setLayoutParams(layoutParams);
            courseButton.setGravity(Gravity.CENTER_VERTICAL);
            courseButton.setPadding(8, 0, 0, 0);
            courseButton.setTypeface(Typeface.DEFAULT);
            courseButton.setAllCaps(btnCaps);
            courseButton.setBackground(getResources().getDrawable(R.drawable.shape));
            courseButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            final String activity = activities.getString(1);
            final String activity_id = activities.getString(0);
            courseButton.setOnClickListener(new View.OnClickListener() {
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


}
