package com.example.realm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;

public class ReadActivity extends AppCompatActivity {

    private TextView listView;
    Realm realm;
    List<DataModel> dataModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        listView = findViewById(R.id.list);
        realm = Realm.getDefaultInstance();

        //to show data
        showData();
    }

    private void content() {
        refresh(1000);
    }

    private void refresh(int milliseconds) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                content();
            }
        };
        handler.postDelayed(runnable, milliseconds);
    }

    //show entered data as a list
    private void showData() {
        dataModels = realm.where(DataModel.class).findAll();
        listView.setText("");
        for (int i = 0; i < dataModels.size(); i++) {
            listView.append("\nCourse ID: " + dataModels.get(i).getId()
                    +"\n"+
                    "Course Name: " + dataModels.get(i).getC_name()
                    +"\n"+
                    "Course Duration: " + dataModels.get(i).getC_duration()
                    +"\n"+
                    "Course Track: " + dataModels.get(i).getC_track()
                    +"\n"+
                    "Course Description: " + dataModels.get(i).getC_desc()
                    + "\n");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.options_menu_back:
                startActivity(new Intent(ReadActivity.this, MainActivity.class));
                break;
            case R.id.options_menu_delete:
                deleteData();
                break;
            case R.id.options_menu_edit:
                updateDataDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //delete data
    private void deleteData() {
        AlertDialog.Builder al = new AlertDialog.Builder(ReadActivity.this);
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
                        Toast.makeText(ReadActivity.this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //update data
    private void updateDataDialog() {

        AlertDialog.Builder al = new AlertDialog.Builder(ReadActivity.this);
        View view = getLayoutInflater().inflate(R.layout.list_data, null);
        al.setView(view);

        EditText id_ = view.findViewById(R.id.showid);
        Button delete = view.findViewById(R.id.delete_);
        AlertDialog alertDialog = al.show();

        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                long id = Long.parseLong(id_.getText().toString());
                DataModel dataModel = realm.where(DataModel.class).equalTo("id", id).findFirst();
                ShowUpdateDialog(dataModel);
            }
        });
    }

    private void ShowUpdateDialog(DataModel dataModel){
        AlertDialog.Builder al = new AlertDialog.Builder(ReadActivity.this);
        View view = getLayoutInflater().inflate(R.layout.activity_update_delete, null);
        al.setView(view);

        EditText name = view.findViewById(R.id.udname);
        EditText dur = view.findViewById(R.id.udduration);
        EditText track = view.findViewById(R.id.udtrack);
        EditText desc = view.findViewById(R.id.uddesc);
        Button update = view.findViewById(R.id.updatebtn);
        AlertDialog alertDialog = al.show();

        name.setText(dataModel.getC_name());
        dur.setText(dataModel.getC_duration());
        track.setText(dataModel.getC_track());
        desc.setText(dataModel.getC_desc());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                dataModel.setC_name(name.getText().toString());
                dataModel.setC_duration(dur.getText().toString());
                dataModel.setC_track(track.getText().toString());
                dataModel.setC_desc(desc.getText().toString());

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(dataModel);
                    }
                });
            }
        });
    }
}