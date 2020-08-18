package com.hcmus.tkpm31_project.Component.habitHome;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hcmus.tkpm31_project.Adapter.HabitRecycleViewAdapter;
import com.hcmus.tkpm31_project.Object.Habit;
import com.hcmus.tkpm31_project.R;

import java.util.ArrayList;
import java.util.List;

public class HabitFragment extends Fragment implements HabitDataRecievedListener {

    private RecyclerView recyclerView;
    private HabitRecycleViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private OnFragmentInteractionListener mListener;
    private HabitHomeActivity mActivity;
    private List<Habit> dataSet;


    public HabitFragment(Context context)
    {
        this.context = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=(HabitHomeActivity)getActivity();
        mActivity.setDataListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_habit, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.cv);
        recyclerView.setHasFixedSize(false);

        layoutManager =new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        dataSet=new ArrayList<>();
        mAdapter = new HabitRecycleViewAdapter(dataSet,context,getActivity());
        recyclerView.setAdapter(mAdapter);


        return rootView;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDataReceived(List<Habit> model,String searchText) {
        dataSet.clear();
       dataSet.addAll(model);
       mAdapter.Filter_name(searchText);
       mActivity.runOnUiThread(new Runnable() {
           @Override
           public void run() {
               mAdapter.notifyDataSetChanged();
           }
       });
    }

    @Override
    public void onFilterData(String newText) {
        mAdapter.Filter_name(newText);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
