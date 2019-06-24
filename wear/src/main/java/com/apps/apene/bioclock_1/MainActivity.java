package com.apps.apene.bioclock_1;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

/*
* Esta clase sirve como ejemplo práctico y simple sobre cómo manejar los listeners sobre sensores del Wearable
* No es necesaria para el funcionamiento de la App, pero su implementación es un ejercicio de aprendizaje para
* desarrollar después la clase HeartRateSensorAdapter que
* **/

public class MainActivity extends WearableActivity implements SensorEventListener {

    // Elementos Gráficos
    protected TextView mCurrentHRlabel;
    protected int iCurrentHR;
    protected Button mButtonStart;

    // Manejadores para el sensor de pulso
    protected SensorManager mSensorManager;
    protected Sensor mHeartRateSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencias
        mCurrentHRlabel = findViewById(R.id.current);
        mButtonStart = findViewById(R.id.nextButton);


        //Inicializamos el valor de las pulsaciones actuales a 0
        iCurrentHR = 0;


        //Inicializamos el texto de las pulsaciones actuales y la visibilidad (INVISIBLE)
        mCurrentHRlabel.setText("Current Heart Rate: ...");
        mCurrentHRlabel.setVisibility(View.INVISIBLE);

        //Inicializamos SensorManager y el sensor especificando que se trata del sensor de pulso (TYPE_HEART_RATE)
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        //Botón START / STOP

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Si se pulsa START:
                if(mButtonStart.getText().equals("START")){
                    //Solicitamos permiso para acceder a los sensores del reloj, puesto que es información sensible y asegurarnos de que tenemos acceso
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{android.Manifest.permission.BODY_SENSORS},
                            1);
                    // Mostramos la etiqueta con el pulso
                    mCurrentHRlabel.setVisibility(v.VISIBLE);
                    // Cambiamos el texto del botón a STOP
                    mButtonStart.setText("STOP");

                } else{// Si pulsa STOP:
                    // Ocultamos el pulso
                    mCurrentHRlabel.setVisibility(v.INVISIBLE);
                    // Cambiamos el texto del botón a START
                    mButtonStart.setText("START");
                    // Volvemos a inicializar los valores de la etiqueta y del pulso
                    mCurrentHRlabel.setText("Current Heart Rate: ...");
                    iCurrentHR = 0;
                    // Desactivamos el listener para parar de registrar el pulso
                    sensorListenerStop(MainActivity.this, mHeartRateSensor);
                }

            }
        });

        // Enables Always-on
        setAmbientEnabled();
    }

    @Override
    // Métodos para manejar la solicitud de permiso de uso de los sensores
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
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    // Método que maneja los cambios en el sensor de pulso, cada vez que hay un cambio lo mostramos por pantalla
    public void onSensorChanged(SensorEvent event){
        // Si recibimos un cambio en el sensor de pulso...
        if(event.sensor.getType() == Sensor.TYPE_HEART_RATE)
        {
            String msg = "" + (int)event.values[0];
            iCurrentHR = (int) event.values[0];
            mCurrentHRlabel.setText("Current Heart Rate: " + iCurrentHR);
            mCurrentHRlabel.setVisibility(View.VISIBLE);
        }

    }

    @Override
    // Método para manejar cambios de precisión. Sin uso en esta aplicación.
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //log change in sensor accuracy
        Log.i("onAccuracyChanged", "-------------onAccuracyChanged - accuracy: " + accuracy);
    }

    // Utilizamos este método para acceder al listener del sensor y desactivarlo
    public void sensorListenerStop (Context context, Sensor sensor){
        mSensorManager.unregisterListener((SensorEventListener) context, sensor);
    }
}
