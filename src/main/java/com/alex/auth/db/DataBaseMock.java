package com.alex.auth.db;

import com.alex.auth.model.Challenge;
import com.alex.auth.model.User;
import org.springframework.stereotype.Component;

import javax.ws.rs.NotFoundException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component public class DataBaseMock implements DataBase {

    private Map<String, User> userMap = new ConcurrentHashMap<String, User>();
    private Map<String, Challenge> challengeMap = new ConcurrentHashMap<String, Challenge>();

    @Override public void createUser(User user) {
        String id = Integer.toString(getId(userMap));
        user.setId(id);
        userMap.put(id, user);
    }

    @Override public void updateUser(User user) {
        String id = user.getId();
        if (!userMap.containsKey(id)) {
            throw new NotFoundException();
        }
        userMap.put(id, user);
    }

    @Override public User getUser(String userID) {
        return userMap.get(userID);
    }

    @Override public Challenge getChallenge(String id) {
        if (!challengeMap.containsKey(id)) {
            throw new NotFoundException();
        }
        return challengeMap.get(id);
    }

    @Override public void createChallenge(Challenge challenge) {
        String id = Integer.toString(getId(challengeMap));
        challenge.setId(id);
        challengeMap.put(id, challenge);
    }

    @Override public void deleteChallenge(String id) {
        challengeMap.remove(id);
    }

    private int getId(Map<?, ?> map) {
        return map.size();
    }
}
