package me.vinnychan.billsplit.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by TING on 17-Oct-2015.
 */
public class Item {
    private String id;
    private String description;
    private BigDecimal price;
    private Map<User, BigDecimal> userProportions;
    private Set<User> userProportionNotSpecified;
    private Set<User> userSpecifiedAmtProportion;
    private Map<User, Integer> specifiedPercentageProportions;

    public Item(String description, BigDecimal price, Receipt receipt) {
        id = createNewId(receipt);
        this.description = description;
        this.price = price;
        userProportions = new HashMap<User, BigDecimal>();
        userProportionNotSpecified = new HashSet<User>();
        userSpecifiedAmtProportion = new HashSet<User>();
        specifiedPercentageProportions = new HashMap<User, Integer>();
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

    public Boolean isUsersProportionManuallySet(User user) {
        if (userSpecifiedAmtProportion.contains(user)) return true;
        if (specifiedPercentageProportions.containsKey(user)) return true;
        return false;
    }

    public void addUser(User user) {
        BigDecimal remainingSum = getAmtNotCoveredBySpecifiedAmts();
        userProportionNotSpecified.add(user);
        BigDecimal dividedPortion = remainingSum.divide(new BigDecimal(userProportionNotSpecified.size()));
        for (User u: userProportionNotSpecified) {
            userProportions.put(u, dividedPortion);
        }
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
        if (userSpecifiedAmtProportion.contains(user)) {
            userSpecifiedAmtProportion.remove(user);
        } else if (specifiedPercentageProportions.containsKey(user)) {
            specifiedPercentageProportions.remove(user);
        } else {
            userProportionNotSpecified.remove(user);
        }
        userProportions.remove(user);
        redistributeNonSpecifiedUsersProportions();
    }

    public void editUserProportion(User user, Integer percentage) throws Exception {
        BigDecimal desiredProportion = price.multiply(new BigDecimal(percentage/100));
        BigDecimal remainingUnspecifiedAmt = getAmtNotCoveredBySpecifiedAmts();
        if (desiredProportion.compareTo(remainingUnspecifiedAmt) == 1) throw new Exception("Percentage is too high!");

        if (userSpecifiedAmtProportion.contains(user)) {

        } else if (specifiedPercentageProportions.containsKey(user)) {

        } else {

        }
        redistributeNonSpecifiedUsersProportions();
        //todo
    }

    public void editUserProportion(User user, BigDecimal amount) throws Exception {
        if (amount.compareTo(getAmtNotCoveredBySpecifiedAmts()) == 1) throw new Exception("Amount is more than possible!");

        if (userSpecifiedAmtProportion.contains(user)) {
            userProportions.put(user, amount);
        } else if (specifiedPercentageProportions.containsKey(user)) {

        } else {

        }
        redistributeNonSpecifiedUsersProportions();
        //todo
    }

    public void removeSpecifiedUserProportion(User user) {
        userSpecifiedAmtProportion.remove(user);
        specifiedPercentageProportions.remove(user);
        userProportionNotSpecified.add(user);
        redistributeNonSpecifiedUsersProportions();
    }

    private String createNewId(Receipt receipt) {
        return receipt.getID() + receipt.getNumItems();
        // TODO; fix up this method -- concurrency, might get clash for ids
        // will probably use Firebase ids instead once it's implemented
    }

    private void redistributeNonSpecifiedUsersProportions() {
        int numUsers = userProportionNotSpecified.size();
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
}
