package xiaofei.com.festival_sms.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Telephony;

import xiaofei.com.festival_sms.bean.SendedMsg;

/**
 * Created by xiaofei on 2015/12/19.
 */
public class SmsDbOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "sms.db";
    private static final int DB_VERSION = 1;

    private static SmsDbOpenHelper smsDbOpenHelper;

    private SmsDbOpenHelper(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, DB_VERSION);
    }

    public static SmsDbOpenHelper getInstance(Context context){
        if(null == smsDbOpenHelper){
            synchronized (SmsDbOpenHelper.class){
                if (null == smsDbOpenHelper){
                    smsDbOpenHelper = new SmsDbOpenHelper(context);
                }
            }
        }
        return smsDbOpenHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + SendedMsg.TABLE_NAME + "(" +
                "_id integer primary key autoincrement, " +
                SendedMsg.COLUMN_MSG + " text, " +
                SendedMsg.COLUMN_DATE + " integer, " +
                SendedMsg.COLUMN_FESTIVAL_NAME + " text, " +
                SendedMsg.COLUMN_NAMES + " text," +
                SendedMsg.COLUMN_NUMBER + " text" + ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
