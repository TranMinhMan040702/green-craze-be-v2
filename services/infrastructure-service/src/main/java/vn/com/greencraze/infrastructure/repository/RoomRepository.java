package vn.com.greencraze.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.infrastructure.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long>,
        JpaSpecificationExecutor<Room> {

    Room findByUserId(String userId);

}