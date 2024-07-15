package kr.co.softcampus.bussearchradio;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Bootpay;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.events.BootpayEventListener;
import android.models.BootExtra;
import android.models.BootItem;
import android.models.BootUser;
import android.models.Payload;

public class SeventhActivity extends AppCompatActivity {

    String DstId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        DstId = intent.getStringExtra("DstId");
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_seventh);
    }

    public void PaymentTest(View v) {
        BootUser user = new BootUser().setPhone("010-8454-1904"); // 구매자 정보

        BootExtra extra = new BootExtra()
                .setCardQuota("0,2,3"); // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)


        List<BootItem> items = new ArrayList<>();
        BootItem item1 = new BootItem().setName("버스 탑승요금(성인)").setId("ITEM_CODE_BUS").setQty(1).setPrice(1250d);
        items.add(item1);

        Payload payload = new Payload();
        payload.setApplicationId(BootpayConstants.application_id)
//                .setPg("나이스페이")
//                .setMethods(Arrays.asList("카드", "휴대폰"))
                .setOrderName("버스 탑승요금 결제")
                .setOrderId("1234")
                .setPrice(1250d)
                .setUser(user)
                .setExtra(extra)
                .setItems(items);

//        payload.set

        Map<String, Object> map = new HashMap<>();
        map.put("1", "abcdef");
        map.put("2", "abcdef55");
        map.put("3", 1234);
        payload.setMetadata(map);
//        payload.setMetadata(new Gson().toJson(map));

        Bootpay.init(getSupportFragmentManager(), getApplicationContext())
                .setPayload(payload)
                .setEventListener(new BootpayEventListener() {
                    @Override
                    public void onCancel(String data) {
                        Log.d("bootpay", "cancel: " + data);
                    }

                    @Override
                    public void onError(String data) {
                        Log.d("bootpay", "error: " + data);
                    }

                    @Override
                    public void onClose() {
                        Log.d("bootpay", "close" );
                        Bootpay.removePaymentWindow();
                    }

                    @Override
                    public void onIssued(String data) {
                        Log.d("bootpay", "issued: " +data);
                    }

                    @Override
                    public boolean onConfirm(String data) {
                        Log.d("bootpay", "confirm: " + data);
//                        Bootpay.transactionConfirm(data); //재고가 있어서 결제를 진행하려 할때 true (방법 1)
                        return true; //재고가 있어서 결제를 진행하려 할때 true (방법 2)
//                        return false; //결제를 진행하지 않을때 false
                    }

                    @Override
                    public void onDone(String data) {
                        Log.d("done", data);
                    }
                }).requestPayment();
    }
}
