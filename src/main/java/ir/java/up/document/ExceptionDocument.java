package ir.java.up.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("MESSAGE_EXCEPTION")
public class ExceptionDocument {
    @Id
    private String id;
    private String message;
    @Indexed(unique = true)
    private String code;
}
