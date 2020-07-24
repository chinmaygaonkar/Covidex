package com.example.covidex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

//
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
//import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
//import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.GoogleApiAvailabilityCache;
import com.squareup.picasso.Picasso;
//

import static java.lang.Thread.sleep;

public class Greetings extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    //
    private ImageView profile_image;
    private TextView name;
    private TextView email;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greetings);

        getSupportActionBar().hide();

        //
        profile_image = findViewById(R.id.profile_image);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        //

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Greetings.this.finish();
                    Intent i = new Intent(Greetings.this, MainActivity.class);
                    startActivity(i);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }

    //
    private void gotoMainActivity() {

        startActivity(new Intent(Greetings.this, LoginActivity.class));
        finish();
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();

            name.setText(account.getDisplayName());
            email.setText(account.getEmail());

            Picasso.get().load(account.getPhotoUrl()).placeholder(R.mipmap.ic_launcher).into(profile_image);
        } else{
            gotoMainActivity();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);

        if(opr.isDone()){
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult result) {
                    handleSignInResult(result);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    //
}