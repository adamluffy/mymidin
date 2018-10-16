package mymidin.com.mymidin.report;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import mymidin.com.mymidin.R;

public class WeeklyReportFragment extends Fragment {

    private HorizontalBarChart weeklyReport;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.report_weekly_fragment,container,false);

        weeklyReport = view.findViewById(R.id.weekly_report_chart);

        ArrayList<String> labels = new ArrayList<>();
        labels.add("Week 1");
        labels.add("Week 2");
        labels.add("Week 3");
        labels.add("Week 4");

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f,2000f,labels.get(0)));
        entries.add(new BarEntry(1f,1365f,labels.get(1)));
        entries.add(new BarEntry(2f,1500f,labels.get(2)));
        entries.add(new BarEntry(3f,1020f,labels.get(3)));

        BarDataSet barDataSet = new BarDataSet(entries,"Weekly Report");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.5f);
        weeklyReport.setData(data);
        weeklyReport.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        weeklyReport.setFitBars(true);
        weeklyReport.invalidate();

        return view;
    }
}
