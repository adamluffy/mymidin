package mymidin.com.mymidin.report;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.ProductSold;
import model.Sales;
import mymidin.com.mymidin.R;
import respository.SalesDatabase;
import utilities.DateUtilities;

public class DailyReportFragment extends Fragment {

    private PieChart dailySales;
    private ArrayList<Sales> sales;

    Calendar date = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.report_daily_fragment,container,false);

        dailySales = view.findViewById(R.id.daily_report_chart);

        List<PieEntry> entryList = new ArrayList<>();
        entryList.add(new PieEntry(30f,"IT Product"));
        entryList.add(new PieEntry(12.5f,"Baby Product"));
        entryList.add(new PieEntry(12.5f,"Books"));
        entryList.add(new PieEntry(20f,"Toiletry"));
        entryList.add(new PieEntry(25f,"Cloth"));

        PieDataSet dataSet = new PieDataSet(entryList,"Daily Sales");
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        PieData data = new PieData(dataSet);

        dailySales.setData(data);
        dailySales.invalidate();

        setPieChartData();

        return view;
    }

    private void setPieChartData() {

        sales = new ArrayList<>();

        //get all sales
        SalesDatabase.getSellerSales()
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (QueryDocumentSnapshot q : queryDocumentSnapshots) {

                        Sales s = q.toObject(Sales.class);
                        if(DateUtilities.DateFormat(s.getDate()).equals(DateUtilities.DateFormat(date.getTime()))){

                            //today report
                        }

                    }


                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());

        //calculate total sales in category(within the products array)

    }

}
