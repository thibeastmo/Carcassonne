package be.kdg.backend_game.service;

import be.kdg.backend_game.domain.user_management.Account;
import be.kdg.backend_game.domain.user_management.FriendRequest;
import be.kdg.backend_game.domain.user_management.Friendship;
import be.kdg.backend_game.repository.FriendRequestRepository;
import be.kdg.backend_game.repository.FriendshipRepository;
import be.kdg.backend_game.service.dto.FriendRequestDto;
import be.kdg.backend_game.service.exception.InvalidFriendRequestException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FriendshipService {
    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipRepository friendshipRepository;

    public FriendshipService(FriendRequestRepository friendRequestRepository, FriendshipRepository friendshipRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.friendshipRepository = friendshipRepository;
    }

    public FriendRequestDto createFriendRequest(Account requester, Account requested) {
        boolean alreadyFriends = friendshipRepository.friendshipExists(requester, requested);
        boolean friendRequestAlreadyExists = friendRequestRepository.friendRequestExists(requester, requested);
        if (alreadyFriends) {
            throw new InvalidFriendRequestException("Friendship already exists");
        }
        if (friendRequestAlreadyExists) {
            throw new InvalidFriendRequestException("Friendrequest already exists");
        }
        FriendRequest request = friendRequestRepository.save(new FriendRequest(requester, requested));
        return new FriendRequestDto(request);
    }

    public void createFriendship(Account requested, UUID friendRequestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId)
                .orElseThrow(() -> new InvalidFriendRequestException("Could not find friendrequest for friendrequest id " + friendRequestId));
        if (!friendRequest.getRequested().getAccountId().equals(requested.getAccountId())) {
            throw new InvalidFriendRequestException("Requested id doesn't match friendrequest requested id");
        }
        boolean alreadyFriends = friendshipRepository.friendshipExists(friendRequest.getRequester(), requested);
        if (alreadyFriends) {
            throw new InvalidFriendRequestException("Friendship already exists");
        }
        friendshipRepository.save(new Friendship(friendRequest.getRequester(), requested));
        friendRequestRepository.delete(friendRequest);
    }

    public Optional<FriendRequest> retrieveFriendRequestByFriendRequestId(UUID friendRequestId) {
        return friendRequestRepository.findById(friendRequestId);
    }

    public void removeFriendRequest(FriendRequest friendRequest) {
        friendRequestRepository.delete(friendRequest);
    }

    public List<FriendRequestDto> retrieveFriendRequestsByRequestedId(UUID accountId) {
        List<FriendRequest> friendRequests = friendRequestRepository.findAllByRequested_AccountId(accountId);
        List<FriendRequestDto> friendRequestDtos = new ArrayList<>();
        for (FriendRequest friendRequest: friendRequests) {
            friendRequestDtos.add(new FriendRequestDto(friendRequest));
        }
        return friendRequestDtos;
    }

    public void removeFriendship(Friendship friendship) {
        friendshipRepository.delete(friendship);
    }

    public Optional<Friendship> retrieveFriendshipByAccountIdAndFriendId(Account account, Account friend) {
        return friendshipRepository.findByAccountAndFriend(account, friend);
    }

    public List<Friendship> retrieveAllFriendshipsByAccount(Account account) {
        return friendshipRepository.findAllByAccount(account);
    }
}
