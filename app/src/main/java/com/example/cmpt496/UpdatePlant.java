package com.example.cmpt496;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdatePlant extends AppCompatActivity {

    EditText towerName_tf, plantName_tf, plantStage_tf, date_tf;
    DBHandler dbHandler;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //responsible for inputs of textfields
        setContentView(R.layout.activity_update_plant);
        towerName_tf = findViewById(R.id.update_tower_name);
        plantName_tf = findViewById(R.id.update_plant_Name);
        date_tf = findViewById(R.id.update_date);
        plantStage_tf = findViewById(R.id.update_plant_stage);

        Button ok_button = (Button) findViewById(R.id.update_button_db);
        Button cancel_button = (Button) findViewById(R.id.cancel_update_button_);

        //This is the current data within a recycler view row
        String curTowerName = getIntent().getStringExtra("TOWER_NAME");
        String curPlantName = getIntent().getStringExtra("PLANT_NAME");
        String curDate = getIntent().getStringExtra("DATE");
        String curPlantStage = getIntent().getStringExtra("PLANT_STAGE");
        String curId = getIntent().getStringExtra("ROW_ID");

        //sets the text with the current data
        towerName_tf.setText(curTowerName);
        plantName_tf.setText(curPlantName);
        date_tf.setText(curDate);
        plantStage_tf.setText(curPlantStage);

        dbHandler = new DBHandler(UpdatePlant.this);


        ok_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                String towerField = towerName_tf.getText().toString();
                String plantNameField = plantName_tf.getText().toString();
                String dateField = date_tf.getText().toString();
                String plantStageField = plantStage_tf.getText().toString();

                //if the current recycler view row value is empty keep cur row item, else get textfield item
                String dbTowerName = towerField.isEmpty() ? curTowerName :towerField;
                String dbPlantName= plantNameField.isEmpty() ? curPlantName : plantNameField;
                String dbDate = dateField.isEmpty() ? curDate : dateField;
                String dbPlantStage = plantStageField.isEmpty() ? curPlantStage : plantStageField;


                dbHandler.updateDb(dbTowerName, dbPlantName, dbPlantStage,dbDate, curId);

                Toast.makeText(UpdatePlant.this, "Tower updated successfully", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(UpdatePlant.this, PlantListActivity.class);
                startActivity(intent);

            }
        });


        cancel_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(UpdatePlant.this, PlantListActivity.class);
                startActivity(intent);
            }
        });




    }





}





