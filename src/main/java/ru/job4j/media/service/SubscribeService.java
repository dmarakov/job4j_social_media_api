package ru.job4j.media.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.media.entity.User;
import ru.job4j.media.entity.UserRelation;
import ru.job4j.media.repository.UserRelationRepository;
import ru.job4j.media.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class SubscribeService {

    private final UserRepository userRepository;
    private final UserRelationRepository userRelationRepository;

    @Transactional
    public void sendFriendRequest(Long fromUserId, Long toUserId) {
        validateUsersAreDifferent(fromUserId, toUserId);

        User fromUser = getUserById(fromUserId);
        User toUser = getUserById(toUserId);

        UserRelation directRelation = getOrCreateRelation(fromUser, toUser);

        directRelation.setSubscribed(true);

        if (!directRelation.isFriend()) {
            directRelation.setFriend(false);
        }
    }

    @Transactional
    public void acceptFriendRequest(Long userId, Long requesterId) {
        validateUsersAreDifferent(userId, requesterId);

        User user = getUserById(userId);
        User requester = getUserById(requesterId);

        UserRelation incomingRelation = userRelationRepository
            .findByFromUserAndToUser(requester, user)
            .orElseThrow(() -> new IllegalArgumentException("Friend request not found"));

        incomingRelation.setSubscribed(true);
        incomingRelation.setFriend(true);

        UserRelation reverseRelation = getOrCreateRelation(user, requester);
        reverseRelation.setSubscribed(true);
        reverseRelation.setFriend(true);
    }

    @Transactional
    public void rejectFriendRequest(Long userId, Long requesterId) {
        validateUsersAreDifferent(userId, requesterId);

        User user = getUserById(userId);
        User requester = getUserById(requesterId);

        UserRelation incomingRelation = userRelationRepository
            .findByFromUserAndToUser(requester, user)
            .orElseThrow(() -> new IllegalArgumentException("Friend request not found"));

        incomingRelation.setSubscribed(true);
        incomingRelation.setFriend(false);
    }

    @Transactional
    public void removeFriend(Long userId, Long friendId) {
        validateUsersAreDifferent(userId, friendId);

        User user = getUserById(userId);
        User friend = getUserById(friendId);

        UserRelation directRelation = userRelationRepository
            .findByFromUserAndToUser(user, friend)
            .orElseThrow(() -> new IllegalArgumentException("Relation user -> friend not found"));

        UserRelation reverseRelation = userRelationRepository
            .findByFromUserAndToUser(friend, user)
            .orElseThrow(() -> new IllegalArgumentException("Relation friend -> user not found"));

        directRelation.setFriend(false);
        directRelation.setSubscribed(false);

        reverseRelation.setFriend(false);
        reverseRelation.setSubscribed(true);
    }

    @Transactional
    public void unsubscribe(Long fromUserId, Long toUserId) {
        validateUsersAreDifferent(fromUserId, toUserId);

        User fromUser = getUserById(fromUserId);
        User toUser = getUserById(toUserId);

        UserRelation directRelation = userRelationRepository
            .findByFromUserAndToUser(fromUser, toUser)
            .orElseThrow(() -> new IllegalArgumentException("Subscription not found"));

        if (directRelation.isFriend()) {
            UserRelation reverseRelation = userRelationRepository
                .findByFromUserAndToUser(toUser, fromUser)
                .orElseThrow(() -> new IllegalArgumentException("Reverse relation not found"));

            directRelation.setFriend(false);
            directRelation.setSubscribed(false);

            reverseRelation.setFriend(false);
            reverseRelation.setSubscribed(true);
        } else {
            directRelation.setSubscribed(false);
            directRelation.setFriend(false);
        }
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found, id = " + userId));
    }

    private UserRelation getOrCreateRelation(User fromUser, User toUser) {
        return userRelationRepository.findByFromUserAndToUser(fromUser, toUser)
            .orElseGet(() -> userRelationRepository.save(
                UserRelation.builder()
                    .fromUser(fromUser)
                    .toUser(toUser)
                    .subscribed(false)
                    .friend(false)
                    .build()
            ));
    }

    private void validateUsersAreDifferent(Long firstUserId, Long secondUserId) {
        if (firstUserId.equals(secondUserId)) {
            throw new IllegalArgumentException("Users must be different");
        }
    }
}
