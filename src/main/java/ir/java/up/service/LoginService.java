package ir.java.up.service;

import ir.java.up.config.JwtTokenProvider;
import ir.java.up.dto.BankResponse;
import ir.java.up.dto.EmailDetails;
import ir.java.up.dto.LoginDto;
import ir.java.up.repository.BankResponseRepository;
import ir.java.up.repository.UserRepository;
import ir.java.up.service.Impl.LoginImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements LoginImpl {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    BankResponseRepository bankResponseRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;


    public BankResponse login(LoginDto loginDto){
        Authentication authentication=null;
        authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
        );
        EmailDetails loginAlert=EmailDetails.builder()
                .subject("you are logged in!")
                .recipient(loginDto.getEmail())
                .messageBody("you logged into youre account.")
                .build();

        emailService.sendEmailAlert(loginAlert);
        return  BankResponse.builder()
                .responseCode("login success")
                .responseMessage(jwtTokenProvider.generateToken(authentication))
                .build();
    }
}
