package service.impl;

import dao.user.AddrMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pojo.user.Addr;
import pojo.user.AddrCriteria;
import service.AddrService;

import java.util.List;

@Service
@Transactional
public class AddrServiceImpl implements AddrService {

    @Autowired
    private AddrMapper addrMapper;

    @Override
    public List<Addr> selectAddrByUsername(String username) {
        AddrCriteria addrCriteria = new AddrCriteria();
        addrCriteria.createCriteria().andUserIdEqualTo(username);
        return addrMapper.selectByExample(addrCriteria);

    }
}
