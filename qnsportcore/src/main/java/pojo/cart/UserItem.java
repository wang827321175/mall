package pojo.cart;

import pojo.product.Sku;
import pojo.user.User;

/**
 * 购物车中商品的实体类
 *
 * @author
 */
public class UserItem {
    //销售单元
    private Sku sku;
    //数量
    private Integer amount;
    //是否有货
    private Boolean isHave;


    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Boolean getHave() {
        return isHave;
    }

    public void setHave(Boolean have) {
        isHave = have;
    }

    @Override
    public String toString() {
        return "UserItem{" +
                "sku=" + sku +
                ", amount=" + amount +
                ", isHave=" + isHave +
                '}';
    }

    //重写hashCode和equals
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((sku == null) ? 0 : sku.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserItem other = (UserItem) obj;
        if (sku == null) {
            if (other.sku != null)
                return false;
            //如果skuId相同的话那么就表示是同款商品
        } else if (!sku.getId().equals(other.getSku().getId()))
            return false;
        return true;
    }
}
