package com.example.realm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;

public class Update_deleteActivity extends AppCompatActivity {

    EditText name, dur, track, desc;
    Button update, delete, x;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);
        //updateDataDialog();

        update = findViewById(R.id.updatebtn);
        x = findViewById(R.id.backbtn);
        realm = Realm.getDefaultInstance();
        
        
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Update_deleteActivity.this, MainActivity.class));
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
                Toast.makeText(Update_deleteActivity.this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*private void updateDataDialog() {

        AlertDialog.Builder al = new AlertDialog.Builder(Update_deleteActivity.this);
        View view = getLayoutInflater().inflate(R.layout.list_data, null);
        al.setView(view);

        EditText id_ = view.findViewById(R.id.showid);
        delete = view.findViewById(R.id.delete_);
        AlertDialog alertDialog = al.show();

        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                long id = Long.parseLong(id_.getText().toString());
                DataModel dataModel = realm.where(DataModel.class).equalTo("id", id).findFirst();
                //ShowUpdateDialog(dataModel);
            }
        });
    }*/

    private void deleteData() {
        AlertDialog.Builder al = new AlertDialog.Builder(Update_deleteActivity.this);
        View view = getLayoutInflater().inflate(R.layout.list_data, null);
        al.setView(view);

        EditText data_id = view.findViewById(R.id.showid);
        Button delete = view.findViewById(R.id.delete_);
        AlertDialog alertDialog = al.show();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = Long.parseLong(data_id.getText().toString());
                DataModel dataModel = realm.where(DataModel.class).equalTo("id", id).findFirst();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        alertDialog.dismiss();
                        dataModel.deleteFromRealm();
                    }
                });
            }
        });
    }

    /*private void ShowUpdateDialog(DataModel dataModel){
        AlertDialog.Builder al = new AlertDialog.Builder(Update_deleteActivity.this);
        View view = getLayoutInflater().inflate(R.layout.activity_update_delete, null);
        al.setView(view);

        name = view.findViewById(R.id.udname);
        dur = view.findViewById(R.id.udduration);
        track = view.findViewById(R.id.udtrack);
        desc = view.findViewById(R.id.uddesc);
        update = view.findViewById(R.id.updatebtn);
        AlertDialog alertDialog = al.show();

        name.setText(dataModel.getC_name());
        dur.setText(dataModel.getC_duration());
        track.setText(dataModel.getC_track());
        desc.setText(dataModel.getC_desc());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        dataModel.setC_name(name.getText().toString());
                        dataModel.setC_duration(dur.getText().toString());
                        dataModel.setC_track(track.getText().toString());
                        dataModel.setC_desc(desc.getText().toString());

                        realm.copyToRealmOrUpdate(dataModel);
                    }
                });
            }
        });
    }*/
}