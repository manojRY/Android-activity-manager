package com.nishanth.android.activitymanagement;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Handler;
import java.util.zip.Inflater;


public class sub_category extends AppCompatActivity {

    ImageButton startDateImgBtn, endDateImgBtn;
    EditText newSubCatEdittext, newSubCatHours, startDateEdittext, endDateEdittext, timingEdittext, percentageEdittext;
    RadioButton weeklyRadioBtn, biWeeklyRadioBtn, yesRadioBtn, noRadioBtn;
    Button addSubCatBtn, plusBtn, startButton, stopButton;
    TextView subCategoryTextView, percentageTextView, completedTextView, timerValue;
    LinearLayout subCategoriesLayout;
    ProgressBar completedProgressBar;
    ListView subCatListView;
    Boolean btnCaps = false;

    private long startTime = 0;
    private android.os.Handler customHandler = new android.os.Handler();
    long timeInMilliseconds = 0;
    long timeSwapBuff = 0;
    long updatedTime = 0;

    private ArrayList<String> data = new ArrayList<>();


    DatabaseHelper myDB;

    int year_start, month_start, day_start, year_end, month_end, day_end, hour_x, minute_x, id;
    static final int DIALOG_ID_START_DATE = 1;
    static final int DIALOG_ID_END_DATE = 2;
    static final int DIALOG_ID_TIMER = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_category);

        myDB = new DatabaseHelper(this);

        final Calendar cal = Calendar.getInstance();
        year_start = cal.get(Calendar.YEAR);
        month_start = cal.get(Calendar.MONTH);
        day_start = cal.get(Calendar.DAY_OF_MONTH);

        year_end = cal.get(Calendar.YEAR);
        month_end = cal.get(Calendar.MONTH);
        day_end = day_start + 1;

        hour_x = 07;
        minute_x = 00;

        plusBtn = (Button) findViewById(R.id.plus_btn);
        subCategoriesLayout = (LinearLayout) findViewById(R.id.sub_category_layout);
        subCategoryTextView = (TextView) findViewById(R.id.sub_category_text_view);
        subCategoryTextView.setText(getIntent().getExtras().getString("activityName"));
        completedProgressBar = (ProgressBar) findViewById(R.id.completed_progress_bar);
        completedTextView = (TextView) findViewById(R.id.completed_text_view);
        subCatListView = (ListView) findViewById(R.id.sub_cat_list_view);

        addSubCatList();

        generateListContent();


        subCatListView.setAdapter(new listAdapter(this, R.layout.list_item, data));

//        final Cursor subCategories = myDB.getSubCategories(getIntent().getExtras().getString("activityID"));
//
//        subCategories.moveToNext();

        completedProgressBar.setMax(100);
        completedProgressBar.setProgress(50);

        float a = ((float) 50 / 100) * 100;
        int b = Math.round(a);

        completedTextView.setText(String.valueOf(b) + "% Completed");


        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View view = LayoutInflater.from(sub_category.this).inflate(R.layout.add_sub_category, null);

                newSubCatEdittext = (EditText) view.findViewById(R.id.new_subcategory_edittext);
                newSubCatHours = (EditText) view.findViewById(R.id.new_subcathours_edittext);
                startDateEdittext = (EditText) view.findViewById(R.id.start_date_edittext);
                endDateEdittext = (EditText) view.findViewById(R.id.end_date_edittext);
                timingEdittext = (EditText) view.findViewById(R.id.timing_edittext);
                percentageEdittext = (EditText) view.findViewById(R.id.percentage_edittext);
                weeklyRadioBtn = (RadioButton) view.findViewById(R.id.weekly_radiobtn);
                biWeeklyRadioBtn = (RadioButton) view.findViewById(R.id.bi_weekly_radiobtn);
                yesRadioBtn = (RadioButton) view.findViewById(R.id.yes_radiobtn);
                noRadioBtn = (RadioButton) view.findViewById(R.id.no_radiobtn);
                percentageTextView = (TextView) view.findViewById(R.id.percentage_textview);


                final AlertDialog.Builder builder = new AlertDialog.Builder(sub_category.this);
                builder.setTitle("New sub category")
                        .setView(view)
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                addSubCatBtnClick();

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .setCancelable(false);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    public void generateListContent() {

        for(int i=0; i<25; i++){

            data.add("This is row number" + i);
        }
    }

    public void showStartDate(View view) {

        showDialog(DIALOG_ID_START_DATE);

    }

    public void showEndDate(View view) {

        showDialog(DIALOG_ID_END_DATE);
    }

    public void showTimer(View view) {

        showDialog(DIALOG_ID_TIMER);
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        this.id = id;
        if (id == DIALOG_ID_START_DATE)
            return new DatePickerDialog(this, datePickerListener, year_start, month_start, day_start);
        if (id == DIALOG_ID_END_DATE)
            return new DatePickerDialog(this, datePickerListener, year_end, month_end, day_start + 1);

        else if (id == DIALOG_ID_TIMER)
            return new TimePickerDialog(this, timePickerListener, hour_x, minute_x, true);

        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            if (id == DIALOG_ID_START_DATE) {

                year_start = year;
                month_start = monthOfYear + 1;
                day_start = dayOfMonth;
                startDateEdittext.setText(String.valueOf(day_start) + "/" + String.valueOf(month_start) + "/" + String.valueOf(year_start));
            }
            if (id == DIALOG_ID_END_DATE) {

                year_end = year;
                month_end = monthOfYear + 1;
                day_end = dayOfMonth;
                endDateEdittext.setText(String.valueOf(day_end) + "/" + String.valueOf(month_end) + "/" + String.valueOf(year_end));
            }
        }
    };

    protected TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            hour_x = hourOfDay;
            minute_x = minute;

            timingEdittext.setText(String.valueOf(hour_x) + ":" + String.valueOf(minute_x));
        }
    };

    private void addSubCatBtnClick() {


        if (newSubCatEdittext.getText().length() == 0) {

            Toast toastMessage = new Toast(this);
            Toast.makeText(sub_category.this, "Please enter the sub category", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newSubCatHours.getText().length() == 0) {

            Toast toastMessage = new Toast(this);
            Toast.makeText(sub_category.this, "Please enter total hours", Toast.LENGTH_SHORT).show();
            return;
        }

        if (startDateEdittext.getText().length() == 0) {

            Toast toastMessage = new Toast(this);
            Toast.makeText(sub_category.this, "Please enter start date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (endDateEdittext.getText().length() == 0) {

            Toast toastMessage = new Toast(this);
            Toast.makeText(sub_category.this, "Please enter end date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (timingEdittext.getText().length() == 0) {

            Toast toastMessage = new Toast(this);
            Toast.makeText(sub_category.this, "Please enter the timing", Toast.LENGTH_SHORT).show();
            return;
        }

        if (yesRadioBtn.isChecked()) {

            if (percentageEdittext.getText().length() == 0) {

                Toast toastMessage = new Toast(this);
                Toast.makeText(sub_category.this, "Please enter required percentage", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        final Button courseButton = new Button(this);
        subCategoriesLayout.addView(courseButton);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 10, 0, 0);
        courseButton.setText(newSubCatEdittext.getText());
        courseButton.setLayoutParams(layoutParams);
        courseButton.setGravity(Gravity.CENTER_VERTICAL);
        courseButton.setPadding(8, 0, 0, 0);
        courseButton.setTypeface(Typeface.DEFAULT);
        courseButton.setAllCaps(btnCaps);
        courseButton.setBackground(getResources().getDrawable(R.drawable.shape));
        courseButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

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


    }

    public void onRadioBtnClick(View view) {


        boolean clicked = ((RadioButton) view).isChecked();

        switch (view.getId()) {

            case R.id.yes_radiobtn:
                if (clicked)
                    percentageTextView.setVisibility(View.VISIBLE);
                percentageEdittext.setVisibility(View.VISIBLE);
                break;
        }
    }

    //shows progress bar

    private void showProgressBar() {

//        final Cursor subCategories = myDB.getSubCategories(getIntent().getExtras().getString("activityID"));
//
//        subCategories.moveToNext();
//
//        completedProgressBar.setMax(100);
//        completedProgressBar.setProgress(50);
    }

    private void addSubCatList() {

        final Cursor subCategories = myDB.getSubCategories(getIntent().getExtras().getString("activityID"));

        while (subCategories.moveToNext()) {

            final Button addSubCatBtn = new Button(this);
            subCategoriesLayout.addView(addSubCatBtn);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 10, 0, 0);
            addSubCatBtn.setLayoutParams(layoutParams);
            addSubCatBtn.setText(subCategories.getString(1));
            addSubCatBtn.setGravity(Gravity.CENTER_VERTICAL);
            addSubCatBtn.setPadding(8, 0, 0, 0);
            addSubCatBtn.setTypeface(Typeface.DEFAULT);
            addSubCatBtn.setAllCaps(btnCaps);
            addSubCatBtn.setBackground(getResources().getDrawable(R.drawable.shape));
            addSubCatBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            final String subTaskID = subCategories.getString(0);

            TextView mainTask = (TextView) findViewById(R.id.sub_category_text_view);
            final String mainTaskName = mainTask.getText().toString();
//            addSubCatBtn.setOnClickListener(new View.OnClickListener() {
//
//
//                @Override
//                public void onClick(View v) {
//
//
//                    //String subcatHours = subCategories.getString(1);
//
//                    Intent intent = new Intent(sub_category.this, individual_category.class);
//                    intent.putExtra("mainTask", mainTaskName);
//                    intent.putExtra("subTask", subTaskID);
//                    //intent.putExtra("subCategoryHours", subcatHours);
//                    startActivity(intent);
//
//                }
//            });

        }

    }

    private class listAdapter extends ArrayAdapter<String> {

        private int layout;

        private listAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);

            layout = resource;
        }

        @Override

        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder mainViewHolder = null;


            if (convertView == null) {

                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.catTextView = (TextView) convertView.findViewById(R.id.list_cat_textview);
                viewHolder.timerImgBtn = (ImageButton) convertView.findViewById(R.id.list_timer_img_btn);
                viewHolder.timerImgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final View timerView = LayoutInflater.from(sub_category.this).inflate(R.layout.timer, null);
                        timerValue = (TextView) timerView.findViewById(R.id.timerValue);

                        startButton = (Button) timerView.findViewById(R.id.startButton);
                        stopButton = (Button) timerView.findViewById(R.id.stopButton);
                        startButton.setText("START");
                        stopButton.setText("STOP");

                        final AlertDialog.Builder builder = new AlertDialog.Builder(sub_category.this);
                        builder.setTitle("Timer")
                                .setView(timerView)
                                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        addSubCatBtnClick();

                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .setCancelable(false);

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        startButton.setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {

                                if (startButton.getText() == "START") {
                                    startTime = SystemClock.uptimeMillis();

                                    customHandler.postDelayed(updateTimerThread, 0);


                                    startButton.setText("PAUSE");
                                    return;
                                } else {

                                    timeSwapBuff += timeInMilliseconds;

                                    customHandler.removeCallbacks(updateTimerThread);
                                    startButton.setText("START");

                                }


                            }
                        });


                        stopButton.setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {

                                if (stopButton.getText() == "STOP") {
                                    timeSwapBuff += timeInMilliseconds;

                                    customHandler.removeCallbacks(updateTimerThread);

                                    stopButton.setText("RESET");
                                    startButton.setText("START");
                                } else {

                                    stopButton.setText("STOP");
                                    startTime = 0;
                                    timeSwapBuff = 0;
                                    timeInMilliseconds = 0;
                                    updatedTime = timeSwapBuff + timeInMilliseconds;
                                    int secs = (int) (updatedTime / 1000);

                                    int mins = secs / 60;
                                    int hours = mins / 60;

                                    secs = secs % 60;

                                    int milliseconds = (int) (updatedTime % 1000);

                                    timerValue.setText("" + String.format("%02d", hours) + ":" + String.format("%02d", mins) + ":"

                                            + String.format("%02d", secs));
                                }


                            }

                        });




                    }
                });
                convertView.setTag(viewHolder);
            } else {

                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.catTextView.setText(getItem(position));
            }


            return convertView;

        }



    }

    private Runnable updateTimerThread = new Runnable() {


        public void run() {


            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;


            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);

            int mins = secs / 60;
            int hours = mins / 60;

            secs = secs % 60;

            int milliseconds = (int) (updatedTime % 1000);

            timerValue.setText("" + String.format("%02d", hours) + ":" + String.format("%02d", mins) + ":"

                    + String.format("%02d", secs));

            customHandler.postDelayed(this, 0);

        }

    };

    public class ViewHolder {

        TextView catTextView;
        ImageButton timerImgBtn;
    }

}




