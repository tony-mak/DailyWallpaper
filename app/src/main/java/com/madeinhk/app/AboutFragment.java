package com.madeinhk.app;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.madeinhk.dailywallpaper.R;

public class AboutFragment extends Fragment {


    public AboutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        Button contactButton = (Button) rootView.findViewById(R.id.contact_button);
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "madeinhk.apps@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback about Daily Wallpaper");
                startActivity(Intent.createChooser(emailIntent, null));
            }
        });
        return rootView;
    }
}
