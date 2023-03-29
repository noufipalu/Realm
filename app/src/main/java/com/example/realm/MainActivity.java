package com.example.realm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    EditText name,duration,track,desc;
    Button add;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();
        name = findViewById(R.id.name);
        duration = findViewById(R.id.duration);
        track = findViewById(R.id.track);
        desc = findViewById(R.id.desc);
        add = findViewById(R.id.btn_add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String cname = name.getText().toString();
               String cduration = duration.getText().toString();
               String ctrack = track.getText().toString();
               String cdesc = desc.getText().toString();

                if (TextUtils.isEmpty(cname)){
                    name.setError("Field cannot be Empty");
                } else if (TextUtils.isEmpty(cduration)) {
                    duration.setError("Field cannot be Empty");
                } else if (TextUtils.isEmpty(ctrack)) {
                    track.setError("Field cannot be empty");
                } else if (TextUtils.isEmpty(cdesc)) {
                    desc.setError("Field cannot be empty");
                } else {
                    addToDatabase(cname, cduration, ctrack, cdesc);
                    Toast.makeText(MainActivity.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                    name.setText("");
                    duration.setText("");
                    track.setText("");
                    desc.setText("");
                }
            }
        });
    }

    private void addToDatabase(String name, String duration, String track, String desc) {
        DataModel dataModel= new DataModel();
        Number id = realm.where(DataModel.class).max("id");

        long nextId;

        if (id==null){
            nextId=1;
        }else {
            nextId = id.intValue()+1;
        }

        dataModel.setId(nextId);
        dataModel.setC_name(name);
        dataModel.setC_duration(duration);
        dataModel.setC_track(track);
        dataModel.setC_desc(desc);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(dataModel);
            }
        });
    }
}