package mx.rfpro.barcodereader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.io.UnsupportedEncodingException;

public class BarCodeActivity extends AppCompatActivity {
    private final static String TAG = BarCodeActivity.class.getName();

    private static final int TRIGGER_CODE = 280;
    private static final int SCAN_BUTTON_CODE = 139;

    private Barcode2DWithSoft barcode2DWithSoft=null;
    private TextView barCodeTextView;
    private String seldata="ASCII";
    private String barCode="";
    private boolean loopFlag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_activity_layout);

        barcode2DWithSoft = Barcode2DWithSoft.getInstance();
        barCodeTextView = findViewById(R.id.barCodetextView);
        loopFlag = false;
        new InitTask().execute();

    }

    private void scanBarcode(){
        if(barcode2DWithSoft!=null) {
            Log.i(TAG,"ScanBarcode");

            barcode2DWithSoft.scan();
            barcode2DWithSoft.setScanCallback(ScanBack);
        }
    }

    public Barcode2DWithSoft.ScanCallback  ScanBack= new Barcode2DWithSoft.ScanCallback(){
        @Override
        public void onScanComplete(int i, int length, byte[] bytes) {
            if (length < 1) {
                if (length == -1) {
                    barCodeTextView.setText("Scan cancel");
                } else if (length == 0) {
                    barCodeTextView.setText("Scan TimeOut");
                } else {
                    Log.i(TAG,"Scan fail");
                }
            }else{
                //SoundManage.PlaySound(MainActivity.this, SoundManage.SoundType.SUCCESS);
                barCode="";


                //  String res = new String(dd,"gb2312");
                try {
                    Log.i("Ascii",seldata);
                    barCode = new String(bytes, 0, length, seldata);
                    zt();
                }
                catch (UnsupportedEncodingException ex)   {}
                barCodeTextView.setText(barCode);
            }

        }
    };



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == TRIGGER_CODE){

            scanBarcode();

        }
        if(keyCode == SCAN_BUTTON_CODE){




        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        Log.i(TAG,"onDestroy");
        if(barcode2DWithSoft!=null){
            barcode2DWithSoft.stopScan();
            barcode2DWithSoft.close();
        }
        super.onDestroy();
        //android.os.Process.killProcess(Process.myPid());
    }

    private void zt() {

        Vibrator vibrator = (Vibrator)this.getSystemService(this.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }


    private class InitTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result=false;
            if(barcode2DWithSoft != null) {
                result=  barcode2DWithSoft.open(BarCodeActivity.this);
                Log.i(TAG,"open="+result);

            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

    }


}
