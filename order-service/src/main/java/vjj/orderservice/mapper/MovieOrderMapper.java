package vjj.orderservice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import vjj.orderservice.model.MovieOrder;
import vjj.orderservice.model.MovieOrderExample;

@Mapper
@Component
public interface MovieOrderMapper {
    int countByExample(MovieOrderExample example);

    int deleteByExample(MovieOrderExample example);

    int deleteByPrimaryKey(Long id);

    int insert(MovieOrder record);

    int insertSelective(MovieOrder record);

    List<MovieOrder> selectByExample(MovieOrderExample example);

    MovieOrder selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MovieOrder record, @Param("example") MovieOrderExample example);

    int updateByExample(@Param("record") MovieOrder record, @Param("example") MovieOrderExample example);

    int updateByPrimaryKeySelective(MovieOrder record);

    int updateByPrimaryKey(MovieOrder record);
}