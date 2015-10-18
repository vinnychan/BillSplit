package me.vinnychan.billsplit.model;

import com.firebase.client.Firebase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TING on 17-Oct-2015.
 */
public class Item {
    private String id;
    private String description;
    private BigDecimal price;
    private Map<User, BigDecimal> userProportions;

    public Item(String description, BigDecimal price) {
//        id = createNewId(receipt);
        this.description = description;
        this.price = price;
        userProportions = new HashMap<User, BigDecimal>();
    }

    private List<Item> items;

    private Firebase firebaseRef;

    public void initializeData() {
        items = new ArrayList<>();
        items.add(new Item("Item 1", new BigDecimal(5.00)));
        items.add(new Item("Item 2", new BigDecimal(15.00)));
        items.add(new Item("Item 3", new BigDecimal(10.00)));

    }

    public List<Item> getItems() {
        initializeData();
        return items;
    }

    public String getID() { return id; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public Map<User, BigDecimal> getUserProportions() { return userProportions; }

    public void setDescription(String descrip) { description = descrip; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getUsersProportion(User user) {
        return userProportions.get(user);
    }

    public void addUser(User user) {
        int numUsers = userProportions.size();
        //todo: will be implemented
    }

    public void addUser(User user, int percentage) {
        //todo
    }

    public void addUser(User user, BigDecimal amount) {

    }

    public void removeUser(User user) {
        //todo: will also be implemented
    }

    public void editUserProportion(int percentage) {
        //todo
    }

    public void editUserProportion(BigDecimal amount) {

    }

    private String createNewId(Receipt receipt) {
        return receipt.getID() + receipt.getNumItems();
        // TODO; fix up this method -- concurrency, might get clash for ids
        // will probably use Firebase ids instead once it's implemented
    }
}
