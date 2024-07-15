package kr.co.softcampus.bussearchradio;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashSet;

public class SecondActivity extends AppCompatActivity {
    EditText edit2;
    EditText edit3;
    TextView textView;
    String TAG = "TestActivity";
    RadioGroup mRgAllButtons;
    String stName;
    String StId;
    Button button4;
    Button button5;

    String data;
    StringBuffer buffer = new StringBuffer();
    StringBuffer buffer2 = new StringBuffer();
    int number;
    String[] StationName = new String[200];
    HashSet<String> hashSet = new HashSet<>();
    String PerfectDstName;
    String PerfectAStName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        edit2 = (EditText)findViewById(R.id.edit2);
        edit3 = (EditText)findViewById(R.id.edit3);
        mRgAllButtons = findViewById(R.id.radiogroup2);
        button4=(Button)findViewById(R.id.button4);
        button5=(Button)findViewById(R.id.button5);

        Intent intent1 =getIntent();
        PerfectDstName=intent1.getStringExtra("PerfectDstName");
        edit3.setText(PerfectDstName);


        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String editStname=edit3.getText().toString();//출발정류장 수정 후 검색 누르면 출발 정류장 재선택

                Intent editIntent = new Intent(SecondActivity.this, MainActivity.class);

                editIntent.putExtra("editStname",editStname);

                startActivity(editIntent);
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String AStname=edit2.getText().toString();

                Intent Intent2 = new Intent(SecondActivity.this, ThirdActivity.class);
                Intent2.putExtra("PerfectAStName",AStname);
                Intent2.putExtra("PerfectDstName",PerfectDstName);
                Intent2.putExtra("OneStId", buffer2.toString()); //액티비티 데이터 전달
                startActivity(Intent2);

               // Intent Intent2 = new Intent(SecondActivity.this, ThirdActivity.class);
                //startActivity(Intent2);
            }
        });


    }

    public void addRadioButtons() {//라디오 버튼 생성 메소드//
        for (String Stname : hashSet) {//라디오 버튼 생성 메소드//
            RadioButton rdbtn2 = new RadioButton(this);
            mRgAllButtons.setOrientation(LinearLayout.VERTICAL);
            rdbtn2.setId(number);
            rdbtn2.setText(Stname);
            mRgAllButtons.addView(rdbtn2);

        rdbtn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                PerfectAStName=rdbtn2.getText().toString();
                edit2.setText(PerfectAStName);
                //도착 정류장 라디오버튼 선택하면 ThirdActivity로 인텐트

            }});


    }}

    public void mOnClick(View v) {
                switch (v.getId()) {


                    case R.id.button3:

                        //도착 정류장 검색 누르면 라디오 버튼으로 정류장 리스트 뜨게 함.
                        new Thread(new Runnable(){

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                data = getStName();//아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기

                                //UI Thread(Main Thread)를 제외한 어떤 Thread도 화면을 변경할 수 없기때문에
                                //runOnUiThread()를 이용하여 UI Thread가 TextView 글씨 변경하도록 함
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        addRadioButtons(); //ScrollView에 라디오 버튼 추가
                                    }
                                });
                            }
                        }).start();
                        break;

                }
            }

    String getStName() {
        String str = edit2.getText().toString();//EditText에 작성된 Text얻어오기
        String route=URLEncoder.encode(str);
        int number=0;

        String queryUrl = "http://apis.data.go.kr/6410000/busstationservice/getBusStationList?serviceKey=euEsltx738hhLpmcMAj7OzpwHp7BiX6f%2B6hCh2TM1pD37o9JO0MSRNahhC22%2BtsVGfgEibU5NMoOCU20%2BpuaBw%3D%3D&keyword="+route;

        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;
            xpp.next();
            int eventType= xpp.getEventType();
            int s=0;

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){

                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기


                        if(tag.equals("busStationList")) {
                            number = number + 1;
                            ;// 첫번째 검색결과

                        }else if(tag.equals("stationName")){
                            xpp.next();
                            hashSet.add(xpp.getText());
                            //정류장 이름 입력인자로 하는 라디오버튼 생성함수 호출
                        }
                        else if(tag.equals("stationId")){
                            xpp.next();
                            if(s==0) buffer2.append(xpp.getText());//정류장 아이디
                            s=s+1;
                        }

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기
                        if(tag.equals("busStationList")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e) {
            buffer.append("예외 발생\n");
            e.printStackTrace();
            // TODO Auto-generated catch blocke.printStackTrace();
        }

        buffer.append("파싱 끝\n");
        return buffer.toString();//StringBuffer 문자열 객체 반환

    }//getXmlData method....

}//SecondActivity class..