package cl.selftourhamburger.Listeners;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Alejandro on 12-07-2016.
 */
public class ListenerSpnGenero implements AdapterView.OnItemSelectedListener,View.OnTouchListener {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}