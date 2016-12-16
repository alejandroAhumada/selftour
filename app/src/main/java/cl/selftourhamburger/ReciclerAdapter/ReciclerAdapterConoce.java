package cl.selftourhamburger.ReciclerAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cl.selftourhamburger.Activity.Activity_Oferta;
import cl.selftourhamburger.R;
import cl.selftourhamburger.Util.UtilOferta;
import cl.selftourhamburger.model.pojo.Destino;
import cl.selftourhamburger.model.pojo.Puntos;

/**
 * Created by Alejandro on 11-07-2016.
 */
public class ReciclerAdapterConoce extends RecyclerView.Adapter<ReciclerAdapterConoce.MyViewHolder> {

    private Destino destino;
    private Context context;

    public ReciclerAdapterConoce(Context context, Destino destino) {
        this.context = context;
        this.destino = destino;



    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        this.context = parent.getContext();
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.fragment_recycler_view_conoce, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder itemsViewHolder, final int position) {
        itemsViewHolder.rlConoce.setBackground(setImageView(destino.getListaPuntos().get(position).getNombreLugar()));
        itemsViewHolder.txtNombreProducto.setText(UtilOferta.getNombreProducto(destino.getListaPuntos().get(position).getNombreLugar()));
        itemsViewHolder.txtDescProducto.setText(UtilOferta.getDescripcionProducto(destino.getListaPuntos().get(position).getNombreLugar()));

        itemsViewHolder.btnOferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_Oferta.class);
                intent.putExtra("punto", destino.getListaPuntos().get(position).getNombreLugar());
                context.startActivity(intent);
            }
        });

    }


    private void onClickButton(final ExpandableLayout expandableLayout) {
        expandableLayout.toggle();
    }

    private Drawable setImageView(String punto){
        Drawable res = null;
        try {
            String puntoSinEspacio = punto.replace("-","").replace("ó","o").replace(" ", "").toLowerCase();
            String uri = "@drawable/" + puntoSinEspacio;

            int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());

            res = context.getResources().getDrawable(imageResource);

        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public int getItemCount() {
        return destino.getListaPuntos().size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtNombreProducto;
        public TextView txtDescProducto;
        public Button btnOferta;
        public RelativeLayout rlConoce;

        public MyViewHolder(View v) {
            super(v);
            txtNombreProducto = (TextView) v.findViewById(R.id.nombreProducto);
            txtDescProducto = (TextView) v.findViewById(R.id.descProducto);
            btnOferta = (Button) v.findViewById(R.id.btnOferta);
            rlConoce = (RelativeLayout) v.findViewById(R.id.rlconoce);
        }
    }


}
