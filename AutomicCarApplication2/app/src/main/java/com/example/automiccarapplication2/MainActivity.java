package com.example.automiccarapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {
    ImageButton btnOff, btnPlay, btnAuto, btnTwin, btnSmlie;
    WebView webView;
    public static String CMD = "0";

    RelativeLayout layout_joystick;

    JoyStickClass js;

//    //서버주소
//    public static final String sIP = "192.168.0.11";
//    //서버주소2
//    public static final String sIP2 = "192.168.0.41";

    public static String sIP = "";
    public static String sIP2 = "";
    //사용할 통신 포트
    public static final int sPORT = 8011;
    //데이터 보낼 클랙스
    public SendData mSendData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //버튼 부분
        btnOff = findViewById(R.id.btnOff);
        btnPlay = findViewById(R.id.btnPlay);
        btnAuto = findViewById(R.id.btnAuto);
        btnTwin = findViewById(R.id.btnTwin);
        btnSmlie = findViewById(R.id.btnSmlie);

        //조이스틱 부분
        layout_joystick = (RelativeLayout) findViewById(R.id.layout_joystick);
        js = new JoyStickClass(getApplicationContext(), layout_joystick, R.drawable.image_button);
        layout_joystick.setVisibility(View.INVISIBLE);

        //눌렀을 때 나오는 스틱 사이즈
        js.setStickSize(150, 150);
        //스틱 움직이는 범위가 되는 배경
        js.setLayoutSize(600, 600);
        js.setLayoutAlpha(80);
        js.setStickAlpha(200);
        js.setOffset(90);
        js.setMinimumDistance(50);

        webView = (WebView) findViewById(R.id.webView);
        webView.setInitialScale(315);//스케일 비율 조정
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        String url ="http://192.168.0.41:8090/javascript_simple.html";
        webView.loadUrl(url);

        //종료 버튼
        btnOff.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnOff.setBackgroundResource(R.drawable.button33);
                    Toast.makeText(getApplication(),"종료합니다",Toast.LENGTH_SHORT).show();
                    mSendData = new SendData();
                    CMD = "PP,00";
                    //보내기 시작
                    mSendData.start();
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnOff.setBackgroundResource(R.drawable.button3);
                }
                return false;
            }
        });

        //조작할 자동차 선택하는 버튼
        btnPlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnPlay.setBackgroundResource(R.drawable.button77);
                    PopupMenu popup= new PopupMenu(getApplicationContext(), v);//v는 클릭된 뷰를 의미
                    getMenuInflater().inflate(R.menu.main_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.m1:
                                    Toast.makeText(getApplication(),"1번 자동차 선택 : 192.168.0.11",Toast.LENGTH_SHORT).show();
                                    layout_joystick.setVisibility(View.VISIBLE);
                                    sIP = "192.168.0.11";
                                    sIP2 = "";
                                    break;
                                case R.id.m2:
                                    Toast.makeText(getApplication(),"2번 자동차 선택 : 192.168.0.41",Toast.LENGTH_SHORT).show();
                                    layout_joystick.setVisibility(View.VISIBLE);
                                    sIP = "192.168.0.41";
                                    sIP2 = "";
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });
                    popup.show();//Popup Menu 보이기
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnPlay.setBackgroundResource(R.drawable.button7);
                }
                return false;
            }
        });

        //자율 주행 모드 버튼
        btnAuto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnAuto.setBackgroundResource(R.drawable.button55);
                    layout_joystick.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplication(),"자율 주행 모드",Toast.LENGTH_SHORT).show();
                    sIP = "192.168.0.11";
                    sIP2 = "192.168.0.41";
                    mSendData = new SendData();
                    CMD = "AA,00";
                    //보내기 시작
                    mSendData.start();
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnAuto.setBackgroundResource(R.drawable.button5);
                }
                return false;
            }
        });

        // 두대 동시에 조작하는 버튼
        btnTwin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnTwin.setBackgroundResource(R.drawable.button66);
                    Toast.makeText(getApplication(),"동시 조작 모드",Toast.LENGTH_SHORT).show();
                    layout_joystick.setVisibility(View.VISIBLE);
                    sIP = "192.168.0.11";
                    sIP2 = "192.168.0.41";
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnTwin.setBackgroundResource(R.drawable.button6);
                }
                return false;
            }
        });

        //퍼포먼스 버튼(?)
        btnSmlie.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnSmlie.setBackgroundResource(R.drawable.button44);
                    layout_joystick.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplication(),"모터쇼~",Toast.LENGTH_SHORT).show();
                    sIP = "192.168.0.11";
                    sIP2 = "192.168.0.41";
                    mSendData = new SendData();
                    CMD = "MM,00";
                    //보내기 시작
                    mSendData.start();
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnSmlie.setBackgroundResource(R.drawable.button4);
                }
                return false;
            }
        });

        //8축 조이스틱
        layout_joystick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js.drawStick(arg1);
                if (arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    int direction = js.get8Direction();
                    if (direction == JoyStickClass.STICK_UP) {
                        mSendData = new SendData();
                        CMD = "FF,80";
                        mSendData.start();
                    } else if (direction == JoyStickClass.STICK_UPRIGHT) {
                        mSendData = new SendData();
                        CMD = "FR,80";
                        mSendData.start();
                    } else if (direction == JoyStickClass.STICK_RIGHT) {
                        mSendData = new SendData();
                        CMD = "RR,80";
                        mSendData.start();
                    } else if (direction == JoyStickClass.STICK_DOWNRIGHT) {
                        mSendData = new SendData();
                        CMD = "BR,80";
                        mSendData.start();
                    } else if (direction == JoyStickClass.STICK_DOWN) {
                        mSendData = new SendData();
                        CMD = "BB,80";
                        mSendData.start();
                    } else if (direction == JoyStickClass.STICK_DOWNLEFT) {
                        mSendData = new SendData();
                        CMD = "BL,80";
                        mSendData.start();
                    } else if (direction == JoyStickClass.STICK_LEFT) {
                        mSendData = new SendData();
                        CMD = "LL,80";
                        mSendData.start();
                    } else if (direction == JoyStickClass.STICK_UPLEFT) {
                        mSendData = new SendData();
                        CMD = "FL,80";
                        mSendData.start();
                    } else if (direction == JoyStickClass.STICK_NONE) {
                        mSendData = new SendData();
                        CMD = "SS,00";
                        mSendData.start();
                    }
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    mSendData = new SendData();
                    CMD = "SS,00";
                    mSendData.start();
                }
                return true;
            }
        });
    }

    //데이터 보내는 쓰레드 클래스
    class SendData extends Thread {
        public void run() {
            try {
                //UDP 통신용 소켓 생성
                DatagramSocket socket = new DatagramSocket();

                //서버 주소 변수
                InetAddress serverAddr = InetAddress.getByName(sIP);
                InetAddress serverAddr2 = InetAddress.getByName(sIP2);

                //보낼 데이터 생성
                byte[] buf = CMD.getBytes();

                //패킷으로 변경
                DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, sPORT);
                DatagramPacket packet2 = new DatagramPacket(buf, buf.length, serverAddr2, sPORT);

                //패킷 전송!
                socket.send(packet);
                socket.send(packet2);

            } catch (Exception e) {

            }
        }
    }
}