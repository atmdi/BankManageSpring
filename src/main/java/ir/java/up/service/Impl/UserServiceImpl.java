package ir.java.up.service.Impl;

import ir.java.up.dto.*;
import ir.java.up.entity.User;

import java.util.List;

public interface UserServiceImpl {

    BankResponse createAccount(UserRequest userRequest);

    User findById(Long id);

    List<User> findAll();

    void delete(Long id);

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    String nameEnquiry(EnquiryRequest enquiryRequest);

    BankResponse creditAccount(CreditDebitRequest creditDebitRequest);

    BankResponse debitAccount(CreditDebitRequest creditDebitRequest);

    BankResponse transfer(TransferRequest request);
}
