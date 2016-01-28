package za.co.empirestate.botspost.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class MySQLiteFunctions
{
    private static final String LOG = "Hey George" ;
    private SQLiteDatabase db;
  private MySQLiteHelper dbHelper;
    public static final String COLUMN_ID = "id";
    public  static  final String COLUMN_METER = "meter_number";
  public MySQLiteFunctions(Context paramContext)
  {
    this.dbHelper = new MySQLiteHelper(paramContext);
  }

  public boolean checkPaymentHistory()
  {
    this.db = this.dbHelper.getReadableDatabase();
    boolean bool1 = this.db.rawQuery("SELECT * FROM payment_tbl", null).moveToFirst();
    boolean bool2 = false;
    if (bool1)
      bool2 = true;
    close();
    return bool2;
  }

  public void close()
  {
    this.dbHelper.close();
  }

  public void createPaymentTable(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7)
  {
    this.db = this.dbHelper.getWritableDatabase();
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("card_number", paramString1);
    localContentValues.put("card_holder_initial", paramString2);
    localContentValues.put("card_holder_surname", paramString3);
    localContentValues.put("card_cvv", paramString4);
    localContentValues.put("card_expiry_month", paramString5);
    localContentValues.put("card_expiry_year", paramString6);
    localContentValues.put("card_type", paramString7);
    this.db.insert("payment_tbl", null, localContentValues);
    close();
  }

    public void createHistoryTable(String reference, String amount, String token, String meterNumber, String units, String time,String date)
    {
        this.db = this.dbHelper.getWritableDatabase();
        ContentValues localContentValues = new ContentValues();
        localContentValues.put(dbHelper.COLUMN_REFRERENCE, reference);
        localContentValues.put(dbHelper.COLUMN_AMOUNT, amount);
        localContentValues.put(dbHelper.COLUMN_TOKEN, token);
        localContentValues.put(dbHelper.COLUMN_METER_NUMBER, meterNumber);
        localContentValues.put(dbHelper.COLUMN_UNITS, units);
        localContentValues.put("date", date);
        localContentValues.put("time", time);

        try {
            this.db.insert("history_tbl", null, localContentValues);
        }catch (SQLiteException e){
            db.execSQL("ALTER TABLE history_tbl ADD COLUMN time TEXT");
            db.execSQL("ALTER TABLE history_tbl ADD COLUMN date TEXT");
        }

        close();
    }

    public void createDeviceIdTable(String deviceId){
        this.db = this.dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("device_id",deviceId);
        this.db.insert("device_id_tbl", null, contentValues);
    }

  public void createUserTable(String meterNumber,String email,String phone)
  {
    this.db = this.dbHelper.getWritableDatabase();
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("meter_number", meterNumber);
      localContentValues.put("email", email);
      localContentValues.put("phone", phone);
    this.db.insert("user_tbl", null, localContentValues);
    close();
  }

    public void createCurrentUserTable(String meterNumber,String email,String phone)
    {
        this.db = this.dbHelper.getWritableDatabase();
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("meter_number", meterNumber);
        localContentValues.put("email", email);
        localContentValues.put("phone", phone);
        this.db.insert("current_user_tbl", null, localContentValues);
        close();
    }

    public void  createTransactionTable(String status,String time){
        this.db = this.dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status",status);
        contentValues.put("time", time);
        this.db.insert("transaction_tbl", null, contentValues);
      //  close();
    }

  public void deletePayment()
  {
    this.db = this.dbHelper.getWritableDatabase();
    this.db.rawQuery("DELETE FROM payment_tbl", null).moveToFirst();
    //close();
  }



    public void deleteTransactionHistory()
    {
        this.db = this.dbHelper.getWritableDatabase();
        this.db.rawQuery("DELETE FROM history_tbl", null).moveToFirst();
       // close();
    }

    public String getDeviceId()
    {
        this.db = this.dbHelper.getReadableDatabase();
        String str = "";
        Cursor localCursor = this.db.rawQuery("SELECT device_id FROM device_id_tbl", null);
        if (localCursor.moveToFirst())
            str = localCursor.getString(0);
        close();
        return str;
    }

  public String getMeterNumber()
  {
    this.db = this.dbHelper.getReadableDatabase();
    String str = "";
    Cursor localCursor = this.db.rawQuery("SELECT meter_number FROM user_tbl", null);
    if (localCursor.moveToFirst())
      str = localCursor.getString(0);
    close();
    return str;
  }

    public boolean getHistory()
    {
        this.db = this.dbHelper.getReadableDatabase();
        boolean str = false;
        Cursor localCursor = this.db.rawQuery("SELECT * FROM history_tbl LIMIT 0,10", null);
        if (localCursor.moveToFirst())
            str = true;
        close();
        return str;
    }

    public String getEmail()
    {
        this.db = this.dbHelper.getReadableDatabase();
        String str = "";
        Cursor localCursor = this.db.rawQuery("SELECT email FROM user_tbl", null);
        if (localCursor.moveToFirst())
            str = localCursor.getString(0);
        close();
        return str;
    }

    public String getPhone()
    {
        this.db = this.dbHelper.getReadableDatabase();
        String str = "";
        Cursor localCursor = this.db.rawQuery("SELECT phone FROM user_tbl", null);
        if (localCursor.moveToFirst())
            str = localCursor.getString(0);
       // close();
        return str;
    }

    public String getExpYear()
    {
        this.db = this.dbHelper.getReadableDatabase();
        String str = "";
        Cursor localCursor = this.db.rawQuery("SELECT meter_number FROM user_tbl", null);
        if (localCursor.moveToFirst())
            str = localCursor.getString(0);
        close();
        return str;
    }

    public void updatePhone(String phone)
    {
        this.db = this.dbHelper.getWritableDatabase();
        String str = "";
        db.rawQuery("UPDATE user_tbl SET phone = "+phone, null).moveToFirst();
        close();
    }

    public void updateEmail(String email)
    {
        this.db = this.dbHelper.getWritableDatabase();
        db.rawQuery("UPDATE user_tbl SET email = '"+email+"'", null).moveToFirst();
        close();
    }

    public void updateHistory(String meterNumber,String reference,String amount,String token,String message,String units)
    {
        this.db = this.dbHelper.getReadableDatabase();
        db.rawQuery("UPDATE user_tbl SET "+dbHelper.COLUMN_METER_NUMBER+"='"+meterNumber+"',"+dbHelper.COLUMN_REFRERENCE+"='"+reference+"',"+dbHelper.COLUMN_AMOUNT+"='"+amount+"',"+dbHelper.COLUMN_TOKEN+"='"+token+"',"+dbHelper.COLUMN_MESSAGE+"='"+message+"',"+dbHelper.COLUMN_UNITS+"='"+units+"'", null).moveToFirst();
        close();
    }

    public void updateCardDetails(String cardNumber,String cvv,String initial,String surname,String expiryMonth,String expiryYear,String last3Digits)
    {
        this.db = this.dbHelper.getWritableDatabase();
        db.rawQuery("UPDATE payment_tbl SET card_number = '"+cardNumber+"',card_cvv='"+cvv+"',card_holder_initial='"+initial+"',card_holder_surname='"+surname+"',card_expiry_month='"+expiryMonth+"',card_expiry_year='"+expiryYear+"',card_type='"+last3Digits+"'", null).moveToFirst();
        close();
    }

    public void updateMeterNumber(String meterNumber)
    {
        this.db = this.dbHelper.getWritableDatabase();
        db.rawQuery("UPDATE user_tbl SET meter_number = "+meterNumber, null).moveToFirst();
        close();
    }

    public void AddNewMeterNumber(String meterNumber){
        this.db = this.dbHelper.getWritableDatabase();
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("meter_number", meterNumber);
        this.db.insert("current_user_tbl", null, localContentValues);
        Log.d(LOG, "new meter number added to local db " + meterNumber);
        close();

    }

    public Cursor getAllMeterNumbers()
    {
        this.db = this.dbHelper.getReadableDatabase();
        return this.db.rawQuery("SELECT * FROM current_user_tbl", null);
    }



    public void deleteMeterNumber(long meterNumber) {
        String selectQuery = "SELECT  * FROM current_user_tbl" ;
        this.db = this.dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                db.delete("current_user_tbl",COLUMN_ID + "= ?",new String[]{String.valueOf(meterNumber)});

                Log.d(LOG,"Meter number deleted "+ meterNumber);
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
    }


    public void delete_byID(long id){
        this.db = this.dbHelper.getWritableDatabase();
        db.delete("current_user_tbl", COLUMN_ID + "=" + id, null);
    }
    public String[] getAllM(){
        String selectQuery = "SELECT * FROM current_user_tbl";

        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.getCount() >0)
        {
            String[] str = new String[cursor.getCount()];

            int i = 0;

            while (cursor.moveToNext())
            {
                str[i] = cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_METER_NUMBER));

                i++;
            }
            return str;
        }
        else
        {
            return new String[] {};
        }
    }


    public void deleteUser()
    {
        this.db = this.dbHelper.getWritableDatabase();
        db.rawQuery("DELETE FROM user_tbl", null).moveToFirst();
        close();
    }

  public Cursor getPaymentHistory()
  {
    this.db = this.dbHelper.getReadableDatabase();
    return this.db.rawQuery("SELECT * FROM payment_tbl", null);
  }



    public Cursor getTransactionHistory()
    {
        this.db = this.dbHelper.getReadableDatabase();
        return this.db.rawQuery("SELECT DISTINCT (reference),token,amount,date,time FROM history_tbl ORDER BY id DESC LIMIT 0,10", null);
    }

    public boolean checkCurrentUser(String meterNumber,String email,String phone){
        boolean isCurrentUser = false;
        Cursor cursor =  db.rawQuery("SELECT id FROM current_user_tbl WHERE email='" + email + "' AND meter_number='" + meterNumber + "' AND phone='" + phone+"'", null);
        if(cursor != null){
           if(cursor.moveToFirst()){
               isCurrentUser = true;
           }
        }
        //close();
        return isCurrentUser;

    }

    public void clearDatabase(){
        this.db = this.dbHelper.getWritableDatabase();
        db.rawQuery("DELETE FROM payment_tbl", null).moveToFirst();
        //db.rawQuery("DELETE FROM user_tbl", null).moveToFirst();
        db.rawQuery("DELETE FROM transaction_tbl", null).moveToFirst();
        db.rawQuery("DELETE FROM history_tbl", null).moveToFirst();
        db.rawQuery("DELETE FROM current_user_tbl", null).moveToFirst();
        close();
    }
}