package me.vinnychan.billsplit.model;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

import me.vinnychan.billsplit.R;

/**
 * Created by vincentchan on 15-10-18.
 */
public class ConvertAdapter extends RecyclerView.Adapter<ConvertAdapter.ItemViewHolder>{

    private List<Item> items;

    ConvertAdapter(List<Item> items){
        this.items = items;
    }

    public void setItemList(List<Item> listItems) {
        this.items=listItems;
        notifyItemRangeChanged(0, items.size());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_card_item, viewGroup, false);
        ItemViewHolder ivh = new ItemViewHolder(v);
        return ivh;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, final int i) {
        itemViewHolder.itemName.setText(items.get(i).getDescription());
        itemViewHolder.itemPrice.setText("$" + items.get(i).getPrice().toString());
        itemViewHolder.editButton.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo
//                Firebase.setAndroidContext(view.getContext());
                try {
                    updateItem(i, 10);
                } catch (Exception e) {
                    System.out.println("exception thrown");
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView itemName;
        TextView itemPrice;
        ImageView editButton; // currently can only edit by set amount, no option for percentage


        ItemViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            itemName = (TextView)itemView.findViewById(R.id.item_name);
            itemPrice = (TextView)itemView.findViewById(R.id.item_price);
            editButton = (ImageView)itemView.findViewById(R.id.edit_button);
        }
    }

    private void updateItem(int itemIndex, int percentage) throws Exception {
        System.out.println("got to update proportions");
        Item targetItem = items.get(itemIndex);
//        itemViewHolder.itemName.setText(items.get(i).getDescription());
//        itemViewHolder.itemPrice.setText("$" + items.get(i).getPrice().toString());
    }
}