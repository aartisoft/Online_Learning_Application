package com.himanshumauri.learning;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.himanshumauri.learning.fragments.LongAQ;
import com.himanshumauri.learning.fragments.MCQ;
import com.himanshumauri.learning.fragments.Notes;
import com.himanshumauri.learning.fragments.ShortAQ;

public class SectionPageAdapter extends FragmentPagerAdapter {

    public SectionPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Notes notes = new Notes();
                return notes;
            case 1:
                MCQ mcq = new MCQ();
                return mcq;
            case 2:
                ShortAQ shortAQ = new ShortAQ();
                return shortAQ;
            case 3:
                LongAQ longAQ = new LongAQ();
                return longAQ;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Notes";
            case 1:
                return "MCQ";
            case 2:
                return "Short AQ";
            case 3:
                return "Long AQ";
            default:
                return null;
        }
    }
}
