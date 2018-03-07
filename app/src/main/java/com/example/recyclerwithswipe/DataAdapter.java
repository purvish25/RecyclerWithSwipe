package com.example.recyclerwithswipe;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ppurv on 26-12-2017.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.Viewholder> {

    MyInterface myInterface;
    int pos;
    MainActivity m;
    ModelClass model = new ModelClass();
    List<ModelClass> modelClasses = new ArrayList<ModelClass>();
    List<Integer> selectedPos = new ArrayList<>();
    public List<ModelClass> selectedUsers = new ArrayList<>();
    int selectedPosition=-1;
    //SwipeRevealLayout swipelayout = new SwipeRevealLayout(m);
    public DataAdapter(MainActivity a, MyInterface mi) {
        this.m = a;
        this.myInterface = mi;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater;
        View v;
        inflater = LayoutInflater.from(m);
        v = inflater.inflate(R.layout.design,parent,false);
        Viewholder viewholder = new Viewholder(v);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(final Viewholder holder,  int position) {

        if(selectedPosition==position) {
            holder.container.setBackgroundColor(Color.parseColor("#cfecf7"));
        }
        else {
            holder.container.setBackgroundColor(Color.TRANSPARENT);
        }

        /*holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myInterface.setPosition(position);
                //pos = position;
                selectedPosition=position;
                notifyDataSetChanged();
                //holder.container.setBackgroundColor(Color.parseColor("#cfecf7"));
            }
        });*/

        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myInterface.setLongClick(position);
                selectedPosition=position;
                notifyDataSetChanged();
            }
        });
       /*holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                myInterface.setLongClick(position);
                selectedPosition=position;
                notifyDataSetChanged();
                return true;
            }
        });*/


        /*if (selectedPos.contains(position)) {
            holder.container.setBackgroundColor(Color.parseColor("#cfecf7"));
        } else {
            holder.container.setBackgroundColor(Color.TRANSPARENT);
        }*/


        /*if (modelClasses.get(position) == model ) {
            holder.container.setBackgroundColor(Color.parseColor("#cfecf7"));
        } else {
            holder.container.setBackgroundColor(Color.TRANSPARENT);
        }*/
        //Log.v("Bind ViewHolder",""+modelClasses.size());
        holder.t1.setText(modelClasses.get(position).getId());
        holder.t2.setText(modelClasses.get(position).getName());
        holder.t3.setText(modelClasses.get(position).getEmail());
        if(modelClasses.get(position).getAddress()!=null) {
            holder.t4.setText(modelClasses.get(position).getAddress().getCity());
        }
    }


    public void setModes(List<ModelClass> modes)
    {
        modelClasses.clear();
        modelClasses.addAll(modes);
        //Log.v("Total",""+modelClasses.size());
        notifyDataSetChanged();
    }

    public void setSelected(int pos) {
        this.selectedPosition = pos;
    }

    /*public void setSelected(ModelClass userSelected) {
        this.model = userSelected;
        notifyDataSetChanged();
    }*/

    public void setSelectedPos(int position) {
        pos = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return modelClasses.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv1)
        public TextView t1;
        @BindView(R.id.tv2)
        public TextView t2;
        @BindView(R.id.tv3)
        public TextView t3;
        @BindView(R.id.tv4)
        public TextView t4;
        @BindView(R.id.container1)
        public LinearLayout container;
        @BindView(R.id.delete)
        public Button deletebtn;
        @BindView(R.id.swipelayout1)
        SwipeRevealLayout swipeRevealLayout;
        public Viewholder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
            swipeRevealLayout.setSwipeListener(new SwipeRevealLayout.SwipeListener() {
                                                   @Override
                                                   public void onClosed(SwipeRevealLayout view) {
                                                      // deletebtn.setVisibility(View.INVISIBLE);
                                                   }

                                                   @Override
                                                   public void onOpened(SwipeRevealLayout view) {
                                                       //deletebtn.setVisibility(View.VISIBLE);
                                                   }

                                                   @Override
                                                   public void onSlide(SwipeRevealLayout view, float slideOffset) {
                                                       //deletebtn.setVisibility(View.VISIBLE);
                                                   }
                                               });
           /* t1 = (TextView)itemView.findViewById(R.id.tv1);
            t2 = (TextView)itemView.findViewById(R.id.tv2);
            t3 = (TextView)itemView.findViewById(R.id.tv3);
            t4 = (TextView)itemView.findViewById(R.id.tv4);
            container = (LinearLayout)itemView.findViewById(R.id.container1);*/
        }
    }
}
