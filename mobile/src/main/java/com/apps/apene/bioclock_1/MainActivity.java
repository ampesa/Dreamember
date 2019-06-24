package com.apps.apene.bioclock_1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    // Elementos Gráficos
    protected TextView mComment = null;
    protected Button mButtonDream = null;
    protected ImageView mProfile = null;
    protected ImageView mTips = null;
    protected RelativeLayout mRelativeProfile = null;
    protected RelativeLayout mRelativeTips = null;

    // Objetos
    protected FirebaseAuth mAuth = null;
    protected String sCurrentUID = null;

    // Objeto Ringtone para usar como sonido de despertador
    protected Ringtone r;
    protected Uri notification;

    // Manejadores para el sensor de pulso
    protected SensorManager mSensorManager;
    protected Sensor mHeartRateSensor;

    // int para recoger el pulso y devolverlo a través del método startHeartRateSensor
    protected int bpm;

    // Constante para el resultado de requestPermissions
    private static final int MY_PERMISSIONS_REQUEST_BODY_SENSORS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Mostramos la barra de acción superior
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // Referencias
        mComment = findViewById(R.id.tv_main_description);
        mButtonDream = findViewById(R.id.bt_dream);
        mProfile = findViewById(R.id.iv_profile);
        mTips = findViewById(R.id.iv_tips);
        mRelativeProfile = findViewById(R.id.rl_profile);
        mRelativeTips = findViewById(R.id.rl_tips);
        mAuth = FirebaseAuth.getInstance();
        sCurrentUID = mAuth.getCurrentUser().getUid();

        // Código adaptado de https://stackoverflow.com/questions/4441334/how-to-play-an-android-notification-sound
        // Utiliza RingtoneManager para hacer sonar una notificación, una alarma o un tono de llamada.
        try {
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Inicializamos SensorManager y el sensor especificando que se trata del sensor de pulso (TYPE_HEART_RATE)
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        // Acciones del botón Dream / Quit Dreaming
        mButtonDream.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if (mButtonDream.getText().equals("Dream")){
                    // Al pulsar Dream cambiamos el texto y los colores del botón. Con ello indicamos que se ha iniciado un proceso que parará cuando se vuelva a pulsar.
                    mButtonDream.setText(getString(R.string.button_quit_dreaming));
                    mButtonDream.setBackgroundColor(getColor(R.color.common_google_signin_btn_text_dark));
                    mButtonDream.setTextColor(getColor(R.color.common_google_signin_btn_text_light));
                    // Manejamos dos condiciones, al menos 7 horas desde el pulsado del botón y al menos 15 minutos desde el inicio de fase REM
                    // Añadimos una cuenta atrás (por defecto 7 horas (25.200.000 millis), en versiones posteriores podemos hacer que este tiempo sea editable por el usuario o mostrar el tiempo previsto hasta la alarma.
                    // Una vez finalizada pondremos a analizar el registro de datos e identificaremos el inicio de la fase REM. A partir de ahí una nueva cuenta atras de 15 minutos y ejecutamos r.play()
                    new CountDownTimer(5000, 1000) {
                        // código que ejecutamos al inicarse la cuenta atrás
                        public void onTick(long millisUntilFinished) {
                            // mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                            // Iniciamos registro de datos pasándole la actividad como contexto
                            // Comprobamos si hay permiso para acceder al sensor de pulso (grupo de sensores BODY_SENSORS)
                            if (ContextCompat.checkSelfPermission(MainActivity.this,
                                    Manifest.permission.BODY_SENSORS)
                                    != PackageManager.PERMISSION_GRANTED) {

                                // En caso de que se hubiera denegado anteriormente podemos lanzar una mensaje de explicación de la necesidad de acceso
                                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                        Manifest.permission.BODY_SENSORS)) {
                                    // Si mostramos el mensaje, depués de mostrarlo volveríamos a solicitar el permiso
                                    ActivityCompat.requestPermissions(MainActivity.this,
                                            new String[]{Manifest.permission.BODY_SENSORS},
                                            MY_PERMISSIONS_REQUEST_BODY_SENSORS);
                                } else {
                                    // Si no es necesario añadir ninguna explicación, solicitamos directamente el permiso.
                                    ActivityCompat.requestPermissions(MainActivity.this,
                                            new String[]{Manifest.permission.BODY_SENSORS},
                                            MY_PERMISSIONS_REQUEST_BODY_SENSORS);
                                }
                            }

                        }

                        // El código dentro de onFinish se ejecutará una vez finalizada la cuenta atrás.
                        public void onFinish() {
                            //mTextField.setText("done!");
                            // Iniciamos análisis de datos para detectar inicio fase REM
                            // Nueva cuenta atrás de 15 minutos
                            // Ejecutamos r.play();
                            // Paramos el registro de datos del sensor de pulso

                        }
                    }.start();


                } else { // Acciones del botón QUIT DREAMING
                    // Si r no es nulo, es decir, está sonando paramos el sonido al pulsar sobre el botón
                    if (r != null) r.stop();
                    // reiniciamos r por si el usuario vuelve a plantear una alarma sin cerrar la aplicación
                    r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    mButtonDream.setText(getString(R.string.button_dream));
                    mButtonDream.setBackgroundColor(getColor(R.color.common_google_signin_btn_text_light_default));
                    mButtonDream.setTextColor(getColor(R.color.common_google_signin_btn_text_dark_default));
                    // Volvemos a inicializar los valores de la etiqueta y del pulso
                    mComment.setText(getString(R.string.tv_main_description));
                    bpm = 0;
                    // Desactivamos el listener para parar de registrar el pulso
                    sensorListenerStop(MainActivity.this, mHeartRateSensor);

                }

            }
        } );

        // Acciones de la imagen Profile
        mRelativeProfile.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // Llevamos al usuario a la pantalla de Perfil
                Intent goToProfile = new Intent(getApplicationContext(), Profile.class);
                startActivity(goToProfile);
                //MainActivity.this.finish(); // finalizamos la actividad Main
            }
        });

        // Acciones de la imagen SingleTip
        mRelativeTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llevamos al usuario a la pantalla con los Consejos sobre el sueño SingleTip
                Intent goToTips = new Intent(getApplicationContext(), Tips.class);
                startActivity(goToTips);
                //MainActivity.this.finish(); // finalizamos la actividad Main
            }
        });
    }

    // Inflamos el menú con el xml correspondiente
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Definimos las acciones de los items del menú
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Intents para cada selección de items del menú
        switch (item.getItemId()){
            // Al pulsar atrás, finalizamos la actividad y volvemos a main
            case android.R.id.home :
                mAuth.signOut();
                this.finish();
                return true;
            case R.id.menu_profile :
                Intent goToProfile = new Intent (this, Profile.class);
                startActivity(goToProfile);
                return true;
            case R.id.menu_settings :
                //Intent gotToSettings = new Intent (this, UserSettings.class);
                //startActivity(gotToSettings);
                return true;
            case R.id.menu_tips :
                Intent goToTips = new Intent (this, Tips.class);
                startActivity(goToTips);
                return true;
            case R.id.menu_logout :
                mAuth.signOut();
                Intent goToLogin = new Intent (this, Login.class);
                startActivity(goToLogin);
                return true;
            default:
                return true;
        }
    }

    // Método para el manejo del sensor de pulso
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Si recibimos un cambio en el sensor de pulso...
        if(event.sensor.getType() == Sensor.TYPE_HEART_RATE)
        {
            // Pasamos las pulsaciones por minuto al int bpm
            bpm = (int) event.values[0];
            // Mostramos en el comentario los la frecuencia cardíaca actual
            mComment.setText("Current Heart Rate: " + bpm + "\nPress Quit Dreaming to cancel your dream");
        }

    }

    @Override
    // Método para manejar cambios de precisión del sensor. Sin uso en esta aplicación.
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //log change in sensor accuracy
        Log.i("onAccuracyChanged", "-------------onAccuracyChanged - accuracy: " + accuracy);
    }

    /*
     * Este método recibe el contexto de la aplicación que lo llama e inicializa el sensor de pulos,
     * recoge el valor en un int y lo devuelve
     * **/
    public int startHeartRateSensor (Context context){
        // Inicializamos bpm a 0
        bpm = 0;

        // Comprobamos si hay permiso para acceder al sensor de pulso (grupo de sensores BODY_SENSORS)
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.BODY_SENSORS)
                != PackageManager.PERMISSION_GRANTED) {

            // En caso de que se hubiera denegado anteriormente podemos lanzar una mensaje de explicación de la necesidad de acceso
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.BODY_SENSORS)) {
                // Si mostramos el mensaje, depués de mostrarlo volveríamos a solicitar el permiso
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_BODY_SENSORS);
            } else {
                // Si no es necesario añadir ninguna explicación, solicitamos directamente el mensaje.
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_BODY_SENSORS);
            }
        }

        return bpm;
    }

    // Utilizamos este método para acceder al listener del sensor y desactivarlo
    public void sensorListenerStop (Context context, Sensor sensor){
        mSensorManager.unregisterListener((SensorEventListener) context, sensor);
    }

    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_BODY_SENSORS: {
                // Si la solicitud es cancelada el array de respuesta estará vacío, por tanto controlamos que su longitud sea > 0
                // además de que se haya concedido el permiso.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Como se han garantizado los permisos, iniciamos el listener del sensor de pulso
                    mSensorManager.registerListener(this, mHeartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);

                } else {

                    // Se ha denegado el permiso o se ha omitido, por tanto no podemos iniciar el sensor
                }
                return;
            }

        }
    }*/


    @Override
    // Método para manejar la solicitud de permiso de uso de los sensores
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.i("got event", "onRequestPermissionsResult: " + requestCode);

        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Obtenemos permiso
                    Log.i("permsGranted", "we have permission registering the heartrate sensor");
                    mSensorManager.registerListener(this, mHeartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    Toast.makeText(MainActivity.this, "sensor listener on",
                            Toast.LENGTH_LONG).show();
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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
