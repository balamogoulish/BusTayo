package kr.co.softcampus.bussearchradio;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SixthActivity extends AppCompatActivity {
    TextView text;
    String DstId;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sixth);
        text=(TextView)findViewById(R.id.text);
        button= findViewById(R.id.button);

        Intent intent = getIntent();
        DstId = intent.getStringExtra("DstId");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6 = new Intent(SixthActivity.this, SeventhActivity.class);
                intent6.putExtra("DstId", DstId);
                startActivity(intent6);
            }
        });

    }
}
