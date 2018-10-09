package pojo.cart;

import org.codehaus.jackson.annotate.JsonIgnore;
import pojo.product.Sku;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车的实体类
 *
 * @author
 */
public class UserCart {
    private List<UserItem> items = new ArrayList<UserItem>();

    public List<UserItem> getItems() {
        return items;
    }

    public void setItems(List<UserItem> items) {
        this.items = items;
    }

    public void addUserItem(UserItem userItem) {
        //有同款商品,数量追加
        if (items.contains(userItem)) {
            for (UserItem item : items) {
                if (userItem.equals(item)) {
                    item.setAmount(userItem.getAmount() + item.getAmount());
                }
            }
        } else {
            items.add(userItem);
        }
    }

    /**
     * 数量小计
     *
     * @return
     */
    @JsonIgnore
    public Integer getProductAmount() {
        Integer sum = 0;
        for (UserItem item : items) {
            sum += item.getAmount();
        }
        return sum;
    }

    /**
     * 商品总金额
     *
     * @return
     */
    @JsonIgnore
    public Double getPrice() {
        Double price = 0d;
        for (UserItem item : items) {
            price += item.getAmount() * item.getSku().getPrice();
        }
        return price;
    }

    /**
     * 运费
     *
     * @return
     */
    @JsonIgnore
    public Float getExtra() {
        //大于99免运费
        Double maxExtra = 99d;
        //0元的时候运费也为0
        Double minExtra = 0d;

        if (getPrice() <= maxExtra && getPrice() > minExtra) {
            return 10f;
        } else {
            return 0f;
        }
    }

    /**
     * 总金额(商品总金额+运费)
     *
     * @return
     */
    @JsonIgnore
    public Double getAllPrice() {
        return getPrice() + getExtra();
    }

    /**
     * 删除购物车中的购物项
     */
    public void delUserItem(Long skuId) {
        Sku sku = new Sku();
        sku.setId(skuId);
        UserItem userItem = new UserItem();
        userItem.setSku(sku);

        items.remove(userItem);
    }

    public void clearCart() {
        items.clear();
    }
}
