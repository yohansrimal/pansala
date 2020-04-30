package com.pansala;

/*custom adapter class for recycle view*/

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pansala.Database.DBHelper;

import java.util.ArrayList;

public class userTempleViewAdapter extends RecyclerView.Adapter<userTempleViewAdapter.HolderRecord>{

    //variables
    private Context context;
    private ArrayList<NModelRecord> recordsList;

    //DB helper
    DBHelper ndbHelper;

    //constructor
    public userTempleViewAdapter(Context context, ArrayList<NModelRecord> recordsList) {
        this.context = context;
        this.recordsList = recordsList;

        ndbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public HolderRecord onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.userrow_record, parent,false);
        return new HolderRecord(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRecord holder, final int position) {

        //get data, set data, handle view clicks in this method.

        //get data
        NModelRecord model = recordsList.get(position);
        final String id = model.getId();
        final String name = model.getName();
        final String image = model.getImage();
        final String templocation = model.getTemplocation();
        final String phone = model.getPhone();

        //set data to views
        holder.nameTv.setText(name);
        holder.locationTv.setText(templocation);
        holder.phoneTv.setText(phone);
        //if user doesn't attach image then imageUri will be null, so set a default image in that case
        if (image.equals("null")){
            //no image in record, set default
            holder.profileIv.setImageResource(R.drawable.templenew);
        }else {
            //have image in the record
            holder.profileIv.setImageURI(Uri.parse(image));
        }


        //handle item clicks
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass record id to next activity to show details of that record
                Intent intent = new Intent(context, TempleView.class);
                intent.putExtra("RECORD_ID", id);
                intent.putExtra("TEMPLE_NAME", name);
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        //return size of list/number of records
        return recordsList.size();
    }


    class HolderRecord extends RecyclerView.ViewHolder{

        //views
        ImageView profileIv;
        TextView nameTv,locationTv, phoneTv;
        ImageButton moreBtn;

        public HolderRecord(@NonNull View itemView) {
            super(itemView);

            //init views
            nameTv =  itemView.findViewById(R.id.nameTv);
            profileIv =  itemView.findViewById(R.id.profileIv);
            phoneTv =  itemView.findViewById(R.id.phoneTv);
            locationTv =  itemView.findViewById(R.id.locationTv);
            moreBtn = itemView.findViewById(R.id.moreBtn);

        }
    }
}
