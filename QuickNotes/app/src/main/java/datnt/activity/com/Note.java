package datnt.activity.com;

import java.io.Serializable;

/**
 * Created by Admin on 10/28/2018.
 */

public class Note implements Serializable {

    private long idNote;
    private String title;
    private String notes;
    private String font;
    private String size;
    private String colorText;
    private String colorNote;
    private String remindDate;
    private String remindTime;
    private String noticeNote;
    private String pathImage;
    private String pinNote;
    private String createNote;
    private long alarmTime;
    private long alarmDate;
    private long alarmNotice;


    public Note() {
    }

    public Note(long idNote, String title, String notes, String font, String size, String colorText, String colorNote,
                String remindDate, String remindTime, String noticeNote, String pathImage, String pinNote, String createNote, long alarmTime, long alarmDate, long alarmNotice) {
        this.idNote = idNote;
        this.title = title;
        this.notes = notes;
        this.font = font;
        this.size = size;
        this.colorText = colorText;
        this.colorNote = colorNote;
        this.remindDate = remindDate;
        this.remindTime = remindTime;
        this.noticeNote = noticeNote;
        this.pathImage = pathImage;
        this.pinNote = pinNote;
        this.createNote = createNote;
        this.alarmTime = alarmTime;
        this.alarmDate = alarmDate;
        this.alarmNotice = alarmNotice;
    }


    public long getIdNote() {
        return idNote;
    }

    public String getTitle() {
        return title;
    }

    public String getNotes() {
        return notes;
    }

    public String getFont() {
        return font;
    }

    public String getSize() {
        return size;
    }

    public String getColorText() {
        return colorText;
    }

    public String getColorNote() {
        return colorNote;
    }

    public String getRemindDate() {
        return remindDate;
    }

    public String getRemindTime() {
        return remindTime;
    }

    public String getNoticeNote() {
        return noticeNote;
    }

    public String getPathImage() {
        return pathImage;
    }

    public long getAlarmTime() {
        return alarmTime;
    }

    public long getAlarmDate() {
        return alarmDate;
    }

    public long getAlarmNotice() {
        return alarmNotice;
    }

    public String getPinNote() {
        return pinNote;
    }

    public String getCreateNote() {
        return createNote;
    }


    public void setIdNote(long idNote) {
        this.idNote = idNote;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setColorText(String colorText) {
        this.colorText = colorText;
    }

    public void setColorNote(String colorNote) {
        this.colorNote = colorNote;
    }

    public void setRemindDate(String remindDate) {
        this.remindDate = remindDate;
    }

    public void setRemindTime(String remindTime) {
        this.remindTime = remindTime;
    }

    public void setNoticeNote(String noticeNote) {
        this.noticeNote = noticeNote;
    }


    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public void setAlarmTime(long alarmTime) {
        this.alarmTime = alarmTime;
    }

    public void setAlarmDate(long alarmDate) {
        this.alarmDate = alarmDate;
    }

    public void setAlarmNotice(long alarmNotice) {
        this.alarmNotice = alarmNotice;
    }

    public void setPinNote(String pinNote) {
        this.pinNote = pinNote;
    }

    public void setCreateNote(String createNote) {
        this.createNote = createNote;
    }


}
