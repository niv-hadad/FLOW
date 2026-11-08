package com.niv.flow.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.niv.flow.models.ActivityLog;
import com.niv.flow.models.User;
import com.niv.flow.models.Win;

import java.util.ArrayList;

public class HelperDB extends SQLiteOpenHelper {
    public static final String DB_FILE = "flow.db";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_WINS = "wins";
    public static final String TABLE_ACTIVITY_LOGS = "activity_logs";
    public static final String USER_ID = "userId";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String CREATED_AT = "createdAt";
    public static final String FLOW_STATE = "flowState";
    public static final String LAST_WIN_DATE = "lastWinDate";
    public static final String WEEKLY_REFLECTION = "weeklyReflection";
    public static final String LAST_REFLECTION_DATE = "lastReflectionDate";
    public static final String WIN_ID = "winId";
    public static final String WIN_TITLE = "winTitle";
    public static final String WIN_CONTENT = "winContent";
    public static final String LOG_ID = "logId";
    public static final String ACTIVITY_TYPE = "activityType";
    public static final String DAY_OF_WEEK = "dayOfWeek";
    public static final String HOUR_OF_DAY = "hourOfDay";

    public HelperDB(Context context) {
        super(context, DB_FILE, null, 1);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String strCreateUsersTable = "CREATE TABLE " + TABLE_USERS + " ("
                + USER_ID + " TEXT PRIMARY KEY NOT NULL, "
                + FIRST_NAME + " TEXT NOT NULL, "
                + LAST_NAME + " TEXT NOT NULL, "
                + EMAIL + " TEXT NOT NULL UNIQUE, "
                + PASSWORD + " TEXT NOT NULL, "
                + CREATED_AT + " TEXT NOT NULL, "
                + FLOW_STATE + " INTEGER NOT NULL DEFAULT 0, "
                + LAST_WIN_DATE + " TEXT, "
                + WEEKLY_REFLECTION + " TEXT, "
                + LAST_REFLECTION_DATE + " TEXT"
                + ")";

        String strCreateWinsTable = "CREATE TABLE " + TABLE_WINS + " ("
                + WIN_ID + " TEXT PRIMARY KEY NOT NULL, "
                + USER_ID + " TEXT NOT NULL, "
                + WIN_TITLE + " TEXT NOT NULL, "
                + WIN_CONTENT + " TEXT, "
                + CREATED_AT + " TEXT NOT NULL, "
                + " FOREIGN KEY (" + USER_ID + ") REFERENCES users(" + USER_ID + ") ON DELETE CASCADE"
                + ")";

        String strCreateActivityLogsTable = "CREATE TABLE " + TABLE_ACTIVITY_LOGS + " ("
                + LOG_ID + " TEXT PRIMARY KEY NOT NULL, "
                + USER_ID + " TEXT NOT NULL, "
                + ACTIVITY_TYPE + " TEXT NOT NULL, "
                + DAY_OF_WEEK + " INTEGER NOT NULL, "
                + HOUR_OF_DAY + " INTEGER NOT NULL, "
                + CREATED_AT + " TEXT NOT NULL, "
                + " FOREIGN KEY (" + USER_ID + ") REFERENCES users(" + USER_ID + ") ON DELETE CASCADE"
                + ")";

        db.execSQL(strCreateUsersTable);
        db.execSQL(strCreateWinsTable);
        db.execSQL(strCreateActivityLogsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean hasAnyUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM users", null);
        int count = 0;

        if (cursor.moveToFirst())
            count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count > 0;
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] arrArgs = {String.valueOf(email)};
        Cursor cursor = db.query(TABLE_USERS, null, EMAIL + "=?", arrArgs, null, null, null);

        User user = null;

        while (cursor.moveToNext()) {
            String userId = cursor.getString(cursor.getColumnIndexOrThrow(USER_ID));
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow(FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow(LAST_NAME));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(PASSWORD));
            String createdAt = cursor.getString(cursor.getColumnIndexOrThrow(CREATED_AT));
            int flowState = cursor.getInt(cursor.getColumnIndexOrThrow(FLOW_STATE));
            String lastWinDate = cursor.getString(cursor.getColumnIndexOrThrow(LAST_WIN_DATE));
            String weeklyReflection = cursor.getString(cursor.getColumnIndexOrThrow(WEEKLY_REFLECTION));
            String lastReflectionDate = cursor.getString(cursor.getColumnIndexOrThrow(LAST_REFLECTION_DATE));
            user = new User(userId, firstName, lastName, email, password, createdAt, flowState, lastWinDate, weeklyReflection, lastReflectionDate);
        }
        cursor.close();
        db.close();
        return user;
    }

    public User getUserById(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] arrArgs = {String.valueOf(userId)};
        Cursor cursor = db.query(TABLE_USERS, null, USER_ID + "=?", arrArgs, null, null, null);

        User user = null;

        while (cursor.moveToNext()) {
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow(FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow(LAST_NAME));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(EMAIL));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(PASSWORD));
            String createdAt = cursor.getString(cursor.getColumnIndexOrThrow(CREATED_AT));
            int flowState = cursor.getInt(cursor.getColumnIndexOrThrow(FLOW_STATE));
            String lastWinDate = cursor.getString(cursor.getColumnIndexOrThrow(LAST_WIN_DATE));
            String weeklyReflection = cursor.getString(cursor.getColumnIndexOrThrow(WEEKLY_REFLECTION));
            String lastReflectionDate = cursor.getString(cursor.getColumnIndexOrThrow(LAST_REFLECTION_DATE));
            user = new User(userId, firstName, lastName, email, password, createdAt, flowState, lastWinDate, weeklyReflection, lastReflectionDate);
        }
        cursor.close();
        db.close();
        return user;
    }

    public String generateUserId(SQLiteDatabase db) {

        String nextUserId = "usr_001";
        Cursor cursor = db.rawQuery("SELECT MAX(CAST(SUBSTR(userId, 5) AS INTEGER)) FROM users", null);
        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            int nextNumber = cursor.getInt(0) + 1;
            nextUserId = String.format("usr_%03d", nextNumber);
        }
        cursor.close();
        return nextUserId;
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(USER_ID, generateUserId(db));
        cv.put(FIRST_NAME, user.getFirstName());
        cv.put(LAST_NAME, user.getLastName());
        cv.put(EMAIL, user.getEmail());
        cv.put(PASSWORD, user.getPassword());
        cv.put(CREATED_AT, user.getCreatedAt());
        cv.put(FLOW_STATE, user.getFlowState());
        cv.put(LAST_WIN_DATE, user.getLastWinDate());
        cv.put(WEEKLY_REFLECTION, user.getWeeklyReflection());
        cv.put(LAST_REFLECTION_DATE, user.getLastReflectionDate());

        db.insert(TABLE_USERS, null, cv);
        db.close();
    }

    private String generateLogId(SQLiteDatabase db) {
        String nextLogId = "log_001";
        Cursor cursor = db.rawQuery("SELECT MAX(CAST(SUBSTR(logId, 5) AS INTEGER)) FROM activity_logs", null);
        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            int nextNumber = cursor.getInt(0) + 1;
            nextLogId = String.format("log_%03d", nextNumber);
        }
        cursor.close();
        return nextLogId;
    }

    public void addActivityLog(ActivityLog activityLog) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(LOG_ID, generateLogId(db));
        cv.put(USER_ID, activityLog.getUserId());
        cv.put(ACTIVITY_TYPE, activityLog.getActivityType());
        cv.put(DAY_OF_WEEK, activityLog.getDayOfWeek());
        cv.put(HOUR_OF_DAY, activityLog.getHourOfDay());
        cv.put(CREATED_AT, activityLog.getCreatedAt());

        db.insert(TABLE_ACTIVITY_LOGS, null, cv);
        db.close();
    }

    private String generateWinId(SQLiteDatabase db) {
        String nextWinId = "win_001";
        Cursor cursor = db.rawQuery("SELECT MAX(CAST(SUBSTR(winId, 5) AS INTEGER)) FROM wins", null);
        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            int nextNumber = cursor.getInt(0) + 1;
            nextWinId = String.format("win_%03d", nextNumber);
        }
        cursor.close();
        return nextWinId;
    }

    public void addWin(Win win) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(WIN_ID, generateWinId(db));
        cv.put(USER_ID, win.getUserId());
        cv.put(WIN_TITLE, win.getWinTitle());
        cv.put(WIN_CONTENT, win.getWinContent());
        cv.put(CREATED_AT, win.getCreatedAt());

        db.insert(TABLE_WINS, null, cv);
        db.close();
    }

    public ArrayList<Win> getWinsByUserId(String userId) {
        ArrayList<Win> wins = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] arrArgs = {userId};
        Cursor cursor = db.query(TABLE_WINS, null, USER_ID + "=?", arrArgs, null, null, CREATED_AT + " DESC");

        while (cursor.moveToNext()) {
            String winId = cursor.getString(cursor.getColumnIndexOrThrow(WIN_ID));
            String winTitle = cursor.getString(cursor.getColumnIndexOrThrow(WIN_TITLE));
            String winContent = cursor.getString(cursor.getColumnIndexOrThrow(WIN_CONTENT));
            String createdAt = cursor.getString(cursor.getColumnIndexOrThrow(CREATED_AT));
            Win win = new Win(winId, userId, winTitle, winContent, createdAt);
            wins.add(win);
        }
        cursor.close();
        db.close();
        return wins;
    }
}
