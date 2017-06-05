package nstv.rxbinding.listenersFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import nstv.rxbinding.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NormalWayFragment extends Fragment {

    //UI Elements
    private Button button;
    private EditText editText;
    private AutoCompleteTextView autoCompleteTextView;
    private RecyclerView recyclerView;


    public NormalWayFragment() {
        // Required empty public constructor
    }

    public static NormalWayFragment getInstance(){
        return new NormalWayFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_listeners_view, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init(){
        //button = getView().findViewById()
        setViewListeners();
    }

    private void setViewListeners() {
        //Button Listener


        //EditText Listener

        //AutoComplete Listener
    }
}
