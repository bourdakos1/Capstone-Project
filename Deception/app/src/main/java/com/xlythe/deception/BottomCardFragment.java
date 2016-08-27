package com.xlythe.deception;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Niko on 8/18/16.
 */

public class BottomCardFragment extends Fragment {
    public BottomCardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_card, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        VotePagerAdapter adapterViewPager = new VotePagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);

        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        return view;
    }
}
