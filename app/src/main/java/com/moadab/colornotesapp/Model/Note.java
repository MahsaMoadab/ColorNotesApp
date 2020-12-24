package com.moadab.colornotesapp.Model;


public class Note {

    public String noteId;
    public String noteTitle;
    public String noteDesc;
    public String noteCraeted;
    public String noteModified;
    public String noteFavorite;

    public Note() {
    }

    public Note(String noteId, String noteTitle, String noteDesc, String noteCraeted, String noteModified, String noteFavorite) {
        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.noteDesc = noteDesc;
        this.noteCraeted = noteCraeted;
        this.noteModified = noteModified;
        this.noteFavorite = noteFavorite;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDesc() {
        return noteDesc;
    }

    public void setNoteDesc(String noteDesc) {
        this.noteDesc = noteDesc;
    }

    public String getNoteCraeted() {
        return noteCraeted;
    }

    public void setNoteCraeted(String noteCraeted) {
        this.noteCraeted = noteCraeted;
    }

    public String getNoteModified() {
        return noteModified;
    }

    public void setNoteModified(String noteModified) {
        this.noteModified = noteModified;
    }

    public String getNoteFavorite() {
        return noteFavorite;
    }

    public void setNoteFavorite(String noteFavorite) {
        this.noteFavorite = noteFavorite;
    }
}
