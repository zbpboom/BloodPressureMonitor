package com.zhanbp.bloodpressuremonitor;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blakequ.bluetooth_manager_lib.BleManager;
import com.blakequ.bluetooth_manager_lib.BleParamsOptions;
import com.blakequ.bluetooth_manager_lib.connect.BluetoothSubScribeData;
import com.blakequ.bluetooth_manager_lib.connect.ConnectConfig;
import com.blakequ.bluetooth_manager_lib.connect.ConnectState;
import com.blakequ.bluetooth_manager_lib.connect.ConnectStateListener;
import com.blakequ.bluetooth_manager_lib.connect.multiple.MultiConnectManager;

import java.util.ArrayList;
import java.util.UUID;

import de.greenrobot.event.EventBus;

/**
* createAt 2019/8/21
* description:  主界面
*/

public class MainActivity extends Activity implements View.OnClickListener {

    private Button btnSelectDevice ;  //选择需要绑定的设备
    private Button btnStartConnect ;  //开始连接按钮
    private TextView txtContentMac ; //获取到的数据解析结果显示

    private ArrayList<String> connectDeviceMacList ; //需要连接的mac设备集合
    private ArrayList<String> connectDeviceNameList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();

    }



    private void initEvent() {
        btnSelectDevice.setOnClickListener(this);
        btnStartConnect.setOnClickListener(this);
    }

    private void initView() {
        btnSelectDevice = (Button) findViewById(R.id.btnSelectDevice);
        btnStartConnect = (Button) findViewById(R.id.btnStartConnect);
        txtContentMac = (TextView) findViewById(R.id.txtContentMac);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSelectDevice:
                // 扫描并选择需要连接的设备
                Intent intentSelect = new Intent();
                intentSelect.setClass(this,SelectDeviceActivity.class);
                startActivityForResult(intentSelect,1);
                break;
            case R.id.btnStartConnect:
                //connentBluetooth();
                if(connectDeviceMacList!=null){
                    Intent intentConnect = new Intent();
                    intentConnect.putStringArrayListExtra("connectMAC", connectDeviceMacList);
                    intentConnect.putStringArrayListExtra("connectNAME", connectDeviceNameList);
                    intentConnect.setClass(this,ChartsActivity.class);
                    startActivity(intentConnect);
                }
                else{
                    Toast.makeText(this, "no BLE need to connect", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            switch (requestCode){
                case 1:
                    connectDeviceMacList = data.getStringArrayListExtra("data");
                    connectDeviceNameList = data.getStringArrayListExtra("name");
                    Log.i("zbp","需要连接的mac"+connectDeviceMacList.toString());
                    //获取设备gatt对象
                    for (int i = 0; i < connectDeviceMacList.size(); i++) {
                        Log.i("zbp","添加了"+connectDeviceMacList.get(i));
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
