package cl.selftourhamburger.ReciclerAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cl.selftourhamburger.Activity.Activity_map;
import cl.selftourhamburger.R;
import cl.selftourhamburger.model.pojo.Recorrido;

/**
 * Created by Alejandro on 06-06-2016.
 */
public class ReciclerAdapterPantallaPrincipal extends RecyclerView.Adapter<ReciclerAdapterPantallaPrincipal.MyViewHolder> {

    private List<Recorrido> items;
    private Context context;
    protected LocationManager manager;

    public ReciclerAdapterPantallaPrincipal(Context context, List<Recorrido> items) {
        this.context = context;
        this.items = items;
        manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder itemsViewHolder, final int i) {
        Resources resources= context.getResources();
        itemsViewHolder.txtDescripcionRecorrido.setText(items.get(i).getDescripcionRecorrido());
        itemsViewHolder.buttonIrMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean isGPSEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if(isGPSEnabled){
                    Intent intent = new Intent(context, Activity_map.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("nomRecorrido", items.get(i).getNombreRecorrido());

                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }else{
                    Intent gpsOptionsIntent = new Intent(
                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    Toast.makeText(context, "Favor Habilitar GPS", Toast.LENGTH_SHORT).show();
                    context.startActivity(gpsOptionsIntent);
                }

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
        public RelativeLayout buttonIrMap;

        public MyViewHolder(View v) {
            super(v);
            imagenRecorrido = (ImageView) v.findViewById(R.id.imageRecorrido);
            txtDescripcionRecorrido = (TextView) v.findViewById(R.id.textoresumenRecorrido);
            buttonIrMap = (RelativeLayout) v.findViewById(R.id.LytRecycler);
        }
    }
}
