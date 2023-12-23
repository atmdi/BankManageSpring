package ir.java.up.document;

import ir.java.up.dto.AccountInfo;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BankResponseDoc {

    private  String responseCode;

    private  String responseMessage;

    private AccountInfo accountInfo;



}
