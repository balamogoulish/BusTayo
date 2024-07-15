package kr.co.softcampus.bussearchradio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;

public class FifthActivity extends AppCompatActivity {

    TextView text;

    String serviceKey = "euEsltx738hhLpmcMAj7OzpwHp7BiX6f%2B6hCh2TM1pD37o9JO0MSRNahhC22%2BtsVGfgEibU5NMoOCU20%2BpuaBw%3D%3D";
    String data;
    String queryUrl;
    String DstId;
    String BusName;
    String AstId;
    String DstName;
    String AstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);

        text=(TextView)findViewById(R.id.text);

        Intent intent = getIntent();
        double x = intent.getDoubleExtra("x",127.123);
        double y = intent.getDoubleExtra("y", 37.54);

        String a = Double.toString(x);
        String b = Double.toString(y);

        Intent intent4 = getIntent();

        if (intent4.hasExtra("DstId")) {
            DstId = intent4.getStringExtra("DstId");
        }
        if (intent4.hasExtra("BusName")) {
            BusName = intent4.getStringExtra("BusName");
        }
        if (intent4.hasExtra("AstId")) {
            AstId = intent4.getStringExtra("AstId");
        }
        if (intent4.hasExtra("DstName")) {
            DstName = intent4.getStringExtra("DstName");
        }
        if (intent4.hasExtra("AstName")) {
            AstName = intent4.getStringExtra("AstName");
        }

        queryUrl = "http://apis.data.go.kr/6410000/busstationservice/getBusStationAroundList?serviceKey=" + serviceKey + "&x=" + a + "&y=" + b;

        new Thread(new Runnable(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                //UI Thread(Main Thread)를 제외한 어떤 Thread도 화면을 변경할 수 없기때문에
                //runOnUiThread()를 이용하여 UI Thread가 TextView 글씨 변경하도록 함

                data=getBusArrivalData(DstId);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                         text.setText(data);

                    }
                });
            }
        }).start();
    }

    String getBusArrivalData(String DstId) {

        StringBuffer buffer = new StringBuffer();

        String st_id = DstId;
        String dt_id = null;

        try {
            //인증키 넣기
            URL url = new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

            String tag;
            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();//태그 이름 얻어오기

                        if (tag.equals("busStationAroundList")) ;
                        else if (tag.equals("stationId")) {
                            // 출발정류소 아이디와 같으면 출력하도록 조건문 작성

                            xpp.next();
                            dt_id = xpp.getText();

                            if(dt_id.equals(st_id)) {
                                buffer.append("정류장 아이디 : ");
                                buffer.append(dt_id);
                                buffer.append("\n");
                            }

                        }
                        else if (tag.equals("distance")) {

                            xpp.next();
                            int ds = Integer.parseInt(xpp.getText());

                            if (dt_id.equals(st_id)) {
                                if (ds <= 20) {
                                    //buffer.append("\n\n\n");
                                    //buffer.append("예약이 완료 되었습니다.\n");
                                    final String msg=BusName+" "+DstName+" "+AstName+" "+AstId; // 나중에 busId, stId로 바꾼다.
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            send(msg);
                                        }
                                    }).start();

                                    Intent intent = new Intent(FifthActivity.this, SixthActivity.class);
                                    intent.putExtra("DstId", DstId);
                                    startActivity(intent);
                                }
                                else buffer.append("거리가 ").append(ds).append("m 남았습니다.");
                                buffer.append("\n\n");
                            }

                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = xpp.next();
            }

        } catch (Exception e) {}

        return buffer.toString();//StringBuffer 문자열 객체 반환
    }

    public void printClientLog(final String msg){
        Log.d("FifthActivity",msg);
    }

    public void send(String msg) {
        try {
            int portNumber = 3003;
            Socket socket = new Socket("127.0.0.1", portNumber);
            printClientLog("소켓 연결함");

            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(msg);
            outputStream.flush();
            printClientLog("데이터 전송함");

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            printClientLog("서버로부터 받음: " + inputStream.readObject());
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
