package datnt.activity.com;

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ListView lvMenu;
    ImageView ivAdd;
    ImageView ivMore;
    ImageView ivMenu;
    RadioGroup rgNote;
    RadioButton rbDateCreated;
    RadioButton rbType;
    RadioButton rbSize;
    RadioButton rbRemind;
    RadioButton rbAscending;
    RadioButton rbDescending;
    RelativeLayout rltoobar;

    ArrayList<Note> notes = new ArrayList<>();
    NoteAdapter noteAdapter;

    private DrawerLayout drawerLayout;

    int selectedNote = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DaylightAppTheme);
        } else {
            setTheme(R.style.LightAppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvMenu = findViewById(R.id.lvMenu);
        ivAdd = findViewById(R.id.ibAddNew);
        ivMore = findViewById(R.id.ivMore);
        ivMenu = findViewById(R.id.ivMenu);
        rgNote = findViewById(R.id.rgNote);
        rbDateCreated = findViewById(R.id.rbDateCreated);
        rbType = findViewById(R.id.rbType);
        rbSize = findViewById(R.id.rbSize);
        rbRemind = findViewById(R.id.rbRemind);
        rbAscending = findViewById(R.id.rbAscending);
        rbDescending = findViewById(R.id.rbDescending);
        drawerLayout = findViewById(R.id.drawer_layout);
        rltoobar = findViewById(R.id.rlToolbar);

        SimpleDateFormat day = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat month = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat hour = new SimpleDateFormat("HH", Locale.getDefault());
        SimpleDateFormat minutes = new SimpleDateFormat("mm", Locale.getDefault());

        long dd = Long.parseLong(day.format(new Date()));
        long MM = Long.parseLong(month.format(new Date()));
        long yyyy = Long.parseLong(year.format(new Date()));
        long HH = Long.parseLong(hour.format(new Date()));
        long mm = Long.parseLong(minutes.format(new Date()));
        final long time = HH * 60 + mm;
        final long date = yyyy + MM + dd;

        final SQLHelper sqlHelper = new SQLHelper(this);


        sqlHelper.getAllNote();
        notes.addAll(sqlHelper.getAllNote());

        noteAdapter = new NoteAdapter(notes, getBaseContext());
        lvMenu.setAdapter(noteAdapter);

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationView navigationView = findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.itTextNotes:
                                ArrayList<Note> notesText = new ArrayList<>();
                                for (int i = 0; i < notes.size(); i++) {
                                    Note note = notes.get(i);
                                    if (note.getPathImage() == null) {
                                        notesText.add(note);
                                    }
                                }
                                noteAdapter = new NoteAdapter(notesText, getBaseContext());
                                lvMenu.setAdapter(noteAdapter);
                                break;
                            case R.id.itPicNotes:
                                ArrayList<Note> notesPic = new ArrayList<>();
                                for (int i = 0; i < notes.size(); i++) {
                                    Note note = notes.get(i);
                                    if (note.getPathImage() != null) {
                                        notesPic.add(note);
                                    }
                                }
                                noteAdapter = new NoteAdapter(notesPic, getBaseContext());
                                lvMenu.setAdapter(noteAdapter);
                                break;
                            case R.id.itRemindNotes:
                                ArrayList<Note> notesRemind = new ArrayList<>();
                                for (int i = 0; i < notes.size(); i++) {
                                    Note note = notes.get(i);
                                    if (note.getRemindTime() != null) {
                                        notesRemind.add(note);
                                    }
                                }
                                noteAdapter = new NoteAdapter(notesRemind, getBaseContext());
                                lvMenu.setAdapter(noteAdapter);
                                break;
                            case R.id.itPinNotes:
                                ArrayList<Note> pinNote = new ArrayList<>();
                                for (int i = 0; i < notes.size(); i++) {
                                    Note note = notes.get(i);
                                    if (note.getPinNote() != null) {
                                        pinNote.add(note);
                                    }
                                }
                                noteAdapter = new NoteAdapter(pinNote, getBaseContext());
                                lvMenu.setAdapter(noteAdapter);
                                break;
                            case R.id.itRemindedNotes:
                                ArrayList<Note> remindedNotes = new ArrayList<>();
                                for (int i = 0; i < notes.size(); i++) {
                                    Note note = notes.get(i);
                                    if (note.getAlarmTime() < time && note.getAlarmDate() < date) {
                                        remindedNotes.add(note);
                                    }
                                }
                                noteAdapter = new NoteAdapter(remindedNotes, getBaseContext());
                                lvMenu.setAdapter(noteAdapter);
                                break;
                            case R.id.itSetting:
                                String[] themeApp = {"Light", "Dark"};
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Theme app")
                                        .setItems(themeApp, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                switch (i) {
                                                    case 0:
                                                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                                        recreate();
                                                        break;
                                                    case 1:
                                                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                                        recreate();
                                                        break;
                                                }
                                            }
                                        })
                                        .create().show();
                                break;
                        }

                        drawerLayout.closeDrawers();

                        return true;
                    }
                });
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), NewNotes.class);
                startActivityForResult(intent, 0);
            }
        });

        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedNote = position;
                Note selectedNote = notes.get(position);

                Intent intent = new Intent(getBaseContext(), NewNotes.class);
                intent.putExtra("notes", selectedNote);
                intent.putExtra("idNote", selectedNote.getIdNote());
                intent.putExtra("font", selectedNote.getFont());
                intent.putExtra("sizeNote", selectedNote.getSize());
                intent.putExtra("colorText", selectedNote.getColorText());
                intent.putExtra("colorNote", selectedNote.getColorNote());
                intent.putExtra("remindDate", selectedNote.getRemindDate());
                intent.putExtra("remindTime", selectedNote.getRemindTime());
                intent.putExtra("noticeNote", selectedNote.getNoticeNote());
                intent.putExtra("pathImage", selectedNote.getPathImage());
                intent.putExtra("pinNote", selectedNote.getPinNote());
                intent.putExtra("createNote", selectedNote.getCreateNote());
                startActivityForResult(intent, 99);
            }
        });

        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, ivMore);
                popupMenu.getMenuInflater().inflate(R.menu.option_main, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.itNewFolder:
                                Toast.makeText(getBaseContext(), "New Folder", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.itSort:
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setView(R.layout.sort_note)
                                        .create().show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == 0) {
                    Note back = (Note) data.getSerializableExtra("note");
                    notes.add(back);

                    noteAdapter.notifyDataSetChanged();

                } else if (requestCode == 99) {
                    Note updateNote = (Note) data.getSerializableExtra("note");
                    if (updateNote != null) {
                        notes.set(selectedNote, updateNote);
                    } else {
                        notes.remove(selectedNote);
                    }

                    noteAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
}


