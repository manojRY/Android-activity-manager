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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

/**
 * Created by prasanth_ev on 5/14/2016.
 */
public class sub_category extends AppCompatActivity {

//    RadioGroup frequencyRadioGrp = (RadioGroup) findViewById(R.id.frequency_radiogrp);
//    RadioGroup prerequisiteRadioGrp = (RadioGroup) findViewById(R.id.prerequisite_radiogrp);
//    LinearLayout constraintsLayout = (LinearLayout) findViewById(R.id.constraints_layout);

    Button addSubCatBtn;
    EditText newSubCatEdittext, newSubCatHours, startDateEdittext, endDateEdittext, timingEdittext, percentageEdittext;
    RadioButton weeklyRadioBtn, biWeeklyRadioBtn, yesRadioBtn, noRadioBtn;
    TextView mainActNameTextView;
    LinearLayout addSubCatLayout;

    DatabaseHelper myDB;
    boolean btnCaps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_category);

        myDB = new DatabaseHelper(this);
        btnCaps = false;

        TextView subCategoryTextView = (TextView) findViewById(R.id.sub_category_text_view);
        subCategoryTextView.setText(getIntent().getExtras().getString("activityName"));

        addSubCatBtn = (Button) findViewById(R.id.add_subcat_button);
        newSubCatEdittext = (EditText) findViewById(R.id.new_subcategory_edittext);
        newSubCatHours = (EditText) findViewById(R.id.new_subcathours_edittext);
        startDateEdittext = (EditText) findViewById(R.id.start_date_edittext);
        endDateEdittext = (EditText) findViewById(R.id.end_date_edittext);
        timingEdittext = (EditText) findViewById(R.id.timing_edittext);
        percentageEdittext = (EditText) findViewById(R.id.percentage_edittext);
        weeklyRadioBtn = (RadioButton) findViewById(R.id.weekly_radiobtn);
        biWeeklyRadioBtn = (RadioButton) findViewById(R.id.bi_weekly_radiobtn);
        yesRadioBtn = (RadioButton) findViewById(R.id.yes_radiobtn);
        noRadioBtn = (RadioButton) findViewById(R.id.no_radiobtn);
        addSubCatLayout = (LinearLayout) findViewById(R.id.sub_category_layout);
        mainActNameTextView = (TextView) findViewById(R.id.sub_category_text_view);

        //addSubCategories();

        final Cursor subCategories = myDB.getSubCategories(getIntent().getExtras().getString("activityID"));

        if (subCategories.getCount() == 0) {

            Toast toast = new Toast(this);
            Toast.makeText(sub_category.this, "No sub categories added yet", Toast.LENGTH_SHORT).show();
        }

        while (subCategories.moveToNext()) {

            final Button addSubCatBtn = new Button(this);
            addSubCatLayout.addView(addSubCatBtn);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 10, 0, 0);
            addSubCatBtn.setLayoutParams(layoutParams);
            addSubCatBtn.setText(subCategories.getString(1));
            addSubCatBtn.setGravity(Gravity.CENTER_VERTICAL);
            addSubCatBtn.setPadding(8, 0, 0, 0);
            addSubCatBtn.setTypeface(Typeface.DEFAULT);
            addSubCatBtn.setAllCaps(btnCaps);
            addSubCatBtn.setBackgroundColor(Color.parseColor("#64B5F6"));
            addSubCatBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            final String subTaskID = subCategories.getString(0);

            TextView mainTask = (TextView) findViewById(R.id.sub_category_text_view);
            final String mainTaskName = mainTask.getText().toString();
            addSubCatBtn.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {



                    //String subcatHours = subCategories.getString(1);

                    Intent intent = new Intent(sub_category.this, individual_category.class);
                    intent.putExtra("mainTask", mainTaskName);
                    intent.putExtra("subTask", subTaskID);
                    //intent.putExtra("subCategoryHours", subcatHours);
                    startActivity(intent);

                }
            });

        }

    }

//    public void onLoad(View view) {
//
//        TextView subCategoryTextView = (TextView) view.findViewById(R.id.sub_category_text_view);
//
//    }

    public void onSubCatPlusClick(View view) {

        LinearLayout constraintsLayout = (LinearLayout) findViewById(R.id.constraints_layout);
        constraintsLayout.setVisibility(View.VISIBLE);

    }

    public void addSubCategories() {


        final String frequency;

        if (weeklyRadioBtn.isChecked())

            frequency = weeklyRadioBtn.getText().toString();
        else

            frequency = biWeeklyRadioBtn.getText().toString();

        final String prerequisite;

        if (yesRadioBtn.isChecked())

            prerequisite = yesRadioBtn.getText().toString();

        else

            prerequisite = noRadioBtn.getText().toString();

//        addSubCatBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {

        boolean result = myDB.insertSubCategory(newSubCatEdittext.getText().toString(), newSubCatHours.getText().toString(), frequency, startDateEdittext.getText().toString(), endDateEdittext.getText().toString(), timingEdittext.getText().toString(), prerequisite, percentageEdittext.getText().toString(), getIntent().getExtras().getString("activityID"));

        if (result = false)

            Toast.makeText(sub_category.this, "Unable to add sub category", Toast.LENGTH_SHORT).show();

        else

            Toast.makeText(sub_category.this, "Sub category added", Toast.LENGTH_SHORT).show();


        //       }

        //  });


    }

    public void onRadioBtnClick(View view) {

        RadioButton yesRadioBtn = (RadioButton) findViewById(R.id.yes_radiobtn);
        TextView percentageTextView = (TextView) findViewById(R.id.percentage_textview);
        EditText percentageEditText = (EditText) findViewById(R.id.percentage_edittext);
        boolean clicked = ((RadioButton) view).isChecked();

        switch (view.getId()) {

            case R.id.yes_radiobtn:
                if (clicked)
                    percentageTextView.setVisibility(View.VISIBLE);
                percentageEditText.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void addNewSubCategory(View view) {

        LinearLayout constraintsLayout = (LinearLayout) findViewById(R.id.constraints_layout);
        LinearLayout addSubcatLayout = (LinearLayout) findViewById(R.id.sub_category_layout);
        EditText newSubcatEditText = (EditText) findViewById(R.id.new_subcategory_edittext);
        Editable newSubCategory = newSubcatEditText.getText();
        int newSubCategorylength = newSubCategory.length();

        if (newSubCategorylength == 0) {

            Toast toastMessage = new Toast(this);
            Toast.makeText(sub_category.this, "Please enter the sub category", Toast.LENGTH_SHORT).show();
            return;
        }

        addSubCategories();

        final Button addSubButton = new Button(this);
        addSubcatLayout.addView(addSubButton);
        LinearLayout.LayoutParams subButtonLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        subButtonLayoutParams.setMargins(0, 10, 0, 0);
        addSubButton.setLayoutParams(subButtonLayoutParams);
        addSubButton.setGravity(Gravity.CENTER_VERTICAL);
        addSubButton.setPadding(8, 0, 0, 0);
        addSubButton.setTypeface(Typeface.DEFAULT);

        final Cursor subCatID = myDB.getSubCategoryId();
        final String subCatCount = Integer.toString(subCatID.getCount());
        TextView mainTask = (TextView) findViewById(R.id.sub_category_text_view);
        final String mainTaskName = mainTask.getText().toString();

        addSubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(sub_category.this, individual_category.class);
                intent.putExtra("mainTask", mainTaskName);
                intent.putExtra("subTask", subCatCount);
                startActivity(intent);
            }
        });

        addSubButton.setText(newSubCategory);
        addSubButton.setBackgroundColor(Color.parseColor("#64B5F6"));
        addSubButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);


        constraintsLayout.setVisibility(View.GONE);
        newSubCatEdittext.getText().clear();
        newSubCatHours.getText().clear();


        // Insert data into tables
        //addSubCategories();
    }
}


