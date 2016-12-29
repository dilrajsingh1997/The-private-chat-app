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

public class Main2Activity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main2);
        firebaseAuth = FirebaseAuth.getInstance();
        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);
        progressDialog = new ProgressDialog(this);
        if(firebaseAuth.getCurrentUser()!=null){
            Intent in = new Intent(Main2Activity.this, MainActivity.class);
            startActivity(in);
        }
    }

    protected boolean isEmpty(EditText editText){

        return editText.getText().toString().trim().length() == 0;

    }

    public void onSignUp(View view){
        String email = editText.getText().toString();
        String pass = "qwertyuiop@asdfghjkl";
        email += "@abc.com";
        if(isEmpty(editText)){
            Toast.makeText(Main2Activity.this, "Please enter some information", Toast.LENGTH_SHORT).show();
        }
        else{
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Registering");
            progressDialog.show();
            if(isNetworkAvailable()){
                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Main2Activity.this, "Successful", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent in = new Intent(Main2Activity.this, MainActivity.class);
                            startActivity(in);
                        }
                        else{
                            Toast.makeText(Main2Activity.this, "Failed", Toast.LENGTH_SHORT).show();
                            Toast.makeText(Main2Activity.this, task.getException().toString().split(": ")[1], Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
            else {
                Toast.makeText(Main2Activity.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
