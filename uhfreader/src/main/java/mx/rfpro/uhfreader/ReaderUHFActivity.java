package mx.rfpro.uhfreader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rscja.deviceapi.RFIDWithUHF;

import java.util.HashSet;
import java.util.Set;

import mx.rfpro.uhfreader.adapter.TagAdpater;

public class ReaderUHFActivity extends AppCompatActivity {

    private static final int TRIGGER_CODE = 280;


    public RFIDWithUHF mReader;
    private Set<String> tagSet;
    private TagAdpater tagAdpater;
    private boolean loopFlag = false;
    private ReaderUHFActivity mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reader_uhf_main_layout);
        mContext = this;


        initUHF();
        tagSet = new HashSet<String>();


        RecyclerView rv = findViewById(R.id.recyclerViewTag);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        tagAdpater = new TagAdpater(tagSet);
        rv.setAdapter(tagAdpater);


    }

    public void initUHF() {
        try {
            mReader = RFIDWithUHF.getInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        if (mReader != null) {
            new InitTask().execute();
        }
    }

    private void readTag(){

        String strUII = mContext.mReader.inventorySingleTag();
        if (!TextUtils.isEmpty(strUII)) {
            String strEPC = mContext.mReader.convertUiiToEPC(strUII);
            tagSet.add(strEPC);
            tagAdpater.updateList(tagSet);
            tagAdpater.notifyDataSetChanged();

        }

    }


    public class InitTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            return mReader.init();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {

        }

    }


    @Override
    protected void onDestroy() {

        if (mReader != null) {
            mReader.free();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==TRIGGER_CODE){

            readTag();

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d("DEBUG----","upkey code "+keyCode);
        return super.onKeyUp(keyCode, event);
    }
}
