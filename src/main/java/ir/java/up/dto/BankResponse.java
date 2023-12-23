package ir.java.up.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankResponse {

    private  String responseCode;

    private  String responseMessage;

    private AccountInfo accountInfo;

}
