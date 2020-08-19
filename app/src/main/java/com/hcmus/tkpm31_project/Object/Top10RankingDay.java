package com.hcmus.tkpm31_project.Object;

import android.content.Context;

import com.hcmus.tkpm31_project.Component.usageHome.PhoneUsageFragment;

import java.util.ArrayList;
import java.util.List;

public class Top10RankingDay extends Top10Ranking {
    @Override
    public ArrayList<PhoneUsage> top10Ranking(Context context) {
        ArrayList<PhoneUsage>listUsageDay= PhoneUsageFragment.listUsageDay(context);
        ArrayList<PhoneUsage>res=new ArrayList<>();
        for(int i=0;i<10;i++)
        {
            PhoneUsage a=listUsageDay.get(i);
            res.add(a);
        }
        return res;
    }
}
