package edu.uncc.inclass14;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Sean on 4/30/2018.
 */

public class ContactsTable {
    static final String TABLE_NAME = "Contacts";
    static final String COLUMN_ID = "id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_EMAIL = "email";
    static final String COLUMN_DEPT = "dept";
    static final String COLUMN_PHONE = "phone";

    public static void onCreate(SQLiteDatabase db) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE " + TABLE_NAME + "(");
        builder.append(COLUMN_ID + " integer primary key autoincrement, ");
        builder.append(COLUMN_NAME + " text not null, ");
        builder.append(COLUMN_EMAIL + " text not null, ");
        builder.append(COLUMN_DEPT + " text not null, ");
        builder.append(COLUMN_PHONE + " text not null");
        builder.append(");");

        try {
            db.execSQL(builder.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void onUpgrade(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
        ContactsTable.onCreate(db);
    }
}
