package com.example.hired;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A class containing the logic for companies users to create their WorkSpace accounts, which are stored using Firebase Authentication.
 */
public class CompanyRegistration extends AppCompatActivity {
    private FirebaseAuth auth;

    /**
     * Setting the content view to the activity_company_registration.xml page, initializing the FirebaseAuth, auth.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_registration);
        auth= FirebaseAuth.getInstance();
    }

    /**
     * Register method to create WorkSpace accounts for new company users. The methods takes the input email and password from the
     * activity_company_registration.xml EditTexts. The method calls the confirmPassword method to confirm that the password and
     * the confirmation are the same. If the same, a user is created using the createUserWithEmailAndPassword method from Firebase Auth.
     * @param v
     */
    public void register(View v){
        EditText companyEmail = findViewById(R.id.companyEmail);
        EditText companyPassword = findViewById(R.id.companyPassword);
        EditText confirmPassword = findViewById(R.id.companyConfirmPassword);

        String email = companyEmail.getText().toString();
        String password = companyPassword.getText().toString();
        String confirm = confirmPassword.getText().toString();
        Log.d("CompanySurveyPage", email + " " + password + " " + confirm);

        if (!confirmPassword(password, confirm)){
            Toast.makeText(CompanyRegistration.this, "Password does not match username.",Toast.LENGTH_SHORT);
            Log.d("CompanySurveyPage", "not confirmed" + " " + password + " " + confirm);
        }
        else{
            Log.d("CompanySurveyPage", "I'm here, about to authenticate");
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                  if(task.isSuccessful()){
                      Toast.makeText(CompanyRegistration.this, "New user created for" + email + ".",Toast.LENGTH_SHORT);
                      Log.d("CompanySurveyPage", "Successful authentication.");
                      performCompanySurvey(v);
                  }
                  else{
                      Log.d("CompanySurveyPage", "Failed to create new user. Try again.");
                      Log.d("CompanySurveyPage", " " + task.getException());
                      Toast.makeText(CompanyRegistration.this, "Failed to create new user. Try again.",Toast.LENGTH_SHORT);
                  }
                }
            });
        }
    }

    /**
     * Checks that the current user is not null and starts the CompanyProfile activity.
     */
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user!=null){
            Intent intent = new Intent(this, CompanyProfile.class);
            startActivity(intent);
            this.finish();
        }
    }

    /**
     * confirms whether the String password and String confirmation parameters are equal (case-sensitive).
     * @param password
     * @param confirmation
     * @return true if both Strings are equal or false if the String are not equal.
     */
    private boolean confirmPassword(String password, String confirmation){
        if (password.equals(confirmation)){
            return true;
        }
        if(TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmation)){
            return false;
        }
        return false;
    }

    /**
     * When the "Already a user? Sign in here." button is clicked, the CompanyLogin activity is started.
     * @param v
     */
    public void performCompanyLogin(View v){
        Intent intent = new Intent(this, CompanyLogin.class);
        startActivity(intent);
    }

    /**
     * Starts the CompanySurveyPage activity when called. This method is called when the user is successfully made an account.
     * @param v
     */
    private void performCompanySurvey(View v){
        Intent intent = new Intent(this, CompanySurveyPage.class);
        startActivity(intent);
    }
}