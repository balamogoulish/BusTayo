package kr.co.softcampus.bussearchradio;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class FourthActivity extends AppCompatActivity {

    TextView text6;
    String DstId;
    String BusName;
    String AstId;
    String DstName;
    String AstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        text6 = (TextView) findViewById(R.id.text6);
        Button button8 = (Button) findViewById(R.id.button8);
        Intent intent = getIntent();

        if (intent.hasExtra("DstId")) {
            DstId = intent.getStringExtra("DstId");
        }
        if (intent.hasExtra("busname")) {
            BusName = intent.getStringExtra("busname");
        }

        if (intent.hasExtra("AstId")) {
            AstId = intent.getStringExtra("AstId");
        }
        if (intent.hasExtra("DstName")) {
            DstName = intent.getStringExtra("DstName");
        }

        if (intent.hasExtra("AstName")) {
            AstName = intent.getStringExtra("AstName");
        }


        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        final LocationListener gpsLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // 위치 리스너는 위치정보를 전달할 때 호출되므로 onLocationChanged()메소드 안에 위지청보를 처리를 작업을 구현 해야합니다.

                Intent intent = new Intent(FourthActivity.this, FifthActivity.class);
                intent.putExtra("x", location.getLongitude());
                intent.putExtra("y", location.getLatitude());
                intent.putExtra("DstId",DstId);
                intent.putExtra("BusName",BusName);
                intent.putExtra("AstId",AstId);
                intent.putExtra("DstName",DstName);
                intent.putExtra("AstName",AstName);
                startActivity(intent);

            } public void onStatusChanged(String provider, int status, Bundle extras) {

            } public void onProviderEnabled(String provider) {

            } public void onProviderDisabled(String provider) {

            }
        };

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( FourthActivity.this, new String[] {
                            android.Manifest.permission.ACCESS_FINE_LOCATION}, 0 );
                }
                else{
                    // 가장최근 위치정보 가져오기
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(location != null) {

                        Intent intent = new Intent(FourthActivity.this, FifthActivity.class);
                        intent.putExtra("x", location.getLongitude());
                        intent.putExtra("y", location.getLatitude());

                        startActivity(intent);
                    }

                    // 위치정보를 원하는 시간, 거리마다 갱신해준다.
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            1000,
                            1,
                            gpsLocationListener);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            1000,
                            1,
                            gpsLocationListener);
                }
            }
        });

    }
}
