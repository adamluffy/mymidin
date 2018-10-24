package mymidin.com.mymidin.report;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import model.Sales;
import mymidin.com.mymidin.R;
import respository.SalesDatabase;
import utilities.DateUtilities;

public class DailyReportFragment extends Fragment {

    private PieChart dailySales;

    Calendar date = Calendar.getInstance();

    private ArrayList<String> labels = new ArrayList<>();
    private ArrayList<Double> datas = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.report_daily_fragment,container,false);

        dailySales = view.findViewById(R.id.daily_report_chart);

        setPieChartData();

        return view;
    }

    private void setPieChartData() {

        List<Task<ArrayList<String>>> tasks = new ArrayList<>();

        SalesDatabase.getSellerSales()
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (DocumentSnapshot doc:queryDocumentSnapshots){
                        Sales sales = doc.toObject(Sales.class);
                        if(DateUtilities.compareDate(date.getTime(),sales.getDate())){
                            tasks.add(SalesDatabase.getTypes(doc.getId()));
                        }
                    }


                });

    }


}
