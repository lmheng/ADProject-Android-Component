package sg.edu.iss.mindmatters.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import sg.edu.iss.mindmatters.model.DailyQuiz;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DailyQuizDB";
    private static final String TABLE_NAME = "DailyQuiz";
    private static final String KEY_ID = "id";
    private static final String KEY_Q1 = "q1";
    private static final String KEY_Q2 = "q2";
    private static final String KEY_Q3 = "q3";
    private static final String KEY_USER = "username";
    private static final String KEY_DATE = "date";
    private static final String[] COLUMNS = { KEY_ID, KEY_Q1, KEY_Q2,
            KEY_Q3, KEY_USER , KEY_DATE};

    DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATION_TABLE = "CREATE TABLE DailyQuiz ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "q1 INTEGER, "
                + "q2 TEXT, " + "q3 INTEGER, " + " username TEXT," + " date TEXT)";

        sqLiteDatabase.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(sqLiteDatabase);
    }

    public DailyQuiz getDailyQuiz(int id) throws ParseException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, // a. table
                COLUMNS, // b. column names
                " id = ?", // c. selections
                new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        DailyQuiz quiz = new DailyQuiz();
        quiz.setId(Integer.parseInt(cursor.getString(0)));
        quiz.setQ1(Integer.parseInt(cursor.getString(1)));
        quiz.setQ2(cursor.getString(2));
        quiz.setQ3(Integer.parseInt(cursor.getString(3)));
        quiz.setUsername(cursor.getString(4));
        quiz.setDate(LocalDate.parse(cursor.getString(5), sdf));

        return quiz;
    }

    public List<DailyQuiz> allQuiz() throws ParseException {

        List<DailyQuiz> allQuiz = new LinkedList<DailyQuiz>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        DailyQuiz quiz = null;

        if (cursor.moveToFirst()) {
            do {
                quiz = new DailyQuiz();
                quiz.setId(Integer.parseInt(cursor.getString(0)));
                quiz.setQ1(Integer.parseInt(cursor.getString(1)));
                quiz.setQ2(cursor.getString(2));
                quiz.setQ3(Integer.parseInt(cursor.getString(3)));
                quiz.setUsername(cursor.getString(4));
                quiz.setDate(LocalDate.parse(cursor.getString(5), sdf));
                allQuiz.add(quiz);
            } while (cursor.moveToNext());
        }

        return allQuiz;
    }

    public void createQuizEntry(DailyQuiz quiz) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_Q1, quiz.getQ1());
        values.put(KEY_Q2, quiz.getQ2());
        values.put(KEY_Q3, quiz.getQ3());
        values.put(KEY_USER, quiz.getUsername());
        values.put(KEY_DATE, sdf.format(quiz.getDate()));
        // insert
        db.insert(TABLE_NAME,null, values);
        db.close();
    }

    public void deleteOne(DailyQuiz quiz) {
        // Get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ?", new String[] { String.valueOf(quiz.getId()) });
        db.close();
    }

    public DailyQuiz findDailyByDate(LocalDate date, String username) throws ParseException, CursorIndexOutOfBoundsException {
        System.out.println("Daily date" + date);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, // a. table
                COLUMNS, // b. column names
                " date = ? AND username = ?", // c. selections
                new String[] { sdf.format(date),  username}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        DailyQuiz quiz = new DailyQuiz();
        quiz.setId(Integer.parseInt(cursor.getString(0)));
        quiz.setQ1(Integer.parseInt(cursor.getString(1)));
        quiz.setQ2(cursor.getString(2));
        quiz.setQ3(Integer.parseInt(cursor.getString(3)));
        quiz.setUsername(cursor.getString(4));
        quiz.setDate(LocalDate.parse(cursor.getString(5), sdf));

        return quiz;
    }

}
