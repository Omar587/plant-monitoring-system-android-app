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


public class AddPlantForm extends AppCompatActivity {

    EditText towerName_tf, plantName_tf, plantStage_tf, boardID;
    DBHandler dbHandler;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adding_plant_form);

         towerName_tf = findViewById(R.id.tower_name);
         plantName_tf = findViewById(R.id.plant_Name);
         boardID = findViewById(R.id.board_id_Name);
         plantStage_tf = findViewById(R.id.plant_stage_Name);
         Button ok_button = (Button) findViewById(R.id.ok_enter_db);
         Button cancel_button = (Button) findViewById(R.id.cancel_button_);


         dbHandler = new DBHandler(AddPlantForm.this);


        ok_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String towerField = towerName_tf.getText().toString();
                String plantNameField = plantName_tf.getText().toString();
                String boardIdField = boardID.getText().toString();
                String plantStageField = plantStage_tf.getText().toString();


                if (towerField.isEmpty() || plantNameField.isEmpty() || boardIdField.isEmpty() || plantStageField.isEmpty()) {
                    Toast.makeText(AddPlantForm.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();

                }else{
                    String entryDate = java.time.LocalDate.now().toString();
                    dbHandler.addNewTower(towerField, plantNameField,entryDate, boardIdField,plantStageField);


                    Toast.makeText(AddPlantForm.this, "Tower added successfully", Toast.LENGTH_SHORT).show();
                    towerName_tf.setText("");
                    plantName_tf.setText("");
                    boardID.setText("");
                    plantStage_tf.setText("");

                    Intent intent = new Intent(AddPlantForm.this, PlantListActivity.class);
                    startActivity(intent);

                }




            }
        });



        cancel_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AddPlantForm.this, PlantListActivity.class);
                startActivity(intent);
            }
        });







    }








}