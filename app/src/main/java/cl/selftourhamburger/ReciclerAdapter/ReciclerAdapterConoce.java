package cl.selftourhamburger.ReciclerAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.Utils;

import java.util.List;

import cl.selftourhamburger.R;
import cl.selftourhamburger.model.pojo.Destino;
import cl.selftourhamburger.model.pojo.Puntos;

/**
 * Created by Alejandro on 11-07-2016.
 */
public class ReciclerAdapterConoce extends RecyclerView.Adapter<ReciclerAdapterConoce.MyViewHolder> {

    private List<Puntos> listPuntos;
    private Context context;
    private SparseBooleanArray expandState = new SparseBooleanArray();

    public ReciclerAdapterConoce(Context context, Destino items) {
        this.context = context;
        this.listPuntos = items.getListaPuntos();
        for (int i = 0; i < listPuntos.size(); i++) {
            expandState.append(i, false);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        this.context = parent.getContext();
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.fragment_recycler_view_conoce, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder itemsViewHolder, final int position) {

        final Puntos puntos = listPuntos.get(position);
        itemsViewHolder.txtNombrePuntoDeInteres.setText(puntos.getNombreLugar());
        itemsViewHolder.txtDescripcionPuntoDeInteres.setText(puntos.getDescLugar());
        itemsViewHolder.expandableLayout.setExpanded(expandState.get(position));
        itemsViewHolder.expandableLayout.setInterpolator(Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR));
        itemsViewHolder.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                expandState.put(position, true);
            }

            @Override
            public void onPreClose() {
                expandState.put(position, false);
            }

        });


        itemsViewHolder.txtNombrePuntoDeInteres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onClickButton(itemsViewHolder.expandableLayout);
            }
        });

    }


    private void onClickButton(final ExpandableLayout expandableLayout) {
        expandableLayout.toggle();
    }

    @Override
    public int getItemCount() {
        return listPuntos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtNombrePuntoDeInteres;
        public TextView txtDescripcionPuntoDeInteres;
        public ExpandableLayout expandableLayout;
        public RelativeLayout layoutPrincipal;

        public MyViewHolder(View v) {
            super(v);
            expandableLayout = (ExpandableLayout) v.findViewById(R.id.expandableLayout);
            txtNombrePuntoDeInteres = (TextView) v.findViewById(R.id.txtNombrePuntoDeInteres);
            txtDescripcionPuntoDeInteres = (TextView) v.findViewById(R.id.txtDescripcionPuntoInteres);
            layoutPrincipal = (RelativeLayout) v.findViewById(R.id.layout_principal);
        }
    }


}
