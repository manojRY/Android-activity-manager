package com.nishanth.android.activitymanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by prasanth_ev on 5/12/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //Table Main activity
    public static final String DATABASE_NAME = "activity_management.db";
    public static final String TABLE_ACTIVITY = "Activity_Table";
    public static final String COL_ACTIVITY_ID = "Activity_ID";
    public static final String COL_ACTIVITY_NAME = "Name";
    public static final String COL_MAIN_ACTIVITY_HOURS = "Total_Hours";

    //Table Sub category
    public static final String TABLE_SUB_CATEGORY = "Sub_category_Table";
    public static final String COL_SUB_CATEGORY_ID = "Sub_Category_ID";
    public static final String COL_SUB_CATEGORY_NAME = "Sub_Category_Name";
    public static final String COL_SUB_CATEGORY_HOURS = "Total_Hours";
    public static final String COL_FREQUENCY = "Frequency";
    public static final String COL_START_DATE = "Start_Date";
    public static final String COL_END_DATE = "End_Date";
    public static final String COL_TIMING = "Timing";
    public static final String COL_PREREQUISITE = "Prerequisite";
    public static final String COL_REQUIRED_PERCENTAGE = "Required_Percentage";
    public static final String COL_ACTIVITY_TABLE_ID = "Activity_ID";

    //Table individual category

    public static final String TABLE_IND_CATEGORY = "Individual_category_Table";
    public static final String COL_IND_CATEGORY_ID = "Sub_Category_ID";
    public static final String COL_HOURS_SPENT = "Hours_Spent";
    public static final String COL_IND_TOTAL_HOURS = "Total_Hours";
    public static final String COL_TASK_LIST = "Task_List";
    public static final String COL_NUM_OF_TASK = "Num_Of_Tasks";
    public static final String COL_SUB_CAT_TABLE_ID = "Sub_Category_ID";
    public static final String COL_CHECK_BOX_INFO = "Check_box_info";


    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_ACTIVITY + "(Activity_ID INTEGER PRIMARY KEY AUTOINCREMENT, Name varchar(30), Total_Hours INTEGER)");

        db.execSQL("CREATE TABLE " + TABLE_SUB_CATEGORY + "(Sub_Category_ID INTEGER PRIMARY KEY AUTOINCREMENT, Sub_Category_Name varchar(20), Total_Hours INTEGER, Frequency varchar(10) DEFAULT 'Weekly', Start_Date DATE, End_Date DATE, Timing TIME, Prerequisite varchar(3) DEFAULT 'No', Required_Percentage INTEGER, Activity_ID INTEGER NOT NULL, " + "FOREIGN KEY (Activity_ID) REFERENCES " + TABLE_ACTIVITY + "(" + COL_ACTIVITY_ID + ") )");

        db.execSQL("CREATE TABLE " + TABLE_IND_CATEGORY + "(Sub_Ind_ID INTEGER PRIMARY KEY AUTOINCREMENT, Hours_Spent INTEGER, Total_Hours INTEGER, Task_List varchar(3) DEFAULT 'No', Num_Of_Tasks INTEGER, Sub_Category_ID INTEGER, Check_box_info varchar(20))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUB_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IND_CATEGORY);
        onCreate(db);

    }

    public boolean insertActivity (String activityName, String total_hours){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ACTIVITY_NAME, activityName);
        contentValues.put(COL_MAIN_ACTIVITY_HOURS, total_hours);
        long result = db.insert(TABLE_ACTIVITY,null,contentValues);

        if (result == -1)
            return false;
        else
            return true;

    }

    public boolean insertSubCategory (String subCatName, String subCatHours, String taskFrequency, String startDate, String endDate, String timing, String taskPrequisite, String requiredPercentage, String mainActivityID){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_SUB_CATEGORY_NAME, subCatName);
        contentValues.put(COL_SUB_CATEGORY_HOURS, subCatHours);
        contentValues.put(COL_FREQUENCY, taskFrequency);
        contentValues.put(COL_START_DATE, startDate);
        contentValues.put(COL_END_DATE, endDate);
        contentValues.put(COL_TIMING, timing);
        contentValues.put(COL_PREREQUISITE, taskPrequisite);
        contentValues.put(COL_REQUIRED_PERCENTAGE, requiredPercentage);
        contentValues.put(COL_ACTIVITY_TABLE_ID, mainActivityID);

        long result = db.insert(TABLE_SUB_CATEGORY, null, contentValues);

        if (result == -1)
            return false;
        else
            return true;

    }

    public boolean insertIndividualCategory ( String hoursSpent, String totalHours, String tasklList, String numOfTasks, String subCatID, String checkBoxInfo){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_HOURS_SPENT, hoursSpent);
        contentValues.put(COL_IND_TOTAL_HOURS, totalHours);
        contentValues.put(COL_TASK_LIST, tasklList);
        contentValues.put(COL_NUM_OF_TASK, numOfTasks);
        contentValues.put(COL_SUB_CAT_TABLE_ID, subCatID);
        contentValues.put(COL_CHECK_BOX_INFO, checkBoxInfo);

        long result = db.insert(TABLE_IND_CATEGORY, null, contentValues);

        if (result == -1)

            return  false;

        else

            return true;
    }

    public Cursor getActivityName(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select " + COL_ACTIVITY_ID + "," + COL_ACTIVITY_NAME + " from " + TABLE_ACTIVITY, null);
        return result;

    }

    public Cursor getSubCategories(String mainActivityID){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor categoriesResult = db.rawQuery("select * from " + TABLE_SUB_CATEGORY +" where " + COL_ACTIVITY_TABLE_ID + "=" + mainActivityID,null);
        return categoriesResult;
    }

    public Cursor getIndCatData(String subCategoryID){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor indCatResult = db.rawQuery("select * from " + TABLE_SUB_CATEGORY +" where " + COL_SUB_CATEGORY_ID + "=" + subCategoryID,null);
        return indCatResult;
    }

    public Cursor getSubCategoryId(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_SUB_CATEGORY, null);
        return result;
    }

    public Cursor getActivityID(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_ACTIVITY, null);
        return result;
    }

    public Cursor getIndCatInfo(String subCatId){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_IND_CATEGORY + " where " + COL_SUB_CAT_TABLE_ID + "=" + subCatId,null);
        return result;
    }

    public boolean updateCheckboxInfo (String checkInfo, String subCatID){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CHECK_BOX_INFO, checkInfo);
        String [] catIdArray = {subCatID};
        long result = db.update(TABLE_IND_CATEGORY, contentValues, COL_SUB_CAT_TABLE_ID + " =?", catIdArray);
        if (result == -1)
            return  false;
        else
            return  true;
    }

}
