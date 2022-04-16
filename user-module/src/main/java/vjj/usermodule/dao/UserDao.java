package vjj.usermodule.dao;

import models.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


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

    int updateUser(User user);

    int updatePassword(@Param("uid") Integer uid, @Param("password") String password);

    int updateUname(String uname);

    int deleteById(Integer uid);

    String queryUserRole(Integer uid);


}
