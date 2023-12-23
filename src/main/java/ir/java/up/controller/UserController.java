package ir.java.up.controller;

import ir.java.up.dto.*;
import ir.java.up.entity.User;
import ir.java.up.service.LoginService;
import ir.java.up.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;
    //UserServiceImplmp userService;

    @Autowired
    LoginService loginService;

    @PostMapping
    @Transactional
    public BankResponse CreateAccount(@RequestBody UserRequest userRequest){
        return  userService.createAccount(userRequest);
    }


    @GetMapping
    public ResponseEntity<List<User>> findAll(){
        return ResponseEntity.ok(userService.findAll());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id){
        User ById=userService.findById(id);
        return ResponseEntity.ok(ById);
    }
    //estelam
    @GetMapping("/balanceEnquiry")
    public  BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest)
    {return  userService.balanceEnquiry(enquiryRequest);}

    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest)
    {
        return  userService.nameEnquiry(enquiryRequest);
    }

    @PostMapping("/credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest request)
    {
        return  userService.creditAccount(request);
    }

    @PostMapping("/debit")
    public  BankResponse debitAccount(@RequestBody CreditDebitRequest request)
    {
        return  userService.debitAccount(request);
    }

    @PostMapping("/transfer")
    @Transactional
    public  BankResponse transfer(@RequestBody TransferRequest request)
    {

        return  userService.transfer(request);
    }

    @PostMapping("/login")
    public  BankResponse login(@RequestBody LoginDto loginDto){
        return  loginService.login(loginDto);
    }
}
