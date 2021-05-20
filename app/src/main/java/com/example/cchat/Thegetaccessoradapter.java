package com.example.cchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Thegetaccessoradapter extends FragmentPagerAdapter {
    public Thegetaccessoradapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "chats";
            case 1:
                return "contact";
            case 2:
                return "groups";
            default:
                return null;
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                chatfragement chatfragementt=new chatfragement();
                return chatfragementt;
            case 1:
                contectfragement contactfragementt=new contectfragement();
                return contactfragementt;
            case 2:
                groupfragement groupsfragementt=new groupfragement();
                return groupsfragementt;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
