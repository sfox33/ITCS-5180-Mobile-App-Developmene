package edu.uncc.inclass14;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleCursorAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Sean on 4/30/2018.
 */

public class ContactDAO {
    private SQLiteDatabase db;

    public ContactDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public long save(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(ContactsTable.COLUMN_NAME, contact.getName());
        values.put(ContactsTable.COLUMN_EMAIL, contact.getEmail());
        values.put(ContactsTable.COLUMN_NAME, contact.getDept());
        values.put(ContactsTable.COLUMN_NAME, contact.getPhone());
        return db.insert(ContactsTable.TABLE_NAME, null, values);
    }

    public boolean update(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(ContactsTable.COLUMN_NAME, contact.getName());
        values.put(ContactsTable.COLUMN_EMAIL, contact.getEmail());
        values.put(ContactsTable.COLUMN_DEPT, contact.getDept());
        values.put(ContactsTable.COLUMN_PHONE, contact.getPhone());
        return db.update(ContactsTable.TABLE_NAME, values, ContactsTable.COLUMN_ID + "=?",
                new String[]{String.valueOf(contact.getId())}) > 0;
    }

    public boolean delete(Contact contact) {
        return db.delete(ContactsTable.TABLE_NAME, ContactsTable.COLUMN_ID + "=?",
                new String[]{String.valueOf(contact.getId())}) > 0;
    }

    public Contact get(long id) {
        Contact contact = null;

        Cursor cursor = db.query(ContactsTable.TABLE_NAME,
                new String[]{ContactsTable.COLUMN_ID,
                        ContactsTable.COLUMN_NAME,
                        ContactsTable.COLUMN_EMAIL,
                        ContactsTable.COLUMN_DEPT,
                        ContactsTable.COLUMN_PHONE},
                ContactsTable.COLUMN_ID + "=?", new String[]{String.valueOf(contact.getId())},
                null, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            contact = buildContactFromCursor(cursor);
            if(!cursor.isClosed()) {
                cursor.close();
            }
        }
        return contact;
    }

    public ArrayList<Contact> getAll() {
        ArrayList<Contact> contacts = new ArrayList<>();

        Cursor cursor = db.query(ContactsTable.TABLE_NAME,
                new String[]{ContactsTable.COLUMN_ID,
                        ContactsTable.COLUMN_NAME,
                        ContactsTable.COLUMN_EMAIL,
                        ContactsTable.COLUMN_DEPT,
                        ContactsTable.COLUMN_PHONE},
                null, null, null, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            do {
                contacts.add(buildContactFromCursor(cursor));
            } while (cursor.moveToNext());
            if(!cursor.isClosed()) {
                cursor.close();
            }
        }
        return contacts;
    }

    public ArrayList<Contact> getFiltered(String filter) {
        ArrayList<Contact> contacts = new ArrayList<>();

        Cursor cursor = db.query(ContactsTable.TABLE_NAME,
                new String[]{ContactsTable.COLUMN_ID,
                        ContactsTable.COLUMN_NAME,
                        ContactsTable.COLUMN_EMAIL,
                        ContactsTable.COLUMN_DEPT,
                        ContactsTable.COLUMN_PHONE},
                ContactsTable.COLUMN_NAME + " LIKE '%?%'", new String[]{filter},
                null, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            do {
                contacts.add(buildContactFromCursor(cursor));
            } while (cursor.moveToNext());
            if(!cursor.isClosed()) {
                cursor.close();
            }
        }
        return contacts;
    }

    public Contact buildContactFromCursor(Cursor cursor) {
        Contact contact = new Contact();
        contact.setId(cursor.getLong(0));
        contact.setName(cursor.getString(1));
        contact.setEmail(cursor.getString(2));
        contact.setDept(cursor.getString(3));
        contact.setPhone(cursor.getString(4));
        return contact;
    }

}
