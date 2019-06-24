package com.apps.apene.bioclock_1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apps.apene.bioclock_1.model.FirebaseAdapter;
import com.apps.apene.bioclock_1.model.User;


public class Login extends AppCompatActivity {
    // Elementos gráficos
    protected EditText mLoginEmail = null;
    protected EditText mLoginPass = null;
    protected Button mButtonSignIn = null;
    protected TextView mForgotten = null;
    protected TextView mSignUp = null;

    // Objeto FirbaseAdapter, llamará a los métodos de login del usuario en Firebase
    protected FirebaseAdapter fbAdapter = null;

    // Objeto User
    protected User mUser = null;

    protected void resetValues(){
        mLoginEmail.setText(null);
        mLoginPass.setText(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        // Referencias
        mLoginEmail = findViewById(R.id.et_login_email);
        mLoginPass = findViewById(R.id.et_login_pass);
        mButtonSignIn = findViewById(R.id.bt_login_sign_in);
        mForgotten = findViewById(R.id.tv_login_forgotten);
        mSignUp = findViewById(R.id.tv_login_sign_up);


        // Referencia del Adaptador Firebase
        fbAdapter = new FirebaseAdapter(this);

        // Acciones del botón SIGN IN
        mButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Si el email y la contraseña están rellenados
                if (!TextUtils.isEmpty(mLoginEmail.getText().toString())) {
                    if (mLoginPass.getText().toString().length()>= 6) {
                        // recogemos los datos introducidos por el usuario
                        String email = mLoginEmail.getText().toString();
                        String password = mLoginPass.getText().toString();
                        // creamos un User con el email y la contraseña
                        mUser = new User(email, password);
                        resetValues();
                        // Llamamos al método login del adaptador
                        fbAdapter.login(mUser);
                    } else {
                        Toast.makeText(Login.this, getString(R.string.signup_no_password),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(Login.this, getString(R.string.signup_no_email),
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        // Acciones del link FORGOTTEN PASSWORD
        mForgotten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llevamos al usuario a la pantalla para resetear la contraseña
                Intent reset = new Intent(getApplicationContext(), Reset.class);
                startActivity(reset);
                Login.this.finish(); // finalizamos la actividad de login
            }
        });

        // Acciones del link SIGNUP
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Enviamos al usuario a la página de registro
                Intent signUp = new Intent(getApplicationContext(), SignUp.class);
                startActivity(signUp);
                Login.this.finish(); // finalizamos la actividad de login
            }
        });

    }

}
