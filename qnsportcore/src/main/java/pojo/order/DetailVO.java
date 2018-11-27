package pojo.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wang
 * 用来接收前台传递的参数
 */
@Getter
@Setter
@ToString
public class DetailVO {
    private Long[] cartIds;
    private String[] names;
    private Float[] prices;
    private Integer[] counts;
    private String[] sizes;
}
