package vjj.webconsumer.FeignServices.Imp;

import models.Favorite;
import org.springframework.stereotype.Component;
import vjj.webconsumer.FeignServices.IFeignFavorService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class FeignFavorService implements IFeignFavorService {
    @Override
    public List<Favorite> getFavoriteHistory(Integer uid) {
        return null;
    }

    @Override
    public Set<String> getRank() {
        Set<String> res = new HashSet<>();
        return res;
    }

    @Override
    public boolean updateFavor(Integer uid, Integer mid) {
        return false;
    }

    @Override
    public boolean deleteFavor(Integer uid, Integer mid) {
        return false;
    }

    @Override
    public boolean query(Integer uid, Integer mid) {
        return false;
    }
}
