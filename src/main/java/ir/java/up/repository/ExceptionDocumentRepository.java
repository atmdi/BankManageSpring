package ir.java.up.repository;

import ir.java.up.document.ExceptionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExceptionDocumentRepository extends MongoRepository<ExceptionDocument, String> {
}
