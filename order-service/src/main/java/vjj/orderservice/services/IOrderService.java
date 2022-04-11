package vjj.orderservice.services;

import models.Movie;
import vjj.orderservice.model.MovieOrder;

import java.util.List;

public interface IOrderService {
    List<MovieOrder> getAllOrders();

    int create(MovieOrder movieOrder);

    int update(Long id, MovieOrder movieOrder);

    List<MovieOrder> listOrders(int pageNum, int pageSize);

    MovieOrder getOrder(Long id);

}
