package mymidin.com.mymidin.report;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import mymidin.com.mymidin.R;

public class MonthlyReportFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.report_monthly_fragment,container,false);

        BarChart barChart = view.findViewById(R.id.monthly_report_chart);

        ArrayList<String> labels = new ArrayList<>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f,1563f,labels.get(0)));
        entries.add(new BarEntry(1f,1365f,labels.get(1)));
        entries.add(new BarEntry(2f,956f,labels.get(2)));
        entries.add(new BarEntry(3f,1020f,labels.get(3)));
        entries.add(new BarEntry(4f,1003f,labels.get(4)));

        BarDataSet dataSet = new BarDataSet(entries,"Total Sales");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        BarData data = new BarData(dataSet);
        data.setBarWidth(1.0f);
        barChart.setData(data);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.setFitBars(true);
        barChart.invalidate();

        return view;
    }
}
