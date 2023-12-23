package ir.java.up.dto;

import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private  String firstName;
    private  String lastName;
    private  String gender;
    private  String address;
    private  String stateOfOrigin;
    private  String accountNumber;
    private BigDecimal accountBalance;
    private  String email;
    private String password;
    private String phoneNumber;
    private String status;
}
