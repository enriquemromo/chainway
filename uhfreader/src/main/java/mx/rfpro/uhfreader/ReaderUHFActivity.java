package mx.rfpro.uhfreader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rscja.deviceapi.RFIDWithUHF;

import java.util.HashSet;
import java.util.Set;

import mx.rfpro.uhfreader.adapter.TagAdpater;

public class ReaderUHFActivity extends AppCompatActivity {

    private static final int TRIGGER_CODE = 280;
    private static final int SCAN_BUTTON_CODE = 139;


    private ReaderUHFActivity mContext;
    private RFIDWithUHF mReader;
    private Set<String> tagSet;
    private TagAdpater tagAdpater;
    private boolean loopFlag;
    private ProgressBar progressBar;
    private Button clearButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reader_uhf_main_layout);
        RecyclerView rv = findViewById(R.id.recyclerViewTag);
        clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tagSet.clear();
                tagAdpater.updateList(tagSet);
                new UpdateTagListTask().execute();
            }
        });
        mContext = this;
        initUHF();
        tagSet = new HashSet<String>();

        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        tagAdpater = new TagAdpater();
        rv.setAdapter(tagAdpater);


        progressBar = findViewById(R.id.progressBar);


    }

    public void initUHF() {
        try {
            mReader = RFIDWithUHF.getInstance();
           /* mContext.mReader.setFilter(RFIDWithUHF.BankEnum.valueOf("UII"), 32, 32,
                    "67808275", false);*/

        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        if (mReader != null) {
            new InitTask().execute();
        }
    }

    private void readSingleTag(){

        String strUII = mContext.mReader.inventorySingleTag();
        if (!TextUtils.isEmpty(strUII)) {
            String strEPC = mContext.mReader.convertUiiToEPC(strUII);
            tagSet.add(strEPC);
            tagAdpater.updateList(tagSet);
            tagAdpater.notifyDataSetChanged();

        }

    }


    @Override
    public void onPause() {
        Log.i("MY", "UHFReadTagFragment.onPause");
        super.onPause();

        // 停止识别
        stopInventory();
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
        if(keyCode == SCAN_BUTTON_CODE || keyCode==TRIGGER_CODE){


            if(!loopFlag){
                if(mContext.mReader.startInventoryTag(0,0)){
                    loopFlag = true;

                    progressBar.setVisibility(View.VISIBLE);
                    new ScanTagTask().start();
                    Log.d("DEBUG---","Start");

                }

            }else {
                loopFlag = false;
                progressBar.setVisibility(View.GONE);
                Log.d("DEBUG---","Stop");
            }


        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d("DEBUG----","upkey code "+keyCode);
        return super.onKeyUp(keyCode, event);
    }


    private void stopInventory() {
        if (loopFlag) {
            loopFlag = false;
        }
    }

    private class UpdateTagListTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            tagAdpater.notifyDataSetChanged();
        }
    }

    private class InitTask extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... voids) {
            mReader.init();

            if (mReader.setFilter(RFIDWithUHF.BankEnum.valueOf("UII"), 32, 16,
                    "454C", false)) {
                //Toast.makeText(mContext, "Success",Toast.LENGTH_LONG).show();
                Log.d("DEBUG---","success");
            } else {
                Log.d("DEBUG---","fail");
            }

            return true;
        }
    }


    private class ScanTagTask extends Thread{

        @Override
        public void run() {
            String strTid;
            String strResult;
            String[] res = null;
            while (loopFlag) {
                res = mContext.mReader.readTagFromBuffer();
                if (res != null) {
                    strTid = res[0];
                    if (strTid.length() != 0 && !strTid.equals("0000000" +
                            "000000000") && !strTid.equals("000000000000000000000000")) {
                        strResult = "TID:" + strTid + "\n";
                    } else {
                        strResult = "";
                    }
                    Log.i("data","EPC:"+res[1]+"|"+strResult);

                    tagSet.add( mContext.mReader.convertUiiToEPC(res[1]) );
                    tagAdpater.updateList(tagSet);
                    new UpdateTagListTask().execute();
                }
            }
        }
    }
}
