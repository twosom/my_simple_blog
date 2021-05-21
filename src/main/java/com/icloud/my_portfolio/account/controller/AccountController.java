package com.icloud.my_portfolio.account.controller;


import com.icloud.my_portfolio.account.Account;
import com.icloud.my_portfolio.account.dto.AccountDto;
import com.icloud.my_portfolio.exception.UserEmailDuplicateException;
import com.icloud.my_portfolio.exception.UserNameDuplicateException;
import com.icloud.my_portfolio.account.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("accountDto", new AccountDto());
        return "account/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute AccountDto accountDto, Model model, RedirectAttributes rttr) {
        Account account = mapper.map(accountDto, Account.class);
        try {
            userService.join(account);
        } catch (UserEmailDuplicateException e) {
            model.addAttribute("emailErrorMsg", account.getEmail() + "은 이미 있는 이메일입니다.");
            return "account/register";
        } catch (UserNameDuplicateException e) {
            model.addAttribute("usernameErrorMsg", account.getUsername() + "은(는) 이미 존재하는 사용자입니다.");
            return "account/register";
        }
        rttr.addFlashAttribute("result", account.getUsername() + "님의 회원가입이 완료되었습니다.");
        return "redirect:/";
    }
}
