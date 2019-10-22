package com.pickupapp.gui.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.pickupapp.R;

import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpaceFragment extends Fragment {
    private CircleImageView imagePerfil;
    private ImageSwitcher imageSwitcher;

    private int[] gallery = { R.drawable.campo
            , R.drawable.splash, R.drawable.soccerproject,
            R.drawable.soccerproject1};

    private int position;

    private static final Integer DURATION = 2500;

    private Timer timer = null;

    public SpaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_space, container, false);
        imageSwitcher = inflate.findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                return new ImageView(getContext());
            }
        });
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                imageSwitcher.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageSwitcher.setImageResource(gallery[position]);
                        position++;
                        if (position == gallery.length) {
                            position = 0;
                        }
                    }
                }, 1000);
            }
        }, 0, DURATION);
        return inflate;
    }
}
