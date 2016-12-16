package cl.selftourhamburger.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cl.selftourhamburger.DataBase.DataBaseHelper;
import cl.selftourhamburger.R;
import cl.selftourhamburger.ReciclerAdapter.ReciclerAdapterConoce;
import cl.selftourhamburger.model.pojo.Destino;
import cl.selftourhamburger.model.pojo.Puntos;

/**
 * Created by Alejandro on 03-06-2016.
 */
public class FragmentConoce extends Fragment {

    private SharedPreferences sp;
    private HashMap<String,Boolean> listaOfertas;

    public FragmentConoce() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conoce, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_conoce);

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        ReciclerAdapterConoce myAdapter = new ReciclerAdapterConoce(getContext(), createListPuntoDeInteres());

        recyclerView.setAdapter(myAdapter);

        return view;
    }

    private Destino createListPuntoDeInteres() {
        DataBaseHelper db = new DataBaseHelper(getContext());
        SQLiteDatabase database = db.getWritableDatabase();

        Destino destino = new Destino();
        List<Puntos> listPuntos = new ArrayList<>();
        String[] columns = {"DESCRIPCION_DESTINO",
                "NOMBRE_DESTINO",
                "NOMBRE_LUGAR",
                "DESCRIPCION_LUGAR",
                "LATITUD",
                "LONGITUD",
                "ID_MARCA",
                "NOMBRE_TIPO_MARCA"};

        sp = getContext().getSharedPreferences("cl.selftourhamburger", Context.MODE_MULTI_PROCESS);
        String user = sp.getString("login_user","Vacio");
        String destinoSeleccionado = getDestinoSelecionado(user);

        String where = "NOMBRE_DESTINO = "+"'"+destinoSeleccionado+"'";

        Cursor cursor = database.query("destino_punto_interes", columns, where, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                destino.setNombreDestino(cursor.getString(cursor.getColumnIndex("NOMBRE_DESTINO")));
                destino.setDescripcionDelDestino(cursor.getString(cursor.getColumnIndex("DESCRIPCION_DESTINO")));

                Puntos puntos = new Puntos();
                puntos.setNombreLugar(cursor.getString(cursor.getColumnIndex("NOMBRE_LUGAR")));
                puntos.setDescLugar(cursor.getString(cursor.getColumnIndex("DESCRIPCION_LUGAR")));
                puntos.setLatitud(cursor.getDouble(cursor.getColumnIndex("LATITUD")));
                puntos.setLongitud(cursor.getDouble(cursor.getColumnIndex("LONGITUD")));
                puntos.setIdMarca(cursor.getInt(cursor.getColumnIndex("ID_MARCA")));
                puntos.setNombreTmarca(cursor.getString(cursor.getColumnIndex("NOMBRE_TIPO_MARCA")));

                listPuntos.add(puntos);


            }
        }

        destino.setListaPuntos(listPuntos);

        destino = getOferta(destino);


        return destino;
    }

    private String getDestinoSelecionado(String nombre) {

        DataBaseHelper db = new DataBaseHelper(getContext());
        SQLiteDatabase database = db.getWritableDatabase();
        String destino = null;
        String[] column = {"NOMBRE_DESTINO"};

        String where = "NOMBRE_USUARIO = "+"'"+nombre+"'";

        Cursor cursor = database.query("usuario_destino", column, where, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                destino = cursor.getString(cursor.getColumnIndex("NOMBRE_DESTINO"));
            }
        }

        return destino;
    }

    private Destino getOferta(Destino destino){
        List<Puntos> listPuntos = new ArrayList<>();
        Destino destinoActual = destino;

        for(int i=0; i<destino.getListaPuntos().size(); i++){
            System.out.println("ENTRO A CHECK: "+destino.getListaPuntos().get(i).getNombreLugar());
            listaOfertas = new HashMap<>();

            listaOfertas.put("Muelle Pratt",true);
            listaOfertas.put("La Piojera",true);
            listaOfertas.put("La Sebastiana",true);

            if(listaOfertas.containsKey(destino.getListaPuntos().get(i).getNombreLugar())){
                System.out.println("SE AGREGÓ: "+destino.getListaPuntos().get(i).getNombreLugar());
                Puntos puntos = destino.getListaPuntos().get(i);
                listPuntos.add(puntos);
            }
        }

        destinoActual.setListaPuntos(listPuntos);

        return destinoActual;

    }


}
