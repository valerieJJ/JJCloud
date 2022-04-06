package vjj.usermodule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vjj.usermodule.dao.TBDao;
import vjj.usermodule.model.TB1;


@Service
public class TBServiceImp implements TBService1 {
    @Autowired
    TBDao tbMapper;

    @Override
    public TB1 queryByMid(int mid) {
        return tbMapper.getTB(mid);
    }
}
