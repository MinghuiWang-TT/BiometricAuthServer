package com.alex.auth.db;

import com.alex.auth.model.Challenge;
import com.alex.auth.model.User;

public interface DataBase {

    void saveUser(User user);

    User getUser(String userID);

    void createChallenge(Challenge challenge);

    Challenge getChallenge(String id);

    void deleteChallenge(String id);
}
