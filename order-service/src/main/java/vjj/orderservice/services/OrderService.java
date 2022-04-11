package vjj.orderservice.services;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vjj.orderservice.mapper.MovieOrderMapper;
import vjj.orderservice.model.MovieOrder;
import vjj.orderservice.model.MovieOrderExample;

import java.util.List;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private MovieOrderMapper movieOrderMapper;

    @Override
    public List<MovieOrder> getAllOrders() {
        return movieOrderMapper.selectByExample(new MovieOrderExample());
    }

    @Override
    public int create(MovieOrder movieOrder) {
        return movieOrderMapper.insertSelective(movieOrder);
    }

    @Override
    public int update(Long id, MovieOrder movieOrder) {
        movieOrder.setId(id);
        return movieOrderMapper.updateByPrimaryKeySelective(movieOrder);
    }

    @Override
    public List<MovieOrder> listOrders(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return movieOrderMapper.selectByExample(new MovieOrderExample());
    }

    @Override
    public MovieOrder getOrder(Long id) {
        return movieOrderMapper.selectByPrimaryKey(id);
    }

}
