package ir.java.up.service;

import ir.java.up.dto.*;
import ir.java.up.entity.Role;
import ir.java.up.entity.User;
import ir.java.up.repository.BankResponseRepository;
import ir.java.up.repository.UserRepository;
import ir.java.up.service.Impl.UserServiceImpl;
import ir.java.up.util.AccountUtils;
import org.apache.catalina.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
public class UserService implements UserServiceImpl {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    BankResponseRepository bankResponseRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override

    public BankResponse createAccount(UserRequest userRequest) {

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            BankResponse bankResponse = BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
            //save in mongodb
            bankResponseRepository.save(bankResponse);
            return bankResponse;

        }
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .gender(userRequest.getGender())
                .accountBalance(BigDecimal.ZERO)
                .address(userRequest.getAddress())
                .phoneNumber(userRequest.getPhoneNumber())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .accountNumber(AccountUtils.generateAccountNumber())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .status("Active")
                .role(Role.valueOf("ROLE_USER"))

                .build();

        User savedUser = userRepository.save(newUser);
        //SEND EMAIL
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Account creation")
                .messageBody("congratulations your account successfully created\n" + " " + "your account name" +
                        savedUser.getLastName() + " " + savedUser.getLastName() + " \n" + "accountNumber:" +
                        savedUser.getAccountNumber())
                .build();

        emailService.sendEmailAlert(emailDetails);
        BankResponse bankResponse = BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + "" + savedUser.getLastName() + " ")
                        .build())
                .build();
        bankResponseRepository.save(bankResponse);
        return bankResponse;
    }

    @Cacheable(cacheNames = "UserService_ById")
    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("not found person"));
    }

    @Cacheable(cacheNames = "UserService_findAll")
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @CacheEvict(cacheNames = "UserService_findAll")
    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isAccountExist) {
            BankResponse bankResponse = BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
            //save in mongoDB
            bankResponseRepository.save(bankResponse);
            return bankResponse;
        }


        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountName(foundUser.getFirstName() + foundUser.getLastName())
                        .accountNumber(foundUser.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());

        if (!isAccountExist) {
            BankResponse bankResponse = BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
            //save in mongoDB
            bankResponseRepository.save(bankResponse);
            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }
        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getLastName();
    }

    //sharg hesab
    @Override
    public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
        //check kardan account
        boolean isAccountExist = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        BigInteger amount = creditDebitRequest.getAmount().toBigInteger();
        if (!isAccountExist || amount.intValue() < 10000) {
            BankResponse bankResponse = BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE+"  OR amount not valid")
                    .accountInfo(null)
                    .build();
            //save in mongoDB
            bankResponseRepository.save(bankResponse);
            return bankResponse;

        }
        User userToCredit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditDebitRequest.getAmount()));
        userRepository.save(userToCredit);

        return BankResponse.builder()
                .responseCode(AccountUtils.CODE_SUCCESS)
                .responseMessage(AccountUtils.SUCCESS)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + "" +
                                userToCredit.getLastName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(userToCredit.getAccountNumber())
                        .build())
                .build();
    }

    //bardasht az hesab
    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {

        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            BankResponse bankResponse = BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
            bankResponseRepository.save(bankResponse);
            return bankResponse;
        }
        User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
        BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = request.getAmount().toBigInteger();
        if (availableBalance.intValue() < debitAmount.intValue()) {
            BankResponse bankResponse = BankResponse.builder()
                    .responseCode(AccountUtils.CODE_FAIL)
                    .responseMessage(AccountUtils.FAIL)
                    .accountInfo(null)
                    .build();
//save in mongodb
            bankResponseRepository.save(bankResponse);
            return bankResponse;
        } else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userRepository.save(userToDebit);
            return BankResponse.builder()
                    .responseCode(AccountUtils.CODE_SUCCESS)
                    .responseMessage(AccountUtils.SUCCESS)
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(request.getAccountNumber())
                            .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName())
                            .accountBalance(userToDebit.getAccountBalance())
                            .build())
                    .build();
        }

    }
    //transfer

    @Override
    public BankResponse transfer(TransferRequest request) {
        //CHEK KON HESAB MABDA VOJOD DARAD?
        boolean isSourceAccountExist=userRepository.existsByAccountNumber(request.getSourceAccountNumber());
        //CHEK KON HESAB MAGHSAD VOJOD DARAD
        boolean isDestinationAccountExist=userRepository.existsByAccountNumber(request.getDestinationAccountNumber());

        if(!isDestinationAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE+" DestinationAccountNotExist")
                    .accountInfo(null)
                    .build();
        }
        if(!isSourceAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE+" SourceAccounNottExist")
                    .accountInfo(null)
                    .build();

        }

        User sourceAccountUser=userRepository.findByAccountNumber(request.getSourceAccountNumber());
        //agar mojodi hesab kam bashad
        if(request.getAmount().compareTo(sourceAccountUser.getAccountBalance())>0){
            return  BankResponse.builder()
                            .responseCode(AccountUtils.CODE_FAIL)
                                    .responseMessage(AccountUtils.FAIL+"not valid amount")
                                            .accountInfo(null)
                    .build();

        }
        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(sourceAccountUser);

        //sharj hesab maghsad
        User destinationAccountUser=userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));

        userRepository.save(destinationAccountUser);

        //send email transfer hesab maghsad
        EmailDetails debitAlert=EmailDetails.builder()
                .subject("TRANSFERD AND DEBIT ALERT")
                .recipient(sourceAccountUser.getEmail())
                .messageBody("the sum of:"+request.getAmount()+"has been deducated from your account." )
                        // your account balance is:"+sourceAccountUser.getAccountBalance().subtract(request.getAmount()))

                .build();

        emailService.sendEmailAlert(debitAlert);

        //send email transfer hesab mabda
        EmailDetails creditAlert=EmailDetails.builder()
                .subject("TRANSFERD AND credit ALERT")
                .recipient(destinationAccountUser.getEmail())
                .messageBody("the sum of:"+request.getAmount()+"has been sent to your account. " )
                        //your account balance is:"+destinationAccountUser.getAccountBalance().add(request.getAmount()))

                .build();

        emailService.sendEmailAlert(creditAlert);


        return  BankResponse.builder()
                .responseCode(AccountUtils.CODE_SUCCESS)
                .responseMessage(AccountUtils.SUCCESS+" TRANSFERD")
                .accountInfo(null)

                .build();

    }




}
