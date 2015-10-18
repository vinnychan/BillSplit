package me.vinnychan.billsplit.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by TING on 17-Oct-2015.
 */
public class Item {
    private String id;
    private String description;
    private BigDecimal price;
    private Map<User, BigDecimal> userProportions;

    public Item(String description, BigDecimal price, Receipt receipt) {
        id = createNewId(receipt);
        this.description = description;
        this.price = price;
        userProportions = new HashMap<User, BigDecimal>();
    }

    private String createNewId(Receipt receipt) {
        return receipt.getID() + receipt.getNumItems();
        // TODO; fix up this method -- concurrency, might get clash for ids
        // will probably use Firebase ids instead once it's implemented
    }
}
