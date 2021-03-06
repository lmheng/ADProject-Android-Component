package sg.edu.iss.mindmatters.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.text.ParseException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import sg.edu.iss.mindmatters.model.DailyQuiz;

import static java.time.LocalDate.now;

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
        db.close();
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

    public void createDummyData(String user){

        String[] sleep = { "Excellent", "Very Good", "Average", "Poor" };
        for(int i = 1;i<10;i++){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
        values.put(KEY_Q1, new Random().nextInt(100));
        values.put(KEY_Q2,sleep[new Random().nextInt(sleep.length)] );
        values.put(KEY_Q3, new Random().nextInt(10));
        System.out.println("user" + user);
        values.put(KEY_USER, user);
        LocalDate today = now();
        today.plusDays(i);  // number of days to add
        values.put(KEY_DATE, sdf.format(today));// insert
        db.insert(TABLE_NAME,null, values);
        db.close();    }
    }

    public DailyQuiz findDailyByDate(LocalDate date, String username) throws ParseException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, // a. table
                COLUMNS, // b. column names
                " date = ? AND username = ?", // c. selections
                new String[] { sdf.format(date),  username}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if(cursor.getCount() == 0)
            return null;
        if (cursor != null)
            cursor.moveToFirst();

        DailyQuiz quiz = new DailyQuiz();
        quiz.setId(Integer.parseInt(cursor.getString(0)));
        quiz.setQ1(Integer.parseInt(cursor.getString(1)));
        quiz.setQ2(cursor.getString(2));
        quiz.setQ3(Integer.parseInt(cursor.getString(3)));
        quiz.setUsername(cursor.getString(4));
        quiz.setDate(LocalDate.parse(cursor.getString(5), sdf));
        db.close();
        return quiz;
    }

    public ArrayList<Entry> getMoodData(String user)
    {
        ArrayList<Entry> DataValues=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        String query="SELECT * FROM (SELECT q1,id FROM DailyQuiz WHERE username=? ORDER BY id DESC LIMIT 7)ORDER BY id ASC";//"SELECT " + KEY_Q1 + " From " +TABLE_NAME+" WHERE "+KEY_USER+ "=? ORDER BY "+KEY_ID+" DESC LIMIT 7";
        Cursor cursor=db.rawQuery(query ,new String[]{user});
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                DataValues.add(new Entry(i + 1, (float) (Integer.parseInt(cursor.getString(0)) * .1)));
            }
        db.close();
        return DataValues;
    }
    public float averageMoodData(String user)
    {
        ArrayList<Entry> DataValues=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        float averageMood=0;
        String query="SELECT AVG(" + KEY_Q1 + ") From " +TABLE_NAME+" WHERE "+KEY_USER+ "=? ORDER BY "+KEY_ID+" DESC LIMIT 7";
        Cursor cursor=db.rawQuery(query ,new String[]{user});
        cursor.moveToFirst();
        if (cursor.getCount()>0) {
            averageMood = Float.parseFloat(cursor.getString(0));
        }
        db.close();
        return averageMood;

    }
    public float averageSleepData(String user)
    {

        SQLiteDatabase db=this.getReadableDatabase();
        float averagesleep=0;
        String query="SELECT AVG(" + KEY_Q3 + ") From " +TABLE_NAME+" WHERE "+KEY_USER+ "=? ORDER BY "+KEY_ID+" DESC LIMIT 7";
        Cursor cursor=db.rawQuery(query ,new String[]{user});
        cursor.moveToFirst();
        if (cursor.getCount()>0) {
            averagesleep = Float.parseFloat(cursor.getString(0));
        }
        db.close();
        return averagesleep;

    }

    public ArrayList<BarEntry> getSleepData(String user)
    {
        ArrayList<BarEntry> DataValues=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        String query="SELECT * FROM (SELECT q3,id FROM DailyQuiz WHERE username=? ORDER BY id DESC LIMIT 7)ORDER BY id ASC";//"SElECT*FROM(SELECT " + KEY_Q3 +","+KEY_ID+ " From " +TABLE_NAME+" WHERE "+KEY_USER+ "=? ORDER BY "+KEY_ID+" DESC LIMIT 7)order by "+KEY_ID+" ASC";
        Cursor cursor=db.rawQuery(query ,new String[]{user});
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                DataValues.add(new BarEntry(i + 1, Integer.parseInt(cursor.getString(0))));
            }
        db.close();
        return DataValues;
    }

    public int countDb(String user)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String query="SELECT * FROM DailyQuiz WHERE username = ?";
        Cursor cursor=db.rawQuery(query,new String[]{user});
        return cursor.getCount();
}

}
