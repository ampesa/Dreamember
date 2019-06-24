package com.apps.apene.bioclock_1.model;


import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.apps.apene.bioclock_1.MainActivity;

import static android.content.Context.SENSOR_SERVICE;

/*
* Clase que contiene los métodos para iniciar y parar la actividad del sensor de pulso del wearable
* **/
public class HeartRateSensorAdapter implements SensorEventListener {

    // Manejadores para el sensor de pulso
    protected SensorManager mSensorManager;
    protected Sensor mHeartRateSensor;

    // int para recoger el pulso y devolverlo a través del método startHeartRateSensor
    protected int bpm;

    protected Context context = null;

    /*
    * Este método recibe el contexto de la aplicación que lo llama e inicializa el sensor de pulos,
    * recoge el valor en un int y lo devuelve
    * **/
    public int startHeartRateSensor (Context context){
        // Inicializamos bpm a 0
        bpm = 0;
        //Inicializamos SensorManager y el sensor especificando que se trata del sensor de pulso (TYPE_HEART_RATE)
        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        //Solicitamos permiso para acceder a los sensores del reloj, puesto que es información sensible y asegurarnos de que tenemos acceso
        ActivityCompat.requestPermissions((MainActivity) context,
                new String[]{android.Manifest.permission.BODY_SENSORS},
                1);


        return bpm;
    }

    public void stopHeartRateSensor (){

    }

    //@Override
    // Método para manejar la solicitud de permiso de uso de los sensores
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.i("got event", "onRequestPermissionsResult: " + requestCode);

        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Obtenemos permiso
                    Log.i("permsGranted", "we have permission registering the heartrate sensor");
                    mSensorManager.registerListener(this, mHeartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);

                } else {
                    //No obtenemos permiso
                    Log.i("permsNotGranted", "we have NO permission to register the heart rate sensor");
                }
                break;
            default:
                //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
