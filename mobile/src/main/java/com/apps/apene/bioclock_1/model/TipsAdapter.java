package com.apps.apene.bioclock_1.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.apene.bioclock_1.R;
import com.apps.apene.bioclock_1.SingleTip;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase adaptador que maneja el RecyclerView que muestra los consejos sobre sueño saludable
 * */

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.TipsViewHolder> {

    // Objeto List para recibir el ArrayList de Tips a mostrar
    protected List<Tip> mTips;

    // Constructor de la clase, recibe un List y lo asigna a la lista de consejos mTips
    public TipsAdapter(List <Tip> tipsList){
        // Le asignamos a mTips el ArrayList que recibe el constructor con la selección de objetos Tip
        mTips = new ArrayList<Tip>(tipsList);
    }

    /**
     * Método onCreateViewHolder crea una vista (view) y la rellena (inflate) con el layout definido para el recycler
     * */
    @NonNull
    @Override
    public TipsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tips_view_holder_layout, viewGroup, false);

        // El médoto devuelve el ViewHolder "inflado"
        return new TipsViewHolder(view);

    }

    /**
     * Método onBindViewHolder se asigna los valores del array a las vistas de cada ViewHolder. Utiliza el int position como
     * índice de referencia
     * */
    @Override
    public void onBindViewHolder(@NonNull final TipsAdapter.TipsViewHolder tipsViewHolder, int position) {

        // product recibe el objeto de la posición
        Tip tip = mTips.get(position);
        // Asignamos los valores de product a las vistas
        tipsViewHolder.mTipTitle.setText(tip.getTitle());
        tipsViewHolder.mTipPreview.setText(tip.getPreview());
        String imageURL = tip.getImage_url();

        Picasso.get().load(imageURL).into(tipsViewHolder.mTipImage);

        // Obtenemos la clave del consejo la usaremos para el intent que mostrará los detalles
        tipsViewHolder.mUid = tip.getUid();

    }

    /**
     * Método que devuelve el total de elementos que podrá mostrar el Recycler
     * */
    @Override
    public int getItemCount() {
        return mTips.size();
    }

    /**
     * Clase TipsViewHolder extiende ViewHolder e implementa OnClickListener para manejar con más
     * facilidad los listeners de las vistas*/
    public class TipsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Elementos del ViewHolder
        protected TextView mTipTitle = null;
        protected TextView mTipPreview= null;
        protected ImageView mTipImage = null;
        protected String mUid = null;
        protected Context mContext = null;

        // Constructor de la clase
        public TipsViewHolder(@NonNull View itemView) {
            super(itemView);

            // Asignamos a cada View su correspondiente elemento gráfico del xml
            mTipTitle = itemView.findViewById(R.id.tv_tips_tip_title);
            mTipPreview = itemView.findViewById(R.id.tv_tips_tip_preview);
            mTipImage = itemView.findViewById(R.id.iv_tips_tip_image);

            // Añadimos listeners a las vistas que tendrán interacción
            mTipTitle.setOnClickListener(this);
            mTipPreview.setOnClickListener(this);
            mTipImage.setOnClickListener(this);

            mContext = itemView.getContext();
        }

        @Override
        public void onClick(View v) {

            int viewId = v.getId();

            // Si el usuario pulsa la imagen o el título mostramos un toast con la clave del consejo y ejecutamos
            // el método showTipDetails con la clave del consejo como parámetro
            if (viewId == mTipImage.getId() || viewId == mTipTitle.getId() || viewId == mTipPreview.getId()){
                // Si pulsa en la imagen o en n los datos del producto, lo llevamos a la vista del producto
                Toast.makeText(mContext, mUid, Toast.LENGTH_LONG).show();
                showTipDetails(mUid);
            }
        }

        /**
         * Método showTipDetails, recibe la clave del consejo (Tip) como parametro y lanza un intent
         * con la clave como extra que lleva al usuario a la vista del consejo*/
        public void showTipDetails(String key){

            Intent showTip = new Intent(mContext, SingleTip.class);
            // Pasamos el int al Bundle
            showTip.putExtra("key", key);
            // Iniciamos la actividad
            mContext.startActivity(showTip);
        }
    }
}
