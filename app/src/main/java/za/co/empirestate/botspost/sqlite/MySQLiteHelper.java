package za.co.empirestate.botspost.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper
{
  public static final String COLUMN_CARD_CVV = "card_cvv";
  public static final String COLUMN_CARD_EXPIRY_MONTH = "card_expiry_month";
  public static final String COLUMN_CARD_EXPIRY_YEAR = "card_expiry_year";
  public static final String COLUMN_CARD_HOLDER_INITIAL = "card_holder_initial";
  public static final String COLUMN_CARD_HOLDER_SURNAME = "card_holder_surname";
  public static final String COLUMN_CARD_NUMBER = "card_number";
  public static final String COLUMN_CARD_TYPE = "card_type";
  public static final String COLUMN_ID = "id";
  public static final String COLUMN_METER_NUMBER = "meter_number";
  public static final String COLUMN_EMAIL = "email";
  public static final String COLUMN_PHONE = "phone";
  public static final String COLUMN_REFRERENCE = "reference";
  public static final String COLUMN_AMOUNT = "amount";
  public static final String COLUMN_UNITS = "units";
  public static final String COLUMN_TOKEN = "token";
  public static final String COLUMN_MESSAGE = "message";
  public static final String PAYMENT_TBL = "payment_tbl";
  public static final String USER_TBL = "user_tbl";
  public static final String CREATE_TABLE_PAYMENT = "CREATE TABLE IF NOT EXISTS payment_tbl(id INTEGER PRIMARY KEY, card_number TEXT,card_holder_initial TEXT,card_holder_surname TEXT,card_expiry_month TEXT,card_expiry_year TEXT,card_cvv TEXT,card_type TEXT);";
  public static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS user_tbl(id INTEGER PRIMARY KEY, meter_number TEXT ,email TEXT,phone TEXT);";
  public static final String CREATE_TABLE_TRANSACTION = "CREATE TABLE IF NOT EXISTS transaction_tbl(id INTEGER PRIMARY KEY,status TEXT,time TEXT);";
  public static final String CREATE_TABLE_HISTORY = "CREATE TABLE IF NOT EXISTS history_tbl(id INTEGER PRIMARY KEY, meter_number TEXT,reference TEXT,amount TEXT,units TEXT,token TEXT,message TEXT,date TEXT,time TEXT);";
  public static final String CREATE_TABLE_DEVICE_ID = "CREATE TABLE IF NOT EXISTS device_id_tbl (device_id TEXT);";
  public static final String CREATE_TABLE_CURRENT_USER = "CREATE TABLE IF NOT EXISTS current_user_tbl (id INTEGER PRIMARY KEY ,email TEXT,phone TEXT,meter_number  );";
  private static final String DB_NAME = "preamped.db";
  private static final int DB_VERSION = 6;

  public MySQLiteHelper(Context paramContext)
  {
    super(paramContext, "preamped.db", null, DB_VERSION);
  }

  public void onCreate(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL(CREATE_TABLE_USER);
    paramSQLiteDatabase.execSQL(CREATE_TABLE_PAYMENT);
    paramSQLiteDatabase.execSQL(CREATE_TABLE_HISTORY);
    paramSQLiteDatabase.execSQL(CREATE_TABLE_TRANSACTION);
    paramSQLiteDatabase.execSQL(CREATE_TABLE_DEVICE_ID);
    paramSQLiteDatabase.execSQL(CREATE_TABLE_CURRENT_USER);
  }

  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
  {
      onCreate(db);
      if(oldVersion < newVersion ){
          //db.execSQL("ALTER TABLE history_tbl ADD COLUMN time TEXT");
          //db.execSQL("ALTER TABLE history_tbl ADD COLUMN date TEXT");
          db.execSQL(CREATE_TABLE_TRANSACTION);
          db.execSQL(CREATE_TABLE_DEVICE_ID);
          db.execSQL(CREATE_TABLE_CURRENT_USER);

      }

  }
}