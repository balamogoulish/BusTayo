package kr.co.softcampus.bussearchradio;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashSet;

public class ThirdActivity extends AppCompatActivity {
    String DstName;
    String AstName;

    Button button6;

    TextView text6;
    TextView text7;
    TextView text8;

    Background1 task1;
    Background2 task2;


    String result_StId;
    String result_StId2;
    String data;//최종 결과
    String data2;//최종 결과

    String RouteOrder;//최종 노선 조회 결과
    String ProperRtId;
    int ProperRtId2;
    int number;
    RadioGroup busRgButtons;

    HashSet<Integer> hashSet1 = new HashSet<>();//출발정류장 노선 아이디
    HashSet<Integer> hashSet2 = new HashSet<>();//도착정류장 노선 아이디
    HashSet<Integer> hashSet3 = new HashSet<>();//출발정류장 노선 순번
    HashSet<Integer> hashSet4 = new HashSet<>();//도착정류장 노선 순번


    String stationDArray[];
    String stationAArray[];
    String DstId;
    Integer[] arr1;//hashSet1의 Integer형 배열
    Integer[] arr2;//hashSet2의 Integer형 배열
    Integer[] arr3;//hashSet3의 Integer형 배열
    Integer[] arr4;//hashSet4의 Integer형 배열

    int[] array1;//arr1의 int형 배열
    int[] array2;//arr2의 int형 배열
    int[] array3;//arr3의 int형 배열
    int[] array4;//arr4의 int형 배열

    String[] array5; //arr5의 int형 배열
    String[] array6; //arr5의 int형 배열

    String[] ar3;//arr3의 String형 배열
    int p;
    int j;
    int index1;
    int index2;
    int index3;
    int index4;
    String arrays;

    String reservation;
    String busname[];
    String checkedRoute[];

    String location1_bus;
    String location2_bus;
    String pretime1;
    String pretime2;
    String busNo1;
    String busNo2;


    String key;
    StringBuffer buffer6 = new StringBuffer();
    StringBuffer buffer7 = new StringBuffer();
    String OneStId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        text6 = (TextView) findViewById(R.id.text6);
        text7 = (TextView)findViewById(R.id.text7);
        text8 = (TextView)findViewById(R.id.text8);
        busRgButtons = findViewById(R.id.busRg);


        Intent intent2 =getIntent();

        if (intent2.hasExtra("PerfectDstName")) {
            DstName=intent2.getStringExtra("PerfectDstName");
            task1 = new Background1();
            task1.execute(DstName);
        }

        if (intent2.hasExtra("PerfectAStName")) {
            AstName=intent2.getStringExtra("PerfectAStName");
            task2 = new Background2();
            task2.execute(AstName);
        }

        if (intent2.hasExtra("OneStId")) {
            OneStId=intent2.getStringExtra("OneStId");
        }

    }
    class Background1 extends AsyncTask<String, String, String> {

        private static final String TAG = "Request_BusIdList";
        int value;

        @Override
        protected String doInBackground(String... params) {

            String station_keyword = (String) params[0];

            String parsingURL = "http://apis.data.go.kr/6410000/busstationservice/getBusStationList?"
                    + "serviceKey=euEsltx738hhLpmcMAj7OzpwHp7BiX6f%2B6hCh2TM1pD37o9JO0MSRNahhC22%2BtsVGfgEibU5NMoOCU20%2BpuaBw%3D%3D&keyword="
                    + station_keyword;

            StringBuffer buffer1 = new StringBuffer();

            String korean = URLEncoder.encode(station_keyword);

            int number = 0;

            while (!isCancelled() && number < 2) {
                number = number + 1;
                try {

                    URL url = new URL(parsingURL);//문자열로 된 요청 url을 URL 객체로 생성.

                    //InputStream is = url.openStream(); //url위치로 입력스트림 연결
                    URLConnection url_connection = url.openConnection();
                    url_connection.setReadTimeout(3000);
                    InputStream is = url_connection.getInputStream();

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
                                tag = xpp.getName();    // 태그 이름 얻어오기
                                if (tag.equals("stationId")) {
                                    xpp.next();
                                    String StId=xpp.getText();
                                    buffer1.append(StId+","); //title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                }
                                break;

                            case XmlPullParser.TEXT:
                                break;

                            case XmlPullParser.END_TAG:
                                tag = xpp.getName();    //테그 이름 얻어오기

                                break;
                        }
                        task1.cancel(true);
                        eventType = xpp.next();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch blocke.printStackTrace();
                    Log.d(TAG, "Error", e);

                }
            }

            //result_StId = Arrays.toString(arr);//배열을 문자열로 출력
            result_StId=buffer1.toString();//버퍼 사용시
            return result_StId;

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            getDRouteId(result_StId);

            new Thread(new Runnable(){

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    data=getDRouteId(result_StId);//아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기
                    DstId=stationDArray[index2];
                    //UI Thread(Main Thread)를 제외한 어떤 Thread도 화면을 변경할 수 없기때문에
                    //runOnUiThread()를 이용하여 UI Thread가 TextView 글씨 변경하도록 함
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            //text6.setText(data);
                            text8.setText(DstId);//도착 정류장 아이디
                        }
                    });
                }
            }).start();

        }
    }

    class Background2 extends AsyncTask<String, String, String> {

        private static final String TAG = "Request_BusIdList";

        @Override
        protected String doInBackground(String... params) {

            String station_keyword = (String) params[0];

            String parsingURL = "http://apis.data.go.kr/6410000/busstationservice/getBusStationList?"
                    + "serviceKey=euEsltx738hhLpmcMAj7OzpwHp7BiX6f%2B6hCh2TM1pD37o9JO0MSRNahhC22%2BtsVGfgEibU5NMoOCU20%2BpuaBw%3D%3D&keyword="
                    + station_keyword;

            StringBuffer buffer = new StringBuffer();

            String korean = URLEncoder.encode(station_keyword);


            int number = 0;

            while (!isCancelled() && number < 2) {
                number = number + 1;
                try {

                    URL url = new URL(parsingURL);//문자열로 된 요청 url을 URL 객체로 생성.

                    //InputStream is = url.openStream(); //url위치로 입력스트림 연결
                    URLConnection url_connection = url.openConnection();
                    url_connection.setReadTimeout(3000);
                    InputStream is = url_connection.getInputStream();

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
                                tag = xpp.getName();    // 태그 이름 얻어오기
                                if (tag.equals("stationId")) {
                                    xpp.next();
                                    String StId=xpp.getText();
                                    buffer.append(StId+","); //title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                }
                                break;

                            case XmlPullParser.TEXT:
                                break;

                            case XmlPullParser.END_TAG:
                                tag = xpp.getName(); //테그 이름 얻어오기

                                break;
                        }
                        task2.cancel(true);
                        eventType = xpp.next();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch blocke.printStackTrace();
                    Log.d(TAG, "Error", e);

                }
            }

            //result_StId = Arrays.toString(arr);//배열을 문자열로 출력
            result_StId2=buffer.toString();//버퍼 사용시

            return result_StId2;

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            getARouteId(result_StId2);
            //editText.setText(result_RouteId);

            //result_RouteId=getRouteId(result_StId);
            // task2=new RouteBackground();
            // task2.execute(result_StId);
            new Thread(new Runnable(){

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    data2=getARouteId(result_StId2);//노선 조회
                    RouteOrder=finalChecked(arr1,arr2,arr3,arr4);//도착 정류장 아이디
                    index2=Integer.parseInt(array5[index1]);
                    String order=Integer.toString(array3[index1-1]);
                    String Route=Integer.toString(array1[index1]);
                    getBusArrivalData(order, Route);
                    //runOnUiThread()를 이용하여 UI Thread가 TextView 글씨 변경하도록 함
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            //ScrollView에 라디오 버튼 추
                            //text7.setText(data2);
                            text6.setText(Route);//알맞은 노선
                          //edit8.setText(RouteOrder);//알맞은 노선을 array1 몇 인덱스에서 찾았는지
                          text7.setText(order);//출발 정류장 아이디 최종 추출
                        }
                    });
                }
            }).start();

        }


    }

    String getDRouteId(String StIds){

        StringBuffer buffer2 = new StringBuffer();

        int figure=StIds.length();

        stationDArray=StIds.split(",", figure/10); //한글 검색 결과 나온 출발 정류장아이디가 다 담긴 배열
        for (int i = 0; i < figure/10; i++) {
            int j=0;
                try {
                    URL url2 = new URL("http://apis.data.go.kr/6410000/busstationservice/getBusStationViaRouteList?serviceKey=euEsltx738hhLpmcMAj7OzpwHp7BiX6f%2B6hCh2TM1pD37o9JO0MSRNahhC22%2BtsVGfgEibU5NMoOCU20%2BpuaBw%3D%3D&stationId=" + stationDArray[i]
                    ); //검색 URL부분 i번째 출발 정류장 노선이 검색됨.
                    XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = parserCreator.newPullParser();

                    parser.setInput(url2.openStream(), null);

                    int parserEvent = parser.getEventType();
                    int q=0;

                    while (parserEvent != XmlPullParser.END_DOCUMENT) {
                        switch (parserEvent) {
                            case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                                if (parser.getName().equals("routeId")) {
                                    parser.next();
                                    String RouteId = parser.getText();
                                    int Route1d = Integer.parseInt(RouteId);
                                    hashSet1.add(Route1d);
                                    buffer2.append(RouteId);//title 만나면 내용을 받을수 있게 하자
                                    buffer6.append(i);
                                    buffer6.append(",");

                                } else if (parser.getName().equals("staOrder")) {
                                    parser.next();
                                    hashSet3.add(Integer.parseInt(parser.getText()));

                                }

                                break;

                            case XmlPullParser.TEXT://parser가 내용에 접근했을때

                                break;
                            case XmlPullParser.END_TAG:

                                break;
                        }
                        parserEvent = parser.next();
                    }
                } catch (Exception e) {
                    buffer2.append(e.getMessage());
                }

        }
        //editText.setText(buffer2.toString());
        arr1 = hashSet1.toArray(new Integer[0]);//hashset의 Integer화
        arr3 = hashSet3.toArray(new Integer[0]);//hashset의 Integer화
        String StIdOrder=buffer6.toString();
        array5=StIdOrder.split(",", StIdOrder.length()/10); //array5에는 몇번째 출발 정류장 아이디에서 나온 노선인지 알려줌, array5[0]=1이면 첫번째 노선은 두번째 정류장에서 나온 것
        return buffer2.toString();
    }

    String getARouteId(String StIds){

        StringBuffer buffer2 = new StringBuffer();

        int figure=StIds.length();


        stationAArray=StIds.split(",", figure/10);
        for (int i = 0; i < figure/10; i++) {
            try {
                URL url2 = new URL("http://apis.data.go.kr/6410000/busstationservice/getBusStationViaRouteList?serviceKey=euEsltx738hhLpmcMAj7OzpwHp7BiX6f%2B6hCh2TM1pD37o9JO0MSRNahhC22%2BtsVGfgEibU5NMoOCU20%2BpuaBw%3D%3D&stationId="+stationAArray[i]
                ); //검색 URL부분
                XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                XmlPullParser parser = parserCreator.newPullParser();

                parser.setInput(url2.openStream(), null);

                int parserEvent = parser.getEventType();


                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                            if (parser.getName().equals("routeId")) {
                                parser.next();
                                String RouteId=parser.getText();
                                int Route1d=Integer.parseInt(RouteId);
                                hashSet2.add(Route1d);
                                buffer2.append(RouteId);//title 만나면 내용을 받을수 있게 하자
                            } else if (parser.getName().equals("staOrder")) {
                                parser.next();
                                hashSet4.add(Integer.parseInt(parser.getText()));
                            }

                            break;

                        case XmlPullParser.TEXT://parser가 내용에 접근했을때

                            break;
                        case XmlPullParser.END_TAG:

                            break;
                    }
                    parserEvent = parser.next();
                }
            } catch (Exception e) {
                buffer2.append(e.getMessage());
            }}
        //editText.setText(buffer2.toString());
        arr2 = hashSet2.toArray(new Integer[0]);//hashset의 Integer화
        arr4 = hashSet4.toArray(new Integer[0]);//hashset의 Integer화
        return buffer2.toString();
    }

    String finalChecked(Integer[]arr1,Integer[]arr2,Integer[]arr3,Integer[]arr4){
        StringBuffer buffer3 = new StringBuffer();

        array1 = Arrays.stream(arr1).mapToInt(Integer::intValue).toArray();//Integer형은 산술 비교 불가함. int형 배열로 바꾸기. 출발 노선 아이디
        array2 = Arrays.stream(arr2).mapToInt(Integer::intValue).toArray(); //도착 노선 아이디
        array3 = Arrays.stream(arr3).mapToInt(Integer::intValue).toArray(); // 출발 순번
        array4 = Arrays.stream(arr4).mapToInt(Integer::intValue).toArray(); // 도착 순번

        for(p=0;p<array1.length;p++){
            for(int j=0;j<array2.length;j++) {
                    int k=0;
                    if (array1[p] == array2[j]) { //출발 노선이랑 도착 노선이 같으면
                        if (array3[p] < array4[j]) { // 출발 순번이 도착 순번보다 작아야 함
                            buffer3.append(arr1[p].toString()); //출발 노선을 출력.
                            buffer3.append(",");//가능한 노선들을 버퍼에 담았음.->출발 정류장에서 이 노선들로 가는 버스 도착 정보를 검색해야 함.
                            index1=p;//알맞은 노선을 찾은 출발 노선 배열 인덱스
                            index3=j;
                        }

                }
            }

        }

        ProperRtId=buffer3.toString();
        if(OneStId.length()<9) return stationAArray[index3];
        else return OneStId;
    }

    String getBusArrivalData(String order, String Route) {

        StringBuffer buffer = new StringBuffer();

        int figure=ProperRtId.length();
        checkedRoute=ProperRtId.split(",", figure/10);

        //stationDArray[index2]


            try {
                String serviceKey = "euEsltx738hhLpmcMAj7OzpwHp7BiX6f%2B6hCh2TM1pD37o9JO0MSRNahhC22%2BtsVGfgEibU5NMoOCU20%2BpuaBw%3D%3D"; //인증키 넣기

                String queryUrl = "http://apis.data.go.kr/6410000/busarrivalservice/getBusArrivalItem?serviceKey=" + serviceKey + "&stationId=" + stationDArray[index2]
                        + "&routeId=" + Route + "&staOrder=" + order;
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

                            if (tag.equals("busArrivalItem")) ;
                            else if (tag.equals("locationNo1")) {
                                buffer.append("첫 번째 차량 위치 정보 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n\n");

                                number = number + 2;

                                location1_bus = xpp.getText();
                            } else if (tag.equals("predictTime1")) {
                                buffer.append("첫 번째 차량 도착 예상 시간 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n\n");

                                pretime1 = xpp.getText();
                            } else if (tag.equals("plateNo1")) {
                                buffer.append("첫 번째 차량 차량 번호 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n\n");

                                busNo1 = xpp.getText();
                            } else if (tag.equals("locationNo2")) {
                                buffer.append("두 번째 차량 위치 정보 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n\n");

                                location2_bus = xpp.getText();
                            } else if (tag.equals("predictTime2")) {
                                buffer.append("두 번째 차량 도착 예상 시간 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n\n");

                                pretime2 = xpp.getText();
                            } else if (tag.equals("plateNo2")) {
                                buffer.append("두 번째 차량 차량 번호 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n\n");

                                busNo2 = xpp.getText();
                            }
                            break;
                        case XmlPullParser.TEXT:
                            break;

                        case XmlPullParser.END_TAG:
                            tag = xpp.getName();
                            if (tag.equals("busArrivalItem")) buffer.append("\n");
                            break;
                    }
                    eventType = xpp.next();
                }

            } catch (Exception e) {
            }
        new Thread(new Runnable(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                //UI Thread(Main Thread)를 제외한 어떤 Thread도 화면을 변경할 수 없기때문에
                //runOnUiThread()를 이용하여 UI Thread가 TextView 글씨 변경하도록 함
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        addRadioButtons(busNo1, location1_bus, pretime1, number);
                        addRadioButtons(busNo2, location2_bus, pretime2, number+1);//ScrollView에 라디오 버튼 추가
                    }
                });
            }
        }).start();
        return buffer.toString();//StringBuffer 문자열 객체 반환
    }

    public void addRadioButtons(String busNo, String location_bus, String pretime, int number) {

        RadioButton rdbtn = new RadioButton(this);
        busRgButtons.setOrientation(LinearLayout.VERTICAL);
        rdbtn.setId(number);
        rdbtn.setText(busNo +"\n"+ location_bus +"번째 전 정류장\n"+ pretime +"분 후 도착 예정");
        busRgButtons.addView(rdbtn);

        rdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    /*
                    Intent intent4 = new Intent(FourthActivity.this, FifthActivity.class);//액티비티 이동해주는 구문
                    intent4.putExtra("", ); //액티비티 데이터 전달
                    startActivity(intent4);
                    */
                reservation=rdbtn.getText().toString();
                busname=reservation.split("\\n"); //버스 이름 넣음~
                //.setText(busname[0]);
                Intent Intent = new Intent(ThirdActivity.this, FourthActivity.class);
                String bname=busname[0];
                Intent.putExtra("DstId",DstId);//String 배열
                Intent.putExtra("busname",bname);
                Intent.putExtra("AstId",RouteOrder);
                Intent.putExtra("DstName",DstName);
                Intent.putExtra("AstName",AstName);
                startActivity(Intent);


            }
        });

    }

}
