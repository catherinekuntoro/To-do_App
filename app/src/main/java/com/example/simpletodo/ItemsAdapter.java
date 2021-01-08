package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//Responsible for taking data at particular precision, put to view holder
//Responsible for displaying data from the model into a row in the recycler view
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{

    public interface OnClickListener{
        void onItemClicked(int position);
    }
    //interface for removing functionality
    public interface OnLongClickListener{
        //To know which position you're long clicking at, notify the adapter of this
        void onItemLongClicked(int position);
    }

    List<String> items;
    OnLongClickListener longClickListener;
    OnClickListener onClickListener;

    public ItemsAdapter(List<String> items, OnLongClickListener longClickListener, OnClickListener onClickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.onClickListener = onClickListener;
    }

    //Create each view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Use layout inflater to inflate a view.
        View toDoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

        //Wrap it inside a View Holder and return it
        return new ViewHolder(toDoView);
    }

    //Taking data at particular precision, put in view holder.
    //Responsible for binding data to particular view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Grab item at the position
        String item = items.get(position);
        //Bnd the item into the specified view holder
        holder.bind(item);
    }

    //Tells the recycler view how many items are in the list
    @Override
    public int getItemCount() {
        return items.size();
    }

    //define view holder. Container to provide easy access to views
    //that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItem = itemView.findViewById(android.R.id.text1);
        }

        //Update the view inside f the view holder with the data of String item
        public void bind(String item) {
            textViewItem.setText(item);
            textViewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClicked(getAdapterPosition());
                }
            });

            //for removing
            textViewItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //Remove the item from the recycler view. Need to communicate this item is clicked, communicate to main activity
                    //Notify position that was long pressed
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
