package mx.rfpro.construparkappalpha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import mx.rfpro.barcodereader.BarCodeActivity;
import mx.rfpro.uhfreader.ReaderUHFActivity;


public class MainActivity extends AppCompatActivity {

    private Button rfidUHFReaderButton;
    private Button barcodeReaderButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rfidUHFReaderButton = findViewById(R.id.rfidUHFReaderButton);
        barcodeReaderButton = findViewById(R.id.barcodeReaderButton);

        rfidUHFReaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent show = new Intent(getBaseContext(), ReaderUHFActivity.class);
                startActivity(show);
            }
        });

        barcodeReaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent show = new Intent(getBaseContext(), BarCodeActivity.class);
                startActivity(show);

            }
        });
    }



}
