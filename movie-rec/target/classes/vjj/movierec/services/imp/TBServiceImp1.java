package vjj.movierec.services.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vjj.movierec.dao.TBDao;
import vjj.movierec.domain.TB1;
import vjj.movierec.services.TBService1;


@Service
public class TBServiceImp1 implements TBService1 {
    @Autowired
    TBDao tbMapper;

    @Override
    public TB1 queryByMid(int mid) {
        return tbMapper.getTB(mid);
    }
}
