package com.orbitsoftlabs.vitals.PatientUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.orbitsoftlabs.vitals.HelperUtil;
import com.orbitsoftlabs.vitals.PatientUtils.DiseasesMedicationUtils.PatientsDiseasesRecyclerViewAdapter;
import com.orbitsoftlabs.vitals.PatientUtils.DiseasesMedicationUtils.PatientsMedicationsRecyclerViewAdapter;
import com.orbitsoftlabs.vitals.PatientUtils.PatientListUtils.Patients;
import com.orbitsoftlabs.vitals.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import crl.android.pdfwriter.PDFWriter;
import crl.android.pdfwriter.PaperSize;
import crl.android.pdfwriter.StandardFonts;

import static com.orbitsoftlabs.vitals.HelperUtil.firestoreDB;

public class PatientDetailedView extends AppCompatActivity {

    private TextView tv_patientName, tv_patientAddress, tv_patientAge, tv_patientBarangay,
            tv_patientBirthday, tv_dateDiagnosed, tv_patientPhoneNo;

    private String patientFName, patientLName, patientAddress, patientAge, patientContactNo, dateDiagnosed,
            patientBirthday,patientBarangay, pID;

    private RecyclerView recyclerViewDiseases, recyclerViewMedications;

    private LinearLayoutManager llm_diseases, llm_medications;

    private ArrayList<String> disease_list, medication_list;

    private PatientsDiseasesRecyclerViewAdapter patientsDiseasesRecyclerViewAdapter;

    private PatientsMedicationsRecyclerViewAdapter patientsMedicationsRecyclerViewAdapter;

    private ImageButton call, message;

    private MaterialButton pdf_export;
    private static final int STORAGE_PERMISSION_CODE = 101;
    //add button
    private static final int STORAGE_PERMISSION_CODE1 = 102;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detailed_view);

        tv_patientName = findViewById(R.id.patientName);
        tv_patientAddress = findViewById(R.id.patientAddress);
        tv_patientAge = findViewById(R.id.patientAge);
        tv_patientBarangay = findViewById(R.id.patientBarangay);
        tv_patientBirthday = findViewById(R.id.patientBirthday);
        tv_patientPhoneNo = findViewById(R.id.patientPhoneNo);
        tv_dateDiagnosed = findViewById(R.id.dateDiagnosed);
        pdf_export = findViewById(R.id.pdf_export);
        call = findViewById(R.id.btn_call);
        message = findViewById(R.id.btn_message);
        //declare recently added button in (activity_patient_detailed_view.xml)

        recyclerViewDiseases = findViewById(R.id.recyclerDiseases);
        recyclerViewMedications = findViewById(R.id.recyclerMedications);

        llm_diseases = new LinearLayoutManager(this);
        llm_diseases.setOrientation(LinearLayoutManager.HORIZONTAL);
        llm_diseases.setItemPrefetchEnabled(true);

        llm_medications = new LinearLayoutManager(this);
        llm_medications.setOrientation(LinearLayoutManager.HORIZONTAL);
        llm_medications.setItemPrefetchEnabled(true);

        recyclerViewDiseases.setHasFixedSize(true);
        recyclerViewDiseases.setLayoutManager(llm_diseases);
        recyclerViewDiseases.setNestedScrollingEnabled(false);
        recyclerViewDiseases.setItemViewCacheSize(100);
        recyclerViewDiseases.setDrawingCacheEnabled(true);
        recyclerViewDiseases.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        recyclerViewMedications.setHasFixedSize(true);
        recyclerViewMedications.setLayoutManager(llm_medications);
        recyclerViewMedications.setNestedScrollingEnabled(false);
        recyclerViewMedications.setItemViewCacheSize(100);
        recyclerViewMedications.setDrawingCacheEnabled(true);
        recyclerViewMedications.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPatient();
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagePatient();
            }
        });

        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE1);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        pdf_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PDFWriter writer = new PDFWriter(PaperSize.FOLIO_WIDTH, PaperSize.FOLIO_HEIGHT);
                writer.setFont(StandardFonts.SUBTYPE, StandardFonts.TIMES_BOLD, StandardFonts.WIN_ANSI_ENCODING);

                String PID = patientFName+" "+patientLName +
                        " (" + patientContactNo.substring(patientContactNo.length() - 4) +")";

                writer.addText(50, 875, 24, patientFName+" "+patientLName);
                writer.addText(50, 850,14,"Address: "+patientAddress);
                writer.addText(50, 825,14,"Age: "+patientAge);
                writer.addText(50, 800,14,"Birthday: "+patientBirthday);
                writer.addText(50, 775,14,"Contact no.: "+patientContactNo);
                writer.addText(50, 750,14,"Barangay: "+patientBarangay);
                writer.addText(50, 725,14,"Diseases: "+disease_list.toString().replace("[","").replace("]",""));
                writer.addText(50, 700,14,"Medications: "+medication_list.toString().replace("[","").replace("]",""));
                writer.addText(50, 650,14,"Date of Check-Up: "+dateDiagnosed);
                writer.addText(185, 25,14,"Generated by Vitals App for Android.");
                outputToFile(PID+".pdf", writer.asString(), "ISO-8859-1");
            }
        });

        //add an onclicklistener to the recently declared button

    }
    public void outputToFile(String fileName, String pdfContent, String encoding) {
        File directory = new File(Environment.getExternalStorageDirectory(), "Vitals");
        directory.mkdir();
        File newFile = new File(Environment.getExternalStorageDirectory() + "/Vitals/" + fileName);
        File myFile = new File(directory+"/"+fileName);
        try {
            newFile.createNewFile();
            try {
                FileOutputStream pdfFile = new FileOutputStream(newFile);
                pdfFile.write(pdfContent.getBytes(encoding));
                pdfFile.close();
                Snackbar.make(pdf_export,
                        "Successfully exported in\n"+directory.getCanonicalPath()+"/"+fileName,
                        Snackbar.LENGTH_LONG).setAction("Open", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            openFile(PatientDetailedView.this, myFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).show();
            } catch(FileNotFoundException e) {
                // ...
            }
        } catch(IOException e) {
            // ...
        }
    }
    public static void openFile(Context context, File url) throws IOException {
        // Create URI
        File file=url;
        Uri uri = Uri.fromFile(file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if(url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    // Function to check and request permission
    public void checkPermission(String permission, int requestCode)
    {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(PatientDetailedView.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(PatientDetailedView.this, new String[] { permission },
                    requestCode);
        }
        else {
            //Toast.makeText(PatientDetailedView.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onStart(){
        super.onStart();

        getIntents();
        loadToviews();
        getDiseases();
        getMedications();
    }

    private void getIntents(){
        Intent i = getIntent();
        pID = i.getStringExtra("pID");
        patientAddress = i.getStringExtra("patientAddress");
        patientAge = i.getStringExtra("patientAge");
        patientBarangay = i.getStringExtra("patientBarangay");
        patientBirthday = i.getStringExtra("patientBirthday");
        patientContactNo = i.getStringExtra("patientContactNo");
        //i.putExtra("patientDisease", patientDisease);
        patientFName = i.getStringExtra("patientFName");
        patientLName = i.getStringExtra("patientLName");
        dateDiagnosed = i.getStringExtra("dateDiagnosed");
    }

    private void loadToviews(){
        tv_patientName.setText(patientFName+" "+patientLName);
        tv_patientAddress.setText(patientAddress);
        tv_patientAge.setText(patientAge);
        tv_patientBarangay.setText(patientBarangay);
        tv_patientBirthday.setText("Born on "+patientBirthday);
        tv_patientPhoneNo.setText(patientContactNo);

        String final_date_diagnosed = dateDiagnosed.replace("/", " ");
        tv_dateDiagnosed.setText("Checked up last "+final_date_diagnosed);
    }

    private void getDiseases(){
        firestoreDB.collection("patient_lists")
                .document(pID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            disease_list = (ArrayList<String>) document.get("patientDisease");
                            //holder.patientDisease.setText(String.valueOf(disease_list));
                            patientsDiseasesRecyclerViewAdapter = new PatientsDiseasesRecyclerViewAdapter(getApplicationContext(), disease_list);
                            patientsDiseasesRecyclerViewAdapter.setHasStableIds(true);
                            recyclerViewDiseases.setAdapter(patientsDiseasesRecyclerViewAdapter);
                            Log.d("Patient", String.valueOf(disease_list));
                        }
                    }
                });
    }

    private void getMedications(){
        firestoreDB.collection("patient_lists")
                .document(pID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            medication_list = (ArrayList<String>) document.get("patientMedications");
                            //holder.patientDisease.setText(String.valueOf(disease_list));
                            patientsMedicationsRecyclerViewAdapter = new PatientsMedicationsRecyclerViewAdapter(getApplicationContext(), medication_list);
                            patientsMedicationsRecyclerViewAdapter.setHasStableIds(true);
                            recyclerViewMedications.setAdapter(patientsMedicationsRecyclerViewAdapter);
                            Log.d("Patient", String.valueOf(medication_list));
                        }
                    }
                });
    }

    public void callPatient(){
        //Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + clinicPhoneNo));
        //startActivity(intent);
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + patientContactNo));// Initiates the Intent
        startActivity(intent);
    }

    public void messagePatient(){
        String number = patientContactNo;  // The number on which you want to send SMS
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        HelperUtil.backNavigationFinish(PatientDetailedView.this, Patients.class);
    }

    //create method for pdf export
}
