package com.apps.apene.bioclock_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Graph extends AppCompatActivity {

    // Elementos gráficos
    protected TextView mComment = null;
    protected ImageView mGraph = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        // Mostramos la barra de acción superior
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Referencias
        mComment = findViewById(R.id.tv_graph_comment);
        mGraph = findViewById(R.id.iv_graph);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        // Al pulsar atrás, finalizamos la actividad y volvemos a Profile
        switch (item.getItemId()){
            case android.R.id.home :
                Intent goToProfile = new Intent (this, Profile.class);
                startActivity(goToProfile);
                this.finish();
                return true;
            default:
                return true;
        }
    }
}
