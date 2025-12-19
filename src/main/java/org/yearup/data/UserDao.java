package org.yearup.data;

import org.yearup.models.User;

import java.util.List;

public interface UserDao {
    // Get user by username (used for authentication)
    User getByUsername(String username);

    List<User> getAll();

    User getUserById(int userId);

    int getIdByUsername(String username);

    User create(User user);

    User getByUserName(String username);

    boolean exists(String username);


}
