package mx.rfpro.construparkappalpha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import mx.rfpro.uhfreader.ReaderUHFActivity;


public class MainActivity extends AppCompatActivity {

    private Button rfidUHFReaderButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rfidUHFReaderButton = findViewById(R.id.rfidUHFReader);

        rfidUHFReaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent show = new Intent(getBaseContext(), ReaderUHFActivity.class);
                startActivity(show);
            }
        });
    }



}
