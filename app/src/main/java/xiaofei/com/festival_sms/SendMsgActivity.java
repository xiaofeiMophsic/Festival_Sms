package xiaofei.com.festival_sms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.security.Key;

import xiaofei.com.festival_sms.bean.Festival;
import xiaofei.com.festival_sms.bean.FestivalLab;
import xiaofei.com.festival_sms.bean.Msg;
import xiaofei.com.festival_sms.view.FlowLayout;

public class SendMsgActivity extends AppCompatActivity {

    public static final String KEY_FESTIVAL_ID = "festivalId";
    public static final String KEY_MSG_ID = "msg";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_msg);

        initDatas();
        initView();
        initEvent();

        setSupportActionBar(toolbar);
    }



    private void initView() {
        mEdMsg = (EditText) findViewById(R.id.id_et_content);
        mBtnAdd = (Button) findViewById(R.id.id_btn_add);
        mFabSend = (FloatingActionButton) findViewById(R.id.id_fab_send);
        mFlContacts = (FlowLayout) findViewById(R.id.id_fl_contacts);
        mLayoutLoading = findViewById(R.id.id_layout_loading);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        mLayoutLoading.setVisibility(View.GONE);
        if(msgId != -1){
            mMsg = FestivalLab.getInstance().getMsgById(msgId);
            mEdMsg.setText(mMsg.getContent());

            mFestival = FestivalLab.getInstance().getFestivalByID(mFestivalId);
            toolbar.setTitle(mFestival.getName());
        }
    }

    private void initDatas() {
        mFestivalId = getIntent().getIntExtra(KEY_FESTIVAL_ID, -1);
        msgId = getIntent().getIntExtra(KEY_MSG_ID, -1);
    }

    private void initEvent() {
        mFabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public static void toActivity(Context context, int festivalId, int msgId){
        Intent intent = new Intent(context, SendMsgActivity.class);
        intent.putExtra(KEY_FESTIVAL_ID, festivalId);
        intent.putExtra(KEY_MSG_ID, msgId);
        context.startActivity(intent);
    }

}
