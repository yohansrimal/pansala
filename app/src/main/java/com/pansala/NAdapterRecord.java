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

public class NAdapterRecord extends RecyclerView.Adapter<NAdapterRecord.HolderRecord>{

    //variables
    private Context context;
    private ArrayList<NModelRecord> recordsList;

    //DB helper
    DBHelper ndbHelper;

    //constructor
    public NAdapterRecord(Context context, ArrayList<NModelRecord> recordsList) {
        this.context = context;
        this.recordsList = recordsList;

        ndbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public HolderRecord onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.nrow_record, parent,false);
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
        final String history = model.getHistory();
        final String start = model.getStart();
        final String monk = model.getMonk();
        final String templocation = model.getTemplocation();
        final String phone = model.getPhone();
        final String addedTime = model.getAddedTime();
        final String updatedTime = model.getUpdatedTime();

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
                Intent intent = new Intent(context, NRecordDetailActivity.class);
                intent.putExtra("RECORD_ID", id);
                context.startActivity(intent);
            }
        });

        //handle more button click listener(Show options like edit/delete)
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show options menu
                showMoreDialog(
                        ""+position,
                        ""+id,
                        ""+name,
                        ""+history,
                        ""+start,
                        ""+monk,
                        ""+templocation,
                        ""+phone,
                        ""+image,
                        ""+addedTime,
                        ""+updatedTime
                );
            }
        });
        Log.d("ImagePath","onBindViewHolder: "+image);

    }

    private void showMoreDialog(String position, final String id, final String name, final String history, final String start, final String monk, final String templocation, final String phone, final String image, final String addedTime, final String updatedTime) {

        //options to display in dialog
        String[] options ={"Edit", "Delete"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //add items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //handle item clicks
                if(which == 0){
                    //edit is clicked

                    //start AddUpdateRecordActivity to update existing record
                    Intent intent = new Intent(context, NAddUpdateRecordActivity.class);
                    intent.putExtra("ID",id);
                    intent.putExtra("NAME",name);
                    intent.putExtra("HISTORY",history);
                    intent.putExtra("START",start);
                    intent.putExtra("MONK",monk);
                    intent.putExtra("TEMPLE_LOCATION",templocation);
                    intent.putExtra("PHONE",phone);
                    intent.putExtra("IMAGE",image);
                    intent.putExtra("ADDED_TIME",addedTime);
                    intent.putExtra("UPDATED_TIME",updatedTime);
                    intent.putExtra("isEditMode", true);//need to edit existing data, set true
                    context.startActivity(intent);

                }else if(which == 1){
                    //delete is clicked
                    ndbHelper.deleteTempleData(id);
                    //refresh record by calling activities onResume method
                    ((NTempleHome)context).onResume();
                }
            }
        });
        //show dialog
        builder.create().show();
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
