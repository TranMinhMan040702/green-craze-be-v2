package vn.com.greencraze.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.infrastructure.entity.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>,
        JpaSpecificationExecutor<Message> {

    List<Message> findAllByRoomId(Long roomId);

    List<Message> findByRoomIdAndUserIdNotLike(Long roomId, String userId);

    List<Message> findByRoomIdAndUserIdLike(Long roomId, String userId);

}