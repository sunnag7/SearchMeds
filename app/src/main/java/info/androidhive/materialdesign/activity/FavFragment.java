package info.androidhive.materialdesign.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.adapter.CardAdapterFavourite;
import info.androidhive.materialdesign.database.Database;
import info.androidhive.materialdesign.model.Vendor;

public class FavFragment extends Fragment {

    Database db;
    ArrayList<Vendor> aVenArr;
    RelativeLayout unAvail;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    CardAdapterFavourite mAdapterFavourite;

    public FavFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);

        getActivity().setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rvSearchRecycle);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        unAvail = (RelativeLayout) rootView.findViewById(R.id.unavail);

        geFav();
        // Inflate the layout for this fragment
        return rootView;
    }


    public  void geFav()
    {
        db = new Database(getActivity());
        aVenArr = new ArrayList<Vendor>();

        aVenArr = db.getAllFavourite_Vendors();

        if(aVenArr.size() != 0){
            unAvail.setVisibility(View.INVISIBLE );
            mAdapterFavourite = new CardAdapterFavourite(getActivity(),aVenArr);
            mRecyclerView.setAdapter(mAdapterFavourite);
        }
        else {
            unAvail.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        geFav();
        super.onResume();
    }
}