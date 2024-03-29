package com.apps.apene.bioclock_1.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.apps.apene.bioclock_1.MainActivity;
import com.apps.apene.bioclock_1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


//import android.support.annotation.NonNull;

/**
 * Clase que contiene los métodos de acceso a Firebase. Por dificultades con la sicronización de datos
 * y la generación de datos nulos algunos métodos que deberían estar en esta case se implementan desde
 * las propias vistas.
 * */
public class FirebaseAdapter {

    // int para el resultado del método signUp()
    protected int mResult = 0;

    // Objeto FirebaseAuth
    private FirebaseAuth mAuth;
    // Objeto FirebaseUser para añadir el uid al registro en la bbdd
    private FirebaseUser mUser;
    // String para recoger el UID del usuario
    private String userUID = null;
    // Objeto Base de Datos
    DatabaseReference mDataBase;
    // Contexto de la aplicación que usa la base de datos
    private final Context context;

    // Constructor recibe el contexto y crea una referencia de autenticación y de BBDD
    public FirebaseAdapter(Context c) {
        context = c;
        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference(context.getString(R.string.db_node_users));
    }

    /**
     * Método para registrar usuario con correo y contraseña en Firebase Authentication
     * recibe un objeto User con los datos para registrar al usuario.
     * Primero crea el usuario. En caso de éxito, obtiene el UID del usuario creado y lo añade a los datos del
     * objeto User para guardarlo junto con el resto de datos en la BBDD utilizando el método saveUser(User u),
     * devuelve 1 y lleva al usuario a la actividad principal.
     * En caso de error, lo notifica con un toast indicando el motivo (task.getException())
     * */
    public int signUp(final User u){
        // Recogemos email y password introducidos por el usuario
        String email = u.getEmail();
        String password = u.getPass();
        // Utilizamos el método createUserWith... para crear el usuario en Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            mUser = mAuth.getCurrentUser();
                            // Obtenemos el UID del nuevo usuario y lo añadimos al objeto User u
                            userUID = mUser.getUid();
                            u.setUid(userUID);
                            u.setPass("");
                            // Guardamos los datos del usuario en Firebase Realtime Database incluyendo el UID
                            saveUser(u);
                            mResult = 1;
                            Toast.makeText(context, context.getString(R.string.signup_ok)+ mUser.getUid(),
                                    Toast.LENGTH_SHORT).show();
                            // Como el registro es correcto llevamos al usuario a la actividad principal
                            Intent goToMain = new Intent(context.getApplicationContext(), MainActivity.class);
                            context.startActivity(goToMain);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, context.getString(R.string.signup_fail) + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                            mResult = 0;
                        }
                    }
                });
        return mResult;
    }

    /**
     * Método para guardar los datos de usuario en la BBDD
     * */
    // Método para guardar datos de un nuevo usuario en Firebase Real Time Database
    public int saveUser(User u){
        // Obtenemos la siguiente clave de la bbdd
        String key = mDataBase.push().getKey();
        // Añadimos el User "u" como hijo del nodo referenciado en el constructor
        mDataBase.child(key).setValue(u);
        return 1;
    } // fin método saveUser()

    // Método para actualizar los datos de un usuario
    public int updateUser(User u){
        // Obtenemos la siguiente clave de la bbdd
        String key = mDataBase.push().getKey();
        // Añadimos el User "u" como hijo del nodo referenciado en el constructor
        mDataBase.child(key).setValue(u);
        return 1;
    }

    /**
     * Método para logear usuario con correo y contraseña en Firebase Authentication
     * Recibe un objeto User y utiliza el email y la contraseña introducidas para loguearle
     * */
    public int login (final User u){
        // Recogemos email y password introducidos por el usuario
        String email = u.getEmail();
        String password = u.getPass();
        // Utilizamos el método siguiente para loguear al usuario en Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            mUser = mAuth.getCurrentUser();
                            // Obtenemos el UID del nuevo usuario y lo añadimos al objeto User u
                            userUID = mUser.getUid();
                            u.setUid(mUser.getUid());
                            mResult = 1;
                            Toast.makeText(context, context.getString(R.string.login_ok)+ mUser.getUid(),
                                    Toast.LENGTH_SHORT).show();
                            // Como el login es correcto llevamos al usuario a la actividad principal
                            Intent goToMain = new Intent(context.getApplicationContext(), MainActivity.class);
                            context.startActivity(goToMain);
                            //((Activity) context).finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, context.getString(R.string.login_fail) + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                            mResult = 0;
                        }
                    }
                });
        return mResult;
    }

    /**
     * Método para enviar un correo de modificación de contraseña al usuario
     * Recibe el objeto User y extrae el correo para usarlo como parámetro en el método
     * sendPasswordResetEmail(email)
     * */
    public void resetPassword (User u){
        String email = u.getEmail();
        mAuth.sendPasswordResetEmail(email);
    }


}
