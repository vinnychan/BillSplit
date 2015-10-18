package me.vinnychan.billsplit;

import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

import me.vinnychan.billsplit.model.Item;
import me.vinnychan.billsplit.model.Room;
import me.vinnychan.billsplit.model.User;

/**
 * Created by vincentchan on 15-10-18.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ItemViewHolder>{

    private List<Item> items;
    private String room;
    private User user;

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
        ItemViewHolder ivh = new ItemViewHolder(v, user, room, items);
        return ivh;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int i) {
        itemViewHolder.itemName.setText(items.get(i).getDescription());
        itemViewHolder.itemPrice.setText("$" + items.get(i).getPrice().toString());
        itemViewHolder.itemUUID.setText(items.get(i).getID());
        itemViewHolder.editButton.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView itemName;
        TextView itemPrice;
        TextView itemUUID;
        ImageView editButton; // currently can only edit by set amount, no option for percentage
        SeekBar seekBar;
        TextView amountPaying;
        TextView percentage;
        Firebase firebaseRef;

        ItemViewHolder(View itemView, User u, String rm, List<Item> its) {
            super(itemView);
            final User user = u;
            final String room = rm;
            final List<Item> items = its;
            cv = (CardView)itemView.findViewById(R.id.cv);
            itemName = (TextView)itemView.findViewById(R.id.item_name);
            itemPrice = (TextView)itemView.findViewById(R.id.item_price);
            itemUUID = (TextView)itemView.findViewById(R.id.item_uuid);
            editButton = (ImageView)itemView.findViewById(R.id.edit_button);
            seekBar = (SeekBar) itemView.findViewById(R.id.SeekBarId);
            amountPaying = (TextView) itemView.findViewById(R.id.amount_paying);
            percentage = (TextView) itemView.findViewById(R.id.percentage);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                public void onStopTrackingTouch(SeekBar bar) {
                    int value = bar.getProgress(); // the value of the seekBar progress
                    String itemID = itemUUID.getText().toString();
                    try {
                        RVAdapter.updateItemProportion(itemID, user, value, items, room);
                    } catch (Exception e) {
                        Toast.makeText(bar.getContext(),e.getMessage(),Toast.LENGTH_SHORT);
                    }
                }

                public void onStartTrackingTouch(SeekBar bar) {

                }

                public void onProgressChanged(SeekBar bar,
                                              int paramInt, boolean paramBoolean) {
                    percentage.setText("" + paramInt + "%"); // here in textView the percent will be shown
                    amountPaying.setText("Amount paying: $" +
                            String.format("%.2f", Double.parseDouble((String) itemPrice.getText()
                                    .subSequence(1, itemPrice.getText().length())) * (double) paramInt / 100));
                }
            });
        }
    }

    static private void updateItemProportion(String itemID, User user, int percentage, List<Item> items, String room) throws Exception {
        System.out.println("got to update proportions");
        Item targetItem = null;
        for (Item i: items) {
            if (i.getID().equals(itemID))
                targetItem = i;
        }
        if (targetItem == null) return;
        targetItem.editUserProportion(user, percentage);
        Firebase firebaseRef = new Firebase("https://billsplitdubhacks.firebaseio.com");
//        firebaseRef.child("rooms").child(room).child("items").child(targetItem.getID()).setValue(createItemMap(targetItem));
        firebaseRef.child("rooms").child(room).child("items").updateChildren(createItemMap(targetItem));
//        firebaseRef.child(room).child("items").setValue(targetItem);
        System.out.println("finished updating proportions");
    }

    static private HashMap<String, Object> createItemMap(Item item) {
        HashMap<String, Object> itemMap = new HashMap<String, Object>();
        itemMap.put("description",item.getDescription());
        itemMap.put("price", NumberFormat.getCurrencyInstance().format(item.getPrice()).toString());

        HashMap<String, String> proportions = new HashMap<String, String>();
        for (User u: item.getUserProportions().keySet()) {
            BigDecimal amt = item.getUserProportions().get(u);
            proportions.put(u.toString(), NumberFormat.getCurrencyInstance().format(amt).toString());
        }
        itemMap.put("userProportions", proportions);

        HashMap<String, String> usersWhoDidNotSpecify = new HashMap<String, String>();
        for (User u: item.getUserProportionNotSpecified()) {
            usersWhoDidNotSpecify.put("name", u.getName());
        }
        itemMap.put("usersWhoDidNotSpecify", usersWhoDidNotSpecify);

        HashMap<String, String> usersWhoSpecifiedAmts = new HashMap<String, String>();
        for (User u: item.getUserSpecifiedAmtProportion()) {
            usersWhoSpecifiedAmts.put("name", u.getName());
        }
        itemMap.put("usersWhoSpecifiedAmts", usersWhoSpecifiedAmts);

        HashMap<String, String> percentages = new HashMap<String, String>();
        for (User u: item.getSpecifiedPercentageProportions().keySet()) {
            percentages.put(u.toString(), item.getSpecifiedPercentageProportions().get(u).toString());
        }
        itemMap.put("usersWhoSpecifiedPercentages", percentages);

        HashMap<String, Object> finalMap = new HashMap<String, Object>();
        finalMap.put(item.getID(), itemMap);

        System.out.println("got to create item map");
        return finalMap;
    }
}