package com.orbitsoftlabs.vitals.DiseaseUtils.TabUtils;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.data.Set;
import com.highsoft.highcharts.Common.HIChartsClasses.HIExporting;
import com.highsoft.highcharts.Common.HIChartsClasses.HISeries;
import com.highsoft.highcharts.Common.HIChartsClasses.HIYAxis;
import com.orbitsoftlabs.vitals.DiseaseUtils.DiseaseViewModel;
import com.orbitsoftlabs.vitals.HelperUtil;
import com.orbitsoftlabs.vitals.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.highsoft.highcharts.Common.HIChartsClasses.HIChart;
import com.highsoft.highcharts.Common.HIChartsClasses.HIColumn;
import com.highsoft.highcharts.Common.HIChartsClasses.HIOptions;
import com.highsoft.highcharts.Common.HIChartsClasses.HISubtitle;
import com.highsoft.highcharts.Common.HIChartsClasses.HITitle;
import com.highsoft.highcharts.Common.HIChartsClasses.HIXAxis;
import com.highsoft.highcharts.Core.HIChartView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.orbitsoftlabs.vitals.HelperUtil.firestoreDB;

public class DiseaseStatistics extends Fragment implements SeekBar.OnSeekBarChangeListener,
        AdapterView.OnItemSelectedListener{

    private List<String> options, diseases;
    private ArrayAdapter<String> adapter_diseases, adapter_options ;

    private Spinner spinner_1, spinner_2;

    private List<DataEntry> data;

    private Cartesian vertical;
    private AnyChartView anyChartView;
    private MaterialCardView crd2;

    private String selected_option_diseases;
    private ArrayList<String>  january_size, february_size, march_size, april_size, may_size, june_size, july_size, august_size, september_size, october_size, november_size, december_size;

    private String diseaseID;

    private String [] monthsArray = {"January", "February", "March", "April", "May", "June", "July", "August",
                                     "September", "October", "November", "December","end"};

    private String [] seasonArray = {"Dry","Wet", "end"};
    private String disease_month;

    private StringBuilder jul_size_str;

    private Set set;

    private HorizontalBarChart chart;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;

    private HIChartView chartView;
    private String currentButtonClicked;

    private ArrayList<Integer> columnData;
    private ArrayList<String> months, season;
    private HIOptions hi_options;
    private HIXAxis xaxis;
    private HIColumn column;
             HISeries newSeries;
    private HIYAxis hiyAxis;
    private DiseaseViewModel diseaseViewModel;
    private ArrayList<DiseaseViewModel> diseaseViewModels;
             AsyncTask<?, ?, ?> runningTask;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disease_statistics, container, false);

        spinner_1 = view.findViewById(R.id.spinner1);
        spinner_2 = view.findViewById(R.id.spinner2);

        crd2 = view.findViewById(R.id.crd2);

        this.chartView = view.findViewById(R.id.hc);

        hi_options = new HIOptions();

        newSeries  = new HISeries();

        HIChart chart = new HIChart();
        chart.setType("column");
        hi_options.setChart(chart);
        chart.setHeight(450);


        currentButtonClicked = "Plain";

        xaxis = new HIXAxis();
        hiyAxis = new HIYAxis();
        column = new HIColumn();
        months = new ArrayList<>();
        season = new ArrayList<>();
        columnData = new ArrayList<>();

        months.add("January");



        //xaxis.setReversed(false);
        hiyAxis.setAllowDecimals(false);
        HIExporting exporting = new HIExporting();
        exporting.setEnabled(false);
        hi_options.setExporting(exporting);
        hi_options.setXAxis(new ArrayList<>(Collections.singletonList(xaxis)));
        hi_options.setYAxis(new ArrayList<>(Collections.singleton(hiyAxis)));

        column.setColorByPoint(true);

        columnData.add(0);
        column.setData(columnData);
        column.setShowInLegend(false);
        hi_options.setSeries(new ArrayList<>(Collections.singletonList(column)));

        chartView.setOptions(hi_options);

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        diseases = new ArrayList<>();
        options = new ArrayList<>();
        diseaseViewModels = new ArrayList<>();

        january_size = new ArrayList<>();
        february_size = new ArrayList<>();
        march_size = new ArrayList<>();
        april_size = new ArrayList<>();
        may_size = new ArrayList<>();
        june_size = new ArrayList<>();
        july_size = new ArrayList<>();
        august_size = new ArrayList<>();
        september_size = new ArrayList<>();
        october_size = new ArrayList<>();
        november_size = new ArrayList<>();
        december_size = new ArrayList<>();


        listOptions();
        listDiseases();

        spinner_1.setOnItemSelectedListener(this);


    }



    private void setMonthsDiseaseStats(String diseaseID){

        chartView.refreshDrawableState();
        june_size.clear();
        june_size.clear();
        january_size.clear();
        months.clear();
        columnData.clear();

        chartView.reload();

        int i;
        for(i=0; i<12;i++){
            int finalI = i;
            firestoreDB.collection("diseases")
                    .document(diseaseID)
                    .collection("cases_list")
                    .whereEqualTo("monthDiagnosed", monthsArray[i])
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                Objects.requireNonNull(task.getResult()).size();

                                String month =  monthsArray[finalI];
                                int size = Objects.requireNonNull(task.getResult()).size();

                                months.add(month);

                                ArrayList<String> monthh = new ArrayList<String>(Arrays.asList(monthsArray));

                                xaxis.setCategories(monthh);
                                hiyAxis.setAllowDecimals(false);
                                hi_options.setXAxis(new ArrayList<>(Collections.singletonList(xaxis)));

                                HIExporting exporting = new HIExporting();
                                exporting.setEnabled(false);
                                hi_options.setExporting(exporting);
                                hi_options.setYAxis(new ArrayList<>(Collections.singleton(hiyAxis)));
                                column.setColorByPoint(true);

                                columnData.add(size);
                                column.setData(columnData);
                                column.setShowInLegend(false);
                                hi_options.setSeries(new ArrayList<>(Collections.singletonList(column)));

                                chartView.setOptions(hi_options);
                            }
                        }
                    });
        }
    }

    private void setSeasonDiseaseStats(String diseaseID){

        chartView.refreshDrawableState();
        june_size.clear();
        june_size.clear();
        january_size.clear();
        months.clear();
        columnData.clear();
        chartView.reload();
        int i;
        for(i=0; i<2;i++){
            int finalI = i;
            firestoreDB.collection("diseases")
                    .document(diseaseID)
                    .collection("cases_list")
                    .whereEqualTo("seasonDiagnosed", seasonArray[i])
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                Objects.requireNonNull(task.getResult()).size();

                                String seasons =  seasonArray[finalI];
                                int size = Objects.requireNonNull(task.getResult()).size();

                                season.add(seasons);


                                xaxis.setCategories(season);
                                //xaxis.setReversed(false);
                                hi_options.setXAxis(new ArrayList<>(Collections.singletonList(xaxis)));

                                hiyAxis.setAllowDecimals(false);
                                HIExporting exporting = new HIExporting();
                                exporting.setEnabled(false);
                                hi_options.setExporting(exporting);
                                hi_options.setYAxis(new ArrayList<>(Collections.singleton(hiyAxis)));
                                column.setColorByPoint(true);

                                columnData.add(size);
                                column.setData(columnData);
                                column.setShowInLegend(false);
                                hi_options.setSeries(new ArrayList<>(Collections.singletonList(column)));

                                chartView.setOptions(hi_options);

                            }
                        }
                    });
        }
    }

    private void setDiseaseStats(){

        june_size.clear();
        june_size.clear();
        january_size.clear();
        months.clear();
        columnData.clear();
        chartView.reload();
        int i;
        for(i=0; i<diseases.size();i++){
            int finalI = i;
            firestoreDB.collection("diseases")
                    .document(diseases.get(i))
                    .collection("cases_list")
                    //.whereEqualTo("seasonDiagnosed", diseases.get(i))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot dc : Objects.requireNonNull(task.getResult())){
                                    //Objects.requireNonNull(task.getResult()).size();
                                    diseaseViewModel = dc.toObject(DiseaseViewModel.class);
                                    diseaseViewModels.add(diseaseViewModel);
                                }
                                String season = diseases.get(finalI);
                                int size = Objects.requireNonNull(task.getResult()).size();
                                months.add(season);
                                xaxis.setCategories(months);

                                hi_options.setXAxis(new ArrayList<>(Collections.singletonList(xaxis)));

                                column.setColorByPoint(true);

                                columnData.add(size);
                                column.setData(columnData);
                                column.setShowInLegend(false);
                                hi_options.setSeries(new ArrayList<>(Collections.singletonList(column)));

                                chartView.setOptions(hi_options);

                            }
                        }
                    });
        }
    }

    private void listOptions(){
        options.add("Months");
        options.add("Season");
        //options.add("Diseases");
        adapter_options = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, options);

        adapter_options.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //adapter_diseases = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, barangay);
        spinner_1.setAdapter(adapter_options);
        adapter_options.notifyDataSetChanged();
    }

    private void listDiseases(){

        diseases.clear();
        firestoreDB.collection("diseases_lists")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                String disease = documentSnapshot.getString("diseaseName");
                                diseases.add(disease);
                                adapter_diseases = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, diseases);

                                diseaseID = diseases.get(0);
                                adapter_diseases.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner_2.setAdapter(adapter_diseases);
                            }
                            adapter_diseases.notifyDataSetChanged();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("ErroCode", "Error fetching data.");
                    }
                });

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onResume(){
        super.onResume();

        spinner_1.setOnItemSelectedListener(this);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                HelperUtil.fadeIn(crd2, getActivity());
                //crd2.setVisibility(View.VISIBLE);
                //columnData.notify();

                chartView.setOptions(hi_options);
                spinner_2.setSelected(true);
                if (spinner_2.getSelectedItemPosition() != 0){
                    spinner_2.setSelection(0, true);
                } else {
                    spinner_2.setSelection(1, true);
                }
                spinner_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        selected_option_diseases = spinner_2.getSelectedItem().toString();
                        //selected_option_diseases = spinner_2.getSelectedItem().toString();
                        diseaseID = selected_option_diseases.replace(" ", "");
                        //if (diseaseID.equals("Diabetes")){
                        //data.clear();


                        HITitle title = new HITitle();
                        title.setText(selected_option_diseases);
                        hi_options.setTitle(title);

                        setMonthsDiseaseStats(diseaseID);

                        Log.d("selected", diseaseID+"");


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            case 1:
                HelperUtil.fadeIn(crd2, getActivity());
                //crd2.setVisibility(View.VISIBLE);
                spinner_2.setSelected(true);
                if (spinner_2.getSelectedItemPosition() != 0){
                    spinner_2.setSelection(0, true);
                } else {
                    spinner_2.setSelection(1, true);
                }

                chartView.setOptions(hi_options);
                //columnData.notify();
                spinner_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selected_option_diseases = spinner_2.getSelectedItem().toString();
                        //selected_option_diseases = spinner_2.getSelectedItem().toString();
                        diseaseID = selected_option_diseases.replace(" ", "");
                        //if (diseaseID.equals("Diabetes")){
                        //data.clear();


                        HITitle title = new HITitle();
                        title.setText(selected_option_diseases);
                        hi_options.setTitle(title);


                        setSeasonDiseaseStats(diseaseID);

                        Log.d("selected", diseaseID+"");


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}