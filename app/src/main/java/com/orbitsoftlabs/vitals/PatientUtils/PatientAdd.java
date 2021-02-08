package com.orbitsoftlabs.vitals.PatientUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orbitsoftlabs.vitals.HelperUtil;
import com.orbitsoftlabs.vitals.HomeUtils.HomeConstituents;
import com.orbitsoftlabs.vitals.HomeUtils.HomeHealthWorker;
import com.orbitsoftlabs.vitals.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.orbitsoftlabs.vitals.HelperUtil.firestoreDB;

public class PatientAdd extends AppCompatActivity {

    private String patientFName, patientLName, patientAddress, patientAge, patientContactNo, dateDiagnosed,
                   season,monthDiagnosed,patientBirthday,patientBarangay, patientDisease, patientMedications;

    //Add another TextInputLayout (tl_address)
    private TextInputLayout tl_patientFName,tl_patientLName, tl_patientAddress, tl_patientContactNo;

    private MaterialButton btn_bday, btn_barangay, btn_finish, btn_diseases, btn_medications;

    private TextView tv_age, tv_date_diagnosed;
    private ArrayList<String> disease, barangay, medications;
    private ArrayAdapter<String> adapter_barangay ;

    private GridListAdapter adapter_diseases, adapter_medication;
    private int mYear, mMonth, mDay;

    private String monthName;
    private String last_activity;
    private Class aClass;

    private BottomSheetBehavior sheetBehavior_diseases,sheetBehavior_barangay, sheetBehavior_medications;
    private RelativeLayout bottom_sheet_diseases,bottom_sheet_medications,bottom_sheet_barangay, bottom_sheet_overlay;

    private ListView lv_diseases, lv_medications, lv_barangay;
    private boolean bottom_sheet_behavior = false;

    private ExtendedFloatingActionButton fab_done_disease, fab_done_medication, fab_done_barangay;

    private ArrayList<String> diseasesToUpload, medicationsToUpload;
    private StringBuilder stringBuilder;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_add);

        tl_patientFName = findViewById(R.id.tl_fName);
        tl_patientLName = findViewById(R.id.tl_lName);
        tl_patientAddress = findViewById(R.id.tl_address);
        tl_patientContactNo = findViewById(R.id.tl_contact_no);

        btn_barangay = findViewById(R.id.btn_barangay);
        btn_bday = findViewById(R.id.btn_birthday);
        btn_finish = findViewById(R.id.btn_finish);
        btn_diseases = findViewById(R.id.btn_diseases);
        btn_medications = findViewById(R.id.btn_medications);
        //declare tl_disease

        bottom_sheet_overlay = findViewById(R.id.bottom_sheet_overlay);

        tv_age = findViewById(R.id.tv_age);
        tv_date_diagnosed = findViewById(R.id.tv_checkup_date);

        fab_done_disease = findViewById(R.id.fab_done_diseases);
        fab_done_barangay = findViewById(R.id.fab_done_barangay);
        fab_done_medication = findViewById(R.id.fab_done_medications);

        bottom_sheet_diseases = findViewById(R.id.bottom_sheet_diseases);
        bottom_sheet_medications = findViewById(R.id.bottom_sheet_medications);
        bottom_sheet_barangay = findViewById(R.id.bottom_sheet_barangay);

        sheetBehavior_diseases = BottomSheetBehavior.from(bottom_sheet_diseases);
        sheetBehavior_medications = BottomSheetBehavior.from(bottom_sheet_medications);
        sheetBehavior_barangay = BottomSheetBehavior.from(bottom_sheet_barangay);

        lv_diseases = findViewById(R.id.list_view_diseases);
        lv_medications = findViewById(R.id.list_view_medications);
        lv_barangay = findViewById(R.id.list_view_barangay);

        disease = new ArrayList<>();
        barangay = new ArrayList<>();
        medications = new ArrayList<>();

        diseasesToUpload = new ArrayList<>();
        medicationsToUpload = new ArrayList<>();

        sheetBehavior_diseases.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehavior_medications.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehavior_barangay.setState(BottomSheetBehavior.STATE_HIDDEN);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        monthName = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH );

        tv_date_diagnosed.setText(monthName+"/"+mDay+"/"+mYear);

        btn_bday.setOnClickListener(v -> {
            selectBirthday();
        });

        checkSeason();

        //function for save
        btn_finish.setOnClickListener(v -> checkFields());

        //function for select disease
        //remove this function
        btn_diseases.setOnClickListener(v -> {
             new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sheetBehavior_diseases.setState(BottomSheetBehavior.STATE_EXPANDED);
                    adapter_diseases = new GridListAdapter(PatientAdd.this, disease, true);
                    lv_diseases.setAdapter(adapter_diseases);
                    listDiseases();
                }
            }, 300);

        });

        btn_barangay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior_barangay.setState(BottomSheetBehavior.STATE_EXPANDED);
                adapter_barangay = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, barangay);
                lv_barangay.setAdapter(adapter_barangay);
                listBarangay();
            }
        });

        btn_medications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior_medications.setState(BottomSheetBehavior.STATE_EXPANDED);
                adapter_medication = new GridListAdapter(PatientAdd.this, medications, true);
                lv_medications.setAdapter(adapter_medication);
                listMedications();
            }
        });

        diseasesToUpload.clear();
        medicationsToUpload.clear();

        fab_done_disease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray selectedRows = adapter_diseases.getSelectedIds();//Get the selected ids from adapter
                //Check if item is selected or not via size
                if (selectedRows.size() > 0) {
                    stringBuilder = new StringBuilder();
                    //Loop to all the selected rows array
                    for (int i = 0; i < selectedRows.size(); i++) {

                        //Check if selected rows have value i.e. checked item
                        if (selectedRows.valueAt(i)) {

                            //Get the checked item text from array list by getting keyAt method of selectedRowsarray
                            patientDisease = disease.get(selectedRows.keyAt(i));

                            //append the row label text
                            stringBuilder.append(patientDisease + ", ");


                        }
                        diseasesToUpload.add(patientDisease);
                    }
                    sheetBehavior_diseases.setState(BottomSheetBehavior.STATE_HIDDEN);
                    Toast.makeText(PatientAdd.this, "Selected Diseases\n" + stringBuilder.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });

        fab_done_medication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray selectedRows = adapter_medication.getSelectedIds();//Get the selected ids from adapter
                //Check if item is selected or not via size
                if (selectedRows.size() > 0) {
                    stringBuilder = new StringBuilder();
                    //Loop to all the selected rows array
                    for (int i = 0; i < selectedRows.size(); i++) {

                        //Check if selected rows have value i.e. checked item
                        if (selectedRows.valueAt(i)) {

                            //Get the checked item text from array list by getting keyAt method of selectedRowsarray
                            patientMedications = medications.get(selectedRows.keyAt(i));

                            //append the row label text
                            stringBuilder.append(patientMedications + ", ");


                        }
                        medicationsToUpload.add(patientMedications);
                    }
                    sheetBehavior_medications.setState(BottomSheetBehavior.STATE_HIDDEN);
                    Toast.makeText(PatientAdd.this, "Selected Medications\n" + stringBuilder.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });

        lv_barangay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                patientBarangay = lv_barangay.getItemAtPosition(i).toString();

                Toast.makeText(PatientAdd.this, patientBarangay, Toast.LENGTH_LONG).show();
                //adapter.dismiss(); // If you want to close the adapter
                sheetBehavior_barangay.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        try {
            Intent i = getIntent();
            last_activity = i.getStringExtra("activity");
            Log.d("Last activity", last_activity+"");

            if (last_activity.contains("Worker")){
                aClass = HomeHealthWorker.class;
            }   else {
                aClass = HomeConstituents.class;
            }
        }catch (Exception e){

        }

        sheetBehavior_diseases.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {

                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    //bottomSheetHeading.setText(getString(R.string.text_collapse_me));
                    //sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                    bottom_sheet_overlay.setVisibility(View.VISIBLE);
                    HelperUtil.fadeIn(bottom_sheet_overlay,PatientAdd.this);

                } else {

                    bottom_sheet_overlay.setVisibility(View.INVISIBLE);
                    HelperUtil.fadeOut(bottom_sheet_overlay,PatientAdd.this);
                    //sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    //bottomSheetHeading.setText(getString(R.string.text_expand_me));
                }

                // Check Logs to see how bottom sheets behaves
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        bottom_sheet_overlay.setVisibility(View.INVISIBLE);
                        HelperUtil.fadeOut(bottom_sheet_overlay,PatientAdd.this);
                        Log.e("Bottom Sheet Behaviour", "STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.e("Bottom Sheet Behaviour", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        bottom_sheet_overlay.setVisibility(View.VISIBLE);
                        HelperUtil.fadeIn(bottom_sheet_overlay,PatientAdd.this);
                        Log.e("Bottom Sheet Behaviour", "STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        bottom_sheet_overlay.setVisibility(View.INVISIBLE);
                        HelperUtil.fadeOut(bottom_sheet_overlay,PatientAdd.this);
                        Log.e("Bottom Sheet Behaviour", "STATE_HIDDEN");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.e("Bottom Sheet Behaviour", "STATE_SETTLING");
                        break;
                }
            }


            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });

        sheetBehavior_medications.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {

                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    //bottomSheetHeading.setText(getString(R.string.text_collapse_me));
                    //sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                    bottom_sheet_overlay.setVisibility(View.VISIBLE);
                    HelperUtil.fadeIn(bottom_sheet_overlay,PatientAdd.this);

                } else {

                    bottom_sheet_overlay.setVisibility(View.INVISIBLE);
                    HelperUtil.fadeOut(bottom_sheet_overlay,PatientAdd.this);
                    //sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    //bottomSheetHeading.setText(getString(R.string.text_expand_me));
                }

                // Check Logs to see how bottom sheets behaves
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        bottom_sheet_overlay.setVisibility(View.INVISIBLE);
                        HelperUtil.fadeOut(bottom_sheet_overlay,PatientAdd.this);
                        Log.e("Bottom Sheet Behaviour", "STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.e("Bottom Sheet Behaviour", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        bottom_sheet_overlay.setVisibility(View.VISIBLE);
                        HelperUtil.fadeIn(bottom_sheet_overlay,PatientAdd.this);
                        Log.e("Bottom Sheet Behaviour", "STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        bottom_sheet_overlay.setVisibility(View.INVISIBLE);
                        HelperUtil.fadeOut(bottom_sheet_overlay,PatientAdd.this);
                        Log.e("Bottom Sheet Behaviour", "STATE_HIDDEN");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.e("Bottom Sheet Behaviour", "STATE_SETTLING");
                        break;
                }
            }


            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });

        sheetBehavior_barangay.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {

                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    //bottomSheetHeading.setText(getString(R.string.text_collapse_me));
                    //sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                    bottom_sheet_overlay.setVisibility(View.VISIBLE);
                    HelperUtil.fadeIn(bottom_sheet_overlay,PatientAdd.this);

                } else {

                    bottom_sheet_overlay.setVisibility(View.INVISIBLE);
                    HelperUtil.fadeOut(bottom_sheet_overlay,PatientAdd.this);
                    //sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    //bottomSheetHeading.setText(getString(R.string.text_expand_me));
                }

                // Check Logs to see how bottom sheets behaves
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        bottom_sheet_overlay.setVisibility(View.INVISIBLE);
                        HelperUtil.fadeOut(bottom_sheet_overlay,PatientAdd.this);
                        Log.e("Bottom Sheet Behaviour", "STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.e("Bottom Sheet Behaviour", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        bottom_sheet_overlay.setVisibility(View.VISIBLE);
                        HelperUtil.fadeIn(bottom_sheet_overlay,PatientAdd.this);
                        Log.e("Bottom Sheet Behaviour", "STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        bottom_sheet_overlay.setVisibility(View.INVISIBLE);
                        HelperUtil.fadeOut(bottom_sheet_overlay,PatientAdd.this);
                        Log.e("Bottom Sheet Behaviour", "STATE_HIDDEN");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.e("Bottom Sheet Behaviour", "STATE_SETTLING");
                        break;
                }
            }


            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });
    }

    private void uploadPatientsInfo(){
        patientFName = Objects.requireNonNull(tl_patientFName.getEditText()).getText().toString();
        patientLName = Objects.requireNonNull(tl_patientLName.getEditText()).getText().toString();
        patientAddress = Objects.requireNonNull(tl_patientAddress.getEditText()).getText().toString();
        patientContactNo = Objects.requireNonNull(tl_patientContactNo.getEditText()).getText().toString();

        //patientDisease = spinner_disease.getSelectedItem().toString();
        patientBirthday = btn_bday.getHint().toString();
        //patientBarangay = spinner_barangay.getSelectedItem().toString();
        patientAge = tv_age.getText().toString();
        dateDiagnosed = tv_date_diagnosed.getText().toString();

        monthDiagnosed = monthName;

        String phone_no = patientContactNo+"";

        checkSeason();

        for (int i=0; i<diseasesToUpload.size();i++){
            String diseaseId = diseasesToUpload.get(i).replace(" ","");

            // Add a new document with a generated ID
            String docDate = dateDiagnosed.replace("/","");
            String pDocID = patientFName+patientLName+docDate+"";
            String final_p_doc = pDocID.replace(" ","");
            //
            Map<String, Object> patient_info = new HashMap<>();
            patient_info.put("patientFName", patientFName);
            patient_info.put("patientLName", patientLName);
            patient_info.put("patientAddress", patientAddress);
            patient_info.put("patientContactNo", phone_no);
            //patient_info.put("patientDisease", patientDisease);
            patient_info.put("patientBirthday", patientBirthday);
            patient_info.put("patientBarangay", patientBarangay);
            patient_info.put("dateDiagnosed", dateDiagnosed);
            patient_info.put("monthDiagnosed", monthDiagnosed);
            patient_info.put("seasonDiagnosed", season);
            patient_info.put("patientAge", patientAge);
            patient_info.put("pID", final_p_doc+"");

            firestoreDB.collection("diseases")
                    .document(diseaseId)
                    .collection("cases_list")
                    .document(final_p_doc)
                    .set(patient_info)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });

            Map<String, Object> patient_diseases = new HashMap<>();
            patient_diseases.put("diseaseName", diseasesToUpload);
            firestoreDB.collection("diseases")
                    .document(diseaseId)
                    .collection("cases_list")
                    .document(final_p_doc)
                    .collection("Diseases")
                    .document("diseases_list")
                    .set(patient_diseases)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });

            String pDocID1 = patientFName+patientLName+docDate+"";
            String final_p_doc1 = pDocID1.replace(" ","");
            Map<String, Object> patient_info1 = new HashMap<>();
            //patient_info1.put("patientFName", patientFName);
            //patient_info1.put("patientLName", patientLName);
            //patient_info1.put("pID", final_p_doc1+"");

            patient_info1.put("patientFName", patientFName);
            patient_info1.put("patientLName", patientLName);
            patient_info1.put("patientAddress", patientAddress);
            patient_info1.put("patientContactNo", phone_no);
            //patient_info.put("patientDisease", patientDisease);
            patient_info1.put("patientBirthday", patientBirthday);
            patient_info1.put("patientBarangay", patientBarangay);
            patient_info1.put("dateDiagnosed", dateDiagnosed);
            patient_info1.put("monthDiagnosed", monthDiagnosed);
            patient_info1.put("seasonDiagnosed", season);
            patient_info1.put("patientAge", patientAge);
            patient_info1.put("patientDisease", diseasesToUpload);
            patient_info1.put("patientMedications", medicationsToUpload);
            patient_info1.put("pID", final_p_doc1+"");
            firestoreDB.collection("patient_lists")
                    .document(final_p_doc1)
                    .set(patient_info1)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Snackbar.make(btn_finish, "Successful",Snackbar.LENGTH_SHORT).show();
                            HelperUtil.backNavigationFinish(PatientAdd.this, PatientActivity.class);
                        }
                    });
        }

        for (int s = 0; s<medicationsToUpload.size()-1; s++){
            String diseaseId = diseasesToUpload.get(s).replace(" ","");
            String docDate = dateDiagnosed.replace("/","");
            String pDocID = patientFName+patientLName+docDate+"";
            String final_p_doc = pDocID.replace(" ","");
            Map<String, Object> patient_medication = new HashMap<>();
            patient_medication.put("medName", medicationsToUpload);
            firestoreDB.collection("diseases")
                    .document(diseaseId)
                    .collection("cases_list")
                    .document(final_p_doc)
                    .collection("Medications")
                    .document("medication_list")
                    .set(patient_medication)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
        }

    }

    private void checkFields(){
        patientFName = Objects.requireNonNull(tl_patientFName.getEditText()).getText().toString();
        patientLName = Objects.requireNonNull(tl_patientLName.getEditText()).getText().toString();
        patientAddress = Objects.requireNonNull(tl_patientAddress.getEditText()).getText().toString();
        patientContactNo = Objects.requireNonNull(tl_patientContactNo.getEditText()).getText().toString();

        //patientDisease = spinner_disease.getSelectedItem().toString();
        patientBirthday = btn_bday.getHint().toString();
        //patientBarangay = spinner_barangay.getSelectedItem().toString();
        patientAge = tv_age.getText().toString();
        dateDiagnosed = tv_date_diagnosed.getText().toString();

        if (patientFName.equals("")||patientLName.equals("")||patientAge.equals("Age")||patientBarangay == null||patientMedications.equals("")||
        patientBirthday.equals("Select Birthday")||patientDisease.equals("")||patientContactNo.equals("")||patientAddress.equals("")){
            Snackbar.make(btn_finish,"Fields must not be empty.", Snackbar.LENGTH_SHORT).show();
        }else{
            uploadPatientsInfo();
            Log.d("Status", "Uploading...");
        }

        //Log.d("Patient barangay", patientBarangay+"");
        //Log.d("Patient diseases", stringBuilder.toString()+"");

        //Log.d("Diseases to upload", diseasesToUpload.toString()+"");
        //diseasesToUpload.clear();
    }

    private void checkSeason(){
        if (monthName.equals("June")||monthName.contains("July")||
                monthName.contains("August")||monthName.contains("September")||monthName.contains("October")){
            season = "Wet";
        } else if(monthName.contains("November")||monthName.contains("December")||
                monthName.contains("January")||monthName.contains("February")||monthName.contains("March")
                ||monthName.contains("April")||monthName.contains("May")) {
            season = "Dry";
        }

    }

    private void selectBirthday(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {

                    String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                    btn_bday.setHint(date);
                    //Objects.requireNonNull(bday.getEditText()).setText(date);

                    String final_age = String.valueOf(mYear - year);

                    tv_age.setText(final_age+" years old");
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    //method for listing the diseases
    private void listDiseases(){
        //disease.add("Select Disease");
        disease.clear();
        firestoreDB.collection("diseases_lists")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                String diseases = documentSnapshot.getString("diseaseName");
                                disease.add(diseases);
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

    private void listMedications(){
        //disease.add("Select Disease");
        medications.clear();
        firestoreDB.collection("med_solutions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                String meds = documentSnapshot.getString("medName");
                                medications.add(meds);
                            }
                            adapter_medication.notifyDataSetChanged();
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

    private void listBarangay(){
        barangay.clear();
        firestoreDB.collection("barangay_lists")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                String diseases = documentSnapshot.getString("barangayName");
                                barangay.add(diseases);
                            }
                            adapter_barangay.notifyDataSetChanged();
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
    public void onBackPressed(){
        super.onBackPressed();
        HelperUtil.backNavigationFinish(this,PatientActivity.class);
    }
}
