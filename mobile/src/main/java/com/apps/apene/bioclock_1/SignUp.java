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

// Pantalla de registro de usarios
public class SignUp extends AppCompatActivity {

    // Elementos Gráficos
    protected EditText mSignUpEmail = null;
    protected EditText mSignUpPass = null;
    protected EditText mSignUpPassConfirm = null;
    protected EditText mSignUpName = null;
    protected EditText mSignUpBirthYear = null;
    protected Button mButtonSignUp = null;
    protected TextView mBacktoLogin = null;

    // Objteto FirebaseAdapter para llamar a los métodos de registro de usuario
    FirebaseAdapter fbAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Mostramos la barra de acción superior
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Referencias
        mSignUpEmail = findViewById(R.id.et_signup_email);
        mSignUpPass = findViewById(R.id.et_signup_pass);
        mSignUpPassConfirm = findViewById(R.id.et_signup_pass_confirm);
        mSignUpName = findViewById(R.id.et_signup_name);
        mSignUpBirthYear = findViewById(R.id.et_signup_birth_year);
        mButtonSignUp = findViewById(R.id.bt_sign_up);
        mBacktoLogin = findViewById(R.id.tv_signup_back_to_login);

        // Inicializamos el adaptador
        fbAdapter = new FirebaseAdapter(this);

        // Acción del botón Sign Up
        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mSignUpName.getText().toString())) {
                    if(!TextUtils.isEmpty(mSignUpBirthYear.getText().toString())) {
                        if (!TextUtils.isEmpty(mSignUpEmail.getText().toString())) {
                            if (mSignUpPass.getText().toString().length()>= 6) {
                                if ((mSignUpPassConfirm.getText().toString()).equals(mSignUpPass.getText().toString())){
                                    // Recogemos los datos introducidos por el usuario en la pantalla de registro
                                    String email = mSignUpEmail.getText().toString();
                                    String pass = mSignUpPass.getText().toString();
                                    String name = mSignUpName.getText().toString();
                                    String birth_year = mSignUpBirthYear.getText().toString();

                                    // Creamos un usuario con estos datos
                                    User u = new User (email, pass, name, birth_year);

                                    // Llamamos al método signUp y le pasamos el usuario como parámetro para crear el usuario en Firebase
                                    fbAdapter.signUp(u);

                                } else { // si no se cumplen las condiciones para datos válidos lo notificamos con un toast
                                    Toast.makeText(SignUp.this, getString(R.string.signup_not_equal),
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(SignUp.this, getString(R.string.signup_no_password),
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(SignUp.this, getString(R.string.signup_no_email),
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(SignUp.this, getString(R.string.signup_no_year),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(SignUp.this, getString(R.string.signup_no_name),
                            Toast.LENGTH_LONG).show();
                }

            } // fin onClick
        }); // fin listener


        // Acciones del link Back To Login
        mBacktoLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // Enviamos al usuario a la página de Login
                Intent backToLogin = new Intent(getApplicationContext(), Login.class);
                startActivity(backToLogin);
                SignUp.this.finish();
            }
        });

    } // Fin onCreate

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
