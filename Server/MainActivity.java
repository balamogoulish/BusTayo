package com.example.server;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/*port 번호 할당

사용자: 3000
버스1: 10001
버스2: 10002
버스3: 10003
*/

public class MainActivity extends AppCompatActivity {

    TextView serverTV;
    Button serverBtn;

    Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serverTV=findViewById(R.id.server_text);
        serverBtn = findViewById(R.id.server_btn);

        serverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startServer();
                    }
                }).start();
            }
        });
    }

    public void printServerLog(final String data){
        Log.d("MainActivity",data);
        handler.post(new Runnable(){
            @Override
            public void run(){
                serverTV.append(data+"\n");
            }
        });
    }

    public void startServer(){
        try{
            int portNumber=3003;
            ServerSocket server=new ServerSocket(portNumber);
            printServerLog("서버 시작함: "+portNumber);

            while(true){
                Socket socket=server.accept();
                InetAddress clientHost=socket.getLocalAddress();
                int clientPort= socket.getPort();
                printServerLog("클라이언트 연결됨: "+clientHost+" : "+clientPort);

                ObjectInputStream inputStream=new ObjectInputStream(socket.getInputStream());
                Object obj=inputStream.readObject();
/* 데이터 전송 코드
                ObjectOutputStream outputStream=new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(obj+" from Server.");
                outputStream.flush();
                printServerLog("데이터 보냄");
*/
//obj를 버스 아이디와 정류장 아이디로 구분 후 버스 아이디에 따라 데이터 전송
                String object=String.valueOf(obj);
                String[] id=object.split(" ");
                /*
                  id[0]: 버스 번호
                  id[1]: 출발 정류장
                  id[2]: 도착 정류장
                */
                printServerLog("버스 번호: "+id[0]+"\n출발 정류장: "+id[1]+"\n도착 정류장: "+id[2]);

                switch(id[0]){
                    case "경기77바2631":  //"1" 자리를 버스 아이디로 대체한다.
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                serverForBus1(id[1], id[2], id[3]);
                            }
                        }).start();
                        break;
                    case "경기77바2593":  //"2" 자리를 버스 아이디로 대체한다.
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                serverForBus2(id[1], id[2], id[3]);
                            }
                        }).start();
                        break;
                    case "3":  //"3" 자리를 버스 아이디로 대체한다.
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                serverForBus3(id[1], id[2], id[3]);
                            }
                        }).start();
                        break;
                }


                socket.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void serverForBus1(String AstName, String DstName, String AstId ){
        try{
            int portNumber=10001;
            ServerSocket server=new ServerSocket(portNumber);
            printServerLog("서버 시작함: "+portNumber);

            while(true){
                Socket socket=server.accept();
                InetAddress clientHost=socket.getLocalAddress();
                int clientPort= socket.getPort();
                printServerLog("클라이언트 연결됨: "+clientHost+" : "+clientPort);

                ObjectOutputStream outputStream=new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(AstName+" "+DstName+" "+AstId);
                outputStream.flush();
                printServerLog("데이터 보냄");

                socket.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void serverForBus2(String AstName, String DstName, String AstId){
        try{
            int portNumber=10002;
            ServerSocket server=new ServerSocket(portNumber);
            printServerLog("서버 시작함: "+portNumber);

            while(true){
                Socket socket=server.accept();
                InetAddress clientHost=socket.getLocalAddress();
                int clientPort= socket.getPort();
                printServerLog("클라이언트 연결됨: "+clientHost+" : "+clientPort);

                ObjectOutputStream outputStream=new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(AstName+" "+DstName+" "+AstId);
                outputStream.flush();
                printServerLog("데이터 보냄");

                socket.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void serverForBus3(String AstName, String DstName, String AstId){
        try{
            int portNumber=10003;
            ServerSocket server=new ServerSocket(portNumber);
            printServerLog("서버 시작함: "+portNumber);

            while(true){
                Socket socket=server.accept();
                InetAddress clientHost=socket.getLocalAddress();
                int clientPort= socket.getPort();
                printServerLog("클라이언트 연결됨: "+clientHost+" : "+clientPort);

                ObjectOutputStream outputStream=new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(AstName+" "+DstName+" "+AstId);
                outputStream.flush();
                printServerLog("데이터 보냄");

                socket.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
