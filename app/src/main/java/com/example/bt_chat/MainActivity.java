package com.example.bt_chat;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {







    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static final int REQUEST_ENABLE_BT = 1;

    Button ONOFF_bt,TEE_bt,LFT_bt,RGHT_bt;








    BluetoothSocket btSocket = null;
    OutputStream OUT_ = null;
    int step_ = 7;
    String str = Integer.toString(step_);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BluetoothAdapter mBluetoothAdapter;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        final BluetoothDevice LOKO_1 = mBluetoothAdapter.getRemoteDevice("98:D3:31:F9:F9:C4");

        ONOFF_bt = findViewById(R.id.ONOFF_btn);
        TEE_bt = findViewById(R.id.TEE_btn);
        LFT_bt = findViewById(R.id.btn_arr_l);
        RGHT_bt = findViewById(R.id.btn_arr_r);




        if (mBluetoothAdapter.isEnabled()) {
            ONOFF_bt.setBackground(getResources().getDrawable(R.drawable.ic_on_bt));                 //(R.drawable.ic_on_bt);
        }
        if(!mBluetoothAdapter.isEnabled()) {
            ONOFF_bt.setBackground(getResources().getDrawable(R.drawable.ic_off_bt));                      //(R.drawable.ic_off_bt);
        }


        if (step_==7){
            /*
            RGHT_bt.setText("<<<<<<<");
            LFT_bt.setText (">>>>>>>");
            */

            LFT_bt.setText("<<<<<<<");
            RGHT_bt.setText (">>>>>>>");
            LFT_bt.setTextColor(getResources().getColor(R.color.Yellow));
            RGHT_bt.setTextColor(getResources().getColor(R.color.Yellow));



        }


ONOFF_bt.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        if (mBluetoothAdapter == null) {
            info_("Bloetoot jest niedostępny");
        }

        if(!mBluetoothAdapter.isEnabled()){
            info_("Włączanie Bluetooth'a");

            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent,REQUEST_ENABLE_BT);


            //if(mBluetoothAdapter.isEnabled())
            ONOFF_bt.setBackground(getResources().getDrawable(R.drawable.ic_on_bt));
        }

        if(mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.disable();
            info_("wyłączanie");

            //if(!mBluetoothAdapter.isEnabled())
            ONOFF_bt.setBackground(getResources().getDrawable(R.drawable.ic_off_bt));
        }

    }
});

        TEE_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
if(mBluetoothAdapter.isEnabled()){
                int counter = 0;
                do {
                    try {
                        btSocket = LOKO_1.createRfcommSocketToServiceRecord(mUUID);
                        System.out.println(btSocket);
                        btSocket.connect();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    counter++;
                } while(!btSocket.isConnected() && counter < 10);

                if(btSocket.isConnected()){

                    info_("EET połączona z "+LOKO_1.getName()+ " " +LOKO_1.getAddress());


                    try {
                        OUT_ = btSocket.getOutputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        OUT_.write(str.getBytes());


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
else {
    info_("Włącz Bluetooth!");
}
}
        });


        RGHT_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                try {




                    if (btSocket.isConnected()) {

                        ++step_;

                        if (step_ > 14) --step_;

                        str = step_ + "\0" + "\n";

                        colorIT(step_);

                        try {
                            OUT_ = btSocket.getOutputStream();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try {
                            OUT_.write(str.getBytes());


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        info_("EET nie podłączona");
                    }


                }catch (Exception e){
                    String msg = "Bluetooth niewłączony lub lokomotywa niesparowana";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                }


            }
        });

        LFT_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {





                if (btSocket.isConnected()) {

                    --step_;

                    if(step_< 0) ++step_;


                    str = Integer.toString(step_)+"\0"+"\n";

                    colorIT(step_);

                    try {
                        OUT_ = btSocket.getOutputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        OUT_.write(str.getBytes());
                        // OUT_.flush();
                        //OUT_ = null;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    info_("EET nie podłączona");
                }
                }catch (Exception e){
                    String msg = "Bluetooth niewłączony lub lokomotywa niesparowana";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                }


            }
        });

    }

    private void printArrow(int step_){

        String arr_l ="";
        String arr_r ="";


        if (step_==7){
            arr_l=">>>>>>>";
            arr_r="<<<<<<<";
        }


        if(step_<7) {
            for (int i = 0; i < (step_ * (-1)) + 7; ++i) {

                arr_l += "<";
                arr_r += "<";

            }
        }
        if(step_>7){
            for(int i = 0; i<step_-7;++i){

                arr_r += ">";
                arr_l += ">";
            }

        }

        RGHT_bt.setText(arr_r);
        LFT_bt.setText(arr_l);

    }


    private void colorIT(int step_){

        int paintIT = step_;


        SpannableString ss_l = new SpannableString(LFT_bt.getText().toString());
        SpannableString ss_r = new SpannableString(RGHT_bt.getText().toString());

        ForegroundColorSpan fcsRed = new ForegroundColorSpan(getResources().getColor(R.color.Red));
        ForegroundColorSpan fcsYellow = new ForegroundColorSpan(getResources().getColor(R.color.Yellow));

        if(paintIT==7){
            ss_r.setSpan(fcsYellow,0,paintIT-6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            RGHT_bt.setText(ss_r);
            ss_l.setSpan(fcsYellow,paintIT,7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            LFT_bt.setText(ss_l);

        }

        if(paintIT > 7){

            ss_r.setSpan(fcsRed,0,paintIT-7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            RGHT_bt.setText(ss_r);

        }

        if(paintIT < 7){

            ss_l.setSpan(fcsRed,paintIT,7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            LFT_bt.setText(ss_l);
        }

    }


private void info_(String msg){
    Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
}  //info_


} // main

/*
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode = REQUEST_ENABLE_BT){
            if(requestCode == RESULT_OK){

            }else if(requestCode ==RESULT_CANCELED){

            }
        }
    }

    }
*/