package vjj.movieservice.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import vjj.movieservice.domain.TB1;


@Mapper
@Repository
public interface TBDao {
    @Select("select * from tb1 where mid=#{mid}")
    TB1 getTB(@Param("mid") int mid);
}
