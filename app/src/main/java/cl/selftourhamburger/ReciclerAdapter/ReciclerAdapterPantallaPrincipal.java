package cl.selftourhamburger.ReciclerAdapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cl.selftourhamburger.Activity.Activity_Pantalla_Principal;
import cl.selftourhamburger.Activity.Activity_map;
import cl.selftourhamburger.DataBase.DataBaseHelper;
import cl.selftourhamburger.Fragment.OneFragment;
import cl.selftourhamburger.R;
import cl.selftourhamburger.model.pojo.Puntos;
import cl.selftourhamburger.model.pojo.Recorrido;

/**
 * Created by Alejandro on 06-06-2016.
 */
public class ReciclerAdapterPantallaPrincipal extends RecyclerView.Adapter<ReciclerAdapterPantallaPrincipal.MyViewHolder> {

    private List<Recorrido> items;
    private Context context;

    public ReciclerAdapterPantallaPrincipal(Context context, List<Recorrido> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder itemsViewHolder, final int i) {
        itemsViewHolder.txtDescripcionRecorrido.setText(items.get(i).getDescripcionRecorrido());
        itemsViewHolder.buttonIrMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_map.class);
                Bundle bundle = new Bundle();
                bundle.putString("nomRecorrido", items.get(i).getNombreRecorrido());

                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.fragment_recycler_view_pantalla_principal, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imagenRecorrido;
        public TextView txtDescripcionRecorrido;
        public Button buttonIrMap;

        public MyViewHolder(View v) {
            super(v);
            imagenRecorrido = (ImageView) v.findViewById(R.id.imageRecorrido);
            txtDescripcionRecorrido = (TextView) v.findViewById(R.id.textoresumenRecorrido);
            buttonIrMap = (Button) v.findViewById(R.id.btn_map);
        }
    }
}
