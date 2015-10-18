package me.vinnychan.billsplit;

import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

import me.vinnychan.billsplit.model.Item;
import me.vinnychan.billsplit.model.User;

/**
 * Created by vincentchan on 15-10-18.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ItemViewHolder>{

    private List<Item> items;
    private String room;
    private User user;
    private Firebase firebaseRef;

    RVAdapter(List<Item> items, String username, String room){
        this.items = items;
        user = new User(username);
        this.room = room;
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
                    updateItemProportion(i, user, 10);
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

    private void updateItemProportion(int itemIndex, User user, int percentage) throws Exception {
        System.out.println("got to update proportions");
        Item targetItem = items.get(itemIndex);
        targetItem.editUserProportion(user, percentage);
        firebaseRef = new Firebase("https://billsplitdubhacks.firebaseio.com");
//        firebaseRef.child("rooms").child(room).child("items").child(targetItem.getID()).setValue(createItemMap(targetItem));
        firebaseRef.child("rooms").child(room).child("items").updateChildren(createItemMap(targetItem));
//        firebaseRef.child(room).child("items").setValue(targetItem);
        System.out.println("finished updating proportions");
    }

    private HashMap<String, Object> createItemMap(Item item) {
        HashMap<String, Object> itemMap = new HashMap<String, Object>();
        itemMap.put("description",item.getDescription());
        itemMap.put("price", NumberFormat.getCurrencyInstance().format(item.getPrice()).toString());

        HashMap<String, String> proportions = new HashMap<String, String>();
        for (User u: item.getUserProportions().keySet()) {
            BigDecimal amt = item.getUserProportions().get(u);
            proportions.put(u.toString(), NumberFormat.getCurrencyInstance().format(amt).toString());
        }
        itemMap.put("userProportions", proportions);
//
//        itemMap.put("usersWhoDidNotSpecify", item.getUserProportionNotSpecified().toArray());
//        itemMap.put("usersWhoSpecifiedAmts", item.getUserSpecifiedAmtProportion().toArray());
//
//        HashMap<String, String> percentages = new HashMap<String, String>();
//        for (User u: item.getSpecifiedPercentageProportions().keySet()) {
//            percentages.put(u.toString(), item.getSpecifiedPercentageProportions().get(u).toString());
//        }
//        itemMap.put("usersWhoSpecifiedPercentages", percentages);

        HashMap<String, Object> finalMap = new HashMap<String, Object>();
        finalMap.put(item.getID(), itemMap);

        System.out.println("got to create item map");
        return finalMap;
    }

}