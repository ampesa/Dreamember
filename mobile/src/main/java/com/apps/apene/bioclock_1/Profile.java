package com.apps.apene.bioclock_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.apps.apene.bioclock_1.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    // Elementos Gráficos
    protected EditText mProfileName = null;
    protected EditText mProfileBirthYear = null;
    protected ImageView mBeatRateGraph = null;
    protected TextView mDreamingTime = null;
    protected TextView mSleepingTime = null;
    protected TextView mAverageBPM = null;

    // Objetos
    protected FirebaseAuth mAuth = null;
    protected String sCurrentUID = null;
    protected User u = null;
    protected DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Mostramos la barra de acción superior
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Referencias
        mProfileName = findViewById(R.id.et_profile_name);
        mProfileBirthYear = findViewById(R.id.et_profile_birth_year);
        mBeatRateGraph = findViewById(R.id.iv_profile_graph);
        mDreamingTime = findViewById(R.id.tv_dreaming_time);
        mSleepingTime = findViewById(R.id.tv_sleeping_time);
        mAverageBPM = findViewById(R.id.tv_profile_bpm);

        // Referencias de autenticación para identificar el usuario actual
        mAuth = FirebaseAuth.getInstance();
        sCurrentUID = mAuth.getCurrentUser().getUid();

        // Referencia de la bbdd
        mDataBase = FirebaseDatabase.getInstance().getReference(getString(R.string.db_node_users));

        final Query currentUserData = mDataBase.orderByChild("uid").equalTo(sCurrentUID);

        // Llamamos al método para recoger los datos de usuario y le pasamos el objeto query como parámetro
        getUserValues(currentUserData);

        // Hacemos que los EditText no sean editables por parte del usuario
        mProfileName.setEnabled(false);
        mProfileBirthYear.setEnabled(false);

        // Acciones de la imagen BPM Graph
        mBeatRateGraph.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // Llevamos al usuario a la pantalla con la Gráfica de frecuencia cardíaca
                Intent goToGraph = new Intent(getApplicationContext(), Graph.class);
                startActivity(goToGraph);
                Profile.this.finish(); // finalizamos la actividad Profile
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        // Al pulsar atrás, finalizamos la actividad y volvemos a main
        switch (item.getItemId()){
            case android.R.id.home :
                Intent goToMain = new Intent (this, MainActivity.class);
                startActivity(goToMain);
                this.finish();
                return true;
            default:
                return true;
        }
    }

    // Método para obtener los datos del usuario actual recibe un objeto Query
    private void getUserValues (Query currentUserData){
        currentUserData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Recorremos el DataSnapshot
                for (DataSnapshot datasnapshot: dataSnapshot.getChildren()) {
                    // Como solo debe haber un resultado, asignamos los valores al user u
                    u = datasnapshot.getValue(User.class);
                    // Rellenamos los EditText con los valores que acabamos de recoger en user
                    mProfileName.setText(getString(R.string.profile_name_label) + ": " + u.getName());
                    mProfileBirthYear.setText(getString(R.string.profile_birth_year_label) + ": " + u.getBirth_year());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
