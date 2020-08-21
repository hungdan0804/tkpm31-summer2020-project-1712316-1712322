package com.hcmus.tkpm31_project.Object;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public abstract class Top10Ranking {
    public abstract ArrayList<PhoneUsage> top10Ranking(Context context,long startTime,long endTime);
}
