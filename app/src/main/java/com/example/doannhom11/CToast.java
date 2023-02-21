package com.example.doannhom11;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CToast {

    //info
    public static void i(Context mContext, String Message, int Duration) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.toast_layout, null);
        ((ImageView) view.findViewById(R.id.toast_icon)).setImageResource(R.drawable.ic_baseline_info_24);
        ((TextView) view.findViewById(R.id.toast_message)).setText(Message);

        Toast toast = new Toast(mContext);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 70);
        toast.setDuration(Duration);

        toast.show();
    }

    //warning
    public static void w(Context mContext, String Message, int Duration)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.toast_layout, null);
        ((ImageView)view.findViewById(R.id.toast_icon)).setImageResource(R.drawable.ic_baseline_warning_24);
        ((TextView)view.findViewById(R.id.toast_message)).setText(Message);

        Toast toast = new Toast(mContext);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 70);
        toast.setDuration(Duration);

        toast.show();
    }

    //error
    public static void e(Context mContext, String Message, int Duration)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.toast_layout, null);
        ((ImageView)view.findViewById(R.id.toast_icon)).setImageResource(R.drawable.ic_baseline_error_24);
        ((TextView)view.findViewById(R.id.toast_message)).setText(Message);

        Toast toast = new Toast(mContext);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 70);
        toast.setDuration(Duration);

        toast.show();
    }
}

