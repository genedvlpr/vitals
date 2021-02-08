package com.orbitsoftlabs.vitals;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.orbitsoftlabs.vitals.HomeUtils.HomeHealthWorker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HelperUtil {
    public static FirebaseUser mUser;
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    static String email, password;
    static ProgressDialog progressDialog;

    public static Animation animFadeIn, animFadeOut;

    public static void authentication(Activity activity, Class c, TextInputLayout userName, TextInputLayout passWord){
        int TIME_OUT = 3000;
        email = Objects.requireNonNull(userName.getEditText()).getText().toString().trim();
        password = Objects.requireNonNull(passWord.getEditText()).getText().toString().trim();

        if (email.isEmpty()){
            emptyFields("Email is required", userName);
            userName.requestFocus();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emptyFields("Enter a valid email address", userName);
            userName.requestFocus();
            return;
        }
        else if (password.isEmpty()){
            emptyFields("Password is required", userName);
            passWord.requestFocus();
            return;
        }
        else if (passWord.getEditText().length()<8){
            emptyFields("Password must be 8 characters", userName);
            passWord.requestFocus();
            return;
        }

        else{
            signInWithEmailPassword(activity, c, userName);
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Logging in");
            progressDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, TIME_OUT);
        }
    }

    private static void signInWithEmailPassword(final Activity activity, final Class c, final View view){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            emptyFields("Successfully logged in", view);
                            mAuth.getCurrentUser();
                            Intent mainIntent = new Intent(activity, c);
                            activity.startActivity(mainIntent);
                            activity.finish();

                        } else {
                            progressDialog.dismiss();
                            emptyFields("Invalid account credentials", view);
                        }
                    }
                });
    }

    private static void emptyFields(String str, View v){
        Snackbar.make(v, str, Snackbar.LENGTH_SHORT).show();
    }

    public static void firebaseInitialization(Context context){
        FirebaseApp.initializeApp(context);
    }

    public static void nextActivity(Activity activity, Class c){
        Intent i = new Intent(activity, c);
        i.putExtra("activity",activity.getClass().getSimpleName());
        activity.startActivity(i);
        activity.finish();
    }

    public static void backNavigation(Activity activity, Class c){
        Intent register = new Intent(activity, c);
        activity.startActivity(register);
    }

    public static void backNavigationWithAlert(final Activity activity, String title, String message){
        new MaterialAlertDialogBuilder(activity, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public static void backNavigationFinish(Activity activity, Class c){
        Intent i = new Intent(activity, c);
        activity.startActivity(i);

        activity.finish();
    }

    public static void backNavigationFinishLast(String last_activity,Activity activity, Class c){
        Intent i = new Intent(activity, c);
        i.putExtra("activity",last_activity);
        activity.startActivity(i);

        activity.finish();
    }

    public static void destroyActivity(Activity activity){
        activity.finish();
    }

    public static void registerUser(final Activity activity, final Class c, TextInputLayout userName, TextInputLayout passWord, TextInputLayout confirm_passWord, final View view, final ProgressBar progressBar){
        final String conpassword = confirm_passWord.getEditText().getText().toString().trim();
        final String email = Objects.requireNonNull(userName.getEditText().getText()).toString();
        final String password = passWord.getEditText().getText().toString().trim();

        if (email.isEmpty()){
            emptyFields("Email is required.", view);
            userName.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emptyFields("Invalid email.", view);
            userName.requestFocus();
            return;
        }
        if (password.isEmpty()){
            emptyFields("Password is required.", view);
            passWord.requestFocus();
            return;
        }
        if (password.length()<=6){
            emptyFields("Password must be 6 characters.", view);
            passWord.requestFocus();
            return;
        }

        if (conpassword.isEmpty()){
            emptyFields("Password must not be empty.", view);
            passWord.requestFocus();
            return;
        }
        if (!conpassword.matches(password)){
            emptyFields("Password didn't matched.", view);
            passWord.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            // Create a new user with a first and last name
                            Map<String, Object> user = new HashMap<>();
                            Map<String, Object> account_details = new HashMap<>();
                            account_details.put("email", Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
                            account_details.put("user_id", Objects.requireNonNull(mAuth.getUid()));
                            user.put("account_details", account_details);

                            firestoreDB.collection("users").document(mAuth.getUid())
                                    .collection("account_details").document("account_info").set(account_details)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //nextActivity(activity,c);
                                        }
                                    });

                            firestoreDB.collection("users_lists").document(mAuth.getUid()).set(account_details)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            nextActivity(activity,c);
                                        }
                                    });

                            emptyFields("Username and password configured successfully", view);

                            progressBar.setVisibility(View.INVISIBLE);
                            //nextActivity(activity,c);
                        }
                        else {
                            emptyFields("Sign Up Failed", view);
                        }
                    }
                });
    }

    public static void registerName(final Activity activity, final Class c, TextInputLayout fname, TextInputLayout mname, TextInputLayout lname, final View view, final ProgressBar progressBar){
        String id = mAuth.getUid();
        final String f_name = fname.getEditText().getText().toString().trim();
        final String m_name = mname.getEditText().getText().toString().trim();
        final String l_name = lname.getEditText().getText().toString().trim();

        if (f_name.isEmpty()){
            emptyFields("First name is required.",view);
            fname.requestFocus();
            return;
        }
        if (m_name.isEmpty()){
            emptyFields("Middle name is required.",view);
            mname.requestFocus();
            return;
        }
        if (l_name.isEmpty()){
            emptyFields("Last name is required.",view);
            lname.requestFocus();
            return;
        }


        else{

            progressBar.setVisibility(View.VISIBLE);
            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            Map<String, Object> account_details = new HashMap<>();
            account_details.put("first_name", f_name);
            account_details.put("middle_name", m_name);
            account_details.put("last_name", l_name);
            user.put("account_details", account_details);

            // Add a new document with a generated ID
            //String idss = String.valueOf(getId());
            firestoreDB.collection("users").document(Objects.requireNonNull(mAuth.getUid())).
                    collection("account_details").document("account_info")
                    .update(account_details)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            progressBar.setVisibility(View.INVISIBLE);
                            //viewPager.setCurrentItem(nextFragment);
                            nextActivity(activity,c);
                            emptyFields("First name, middle name and last name has been added",view);
                        }
                    });


        }

    }

    public static void registerPersonalDetails(final Activity activity, final Class c, TextInputLayout mobileNo, TextInputLayout messengerUsername, TextInputLayout barangayName, TextInputLayout address, final View view, final ProgressBar progressBar){
        String id = mAuth.getUid();
        final String mobile = mobileNo.getEditText().getText().toString().trim();
        final String messenger = messengerUsername.getEditText().getText().toString().trim();
        final String barangay = barangayName.getEditText().getText().toString().trim();
        final String address_loc = address.getEditText().getText().toString().trim();

        if (mobile.isEmpty()){
            emptyFields("Mobile no. is required.",view);
            mobileNo.requestFocus();
            return;
        }
        if (messenger.isEmpty()){
            emptyFields("Messenger username is required.",view);
            messengerUsername.requestFocus();
            return;
        }
        if (barangay.isEmpty()){
            emptyFields("Barangay is required.",view);
            barangayName.requestFocus();
            return;
        }

        if (address_loc.isEmpty()){
            emptyFields("Address is required.",view);
            address.requestFocus();
            return;
        }


        else{

            progressBar.setVisibility(View.VISIBLE);
            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            Map<String, Object> account_details = new HashMap<>();
            account_details.put("mobile_no", "("+mobile+")");
            account_details.put("messenger_username", messenger);
            account_details.put("barangay", barangay);
            account_details.put("address", address_loc);
            user.put("account_details", account_details);

            // Add a new document with a generated ID
            //String idss = String.valueOf(getId());
            firestoreDB.collection("users").document(Objects.requireNonNull(mAuth.getUid())).
                    collection("account_details").document("account_info")
                    .update(account_details)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            progressBar.setVisibility(View.INVISIBLE);
                            //viewPager.setCurrentItem(nextFragment);
                            nextActivity(activity,c);
                            emptyFields("Personal details has been added",view);
                        }
                    });


        }

    }

    public static void displayUserCredentials(final Activity activity, final TextView name, final TextView emailAdd){
        firestoreDB.collection("users")
                .document(Objects.requireNonNull(mAuth.getUid()))
                .collection("account_details")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                String fname, lname, email;

                                fname = documentSnapshot.getString("first_name");
                                lname = documentSnapshot.getString("last_name");
                                email = documentSnapshot.getString("barangay");

                                name.setText(fname+" "+lname);
                                emailAdd.setText(email);
                                Log.d("Credentials", "First name: "+fname+" "+ "Last name: "+ lname+" ");
                                //Toast.makeText(activity, ""+fname,Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    public static void checkUserExistence(Activity activity){
        if (mAuth.getCurrentUser() == null){
            nextActivity(activity, Login.class);
        }else {
            nextActivity(activity, HomeHealthWorker.class);
        }
    }

    public static void signOut(Activity activity){
        if (mAuth.getCurrentUser() != null){
            mAuth.signOut();
        }
        nextActivity(activity,Login.class);
    }

    public static void fadeIn(View v, Context c){
        animFadeIn = AnimationUtils.loadAnimation(c,R.anim.anim_fade_in);
        v.startAnimation(animFadeIn);
    }
    public static void fadeOut(View v, Context c){
        animFadeOut = AnimationUtils.loadAnimation(c,R.anim.anim_fade_out);
        v.startAnimation(animFadeOut);
    }
}
