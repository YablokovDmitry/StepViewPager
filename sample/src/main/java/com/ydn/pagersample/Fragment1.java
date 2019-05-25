package com.ydn.pagersample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Fragment1 extends Fragment {
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment1, container, false);
        listView = rootView.findViewById(R.id.list);

        String[] values = new String[] { "Android List Item 0",
                "Android List Item 1",
                "Android List Item 2",
                "Android List Item 3",
                "Android List Item 4",
                "Android List Item 5",
                "Android List Item 6",
                "Android List Item 7"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        listView.setAdapter(adapter);
        return rootView;
    }
}
