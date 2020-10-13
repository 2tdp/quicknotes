package datnt.activity.com;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class NewNotes extends MainActivity {

    ImageView ivBack;
    ImageView ivRemind;
    TextView tvNewNote;
    EditText etTitle;
    EditText etNote;
    ImageView ivMoreNewNote;
    TextView tvDate;
    TextView tvTime;
    TextView tvNotice;
    ImageView ivPhoto;
    ImageView ivTrash;
    RelativeLayout rlNote;
    String[] pathImage;
    ImageView ivPin;
    long alarmTime;
    long alarmDate;
    long notice;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
    private File destFile;
    private File file;
    private Uri takePhotoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notes);

        ivBack = findViewById(R.id.ivBack);
        ivRemind = findViewById(R.id.ivReMind);
        etTitle = findViewById(R.id.etTitle);
        etNote = findViewById(R.id.etNotes);
        tvNewNote = findViewById(R.id.tvNewNote);
        ivMoreNewNote = findViewById(R.id.ivMoreNewNote);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvNotice = findViewById(R.id.tvNotice);
        ivPhoto = findViewById(R.id.ivPhoto);
        ivPin = findViewById(R.id.ivPin);
        rlNote = findViewById(R.id.rlNote);
        ivTrash = findViewById(R.id.ivTrash);

        SimpleDateFormat day = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat month = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat hour = new SimpleDateFormat("HH", Locale.getDefault());
        SimpleDateFormat minutes = new SimpleDateFormat("mm", Locale.getDefault());

        final String dd = day.format(new Date());
        final String MM = month.format(new Date());
        final String yyyy = year.format(new Date());
        final String HH = hour.format(new Date());
        final String mm = minutes.format(new Date());


        final Intent receivedIntent = getIntent();
        final Note selectedNote = (Note) receivedIntent.getSerializableExtra("notes");
        Note noteAlarm = (Note) receivedIntent.getSerializableExtra("notesAlarm");


        final long idNote = receivedIntent.getLongExtra("idNote", -1);
        final String[] font = {receivedIntent.getStringExtra("font")};
        final String[] sizeNote = {receivedIntent.getStringExtra("sizeNote")};
        final String[] colorText = {receivedIntent.getStringExtra("colorText")};
        final String[] colorNote = {receivedIntent.getStringExtra("colorNote")};
        final String[] remindDate = {receivedIntent.getStringExtra("remindDate")};
        final String[] remindTime = {receivedIntent.getStringExtra("remindTime")};
        final String[] noticeNote = {receivedIntent.getStringExtra("noticeNote")};
        pathImage = new String[]{receivedIntent.getStringExtra("pathImage")};
        final String[] pinNote = {receivedIntent.getStringExtra("pinNote")};
        final String[] createNote = {receivedIntent.getStringExtra("createNote")};

        //AlarmNotification Remind
        BroadcastReceiver alarmBroadcast = new AlarmBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.EXTRA_NO_CONNECTIVITY);
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        this.registerReceiver(alarmBroadcast, intentFilter);
        Intent intent = new Intent(NewNotes.this, AlarmBroadcastReceiver.class);

        Calendar calendar = Calendar.getInstance();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(NewNotes.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60000, pendingIntent);
            createNotificationChannel();
        }

        //Open notification
        receiveIntent(noteAlarm);
        //Open Update Note
        receiveIntent(selectedNote);

        ivRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] remind = {"Date", "Time", "Notice (Minute)"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(NewNotes.this);
                builder.setTitle("Add Remind")
                        .setItems(remind, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        DatePickerDialog dateRemind = new DatePickerDialog(NewNotes.this, new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                                remind[0] = date;
                                                alarmDate = year + (monthOfYear + 1) + dayOfMonth;
                                                builder.show();
                                            }
                                        }, 2018, 3, 8);
                                        dateRemind.show();
                                        break;
                                    case 1:
                                        TimePickerDialog timeRemind = new TimePickerDialog(NewNotes.this, new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                String time = hourOfDay + ":" + minute;
                                                remind[1] = time;
                                                alarmTime = hourOfDay * 60 + minute;
                                                builder.show();
                                            }
                                        }, 8, 40, true);
                                        timeRemind.show();
                                        break;
                                    case 2:
                                        RelativeLayout linearLayout = new RelativeLayout(NewNotes.this);

                                        final NumberPicker numberPicker = new NumberPicker(NewNotes.this);
                                        numberPicker.setMinValue(0);
                                        numberPicker.setMaxValue(60);
                                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
                                        RelativeLayout.LayoutParams numParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                        numParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                                        linearLayout.setLayoutParams(params);
                                        linearLayout.addView(numberPicker, numParams);
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(NewNotes.this);
                                        builder1.setView(linearLayout)
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        remind[2] = numberPicker.getValue() + " Minute";
                                                        notice = numberPicker.getValue();
                                                        builder.show();
                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        builder.show();
                                                    }
                                                }).create().show();
                                        break;
                                }
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (remind[0].equals("Date") && remind[1].equals("Time") && remind[2].equals("Notice (Minute)")) {
                                    tvDate.setText("");
                                    tvTime.setText("");
                                    tvNotice.setText("");

                                    remindDate[0] = null;
                                    remindTime[0] = null;
                                    noticeNote[0] = null;
                                } else if (remind[1].equals("Time") && remind[2].equals("Notice (Minute)")) {
                                    tvDate.setText(remind[0]);
                                    tvTime.setText("");
                                    tvNotice.setText("");

                                    remindDate[0] = remind[0];
                                    remindTime[0] = null;
                                    noticeNote[0] = null;
                                } else if (remind[0].equals("Date") && remind[2].equals("Notice (Minute)")) {
                                    tvDate.setText("");
                                    tvTime.setText(remind[1]);
                                    tvNotice.setText("");

                                    remindDate[0] = null;
                                    remindTime[0] = remind[1];
                                    noticeNote[0] = null;
                                } else {
                                    tvDate.setText(remind[0]);
                                    tvTime.setText(remind[1]);
                                    tvNotice.setText(remind[2]);

                                    remindDate[0] = remind[0];
                                    remindTime[0] = remind[1];
                                    noticeNote[0] = remind[2];
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create().show();
            }
        });

        ivPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pinNote[0] == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewNotes.this);
                    builder.setTitle("Pin notes?")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    pinNote[0] = "Pin";
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            })
                            .create().show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewNotes.this);
                    builder.setTitle("Unpin notes?")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    pinNote[0] = "";
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            })
                            .create().show();
                }
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLHelper sqlHelper = new SQLHelper(getBaseContext());

                String title = etTitle.getText().toString();
                String notes = etNote.getText().toString();
                int photo = ivPhoto.getHeight();

                Note note;

                createNote[0] = (dd + "/" + MM + "/" + yyyy + ", " + HH + " : " + mm);
                if (idNote > -1 && (!title.equals("") || !notes.equals("") || photo != 0)) {
                    sqlHelper.updateNotes(idNote, title, notes, font[0], sizeNote[0], colorText[0], colorNote[0], remindDate[0], remindTime[0],
                            noticeNote[0], pathImage[0], pinNote[0], createNote[0], alarmTime, alarmDate, notice);
                    note = new Note(idNote, title, notes, font[0], sizeNote[0], colorText[0], colorNote[0], remindDate[0], remindTime[0],
                            noticeNote[0], pathImage[0], pinNote[0], createNote[0], alarmTime, alarmDate, notice);

                    Intent intent = new Intent();
                    intent.putExtra("note", note);
                    setResult(RESULT_OK, intent);

                    finish();
                } else if (idNote == -1 && !title.equals("") || !notes.equals("") || photo != 0) {
                    long id = sqlHelper.insertNotes(title, notes, font[0], sizeNote[0],
                            colorText[0], colorNote[0], remindDate[0], remindTime[0], noticeNote[0], pathImage[0], pinNote[0], createNote[0], alarmTime, alarmDate, notice);
                    note = new Note(id, title, notes, font[0], sizeNote[0], colorText[0], colorNote[0], remindDate[0], remindTime[0],
                            noticeNote[0], pathImage[0], pinNote[0], createNote[0], alarmTime, alarmDate, notice);

                    Intent intent = new Intent();
                    intent.putExtra("note", note);
                    setResult(RESULT_OK, intent);

                    finish();
                } else if (idNote > -1){
                    sqlHelper.deleteNotes(idNote);
                    Note note1 = null;
                    Intent intent = new Intent();
                    intent.putExtra("note", note1);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    finish();
                }
            }

        });

        ivMoreNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(NewNotes.this, ivMoreNewNote);
                popupMenu.getMenuInflater().inflate(R.menu.more_newnote, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.itTakePhoto:
                                file = new File(getFilesDir(), "images");
                                if (!file.exists()) {
                                    file.mkdir();
                                }
                                destFile = new File(file, "img_" + dateFormatter.format(new Date()).toString() + ".png");
                                takePhotoUri = FileProvider.getUriForFile(NewNotes.this, "com.datnt.fileprovider", destFile);
                                pathImage[0] = takePhotoUri.toString();
                                if (ContextCompat.checkSelfPermission(NewNotes.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    if (ActivityCompat.shouldShowRequestPermissionRationale(NewNotes.this, Manifest.permission.CAMERA)) {
                                        Toast.makeText(NewNotes.this, "Camera can't open!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        ActivityCompat.requestPermissions(NewNotes.this, new String[]{Manifest.permission.CAMERA}, 13);
                                    }
                                } else {
                                    Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    if (takePhoto.resolveActivity(getPackageManager()) != null) {
                                        takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, takePhotoUri);
                                        startActivityForResult(takePhoto, 505);
                                    }
                                }
                                break;
                            case R.id.itChooseImage:
                                if (ContextCompat.checkSelfPermission(NewNotes.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    if (ActivityCompat.shouldShowRequestPermissionRationale(NewNotes.this,
                                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                        Toast.makeText(NewNotes.this, "Can't access", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                            ActivityCompat.requestPermissions(NewNotes.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 12);
                                        }
                                    }
                                } else {
                                    Intent intentCamera = new Intent(Intent.ACTION_PICK);
                                    intentCamera.setType("image/*");
                                    startActivityForResult(intentCamera, 55);
                                }
                                break;
                            case R.id.itFont:
                                final String[] fontFamily = {"TimesNewRoman.TTF", "Chikita.TTF", "CiderScrip.TTF", "JustaWord.TTF", "RobotoRegular.TTF", "ZemkeHand.TTF"};
                                AlertDialog.Builder builderFontText = new AlertDialog.Builder(NewNotes.this);
                                builderFontText.setTitle("Choose Font: ")
                                        .setItems(fontFamily, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                font[0] = fontFamily[i];
                                                switch (i) {
                                                    case 0:
                                                        Typeface font = ResourcesCompat.getFont(getBaseContext(), R.font.timesnewroman);
                                                        etTitle.setTypeface(font);
                                                        etNote.setTypeface(font);
                                                        break;
                                                    case 1:
                                                        Typeface font1 = ResourcesCompat.getFont(getBaseContext(), R.font.chikita);
                                                        etTitle.setTypeface(font1);
                                                        etNote.setTypeface(font1);
                                                        break;
                                                    case 2:
                                                        Typeface font2 = ResourcesCompat.getFont(getBaseContext(), R.font.ciderscript);
                                                        etTitle.setTypeface(font2);
                                                        etNote.setTypeface(font2);
                                                        break;
                                                    case 3:
                                                        Typeface font3 = ResourcesCompat.getFont(getBaseContext(), R.font.justaword);
                                                        etTitle.setTypeface(font3);
                                                        etNote.setTypeface(font3);
                                                        break;
                                                    case 4:
                                                        Typeface font4 = ResourcesCompat.getFont(getBaseContext(), R.font.robotoregular);
                                                        etTitle.setTypeface(font4);
                                                        etNote.setTypeface(font4);
                                                        break;
                                                    case 5:
                                                        Typeface font5 = ResourcesCompat.getFont(getBaseContext(), R.font.zemkehand);
                                                        etTitle.setTypeface(font5);
                                                        etNote.setTypeface(font5);
                                                        break;
                                                }
                                            }
                                        })
                                        .create().show();
                                break;
                            case R.id.itTextSize:
                                final String[] sizeText = {"8", "10", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "72"};
                                AlertDialog.Builder builderSizeText = new AlertDialog.Builder(NewNotes.this);
                                builderSizeText.setTitle("Choose Size: ")
                                        .setItems(sizeText, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                sizeNote[0] = sizeText[which];
                                                etTitle.setTextSize(Float.parseFloat(sizeText[which]));
                                                etNote.setTextSize(Float.parseFloat(sizeText[which]));
                                            }
                                        })
                                        .create().show();
                                break;
                            case R.id.itTextColor:
                                final String[] textColor = {"Red", "Orange", "Yellow", "Green", "Blue - Green", "Blue", "Violet", "Purple"};
                                final AlertDialog.Builder textColorBuilder = new AlertDialog.Builder(NewNotes.this);
                                textColorBuilder.setTitle("Choose Color Note: ")
                                        .setItems(textColor, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                colorText[0] = textColor[which];
                                                switch (which) {
                                                    case 0:
                                                        etTitle.setTextColor(getResources().getColor(R.color.red));
                                                        etNote.setTextColor(getResources().getColor(R.color.red));
                                                        break;
                                                    case 1:
                                                        etTitle.setTextColor(getResources().getColor(R.color.orange));
                                                        etNote.setTextColor(getResources().getColor(R.color.orange));
                                                        break;
                                                    case 2:
                                                        etTitle.setTextColor(getResources().getColor(R.color.yellow));
                                                        etNote.setTextColor(getResources().getColor(R.color.yellow));
                                                        break;
                                                    case 3:
                                                        etTitle.setTextColor(getResources().getColor(R.color.green));
                                                        etNote.setTextColor(getResources().getColor(R.color.green));
                                                        break;
                                                    case 4:
                                                        etTitle.setTextColor(getResources().getColor(R.color.bluegreen));
                                                        etNote.setTextColor(getResources().getColor(R.color.bluegreen));
                                                        break;
                                                    case 5:
                                                        etTitle.setTextColor(getResources().getColor(R.color.blue));
                                                        etNote.setTextColor(getResources().getColor(R.color.blue));
                                                        break;
                                                    case 6:
                                                        etTitle.setTextColor(getResources().getColor(R.color.violet));
                                                        etNote.setTextColor(getResources().getColor(R.color.violet));
                                                        break;
                                                    case 7:
                                                        etTitle.setTextColor(getResources().getColor(R.color.purple));
                                                        etNote.setTextColor(getResources().getColor(R.color.purple));
                                                        break;
                                                }
                                            }
                                        })
                                        .create().show();
                                break;
                            case R.id.itNoteColor:
                                final String[] noteColor = {"Red", "Orange", "Yellow", "Green", "Blue - Green", "Blue", "Violet", "Purple"};
                                AlertDialog.Builder noteColorBuilder = new AlertDialog.Builder(NewNotes.this);
                                noteColorBuilder.setTitle("Choose Color Note: ")
                                        .setItems(noteColor, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                colorNote[0] = noteColor[which];
                                                switch (which) {
                                                    case 0:
                                                        rlNote.setBackgroundColor(getResources().getColor(R.color.red));
                                                        break;
                                                    case 1:
                                                        rlNote.setBackgroundColor(getResources().getColor(R.color.orange));
                                                        break;
                                                    case 2:
                                                        rlNote.setBackgroundColor(getResources().getColor(R.color.yellow));
                                                        break;
                                                    case 3:
                                                        rlNote.setBackgroundColor(getResources().getColor(R.color.green));
                                                        break;
                                                    case 4:
                                                        rlNote.setBackgroundColor(getResources().getColor(R.color.bluegreen));
                                                        break;
                                                    case 5:
                                                        rlNote.setBackgroundColor(getResources().getColor(R.color.blue));
                                                        break;
                                                    case 6:
                                                        rlNote.setBackgroundColor(getResources().getColor(R.color.violet));
                                                        break;
                                                    case 7:
                                                        rlNote.setBackgroundColor(getResources().getColor(R.color.purple));
                                                        break;
                                                }
                                            }
                                        })
                                        .create().show();
                                break;
                            case R.id.itDelete:
                                SQLHelper sqlHelper = new SQLHelper(getBaseContext());
                                sqlHelper.deleteNotes(idNote);

                                if (selectedNote != null) {
                                    Note note = null;
                                    Intent intent = new Intent();
                                    intent.putExtra("note", note);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                } else {
                                    finish();
                                }
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        ivTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLHelper sqlHelper = new SQLHelper(getBaseContext());
                sqlHelper.deleteNotes(idNote);

                if (selectedNote != null) {
                    Note note = null;
                    Intent intent = new Intent();
                    intent.putExtra("note", note);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    finish();
                }
            }
        });
    }

    public void receiveIntent(Note note) {
        if (note != null) {
            tvNewNote.setText(R.string.update_note);
            etTitle.setText(note.getTitle());
            etNote.setText(note.getNotes());

            //Image
            if (note.getPathImage() != null) {
                Glide.with(this)
                        .load(note.getPathImage())
                        .into(ivPhoto);
                pathImage[0] = note.getPathImage();
            }

            //change font
            if (note.getFont() != null) {
                switch (note.getFont()) {
                    case "TimesNewRoman.TTF":
                        Typeface tnrfont = ResourcesCompat.getFont(getBaseContext(), R.font.timesnewroman);
                        etTitle.setTypeface(tnrfont);
                        etNote.setTypeface(tnrfont);
                        break;
                    case "Chikita.TTF":
                        Typeface chikitafont = ResourcesCompat.getFont(getBaseContext(), R.font.chikita);
                        etTitle.setTypeface(chikitafont);
                        etNote.setTypeface(chikitafont);
                        break;
                    case "CiderScrip.TTF":
                        Typeface ciderScrip = ResourcesCompat.getFont(getBaseContext(), R.font.ciderscript);
                        etTitle.setTypeface(ciderScrip);
                        etNote.setTypeface(ciderScrip);
                        break;
                    case "JustaWord.TTF":
                        Typeface justaWord = ResourcesCompat.getFont(getBaseContext(), R.font.justaword);
                        etTitle.setTypeface(justaWord);
                        etNote.setTypeface(justaWord);
                        break;
                    case "RobotoRegular.TTF":
                        Typeface robotoRegular = ResourcesCompat.getFont(getBaseContext(), R.font.robotoregular);
                        etTitle.setTypeface(robotoRegular);
                        etNote.setTypeface(robotoRegular);
                        break;
                    case "ZemkeHand.TTF":
                        Typeface zemkeHand = ResourcesCompat.getFont(getBaseContext(), R.font.zemkehand);
                        etTitle.setTypeface(zemkeHand);
                        etNote.setTypeface(zemkeHand);
                        break;
                }
            }
            //change size

            if (note.getSize() != null) {
                etTitle.setTextSize(Float.parseFloat(note.getSize()));
            }
            //change color text
            if (note.getColorText() != null) {
                switch (note.getColorText()) {
                    case "Red":
                        etTitle.setTextColor(getResources().getColor(R.color.red));
                        etNote.setTextColor(getResources().getColor(R.color.red));
                        break;
                    case "Orange":
                        etTitle.setTextColor(getResources().getColor(R.color.orange));
                        etNote.setTextColor(getResources().getColor(R.color.orange));
                        break;
                    case "Yellow":
                        etTitle.setTextColor(getResources().getColor(R.color.yellow));
                        etNote.setTextColor(getResources().getColor(R.color.yellow));
                        break;
                    case "Green":
                        etTitle.setTextColor(getResources().getColor(R.color.greentext));
                        etNote.setTextColor(getResources().getColor(R.color.greentext));
                        break;
                    case "Blue - Green":
                        etTitle.setTextColor(getResources().getColor(R.color.bluegreen));
                        etNote.setTextColor(getResources().getColor(R.color.bluegreen));
                        break;
                    case "Blue":
                        etTitle.setTextColor(getResources().getColor(R.color.blue));
                        etNote.setTextColor(getResources().getColor(R.color.blue));
                        break;
                    case "Violet":
                        etTitle.setTextColor(getResources().getColor(R.color.violet));
                        etNote.setTextColor(getResources().getColor(R.color.violet));
                        break;
                    case "Purple":
                        etTitle.setTextColor(getResources().getColor(R.color.purple));
                        etNote.setTextColor(getResources().getColor(R.color.purple));
                        break;
                }
            }
            //change color note
            if (note.getColorNote() != null) {
                switch (note.getColorNote()) {
                    case "Red":
                        rlNote.setBackgroundColor(getResources().getColor(R.color.red));
                        break;
                    case "Orange":
                        rlNote.setBackgroundColor(getResources().getColor(R.color.orange));
                        break;
                    case "Yellow":
                        rlNote.setBackgroundColor(getResources().getColor(R.color.yellow));
                        break;
                    case "Green":
                        rlNote.setBackgroundColor(getResources().getColor(R.color.greentext));
                        break;
                    case "Blue - Green":
                        rlNote.setBackgroundColor(getResources().getColor(R.color.bluegreen));
                        break;
                    case "Blue":
                        rlNote.setBackgroundColor(getResources().getColor(R.color.blue));
                        break;
                    case "Violet":
                        rlNote.setBackgroundColor(getResources().getColor(R.color.violet));
                        break;
                    case "Purple":
                        rlNote.setBackgroundColor(getResources().getColor(R.color.purple));
                        break;
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //set image to ImageView
            switch (requestCode) {
                case 505:
                    Log.d("TakeCamera: ", "Selected image uri path :" + takePhotoUri);
                    ivPhoto.setImageURI(takePhotoUri);
                    break;
                case 55:
                    Uri uriPhoto = data.getData();
                    if (uriPhoto != null) {
                        Log.d("CameraSelected: ", "Selected image uri path :" + uriPhoto.toString());
                        pathImage = new String[]{uriPhoto.toString()};
                    }
                    ivPhoto.setImageURI(uriPhoto);
            }
        }

    }

    @Override
    public void onBackPressed() {
        ivBack = findViewById(R.id.ivBack);
        ivBack.callOnClick();
        super.onBackPressed();
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "remind";
            String description = "remind note";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("05", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }

        }
    }
}
