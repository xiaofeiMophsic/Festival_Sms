package xiaofei.com.festival_sms;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import xiaofei.com.festival_sms.bean.FestivalLab;
import xiaofei.com.festival_sms.bean.Msg;
import xiaofei.com.festival_sms.fragement.FestivalCategoryFragement;

public class ChooseMsgActivity extends AppCompatActivity {

    private ListView mLvMsgs;
    private FloatingActionButton mFabToSend;
    private ArrayAdapter<Msg> mAdapter;
    private int mFestivalId;
    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_msg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mInflater = getLayoutInflater();
        mFestivalId = getIntent().getIntExtra(FestivalCategoryFragement.ID_FESTIVAL, -1);
        initView();
        initEvent();
    }

    private void initEvent() {
        mFabToSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initView() {
        mLvMsgs = (ListView)findViewById(R.id.id_lv_msgs);
        mFabToSend = (FloatingActionButton)findViewById(R.id.id_fab_toSend);

        mLvMsgs.setAdapter(mAdapter = new ArrayAdapter<Msg>(this, -1, FestivalLab.getInstance().getMsgByFestivalId(mFestivalId)){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = mInflater.inflate(R.layout.item_msg, parent, false);
                }
                TextView content = (TextView)convertView.findViewById(R.id.id_tv_content);
                Button toSend = (Button)convertView.findViewById(R.id.id_btn_toSend);
                content.setText(getItem(position).getContent());

                toSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                return convertView;
            }
        });
    }

}
