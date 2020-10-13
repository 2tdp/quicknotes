package datnt.activity.com;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Admin on 11/13/2018.
 */

public class SQLHelper extends SQLiteOpenHelper {

    private static final String DB_Name = "QuickNotes.db";

    SQLHelper(Context context) {
        super(context, DB_Name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String querycreateTable = "create table QuickNotes(" +
                "idNote integer primary key autoincrement," +
                "title NTEXT," +
                "notes NTEXT," +
                "font text," +
                "sizeText integer," +
                "colorText text, " +
                "colorNote text," +
                "remindDate text," +
                "remindTime text," +
                "noticeNote text," +
                "pathImage text," +
                "pinNote text," +
                "createNote text," +
                "alarmTime integer," +
                "alarmDate integer," +
                "alarmNotice integer);";

        db.execSQL(querycreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == newVersion) {
            db.execSQL("drop if exists QuickNotes");

            onCreate(db);
        }
    }

    long insertNotes(String newTitle, String newNotes, String fontNote, String sizeNote, String colorText, String colorNote,
                     String remindDate, String remindTime, String noticeNote, String pathImage, String pinNote, String createNote,
                     long alarmTime, long alarmDate, long alarmNotice) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("title", newTitle);
        values.put("notes", newNotes);
        values.put("font", fontNote);
        values.put("sizeText", sizeNote);
        values.put("colorText", colorText);
        values.put("colorNote", colorNote);
        values.put("remindDate", remindDate);
        values.put("remindTime", remindTime);
        values.put("noticeNote", noticeNote);
        values.put("pathImage", pathImage);
        values.put("pinNote", pinNote);
        values.put("createNote", createNote);
        values.put("alarmTime", alarmTime);
        values.put("alarmDate", alarmDate);
        values.put("alarmNotice", alarmNotice);


        long idNote = db.insert("QuickNotes", null, values);

        return idNote;
    }

    void updateNotes(long idNote, String updateTitle, String updateNotes, String updateFont, String updateSize, String colorText, String colorNote,
                     String remindDate, String remindTime, String noticeNote, String pathImage, String pinNote, String createNote,
                     long alarmTime, long alarmDate, long alarmNotice) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", updateTitle);
        values.put("notes", updateNotes);
        values.put("font", updateFont);
        values.put("sizeText", updateSize);
        values.put("colorText", colorText);
        values.put("colorNote", colorNote);
        values.put("remindDate", remindDate);
        values.put("remindTime", remindTime);
        values.put("noticeNote", noticeNote);
        values.put("pathImage", pathImage);
        values.put("alarmTime", alarmTime);
        values.put("alarmDate", alarmDate);
        values.put("alarmNotice", alarmNotice);
        values.put("pinNote", pinNote);
        values.put("createNote", createNote);

        db.update("QuickNotes", values, "idNote = ?", new String[]{String.valueOf(idNote)});
    }

    void deleteNotes(long idNote) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete("QuickNotes", "idNote = ?", new String[]{String.valueOf(idNote)});
    }

    ArrayList<Note> getAllNote() {
        SQLiteDatabase db = getReadableDatabase();

        ArrayList<Note> allNote = new ArrayList<>();
        String title;
        String notes;
        String font;
        String size;
        String colorText;
        String colorNote;
        String remindDate;
        String remindTime;
        String noticeNote;
        String pathImage;
        String pinNote;
        String createNote;
        long alarmNote;
        long alarmDate;
        long alarmNotice;
        long idNote;

        @SuppressLint("Recycle")
        Cursor cursor = db.query(false, "QuickNotes", null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            title = cursor.getString(cursor.getColumnIndex("title"));
            notes = cursor.getString(cursor.getColumnIndex("notes"));
            idNote = cursor.getLong(cursor.getColumnIndex("idNote"));
            font = cursor.getString(cursor.getColumnIndex("font"));
            size = cursor.getString(cursor.getColumnIndex("sizeText"));
            colorText = cursor.getString(cursor.getColumnIndex("colorText"));
            colorNote = cursor.getString(cursor.getColumnIndex("colorNote"));
            remindDate = cursor.getString(cursor.getColumnIndex("remindDate"));
            remindTime = cursor.getString(cursor.getColumnIndex("remindTime"));
            noticeNote = cursor.getString(cursor.getColumnIndex("noticeNote"));
            pathImage = cursor.getString(cursor.getColumnIndex("pathImage"));
            pinNote = cursor.getString(cursor.getColumnIndex("pinNote"));
            createNote = cursor.getString(cursor.getColumnIndex("createNote"));
            alarmNote = cursor.getLong(cursor.getColumnIndex("alarmTime"));
            alarmDate = cursor.getLong(cursor.getColumnIndex("alarmDate"));
            alarmNotice = cursor.getLong(cursor.getColumnIndex("alarmNotice"));

            allNote.add(new Note(idNote, title, notes, font, size, colorText, colorNote, remindDate, remindTime, noticeNote, pathImage, pinNote, createNote, alarmNote, alarmDate, alarmNotice));

            Log.d("Notes", "Notes.getAll: ID: " + idNote + " Title: " + title + ", Notes: " + notes + ", Font: " + font + ", Size: " + size + ",/n ColorText: " + colorText + ", ColorNote: " + colorNote
                    + ", RemindDate: " + remindDate + ", RemindTime: " + remindTime + ", noticeNote: " + noticeNote + ", pathImage: " + pathImage + "pinNote: " + pinNote + "createNote: " + createNote +
                    ", alarmNotice: " + alarmNotice);
        }

        return allNote;
    }
}
