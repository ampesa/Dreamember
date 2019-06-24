package com.apps.apene.bioclock_1;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.apene.bioclock_1.model.Tip;
import com.apps.apene.bioclock_1.model.TipsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Tips extends AppCompatActivity {

    protected FirebaseAuth mAuth = null;

    // Lista para recoger las entradas de los personajes parseadas de la base de datos
    protected List<Tip> mListTips = null;

    // Objeto para referenciar el RecyclerView
    protected RecyclerView mRecyclerView = null;

    // Objeto para referenciar el Adapter que rellenará el RecyclerView
    protected TipsAdapter mAdapter = null;

    // Objeto para referenciar el LayoutManager que dipondrá las vistas del RecyclerView
    protected LinearLayoutManager mManager = null;

    // Objeto para el acceso a la base de datos
    protected DatabaseReference mDataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        mAuth = FirebaseAuth.getInstance();

        // Referencia del ArrayList que contendrá los objetos Tip
        mListTips = new ArrayList<>();

        // Llamamos al método getAllTips
        getAllTips();
    }

    // Método para obtener los consejos y mostrarlos en la actividad Tips
    public void getAllTips() {
        mDataBase = FirebaseDatabase.getInstance().getReference();

        Query query = mDataBase.child(getString(R.string.db_node_tips));

        Toast.makeText(getApplicationContext(), "iniciamos búsqueda", Toast.LENGTH_LONG).show();

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            Tip tip = new Tip();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Recorremos el DataSnapshot
                for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                    // Pasamos al objeto tip cada consejo encontrado y lo añadimos al arraylist
                    tip = datasnapshot.getValue(Tip.class);
                    mListTips.add(tip);
                }
                // Creamos la referencia del Recycler pasándole el elemento gráfico con el método findVeiwById
                mRecyclerView = findViewById(R.id.rv_tips_viewer);

                // Creamos la referencia del adapter pasándole como parámetro a su constructor el List en el que hemos cargado los resultados
                mAdapter = new TipsAdapter(mListTips);

                // Creamos la referencia del LayoutManager pasándole el contexto (this)
                mManager = new LinearLayoutManager(getApplicationContext());

                // Cambiamos la orientación del LayoutManager
                mManager.setOrientation(RecyclerView.VERTICAL);

                // Creadas las referencias le asignamos al RecyclerView su Adapter u su LayoutManager
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(mManager);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
