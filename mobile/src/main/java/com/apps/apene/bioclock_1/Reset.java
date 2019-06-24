package com.apps.apene.bioclock_1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apps.apene.bioclock_1.model.FirebaseAdapter;
import com.apps.apene.bioclock_1.model.User;

public class Reset extends AppCompatActivity {

    // Elementos gráficos
    protected EditText mResetPassEmail = null;
    protected Button mButtonReset = null;
    protected TextView mBacktoLogin = null;

    // Objeto FirebaseAdapter, llamará al método para resetear la contraseña
    FirebaseAdapter fbAdapter = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        // Referencias
        mResetPassEmail = findViewById(R.id.et_reset_email);
        mButtonReset = findViewById(R.id.bt_reset);
        mBacktoLogin = findViewById(R.id.tv_reset_back_to_login);

        // Mostramos la barra de acción superior
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Acciones del botón Reset Password
        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Si el email está completado
                if (!TextUtils.isEmpty(mResetPassEmail.getText().toString())) {
                    // Recogemosel email en un String
                    String email = mResetPassEmail.getText().toString();
                    // Creamos un nuevo usuario con el string
                    User u = new User(email);
                    // Llamamos al método resetPassword pasándole el usuario creado como parámetro
                    fbAdapter.resetPassword(u);
                    // Indicamos al usuario que siga el link recibido por correo para actualizar su contraseña
                    Toast.makeText(Reset.this, getString(R.string.reset_pass_ok),
                            Toast.LENGTH_LONG).show();
                } else {
                    // Si no ha introducido un correo, indicamos al usuario que debe hacerlo
                    Toast.makeText(Reset.this, getString(R.string.reset_pass_fail),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        // Acciones del link Back To Login
        mBacktoLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // Enviamos al usuario a la página de Login
                Intent backToLogin = new Intent(getApplicationContext(), Login.class);
                startActivity(backToLogin);
                Reset.this.finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        // Al pulsar atrás, finalizamos la actividad y volvemos a Login
        switch (item.getItemId()){
            case android.R.id.home :
                Intent backToLogin = new Intent (this, Login.class);
                startActivity(backToLogin);
                this.finish();
                return true;
            default:
                return true;
        }
    }
}
