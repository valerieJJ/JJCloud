package vjj.usermodule.service;


import vjj.usermodule.model.User;

import java.util.List;

public interface UserService {

    User queryById(Integer uid);

    User queryByName(String uname);

    User queryAll(User user);

    // 查询多条数据
    List<User> queryAllByLimit(int offset, int limit);

    User addUser(User user);

//    User updateById(Integer uid);

    User updatePassword(User user);

    boolean deleteById(Integer uid);

    String getRole(Integer uid);
}
