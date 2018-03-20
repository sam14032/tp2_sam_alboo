package ca.csf.mobile1.tp2.activity;

import android.support.v7.app.AppCompatActivity;

import ca.csf.mobile1.tp2.application.Tp2Application;

public abstract class Tp2Activity extends AppCompatActivity {

    public Tp2Application getTp2Application() {
        return (Tp2Application) getApplication();
    }

}
