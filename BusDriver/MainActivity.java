package com.example.bus1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


//버스는 서버의 부름을 계속해서 확인해야 하기 때문에 while문으로 계속해서 port 연결을 대기?
public class MainActivity extends AppCompatActivity {
    TextView clientTV;
    String AstName;
    String DstName;
    String AstId;

    Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clientTV=findViewById(R.id.send_text);

        Button sendBtn=findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //data == 전송값
                final String data="버스아이디 정류장아이디"; // 나중에 busId, stId로 바꾼다.
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        send(data);
                    }
                }).start();
                new Handler().postDelayed(new Runnable() {  // 5초뒤에 AlertDialog 실행
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                        startActivity(intent);
                    }
                },5000);
            }
        });
    }

    public void printClientLog(final String data){
        Log.d("MainActivity",data);
        handler.post(new Runnable(){
            @Override
            public void run(){
                clientTV.append(data+"\n");
            }
        });
    }

    public void send(String data) {
        try {
            int portNumber = 10001;
            Socket socket = new Socket("127.0.0.1", portNumber);


            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Object obj=inputStream.readObject();
            String object=String.valueOf(obj);
            String[] id=object.split(" ");

            DstName = id[0];
            AstName = id[1];
            AstId = id[2];


            printClientLog("출발 정류장: "+AstName+"\n도착 정류장: "+DstName);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
