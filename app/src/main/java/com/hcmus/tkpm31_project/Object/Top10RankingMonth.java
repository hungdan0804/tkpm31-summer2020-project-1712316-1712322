package com.hcmus.tkpm31_project.Object;

import android.content.Context;

import com.hcmus.tkpm31_project.Component.usageHome.PhoneUsageFragment;

import java.util.ArrayList;
import java.util.List;

public class Top10RankingMonth extends Top10Ranking {
    @Override
    public ArrayList<PhoneUsage> top10Ranking(Context context) {

        ArrayList<PhoneUsage> listUsageMonth= PhoneUsageFragment.listUsageMonth(context);
        ArrayList<PhoneUsage>res=new ArrayList<>();
        for(int i=0;i<10;i++)
        {
            PhoneUsage a=listUsageMonth.get(i);
            a.set_stt(i+1);
            res.add(a);
        }
        return res;
    }
}
