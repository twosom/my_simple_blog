package com.icloud.my_portfolio.account.controller;


import com.icloud.my_portfolio.account.Account;
import com.icloud.my_portfolio.account.dto.AccountDto;
import com.icloud.my_portfolio.exception.UserEmailDuplicateException;
import com.icloud.my_portfolio.exception.UserNameDuplicateException;
import com.icloud.my_portfolio.account.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;
    private final ModelMapper mapper;

    @GetMapping("/login")
    public String login() {
        return "account/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
//        model.addAttribute("accountDto", new AccountDto());
        return "account/register";
    }

    @PostMapping(value = "/register")
    @ResponseBody
    public ResponseEntity registerUser(@RequestBody AccountDto accountDto, Model model, RedirectAttributes rttr) {
        Account account = mapper.map(accountDto, Account.class);
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        try {
            userService.join(account);

            result.put("registerStatus", "OK");
            result.put("message", "회원가입이 완료되었습니다.");
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(result);
        } catch (UserEmailDuplicateException e) {
//            model.addAttribute("emailErrorMsg", account.getEmail() + "은 이미 있는 이메일입니다.");
            //            return "account/register";
            result.put("registerStatus", "Failed");
            result.put("field", "email");
            result.put("message", account.getEmail() + "은(는) 이미 가입된 이메일입니다.");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(result);
        } catch (UserNameDuplicateException e) {
//            model.addAttribute("usernameErrorMsg", account.getUsername() + "은(는) 이미 존재하는 사용자입니다.");
//            return "account/register";

            result.put("registerStatus", "Failed");
            result.put("field", "username");
            result.put("message", account.getUsername() + "은(는) 이미 가입된 사용자입니다.");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(result);
        }
//        rttr.addFlashAttribute("result", account.getUsername() + "님의 회원가입이 완료되었습니다.");
    }
}
