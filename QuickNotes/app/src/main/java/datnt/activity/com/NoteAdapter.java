package datnt.activity.com;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Admin on 10/28/2018.
 */

public class NoteAdapter extends BaseAdapter {
    private ArrayList<Note> notes;
    private Context context;


    NoteAdapter(ArrayList<Note> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        @SuppressLint("ViewHolder")
        View rowView = inflater.inflate(R.layout.item_texts, parent, false);

        RadioGroup rgNote = rowView.findViewById(R.id.rgNote);

        TextView tvTitle = rowView.findViewById(R.id.tvTitle);
        TextView tvNotes = rowView.findViewById(R.id.tvNotes);
        ImageView ivPin = rowView.findViewById(R.id.ivPin);
        TextView tvCreate = rowView.findViewById(R.id.tvCreate);

        Note note = notes.get(position);

        if (note != null) {
            tvTitle.setText(note.getTitle());
            tvNotes.setText(note.getNotes());
            tvCreate.setText(note.getCreateNote());

            if (note.getPinNote() != null) {
                ivPin.setImageResource(R.drawable.pushpin);
            }

            //text color
            if (note.getColorText() != null) {
                switch (note.getColorText()) {
                    case "Red":
                        tvTitle.setTextColor(ContextCompat.getColor(context, R.color.red));
                        tvNotes.setTextColor(ContextCompat.getColor(context, R.color.red));
                        break;
                    case "Orange":
                        tvTitle.setTextColor(ContextCompat.getColor(context, R.color.orange));
                        tvNotes.setTextColor(ContextCompat.getColor(context, R.color.orange));
                        break;
                    case "Yellow":
                        tvTitle.setTextColor(ContextCompat.getColor(context, R.color.yellow));
                        tvNotes.setTextColor(ContextCompat.getColor(context, R.color.yellow));
                        break;
                    case "Green":
                        tvTitle.setTextColor(ContextCompat.getColor(context, R.color.greentext));
                        tvNotes.setTextColor(ContextCompat.getColor(context, R.color.greentext));
                        break;
                    case "Blue - Green":
                        tvTitle.setTextColor(ContextCompat.getColor(context, R.color.bluegreen));
                        tvNotes.setTextColor(ContextCompat.getColor(context, R.color.bluegreen));
                        break;
                    case "Blue":
                        tvTitle.setTextColor(ContextCompat.getColor(context, R.color.blue));
                        tvNotes.setTextColor(ContextCompat.getColor(context, R.color.blue));
                        break;
                    case "Violet":
                        tvTitle.setTextColor(ContextCompat.getColor(context, R.color.violet));
                        tvNotes.setTextColor(ContextCompat.getColor(context, R.color.violet));
                        break;
                    case "Purple":
                        tvTitle.setTextColor(ContextCompat.getColor(context, R.color.purple));
                        tvNotes.setTextColor(ContextCompat.getColor(context, R.color.purple));
                        break;
                    default:
                        tvTitle.setTextColor(ContextCompat.getColor(context, R.color.black));
                        tvNotes.setTextColor(ContextCompat.getColor(context, R.color.black));
                        break;
                }
            }

            //note color
            if (note.getColorNote() != null) {
                switch (note.getColorNote()) {
                    case "Red":
                        rgNote.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                        break;
                    case "Orange":
                        rgNote.setBackgroundColor(ContextCompat.getColor(context, R.color.orange));
                        break;
                    case "Yellow":
                        rgNote.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
                        break;
                    case "Green":
                        rgNote.setBackgroundColor(ContextCompat.getColor(context, R.color.greentext));
                        break;
                    case "Blue - Green":
                        rgNote.setBackgroundColor(ContextCompat.getColor(context, R.color.bluegreen));
                        break;
                    case "Blue":
                        rgNote.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));
                        break;
                    case "Violet":
                        rgNote.setBackgroundColor(ContextCompat.getColor(context, R.color.violet));
                        break;
                    case "Purple":
                        rgNote.setBackgroundColor(ContextCompat.getColor(context, R.color.purple));
                        break;
                    default:
                        rgNote.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
                }
            }
            //font
            if (note.getFont() != null) {
                switch (note.getFont()) {
                    case "TimesNewRoman.TTF":
                        Typeface tnrfont = ResourcesCompat.getFont(context, R.font.timesnewroman);
                        tvTitle.setTypeface(tnrfont);
                        tvNotes.setTypeface(tnrfont);
                        break;
                    case "Chikita.TTF":
                        Typeface chikitafont = ResourcesCompat.getFont(context, R.font.chikita);
                        tvTitle.setTypeface(chikitafont);
                        tvNotes.setTypeface(chikitafont);
                        break;
                    case "CiderScrip.TTF":
                        Typeface ciderScrip = ResourcesCompat.getFont(context, R.font.ciderscript);
                        tvTitle.setTypeface(ciderScrip);
                        tvNotes.setTypeface(ciderScrip);
                        break;
                    case "JustaWord.TTF":
                        Typeface justaWord = ResourcesCompat.getFont(context, R.font.justaword);
                        tvTitle.setTypeface(justaWord);
                        tvNotes.setTypeface(justaWord);
                        break;
                    case "RobotoRegular.TTF":
                        Typeface robotoRegular = ResourcesCompat.getFont(context, R.font.robotoregular);
                        tvTitle.setTypeface(robotoRegular);
                        tvNotes.setTypeface(robotoRegular);
                        break;
                    case "ZemkeHand.TTF":
                        Typeface zemkeHand = ResourcesCompat.getFont(context, R.font.zemkehand);
                        tvTitle.setTypeface(zemkeHand);
                        tvNotes.setTypeface(zemkeHand);
                        break;
                }
            }
            //size
            if (note.getSize() != null) {
                switch (note.getSize()) {
                    case "8":
                        tvTitle.setTextSize(8);
                        tvNotes.setTextSize(8);
                        break;
                    case "10":
                        tvTitle.setTextSize(10);
                        tvNotes.setTextSize(10);
                        break;
                    case "12":
                        tvTitle.setTextSize(12);
                        tvNotes.setTextSize(12);
                        break;
                    case "14":
                        tvTitle.setTextSize(14);
                        tvNotes.setTextSize(14);
                        break;
                    case "16":
                        tvTitle.setTextSize(16);
                        tvNotes.setTextSize(16);
                        break;
                    case "18":
                        tvTitle.setTextSize(18);
                        tvNotes.setTextSize(18);
                        break;
                    case "20":
                        tvTitle.setTextSize(20);
                        tvNotes.setTextSize(20);
                        break;
                    case "22":
                        tvTitle.setTextSize(22);
                        tvNotes.setTextSize(22);
                        break;
                    case "24":
                        tvTitle.setTextSize(24);
                        tvNotes.setTextSize(24);
                        break;
                    case "26":
                        tvTitle.setTextSize(26);
                        tvNotes.setTextSize(26);
                        break;
                    case "28":
                        tvTitle.setTextSize(28);
                        tvNotes.setTextSize(28);
                        break;
                    case "36":
                        tvTitle.setTextSize(36);
                        tvNotes.setTextSize(36);
                        break;
                    case "48":
                        tvTitle.setTextSize(48);
                        tvNotes.setTextSize(48);
                        break;
                    case "72":
                        tvTitle.setTextSize(72);
                        tvNotes.setTextSize(72);
                        break;
                }
            }
        }

        return rowView;
    }
}
