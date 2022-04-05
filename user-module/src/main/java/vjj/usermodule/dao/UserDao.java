package vjj.usermodule.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import vjj.usermodule.model.User;

import java.util.List;

@Mapper
@Repository
public interface UserDao {
    User queryById(Integer uid);

    User queryByUname(String uname);

    User queryAll(User user);

    List<User> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    int addUser(User user);

//    int updateById(Integer uid);

    int update(User user);

    int deleteById(Integer uid);
}
