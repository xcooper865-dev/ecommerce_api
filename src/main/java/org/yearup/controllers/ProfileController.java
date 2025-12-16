package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
@PreAuthorize("isAuthenticated()")  // Fixed: add parentheses
public class ProfileController {

    private final ProfileDao profileDao;
    private final UserDao userDao;

    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao) {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    @GetMapping
    public Profile getProfile(Principal principal) {
        User user = userDao.getByUsername(principal.getName());  // Fixed method name
        if (user == null) {
            throw new RuntimeException("User not found: " + principal.getName());
        }
        return profileDao.getByUserId(user.getId());
    }

    @PutMapping
    public void updateProfile(@RequestBody Profile profile, Principal principal) {
        User user = userDao.getByUsername(principal.getName());  // Fixed method name
        if (user == null) {
            throw new RuntimeException("User not found: " + principal.getName());
        }
        profileDao.update(user.getId(), profile);
    }
}
