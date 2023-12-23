package ir.java.up.repository;

import ir.java.up.document.BankResponseDoc;
import ir.java.up.dto.BankResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface BankResponseRepository extends MongoRepository<BankResponse,String> {
}
