package com.example.android.activitymanagement;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

/**
 * Created by prasanth_ev on 5/24/2016.
 */
public class individual_category extends AppCompatActivity {

    DatabaseHelper myDB;

    TextView indHoursSpent, indTotalHours, listTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.individual_category);

        myDB = new DatabaseHelper(this);

        TextView mainTaskTextView = (TextView) findViewById(R.id.main_task_textview);
        //TextView subTaskTextView = (TextView) findViewById(R.id.sub_task_textview);
        indHoursSpent= (TextView) findViewById(R.id.hours_count_textview);
        indTotalHours = (TextView) findViewById(R.id.total_hours_count_textview);
        listTextView = (TextView) findViewById(R.id.tasks_list_textview);

        //Get data from database

        String subTaskID = getIntent().getExtras().getString("subTask").toString();
        final Cursor indCat = myDB.getIndCatData(subTaskID);

        while (indCat.moveToNext()){

            indTotalHours.setText(indCat.getString(2));
            StringBuffer taskListBuffer = new StringBuffer();
            taskListBuffer.append(listTextView.getText());
            taskListBuffer.append(" " + indCat.getString(1) + "s ?");
            listTextView.setText(taskListBuffer);
            mainTaskTextView.setText(getIntent().getExtras().getString("mainTask") + " : " + indCat.getString(1));
        }

        indHoursSpent.setText("0");

    }

    public void addIndCatData(){


    }

    public void onRadioBtnClick (View view){

        RadioButton taskYesRadioBtn = (RadioButton) findViewById(R.id.tasks_yes_radioBtn);
        EditText numOfTasktsEdittext = (EditText) findViewById(R.id.numOfTasks_editText);
        Button addTasksBtn = (Button) findViewById(R.id.addTasks_button);

        String subTaskID = getIntent().getExtras().getString("subTask").toString();
        final Cursor indCat = myDB.getIndCatData(subTaskID);

        while (indCat.moveToNext()){

            numOfTasktsEdittext.setHint("Number of " + indCat.getString(1) + "s");
        }

        if (taskYesRadioBtn.isChecked()){

            numOfTasktsEdittext.setVisibility(View.VISIBLE);

            addTasksBtn.setVisibility(View.VISIBLE);
            return;
        }

        else {

            addTasksBtn.setVisibility(View.GONE);
            numOfTasktsEdittext.setVisibility(View.GONE);
        }

    }

    public void onAddTasksBtnClick(View view){

        LinearLayout indCatLayout = (LinearLayout) findViewById(R.id.ind_cat_layout);
        EditText numOfTasksEdittext = (EditText) findViewById(R.id.numOfTasks_editText);
        Button addTasksBtn = (Button) findViewById(R.id.addTasks_button);
        RadioButton taskYesRadioBtn = (RadioButton) findViewById(R.id.tasks_yes_radioBtn);
        RadioButton taskNoRadioBtn = (RadioButton) findViewById(R.id.tasks_no_radioBtn);

        String subTaskID = getIntent().getExtras().getString("subTask").toString();
        final Cursor indCat = myDB.getIndCatData(subTaskID);



        int numOfTasks = Integer.parseInt(numOfTasksEdittext.getText().toString());

        for (int task=1; task<=numOfTasks; task++) {

            String subCatName ="";

            while (indCat.moveToNext()) {

                subCatName = indCat.getString(1);
            }

                CheckBox addcheckbox = new CheckBox(this);

                indCatLayout.addView(addcheckbox);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 10, 0, 0);
                addcheckbox.setTypeface(Typeface.DEFAULT);
                addcheckbox.setText(subCatName + " " + task);
        }


        numOfTasksEdittext.getText().clear();
        numOfTasksEdittext.setVisibility(View.GONE);
        addTasksBtn.setVisibility(View.GONE);
        taskYesRadioBtn.toggle();
        taskNoRadioBtn.toggle();

    }


}
