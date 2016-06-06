package cl.selftourhamburger.Fragment;

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

import cl.selftourhamburger.R;
import cl.selftourhamburger.ReciclerAdapter.ReciclerAdapterPantallaPrincipal;

/**
 * Created by Alejandro on 03-06-2016.
 */
public class OneFragment extends Fragment {

    public OneFragment() {
        // Required empty public constructor
    }

    CarouselView carouselView;

    int[] sampleImages = {R.drawable.cajon1, R.drawable.cajon2, R.drawable.cajon3, R.drawable.cajon4};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_one, container, false);
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

        ReciclerAdapterPantallaPrincipal myAdapter = new ReciclerAdapterPantallaPrincipal(this.getContext(), createList());
        recyclerView.setAdapter(myAdapter);

        return view;
    }

    private List<String> createList() {
        List<String> stringList = new ArrayList<>();

        stringList.add("Alejandro");
        stringList.add("Ahumada");
        stringList.add("Rodrigo");
        stringList.add("Ramirez");
        stringList.add("Hector");
        stringList.add("Martinez");
        stringList.add("Carlos");
        stringList.add("Orellana");
        stringList.add("Eduardo");
        stringList.add("Sandoval");
        stringList.add("Motorola");
        stringList.add("G3");
        stringList.add("Taza");
        stringList.add("Casa");
        stringList.add("Perro");
        stringList.add("Gato");
        stringList.add("Pantalla");
        stringList.add("Mouse");
        stringList.add("Teclado");
        stringList.add("CPU");

        return stringList;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {

            imageView.setImageResource(sampleImages[position]);
        }
    };

}