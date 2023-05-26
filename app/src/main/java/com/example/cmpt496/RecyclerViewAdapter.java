package com.example.cmpt496;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<TowerModel> towerModel;

    public  RecyclerViewAdapter(Context context, ArrayList<TowerModel> towerModel, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.towerModel = towerModel;
        this.recyclerViewInterface = recyclerViewInterface;

    }

    //inflates layout gives look to rolls
    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from((context));
        View view = inflater.inflate(R.layout.config, parent, false);
        return new RecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }


    //assigns values to the views  created in the
    //plant list file based on the position of the recycler view
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {

        holder.towerName.setText(towerModel.get(position).getTowerName());
        holder.plantName.setText(towerModel.get(position).getPlantName());
        holder.date.setText(towerModel.get(position).getDate());
        holder.plantStage.setText(towerModel.get(position).getPlantStage());
        //holder.imageView.setImageResource(towerModel.get(position).getCol_id());

    }

    //how many items are in the view
    @Override
    public int getItemCount() {
        return towerModel.size();
    }

    //grabs views from recycler view and assigns them to variables.
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView plantStage, date, plantName, towerName;


        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {

            super(itemView);
            //imageView = itemView.findViewById(R.id.imageView);
            plantName = itemView.findViewById((R.id.plant_Name));
            plantStage = itemView.findViewById((R.id.stage_view));
            date = itemView.findViewById(R.id.entry_date_view);
            towerName = itemView.findViewById(R.id.tower_view);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }

                    }
                }
            });



            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemLongClick(pos);
                        }

                    }

                    return true;

                }


            });




        }

    }
}
