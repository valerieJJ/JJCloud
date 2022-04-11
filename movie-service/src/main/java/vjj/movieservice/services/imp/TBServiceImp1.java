package vjj.movieservice.services.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vjj.movieservice.dao.TBDao;
import vjj.movieservice.domain.TB1;
import vjj.movieservice.services.TBService1;


@Service
public class TBServiceImp1 implements TBService1 {
    @Autowired
    TBDao tbMapper;

    @Override
    public TB1 queryByMid(int mid) {
        return tbMapper.getTB(mid);
    }
}
