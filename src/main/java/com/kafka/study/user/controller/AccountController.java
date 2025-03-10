package com.kafka.study.user.controller;

import com.kafka.study.user.dto.createAccountRequest;
import com.kafka.study.user.entity.Account;
import com.kafka.study.user.respository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountRepository accountRepository;

    // 데이터를 충전 시켜준다.
    @PostMapping()
    public void addAccount(@RequestBody createAccountRequest request) {
        // 데이터를 모두 지우고
        accountRepository.deleteAll();
        // 데이터를 넣는다.
        accountRepository.save(new Account(request));

    }
}
