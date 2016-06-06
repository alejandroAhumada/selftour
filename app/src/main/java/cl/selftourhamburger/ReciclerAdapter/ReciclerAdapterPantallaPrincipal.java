package cl.selftourhamburger.ReciclerAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cl.selftourhamburger.R;

/**
 * Created by Alejandro on 06-06-2016.
 */
public class ReciclerAdapterPantallaPrincipal extends RecyclerView.Adapter<ReciclerAdapterPantallaPrincipal.MyViewHolder> {

    private List<String> items;
    private Context context;

    public ReciclerAdapterPantallaPrincipal(Context context, List<String> items) {
        this.context = context;
        this.items= items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder itemsViewHolder, int i) {
        //itemsViewHolder.vTitle.setText(items.get(i));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.fragment_recycler_view_pantalla_principal, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        protected TextView vTitle;

        public MyViewHolder(View v) {
            super(v);
            //vTitle = (TextView) v.findViewById(R.id.title);
        }
    }
}
