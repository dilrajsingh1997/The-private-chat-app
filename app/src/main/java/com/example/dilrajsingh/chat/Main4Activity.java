package com.example.dilrajsingh.chat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Main4Activity extends AppCompatActivity {

    Button button;
    EditText editText, editText2;
    TextView textView;
    ProgressDialog progressDialog;

    public FirebaseAuth firebaseAuth;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        firebaseAuth = FirebaseAuth.getInstance();
        button = (Button) findViewById(R.id.button_in);
        editText = (EditText) findViewById(R.id.editText3);
        editText2 = (EditText) findViewById(R.id.editText4);
        progressDialog = new ProgressDialog(this);
    }

    protected boolean isEmpty(EditText editText){
        return editText.getText().toString().trim().length() == 0;
    }

    public void onSignup(View view){
        Intent i = new Intent(this, Main2Activity.class);
        startActivity(i);
    }

    public void onSignin(View view){
        String email = editText.getText().toString();
        String pass = editText2.getText().toString();
        email += "@abc.com";
        if(isEmpty(editText) || isEmpty(editText2)){
            Toast.makeText(Main4Activity.this, "Please enter valid information", Toast.LENGTH_SHORT).show();
        }
        else{
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Please wait");
            progressDialog.show();
            if(isNetworkAvailable()){
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Main4Activity.this, "Successful", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent in = new Intent(Main4Activity.this, MainActivity.class);
                            startActivity(in);
                        }
                        else{
                            Toast.makeText(Main4Activity.this, "Failed", Toast.LENGTH_SHORT).show();
                            Toast.makeText(Main4Activity.this, task.getException().toString().split(": ")[1], Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
                /*firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Main4Activity.this, "Successful", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent in = new Intent(Main4Activity.this, MainActivity.class);
                            startActivity(in);
                        }
                        else{
                            Toast.makeText(Main4Activity.this, "Failed", Toast.LENGTH_SHORT).show();
                            Toast.makeText(Main4Activity.this, task.getException().toString().split(": ")[1], Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });*/
            }
            else {
                Toast.makeText(Main4Activity.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
