package com.hcmus.tkpm31_project.Object;

import android.content.Context;

import com.hcmus.tkpm31_project.Component.usageHome.PhoneUsageFragment;

import java.util.ArrayList;
import java.util.List;

public class Top10RankingDay extends Top10Ranking {
    @Override
    public ArrayList<PhoneUsage> top10Ranking(Context context,long startTime,long endTime) {
        PhoneUsageFragment psf=new PhoneUsageFragment(context);
        ArrayList<PhoneUsage>listUsageDay= psf.getListUsage(startTime, endTime);
        ArrayList<PhoneUsage>res=new ArrayList<>();

        if(listUsageDay.size()>0)
        {
            for(int i=0;i<listUsageDay.size();i++)
            {
                PhoneUsage a=listUsageDay.get(i);
                a.set_stt(i+1);

                res.add(a);
            }
        }


        return res;
    }
}
