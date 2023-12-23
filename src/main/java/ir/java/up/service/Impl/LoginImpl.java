package ir.java.up.service.Impl;

import ir.java.up.dto.BankResponse;
import ir.java.up.dto.LoginDto;

public interface LoginImpl {
    BankResponse login(LoginDto loginDto);
}
