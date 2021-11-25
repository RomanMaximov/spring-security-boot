package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.UserDao;
import web.model.Role;
import web.model.User;

import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, UserDetailsService{
    private PasswordEncoder passwordEncoder;
    private UserDao userDao;

    @Autowired
    public UserServiceImpl(@Lazy UserDao userDao, @Lazy PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.getUserByUsername(username);
    }

    @Transactional
    @Override
    public User getUserByUsername(String userName){
        return userDao.getUserByUsername(userName);
    }

    @Transactional
    @Override
    public User getUserById(int id){
        return userDao.getUserById(id);
    }

    @Transactional
    @Override
    public void addUser(User user, Set<Role> roles){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.addUser(user, roles);
    }

    @Transactional
    @Override
    public void delete(int id){
        userDao.delete(id);
    }

    @Override
    public List<User> getAllUsers(){
        return userDao.getAllUsers();
    }

    @Transactional
    @Override
    public void update(User user) {
        user.setRoles(userDao.getUserById(user.getId()).getRoles());
        if (!user.getPassword().equals(userDao.getUserById(user.getId()).getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userDao.update(user);
    }
}
