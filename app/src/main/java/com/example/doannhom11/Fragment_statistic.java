package com.example.doannhom11;


import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_statistic#newInstance} factory method to
 * create an instance of this fragment.
 */

public class Fragment_statistic extends Fragment {
    private View mview;
    SimpleDateFormat dateFormat;
    BarChart barChart;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_statistic() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_statistic.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_statistic newInstance(String param1, String param2) {
        Fragment_statistic fragment = new Fragment_statistic();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView sumtv, avgtv, peaktv;
    RadioGroup radios;

    private String[] MOY = new String[]{"Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12",},
            WEEK = new String[] {"Tuần 1", "Tuần 2", "Tuần 3", "Tuần 4" };

    FirebaseAuth mAuth;
    DocumentReference db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mview = inflater.inflate(R.layout.fragment_statistic, container, false);

        // setup chart
        barChart = mview.findViewById(R.id.barChart);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularity(1.0f);
        barChart.getXAxis().setLabelRotationAngle(-45);
        barChart.getXAxis().setTextSize(10);
        barChart.getXAxis().setCenterAxisLabels(false);
        barChart.getXAxis().setTypeface(Typeface.DEFAULT_BOLD);
        barChart.getAxisLeft().setTextSize(10);
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setAxisMinimum(0);
        barChart.getAxisLeft().setSpaceBottom(10);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.getAxisLeft().setTypeface(Typeface.DEFAULT_BOLD);
        barChart.setPinchZoom(false);
        barChart.setVisibleXRangeMinimum(10);
        barChart.getLegend().setEnabled(false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance().document("CUAHANG/" + mAuth.getUid());

        sumtv = (TextView) mview.findViewById(R.id.summarytv);
        avgtv = (TextView) mview.findViewById(R.id.avgtv);
        peaktv = (TextView) mview.findViewById(R.id.peaktv);

        radios = (RadioGroup)mview.findViewById(R.id.radioGr);


        radios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

            }
        });

        ((RadioButton)radios.getChildAt(0)).setChecked(true);
        Get1WeekPirorData();

        radios.setOnCheckedChangeListener((radioGroup, id) -> {
            switch (id)
            {
                case R.id.one_week:
                    Get1WeekPirorData();
                    break;
                case R.id.four_week:
                    GetFourPirorWeekData();
                    break;
                case R.id.lastyear:
                    GetPreviousYearData();
                    break;
                case R.id.ytd:
                    GetYTDData();
                    break;
            }
        });

        return mview;
    }

    private void Get1WeekPirorData() //  7 days
    {
        Calendar instance = Calendar.getInstance();

        // set date to end of previuos day
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.add(Calendar.SECOND, -1);

        //get end tick
        long end = instance.getTimeInMillis() / 1000;

        // set date to start of 7-piror date
        instance.add(Calendar.DAY_OF_YEAR, -6);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);

        // get start tick
        long start = instance.getTimeInMillis() / 1000;

        db.collection("HOADON").whereLessThan("NGHD", end).whereGreaterThanOrEqualTo("NGHD" ,start)
                .orderBy("NGHD", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        ArrayList<BarEntry> entries = new ArrayList<>();
                        long sum = 0, avg, peak;
                        String[] DATE = new String[7];
                        Map<String, Integer> map = new HashMap<>();
                        for (int i = 0; i < 7; i++)
                        {
                            entries.add(new BarEntry(i, 0));
                            String date = instance.get(Calendar.DAY_OF_MONTH) + "/" + (instance.get(Calendar.MONTH)+1);
                            DATE[i] = date;
                            map.put(date, i);
                            instance.add(Calendar.DAY_OF_YEAR, 1);
                        }

                        for (DocumentSnapshot data: task.getResult()) {
                            long value = data.getLong("TRIGIA");
                            sum += value;
                            instance.setTimeInMillis(data.getLong("NGHD") * 1000);
                            int index = (int)map.get(instance.get(Calendar.DAY_OF_MONTH) + "/" + (instance.get(Calendar.MONTH)+1));
                            entries.get(index).setY(entries.get(index).getY() + value);
                        }

                        avg = sum / 7;
                        peak = (long)entries.get(0).getY();

                        for (int i = 1; i < 7; i++)
                            if (peak < (long)entries.get(i).getY())
                                peak = (long)entries.get(i).getY();

                        sumtv.setText(sum + "");

                        avgtv.setText(avg + "");

                        peaktv.setText(peak + "");


                        BarDataSet dataSet = new BarDataSet(entries, "");
                        dataSet.setColors(Color.argb( 200,56, 161, 74)); // red
                        dataSet.setValueTextSize(10f);

                        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(DATE));
                        BarData barData = new BarData(dataSet);
                        barData.setBarWidth(0.5f);

                        barChart.setData(barData);
                        barChart.animateY(3000);
                    }
                });
    }

    private void GetFourPirorWeekData() // 28days
    {

        Calendar instance = Calendar.getInstance();

        // set date to end of previuos month
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        instance.add(Calendar.MILLISECOND, -1);

        // get end tick
        long end = instance.getTimeInMillis() / 1000;

        // minus date to 28 of the previous month
        instance.add(Calendar.DAY_OF_MONTH, -27);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);

        // get start tick
        long start = instance.getTimeInMillis() / 1000;


        db.collection("HOADON").whereLessThan("NGHD", end).whereGreaterThanOrEqualTo("NGHD" ,start)
                .orderBy("NGHD", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        long sum =0, avg, peak;
                        for (int i = 0; i < 4; i++)
                            entries.add(new BarEntry(i, 0));
                        for (DocumentSnapshot data : task.getResult())
                        {
                            instance.setTimeInMillis(data.getLong("NGHD") * 1000);
                            int index = (int) ((instance.getTimeInMillis() / 1000 - start) / 604800);
                            long value = data.getLong("TRIGIA");
                            sum += value;
                            entries.get(index).setY(entries.get(index).getY() + value);
                        }

                        avg = sum / 4;
                        peak = (long)entries.get(0).getY();

                        for (int i = 1; i < 4; i++)
                            if (peak < (long)entries.get(i).getY())
                                peak = (long)entries.get(i).getY();

                        sumtv.setText(sum + "");

                        avgtv.setText(avg + "");

                        peaktv.setText(peak + "");

                        BarDataSet dataSet = new BarDataSet(entries, "");
                        dataSet.setColors(Color.argb( 200,56, 161, 74)); // green
                        dataSet.setValueTextSize(10f);

                        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(WEEK));
                        BarData barData = new BarData(dataSet);
                        barData.setBarWidth(0.5f);

                        barChart.setData(barData);
                        barChart.animateY(3000);
                        barChart.invalidate();
                    }
                });
    }

    private void GetPreviousYearData() // 1 year
    {
        // get proper time
        // set time to start of the year
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.DAY_OF_YEAR, 1);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);

        // back to end of previous year
        instance.add(Calendar.SECOND, -1);

        // get end tick
        long end = instance.getTimeInMillis() / 1000;

        // back to start of previous year
        instance.set(Calendar.MONTH, 1);
        instance.set(Calendar.DAY_OF_MONTH, 1);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);

        // get start tick
        long start = instance.getTimeInMillis() / 1000;

        db.collection("HOADON").whereLessThan("NGHD", end).whereGreaterThanOrEqualTo("NGHD" ,start)
                .orderBy("NGHD", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        long sum = 0, peak, avg;
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        for (int i = 0; i < 12; i++)
                            entries.add(new BarEntry(i ,0));

                        for (DocumentSnapshot data : task.getResult())
                        {
                            instance.setTimeInMillis(data.getLong("NGHD") * 1000);
                            int index = instance.get(Calendar.MONTH);
                            long value = data.getLong("TRIGIA");
                            sum += value;
                            entries.get(index).setY(entries.get(index).getY() + value);
                        }

                        avg = sum / 12;
                        peak = (long)entries.get(0).getY();

                        for (int i = 1; i < 12; i++)
                            if (peak < (long)entries.get(i).getY())
                                peak = (long)entries.get(i).getY();

                        sumtv.setText(sum + "");

                        avgtv.setText(avg + "");

                        peaktv.setText(peak + "");

                        BarDataSet dataSet = new BarDataSet(entries, "");
                        dataSet.setColors(Color.argb( 200,56, 161, 74)); // green
                        dataSet.setValueTextSize(10f);

                        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(MOY));
                        BarData barData = new BarData(dataSet);
                        barData.setBarWidth(0.5f);
                        barChart.setData(barData);
                        barChart.animateY(3000);
                        barChart.invalidate();
                    }
                });
    }

    private void GetYTDData() // this year
    {
        Calendar instance = Calendar.getInstance();

        if (instance.get(Calendar.MONTH) < 1)
        {
            CustomToast.i(getActivity(), "No Data!", Toast.LENGTH_SHORT);
            return;
        }

        instance.set(Calendar.DAY_OF_MONTH, 1);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);

        final int maxMonth = instance.get(Calendar.MONTH);

        // back to end of previous month
        instance.add(Calendar.SECOND, -1);

        //get start tick
        long end = instance.getTimeInMillis() / 1000;

        instance.set(Calendar.MONTH, 0);
        instance.set(Calendar.DAY_OF_MONTH, 1);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);

        long start = instance.getTimeInMillis() / 1000;

        db.collection("HOADON").whereLessThanOrEqualTo("NGHD", end).whereGreaterThanOrEqualTo("NGHD", start)
                .orderBy("NGHD", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            ArrayList<BarEntry> entries = new ArrayList<>();
                            String[] MONTH = new String[maxMonth];
                            for (int i = 0; i < maxMonth; i++) {
                                entries.add(new BarEntry(i ,0));
                                MONTH[i] = MOY[i];
                            }

                            long sum = 0, peak, avg;
                            for (DocumentSnapshot data : task.getResult())
                            {
                                instance.setTimeInMillis( data.getLong("NGHD") * 1000);
                                int index = instance.get(Calendar.MONTH);
                                long value = data.getLong("TRIGIA");
                                sum += value;
                                entries.get(index).setY(entries.get(index).getY() + value);
                            }

                            avg = sum / maxMonth;
                            peak = (long)entries.get(0).getY();

                            for (int i = 1; i < maxMonth; i++)
                                if (peak < (long)entries.get(i).getY())
                                    peak = (long)entries.get(i).getY();

                            sumtv.setText(sum + "");

                            avgtv.setText(avg + "");

                            peaktv.setText(peak + "");

                            BarDataSet dataSet = new BarDataSet(entries, "");
                            dataSet.setColors(Color.argb( 200,56, 161, 74)); // green
                            dataSet.setValueTextSize(10f);

                            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(MONTH));
                            BarData barData = new BarData(dataSet);
                            barData.setBarWidth(0.5f);
                            barChart.setData(barData);
                            barChart.animateY(3000);
                            barChart.invalidate();
                        }
                        else
                            CustomToast.e(getActivity(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT);
                    }
                });
    }
}