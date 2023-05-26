package com.example.cmpt496;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class PlantListActivity extends AppCompatActivity implements RecyclerViewInterface {
    ArrayList<TowerModel> towerModels = new ArrayList<>();
    RecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tower_list);

        RecyclerView rec = findViewById(R.id.mRecyclerView);

        setUpTowerModels();

        adapter = new RecyclerViewAdapter(this,towerModels, this);

        rec.setAdapter(adapter);
        rec.setLayoutManager(new LinearLayoutManager(this));


        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayTowerListActivity();

            }
        });



    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();

    }

    public void setUpTowerModels() {


        DBHandler db = new DBHandler(this);
        HashMap<Integer, ArrayList<String>> items = db.getDbEntries();


        int[] plantImage = {R.drawable.plant};


        for (int i = 0; i < items.size(); i++) {

            String towerName = items.get(i).get(0);
            String name = items.get(i).get(1);
            String date = items.get(i).get(2);
            String stage = items.get(i).get(3);
            String col_id = items.get(i).get(4);


            towerModels.add(new TowerModel(towerName,
                    date, stage, name, col_id));


        }

    }






    private void displayTowerListActivity(){

        Intent intent = new Intent(PlantListActivity.this, AddPlantForm.class);
        startActivity(intent);
    }


    //THIS METHOD UPONS UP THE UPDATE SCREEN, IF YOU CICK ON A PLANT.
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(PlantListActivity.this, UpdatePlant.class);

        //grabs the row list items and sends it to the UpdatePlant activity.

        intent.putExtra("TOWER_NAME", towerModels.get(position).getTowerName());
        intent.putExtra("PLANT_NAME", towerModels.get(position).getPlantName());
        intent.putExtra("DATE", towerModels.get(position).getDate());
        intent.putExtra("PLANT_STAGE", towerModels.get(position).getPlantStage());
        intent.putExtra("ROW_ID", towerModels.get(position).getCol_id());


        startActivity(intent);




    }

    @Override
    public void onItemLongClick(int position) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DBHandler dbHandler =  new DBHandler(this);;

        builder.setMessage("Are you sure you want to delete plant Info?")
                .setTitle("Delete").
                setIcon(R.drawable.trash).
                setCancelable(false).
                setPositiveButton("Erase", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                dbHandler.deleteEntry(towerModels.get(position).getCol_id());
                towerModels.remove(position);

                adapter.notifyItemRemoved(position);

            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();










    }
}