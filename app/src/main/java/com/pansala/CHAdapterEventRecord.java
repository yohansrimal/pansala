package com.pansala;

/*custom adapter class for recycle view*/

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pansala.Database.DBHelper;

import java.util.ArrayList;

public class CHAdapterEventRecord extends RecyclerView.Adapter<CHAdapterEventRecord.HolderRecord>{

    //variables
    private Context context;
    private ArrayList<CHEventModel> recordsList;

    //DB helper
    DBHelper CHDBEventHelper;

    //constructor
    public CHAdapterEventRecord(Context context, ArrayList<CHEventModel> recordsList) {
        this.context = context;
        this.recordsList = recordsList;

        CHDBEventHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public HolderRecord onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.chrow_record, parent,false);
        return new HolderRecord(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRecord holder, final int position) {

        //get data, set data, handle view clicks in this method.

        //get data
        CHEventModel model = recordsList.get(position);
        final String eventId = model.getId();
        final String eventName = model.getEvent();
        final String eventDes = model.getEventDes();
        final String eventDate = model.getEventDate();
        final String eventTime = model.getEventTime();
        final String addedTime = model.getAddedTime();
        final String updatedTime = model.getUpdatedTime();

        //set data to views
        holder.givingETv.setText(eventName);
       // holder.eventDateTv.setText(eventDes);
        holder.dateETv.setText(eventDate);
        holder.timeETv.setText(eventTime);

        //if user doesn't attach image then imageUri will be null, so set a default image in that case
//        if (image.equals("null")){
//            //no image in record, set default
//            holder.profileIv.setImageResource(R.drawable.testtemple2);
//        }else {
//            //have image in the record
//            holder.profileIv.setImageURI(Uri.parse(image));
//        }


        //handle item clicks
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass record id to next activity to show details of that record
                Intent intent = new Intent(context, CHEventDetailActivity.class);
                intent.putExtra("RECORD_ID", eventId);
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
                        ""+eventId,
                        ""+eventName,
                        ""+eventDes,
                        ""+eventDate,
                        ""+eventTime,
                        ""+addedTime,
                        ""+updatedTime
                );
            }
        });

    }

    private void showMoreDialog(String position, final String id, final String eventName, final String eventDes, final String eventDate,
                                final String eventTime, final String addedTime, final String updatedTime) {

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
                    Intent intent = new Intent(context, CHAddUpdateEventActivity.class);
                    intent.putExtra("EVENT_ID",id);
                    intent.putExtra("EVENT_NAME",eventName);
                    intent.putExtra("EVENT_DES",eventDes);
                    intent.putExtra("EVENT_DATE",eventDate);
                    intent.putExtra("EVENT_TIME",eventTime);
                    intent.putExtra("ADDED_TIME",addedTime);
                    intent.putExtra("UPDATED_TIME",updatedTime);
                    intent.putExtra("isEditMode", true);//need to edit existing data, set true
                    context.startActivity(intent);

                }else if(which == 1){
                    //delete is clicked
                    CHDBEventHelper.deleteData(id);
                    //refresh record by calling activities onResume method
                    ((ChEventHadingHome)context).onResume();
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
        //ImageView profileIv;
        TextView givingETv, dateETv, timeETv, eventDesTv;
        ImageButton moreBtn;

        public HolderRecord(@NonNull View itemView) {
            super(itemView);

            //init views
            givingETv =  itemView.findViewById(R.id.givingETv);
            //eventDesTv=  itemView.findViewById(R.id.eventDesTv);
            dateETv =  itemView.findViewById(R.id.dateETv);
            timeETv =  itemView.findViewById(R.id.timeETv);
            moreBtn = itemView.findViewById(R.id.moreBtn);

        }
    }
}
