package com.example.woofer;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class FOFLayout extends LinearLayout {

    TextView friend, fof;

    public FOFLayout(Context context) {
        super(context);
        setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.weight = (float) 0.5;

        friend = new TextView(context);
        friend.setPadding(20,20,50,20);
        friend.setTextSize(15);
        addView(friend, lp);


        fof = new TextView(context);
        fof.setPadding(20,20,20,20);
        fof.setTextSize(15);

        addView(fof);
    }

    public void populate(JSONObject jo) throws JSONException {
        friend.setText(jo.getString("USERNAME1"));
        fof.setText(jo.getString("USERNAME2"));
    }
}
