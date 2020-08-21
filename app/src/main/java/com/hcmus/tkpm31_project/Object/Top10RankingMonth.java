package com.hcmus.tkpm31_project.Object;

import android.content.Context;

import com.hcmus.tkpm31_project.Component.usageHome.PhoneUsageFragment;

import java.util.ArrayList;
import java.util.List;

public class Top10RankingMonth extends Top10Ranking {
    @Override
    public ArrayList<PhoneUsage> top10Ranking(Context context,long startTime,long endTime) {
        PhoneUsageFragment psf=new PhoneUsageFragment(context);

        ArrayList<PhoneUsage> listUsageMonth= psf.getListUsage(startTime,endTime);
        ArrayList<PhoneUsage>res=new ArrayList<>();
        if(listUsageMonth.size()>0)
        {
            for(int i=0;i<listUsageMonth.size();i++)
            {
                PhoneUsage a=listUsageMonth.get(i);
                a.set_stt(i+1);
                res.add(a);
            }
        }

        return res;
    }
}
