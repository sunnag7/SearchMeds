package info.androidhive.materialdesign.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vstechlab.easyfonts.EasyFonts;

import java.util.ArrayList;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.adapter.CardAdapterNotifications;
import info.androidhive.materialdesign.database.Database;
import info.androidhive.materialdesign.model.Constatnts;
import info.androidhive.materialdesign.model.Notifications;

public class NotificationsFragment extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    CardAdapterNotifications mAdapter;
    ArrayList<Notifications> aNotificationsArr;
    Database db;
    TextView txtV_noNotification;
    RelativeLayout rel_noNotifications;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);

        getActivity().setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        rel_noNotifications = (RelativeLayout) rootView.findViewById(R.id.rel_noNotifications);
        txtV_noNotification = (TextView) rootView.findViewById(R.id.txtV_noNotification);

        txtV_noNotification.setTypeface(EasyFonts.caviarDreams(getContext()));

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rvNotifications);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        db = new Database(getActivity());
        aNotificationsArr = new ArrayList<Notifications>();

        aNotificationsArr =  db.getAllNotifications();

        if(aNotificationsArr.size() != 0){

            mAdapter = new CardAdapterNotifications(getActivity(),aNotificationsArr);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            rel_noNotifications.setVisibility(View.INVISIBLE);

        }
        else {

            rel_noNotifications.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
        }

        return rootView;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
