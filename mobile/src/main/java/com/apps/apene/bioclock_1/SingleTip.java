package com.apps.apene.bioclock_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.apps.apene.bioclock_1.model.Tip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SingleTip extends AppCompatActivity {

    // Elementos Gráficos
    protected TextView mTipTitle = null;
    protected TextView mTipContent = null;
    protected ImageView mTipImage = null;

    // Objeto FirebaseAuth para comprobar el usuario actual
    protected FirebaseAuth mAuth = null;
    // String para recoger el UID del usuario
    protected String sUserUID = null;

    // String para recibir la clave del producto a través del Bundle
    protected String key = null;
    // Objeto Tip, recibirá los valores desde Firebase
    protected Tip tip = null;
    // Objeto BBDD
    protected DatabaseReference mDataBase;
    // String para la URL de la imagen
    protected String imageURL = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_tip);

        // Referencias
        mTipTitle = findViewById(R.id.tv_tip_title);
        mTipContent = findViewById(R.id.tv_tip_content);
        mTipImage = findViewById(R.id.iv_tip_imgage);
        mAuth = FirebaseAuth.getInstance();
        sUserUID = mAuth.getUid();

        // Mostramos la barra de acción superior
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Recogemos el identificador del Tip
        getData();

        // Referencia de la bbdd sobre el nodo "tips"
        mDataBase = FirebaseDatabase.getInstance().getReference(getString(R.string.db_node_tips));

        // Query para buscar el producto que coincide con la clave
        final Query selectedTipData = mDataBase.orderByChild(getString(R.string.db_node_tips_uid)).equalTo(key);

        getTipValues(selectedTipData);

    }

    // Método que recoge los extras de la actividad padre
    protected void getData(){
        Bundle extras = getIntent().getExtras();
        key = extras.getString("key");
        Toast.makeText(getApplicationContext(), "Se ha recuperado tìp nº" + key, Toast.LENGTH_LONG).show();

    }


    /**
     * Método para obtener los datos del consejo seleccionado recibe un objeto Query,
     * le añade un listener y recoge los datos de la base de datos en un Datasnapshot.
     * La consulta es sobre la clave del consejo, por lo que solo puede haber un resultado y no
     * es necesario un Array, simplemente pasamos el resultado a "tip" y con los datos de este
     * rellenamos las vistas.
     * */
    private void getTipValues (Query selectedProductData){
        selectedProductData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Recorremos el DataSnapshot
                for (DataSnapshot datasnapshot: dataSnapshot.getChildren()) {
                    // Como solo debe haber un resultado, asignamos los valores a product
                    tip = datasnapshot.getValue(Tip.class);
                    // Rellenamos Vistas con los valores que acabamos de recoger en Tip
                    mTipTitle.setText(tip.getTitle());
                    mTipContent.setText(tip.getContent());
                    imageURL = tip.getImage_url();
                }
                Toast.makeText(getApplicationContext(), key, Toast.LENGTH_LONG).show();

                // Asignamos la imagen con Picasso
                Picasso.get().load(imageURL).into(mTipImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        // Al pulsar atrás, finalizamos la actividad y volvemos a main
        switch (item.getItemId()){
            case android.R.id.home :
                Intent goToTips = new Intent (this, Tips.class);
                startActivity(goToTips);
                this.finish();
                return true;
            default:
                return true;
        }
    }
}
