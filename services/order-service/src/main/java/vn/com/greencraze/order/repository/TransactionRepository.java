package vn.com.greencraze.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.order.entity.Transaction;

import java.time.Instant;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    List<Transaction> findAllByCreatedAtBetween(Instant startDate, Instant endDate);

}