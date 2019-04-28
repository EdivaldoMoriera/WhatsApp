package com.cursoandriod.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cursoandriod.whatsapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

   // DatabaseReference referenciaFirebase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  referenciaFirebase.child("pontos").setValue(100);

        reference.child("Pontos").setValue(100);
    }
}
