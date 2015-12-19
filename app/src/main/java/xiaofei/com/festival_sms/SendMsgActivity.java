package xiaofei.com.festival_sms;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashSet;
import java.util.Set;

import xiaofei.com.festival_sms.bean.Festival;
import xiaofei.com.festival_sms.bean.FestivalLab;
import xiaofei.com.festival_sms.bean.Msg;
import xiaofei.com.festival_sms.bean.SendedMsg;
import xiaofei.com.festival_sms.biz.SmsBiz;
import xiaofei.com.festival_sms.view.FlowLayout;

public class SendMsgActivity extends AppCompatActivity {

    public static final String KEY_FESTIVAL_ID = "festivalId";
    public static final String KEY_MSG_ID = "msg";
    private static final int CODE_REQUEST = 1;

    private int mFestivalId;
    private int msgId;

    private Festival mFestival;
    private Msg mMsg;

    private EditText mEdMsg;
    private Button mBtnAdd;
    private FlowLayout mFlContacts;
    private View mLayoutLoading;
    private FloatingActionButton mFabSend;
    private Toolbar toolbar;

    private Set<String> mContactNames = new HashSet<String>();
    private Set<String> mContactNums = new HashSet<String>();

    private LayoutInflater mInflater;

    public static final String ACTION_SEND_MSG = "ACTION_SEND_MSG";
    public static final String ACTION_DELIVER_MSG = "ACTION_DELIVER_MSG";

    private PendingIntent mSendPi;
    private PendingIntent mDeliverPi;

    private BroadcastReceiver mSendBroadcastReceiver;
    private BroadcastReceiver mDeliverBroadcastReceiver;

    private SmsBiz smsBiz ;

    private int mMsgSendCount;
    private int mTotalCount;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_msg);

        mInflater = LayoutInflater.from(this);
        smsBiz = new SmsBiz(this);
        initDatas();
        initView();
        initEvent();
        initRecivers();
        setSupportActionBar(toolbar);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initRecivers() {
        Intent sendIntent = new Intent(ACTION_SEND_MSG);
        mSendPi = PendingIntent.getBroadcast(this, 0, sendIntent, 0);
        Intent deliverIntent = new Intent(ACTION_DELIVER_MSG);
        mDeliverPi = PendingIntent.getBroadcast(this, 0, deliverIntent, 0);

        registerReceiver(mSendBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mMsgSendCount ++;
                if (getResultCode() == RESULT_OK) {
                    Log.e("TAG", "短信发送成功" + (mMsgSendCount + "/" + mTotalCount));
                }else{
                    Log.e("TAG", "短信发送失败");
                }

                Toast.makeText(SendMsgActivity.this, (mMsgSendCount + "/" + mTotalCount) + "短信发送成功", Toast.LENGTH_SHORT).show();

                if(mMsgSendCount == mTotalCount){
                    finish();
                }
            }
        }, new IntentFilter(ACTION_SEND_MSG));

        registerReceiver(mDeliverBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("TAG", "联系人已经成功接收到短信");
            }
        }, new IntentFilter(ACTION_DELIVER_MSG));
    }


    private void initView() {
        mEdMsg = (EditText) findViewById(R.id.id_et_content);
        mBtnAdd = (Button) findViewById(R.id.id_btn_add);
        mFabSend = (FloatingActionButton) findViewById(R.id.id_fab_send);
        mFlContacts = (FlowLayout) findViewById(R.id.id_fl_contacts);
        mLayoutLoading = findViewById(R.id.id_layout_loading);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        mLayoutLoading.setVisibility(View.GONE);
        if (msgId != -1) {
            mMsg = FestivalLab.getInstance().getMsgById(msgId);
            mEdMsg.setText(mMsg.getContent());
        }
        if (mFestivalId != -1){
            mFestival = FestivalLab.getInstance().getFestivalByID(mFestivalId);
            toolbar.setTitle(mFestival.getName());
        }
    }

    private void initDatas() {
        mFestivalId = getIntent().getIntExtra(KEY_FESTIVAL_ID, -1);
        msgId = getIntent().getIntExtra(KEY_MSG_ID, -1);
    }

    private void initEvent() {
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, CODE_REQUEST);
            }
        });

        mFabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mContactNums.size() == 0){
                    Toast.makeText(SendMsgActivity.this, "请选择联系人", Toast.LENGTH_SHORT).show();
                    return;
                }
                String msgContent = mEdMsg.getText().toString();
                if(TextUtils.isEmpty(msgContent)){
                    Toast.makeText(SendMsgActivity.this, "短信内容不能空", Toast.LENGTH_SHORT).show();
                    return;
                }
                mLayoutLoading.setVisibility(View.VISIBLE);
                mTotalCount = smsBiz.sendMsg(mContactNums, buildSendMsg(mMsg), mSendPi, mDeliverPi);
                mMsgSendCount = 0;
            }
        });
    }

    private SendedMsg buildSendMsg(Msg mMsg) {
        SendedMsg sendedMsg = new SendedMsg();
        sendedMsg.setFestivalName(mFestival.getName());
        sendedMsg.setMsg(mMsg.getContent());
        String names = "";
        for(String name : mContactNames){
            names += name + ":";
        }
        sendedMsg.setNames(names.substring(0, names.length() - 1));
        String nums = "";
        for(String num : mContactNums){
            nums += num + ":";
        }
        sendedMsg.setNumbers(nums.substring(0, nums.length() - 1));
        return sendedMsg;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri contentUri = data.getData();
                Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
                cursor.moveToFirst();
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                String number = getContactNumber(cursor);
                if(!TextUtils.isEmpty(number)){
                    mContactNums.add(number);
                    mContactNames.add(contactName);

                    addTag(contactName);
                }
            }
        }
    }

    private void addTag(String contactName) {
        TextView view = (TextView)mInflater.inflate(R.layout.tag, mFlContacts, false);
        view.setText(contactName);
        mFlContacts.addView(view);
    }

    private String getContactNumber(Cursor cursor) {
        String number = null;
        int numberCount = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
        if (numberCount > 0) {
            int contactId = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
            phoneCursor.moveToFirst();

            number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneCursor.close();
        }
        cursor.close();
        return number;
    }

    public static void toActivity(Context context, int festivalId, int msgId) {
        Intent intent = new Intent(context, SendMsgActivity.class);
        intent.putExtra(KEY_FESTIVAL_ID, festivalId);
        intent.putExtra(KEY_MSG_ID, msgId);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mSendBroadcastReceiver);
        unregisterReceiver(mDeliverBroadcastReceiver);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SendMsg Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://xiaofei.com.festival_sms/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SendMsg Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://xiaofei.com.festival_sms/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
