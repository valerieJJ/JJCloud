package vjj.usermodule.service;

import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import vjj.usermodule.dao.UserDao;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User queryById(Integer uid) {
        return this.userDao.queryById(uid);
    }

    @Override
    public User queryByName(String uname) {
        return this.userDao.queryByUname(uname);
    }

    @Override
    public User queryAll(User user) {
        return this.userDao.queryAll(user);
    }

    @Override
    public List<User> queryAllByLimit(int offset, int limit) {
        return this.userDao.queryAllByLimit(offset, limit);
    }

    @Override
    @Transactional
    public User addUser(User user) {
        this.userDao.addUser(user);
        return user;
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        this.userDao.updateUser(user);
        return this.queryById(user.getUid());
    }

    @Override
    public User updatePwd(Integer uid, String password) {
        this.userDao.updatePassword(uid, password);
        return this.queryById(uid);
    }

    @Override
    @Transactional
    public boolean deleteById(Integer uid) {
        return this.userDao.deleteById(uid)>0;
    }

    @Override
    public String getRole(Integer uid) {
        return userDao.queryUserRole(uid);
    }
}
