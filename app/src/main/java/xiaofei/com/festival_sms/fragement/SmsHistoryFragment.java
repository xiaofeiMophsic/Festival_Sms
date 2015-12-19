package xiaofei.com.festival_sms.fragement;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import xiaofei.com.festival_sms.R;
import xiaofei.com.festival_sms.bean.SendedMsg;
import xiaofei.com.festival_sms.db.SmsProvider;
import xiaofei.com.festival_sms.view.FlowLayout;

/**
 * Created by xiaofei on 2015/12/19.
 */
public class SmsHistoryFragment extends ListFragment {

    private static final int LOADER_ID = 1;
    private LayoutInflater mInflater;
    private CursorAdapter mCursorAdapter;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mInflater = LayoutInflater.from(getActivity());
        initLoader();
        setupListAdapter();
    }

    private void setupListAdapter() {
        mCursorAdapter = new CursorAdapter(getActivity(), null, false) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = mInflater.inflate(R.layout.item_sended_msg, parent, false);
                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView tvMsg = (TextView)view.findViewById(R.id.id_tv_msg);
                FlowLayout flContacts = (FlowLayout)view.findViewById(R.id.id_fl_contacts);
                TextView tvFes = (TextView)view.findViewById(R.id.id_tv_fes);
                TextView tvDate = (TextView)view.findViewById(R.id.id_tv_date);

                tvMsg.setText(cursor.getString(cursor.getColumnIndex(SendedMsg.COLUMN_MSG)));
                tvFes.setText(cursor.getString(cursor.getColumnIndex(SendedMsg.COLUMN_FESTIVAL_NAME)));
                long date = cursor.getLong(cursor.getColumnIndex(SendedMsg.COLUMN_DATE));
                tvDate.setText(parseDate(date));

                String names = cursor.getString(cursor.getColumnIndex(SendedMsg.COLUMN_NAMES));
                if(TextUtils.isEmpty(names)){
                    return ;
                }
                flContacts.removeAllViews();
                for(String name : names.split(":")){
                    addTag(name, flContacts);
                }
            }
        };
        setListAdapter(mCursorAdapter);
    }
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private String parseDate(long date) {
        return dateFormat.format(date);
    }

    private void addTag(String name, FlowLayout flContacts) {
        TextView tv = (TextView)mInflater.inflate(R.layout.tag, flContacts, false);
        tv.setText(name);
        flContacts.addView(tv);
    }

    private void initLoader() {
        getLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                CursorLoader loader = new CursorLoader(getActivity(), SmsProvider.URI_SMS_ALL, null, null, null, null);
                return loader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if(loader.getId() == LOADER_ID){
                    mCursorAdapter.swapCursor(data);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                    mCursorAdapter.swapCursor(null);
            }
        });
    }
}
