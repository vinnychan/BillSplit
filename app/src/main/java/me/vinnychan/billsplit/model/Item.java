package me.vinnychan.billsplit.model;

import com.firebase.client.Firebase;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by TING on 17-Oct-2015.
 */

public class Item implements Serializable{
    private String id;
    private String description;
    private BigDecimal price;
    private Map<User, BigDecimal> userProportions;
    private Set<User> userProportionNotSpecified;
    private Set<User> userSpecifiedAmtProportion;
    private Map<User, Integer> specifiedPercentageProportions;

    public Item(String description, BigDecimal price) {
        id = UUID.randomUUID().toString();
        this.description = description;
        this.price = price;
        userProportions = new HashMap<User, BigDecimal>();
        userProportionNotSpecified = new HashSet<User>();
        userSpecifiedAmtProportion = new HashSet<User>();
        specifiedPercentageProportions = new HashMap<User, Integer>();

    }

    private List<Item> items;

    public String getID() { return id; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public Map<User, BigDecimal> getUserProportions() { return userProportions; }
    public Set<User> getUserProportionNotSpecified() { return userProportionNotSpecified; }
    public Set<User> getUserSpecifiedAmtProportion() { return userSpecifiedAmtProportion; }
    public Map<User, Integer> getSpecifiedPercentageProportions() { return specifiedPercentageProportions; }

    public void setDescription(String descrip) { description = descrip; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getUsersProportion(User user) {
        return userProportions.get(user);
    }

    public Boolean isUsersProportionManuallySet(User user) {
        return (userSpecifiedAmtProportion.contains(user) || specifiedPercentageProportions.containsKey(user));
    }

    public void addUser(User user) {
        userProportionNotSpecified.add(user);
        redistributeNonSpecifiedUsersProportions();
    }

    public void addUser(User user, int percentage) throws Exception {
        addUser(user);
        editUserProportion(user, percentage);
    }

    public void addUser(User user, BigDecimal amount) throws Exception {
        addUser(user);
        editUserProportion(user, amount);
    }

    public void removeUser(User user) {
        userSpecifiedAmtProportion.remove(user);
        specifiedPercentageProportions.remove(user);
        userProportionNotSpecified.remove(user);
        userProportions.remove(user);
        redistributeNonSpecifiedUsersProportions();
    }

    public void editUserProportion(User user, Integer percentage) throws Exception {
//        BigDecimal desiredProportion = price.multiply(new BigDecimal(percentage/100));
//        BigDecimal remainingUnspecifiedAmt = getAmtNotCoveredBySpecifiedAmts();
//        if (desiredProportion.compareTo(remainingUnspecifiedAmt) == 1) throw new Exception("Percentage is too high!");

        userSpecifiedAmtProportion.remove(user);
        specifiedPercentageProportions.put(user, percentage);
        userProportionNotSpecified.remove(user);
        userProportions.put(user, price.multiply(new BigDecimal(percentage/100)));
        redistributeNonSpecifiedUsersProportions();
    }

    public void editUserProportion(User user, BigDecimal amount) throws Exception {
        if (amount.compareTo(getAmtNotCoveredBySpecifiedAmts()) == 1) throw new Exception("Amount is more than possible!");

        userProportions.put(user, amount);
        specifiedPercentageProportions.remove(user);
        userProportionNotSpecified.remove(user);
        userProportions.put(user, amount);
        redistributeNonSpecifiedUsersProportions();
    }

    public void removeSpecifiedUserProportion(User user) {
        userSpecifiedAmtProportion.remove(user);
        specifiedPercentageProportions.remove(user);
        userProportionNotSpecified.add(user);
        redistributeNonSpecifiedUsersProportions();
    }

    private void redistributeNonSpecifiedUsersProportions() {
        int numUsers = userProportionNotSpecified.size();
        if (numUsers == 0) return;
        BigDecimal sumToDistribute = getAmtNotCoveredBySpecifiedAmts();
        BigDecimal amtPerUser = sumToDistribute.divide(new BigDecimal(numUsers));
        for (User u: userProportionNotSpecified) {
            userProportions.put(u, amtPerUser);
        }
    }

    private BigDecimal getAmtNotCoveredBySpecifiedAmts() {
        ArrayList<User> usersWhoSpecifiedProportions = new ArrayList<User>(userProportions.size());
        usersWhoSpecifiedProportions.addAll(userSpecifiedAmtProportion);
        usersWhoSpecifiedProportions.addAll(specifiedPercentageProportions.keySet());
        BigDecimal remainingSum = price;
        for (User u: usersWhoSpecifiedProportions) {
            remainingSum = remainingSum.subtract(userProportions.get(u));
        }
        return remainingSum;
    }

    private boolean proportionsAddUp() {
        Collection<BigDecimal> proportions = userProportions.values();
        BigDecimal totalSoFar = new BigDecimal(0);
        for (BigDecimal amt: proportions) {
            totalSoFar.add(amt);
            if (totalSoFar.compareTo(price) == 1) return false;
        }
        return totalSoFar.equals(price);
    }

    public String toString() {
        return description + " ($" + price + ")";
    }
}
