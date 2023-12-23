package ir.java.up.service;

import ir.java.up.document.ExceptionDocument;
import ir.java.up.repository.ExceptionDocumentRepository;
import ir.java.up.service.Impl.ExceptionDocumentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExceptionDocumentService implements ExceptionDocumentServiceImpl {

    @Autowired
    ExceptionDocumentRepository exceptionDocumentRepository;

    @Override
    public void saveException(ExceptionDocument exceptionDocument) {
        exceptionDocumentRepository.save(exceptionDocument);
    }
}
