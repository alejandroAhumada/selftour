package cl.selftourhamburger.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

import cl.selftourhamburger.DataBase.DataBaseHelper;
import cl.selftourhamburger.R;
import cl.selftourhamburger.ReciclerAdapter.ReciclerAdapterPantallaPrincipal;
import cl.selftourhamburger.model.pojo.Recorrido;

/**
 * Created by Alejandro on 03-06-2016.
 */
public class FragmentRecorre extends Fragment {

    public FragmentRecorre() {
        // Required empty public constructor
    }

    private CarouselView carouselView;
    private int[] sampleImages = {R.drawable.cajon1, R.drawable.cajon2, R.drawable.cajon3, R.drawable.cajon4};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recorre, container, false);
        carouselView = (CarouselView) view.findViewById(R.id.carouselViewOneFragment);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llmanager = new LinearLayoutManager(this.getContext());
        llmanager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llmanager);

        RecyclerViewHeader header = (RecyclerViewHeader) view.findViewById(R.id.header);
        header.attachTo(recyclerView);

        ReciclerAdapterPantallaPrincipal myAdapter = new ReciclerAdapterPantallaPrincipal(this.getContext(), createListRecorridos());
        recyclerView.setAdapter(myAdapter);

        return view;
    }

    private List<Recorrido> createListRecorridos() {
        DataBaseHelper db = new DataBaseHelper(getContext());
        SQLiteDatabase database = db.getWritableDatabase();

        List<Recorrido> listRecorrido = new ArrayList<>();
        String[] columns = {"NOMBRE_RECORRIDO", "DESCRIPCION_RECORRIDO", "DURACION"};

        Cursor cursor = database.query("puntos_de_recorridos", columns, null, null, "NOMBRE_RECORRIDO", null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Recorrido recorrido = new Recorrido();

                recorrido.setNombreRecorrido(cursor.getString(cursor.getColumnIndex("NOMBRE_RECORRIDO")));
                recorrido.setDescripcionRecorrido(cursor.getString(cursor.getColumnIndex("DESCRIPCION_RECORRIDO")));
                recorrido.setDuracion(cursor.getString(cursor.getColumnIndex("DURACION")));

                listRecorrido.add(recorrido);

            }
        }

        return listRecorrido;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {

            imageView.setImageResource(sampleImages[position]);
        }
    };

}