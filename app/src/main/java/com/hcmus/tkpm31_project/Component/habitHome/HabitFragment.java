package com.hcmus.tkpm31_project.Component.habitHome;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hcmus.tkpm31_project.Adapter.HabitRecycleViewAdapter;
import com.hcmus.tkpm31_project.R;

public class HabitFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private OnFragmentInteractionListener mListener;

    public HabitFragment(Context context) {
        this.context = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_habit, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.cv);
        recyclerView.setHasFixedSize(false);

        layoutManager =new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        String[] myDataset ={"abc","def","def","def","def","def","def","def","def","def","def","def","def","def","def","def","def"};

        mAdapter = new HabitRecycleViewAdapter(myDataset);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
