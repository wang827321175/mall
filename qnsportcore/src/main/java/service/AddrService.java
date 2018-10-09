package service;

import pojo.user.Addr;

import java.util.List;

/**
 * 收货地址和其他信息
 *
 * @author wang
 */
public interface AddrService {
    List<Addr> selectAddrByUsername(String username);
}
