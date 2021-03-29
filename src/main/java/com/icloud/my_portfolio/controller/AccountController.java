package com.icloud.my_portfolio.controller;


import com.icloud.my_portfolio.controller.dto.UserDto;
import com.icloud.my_portfolio.domain.User;
import com.icloud.my_portfolio.exception.UserEmailDuplicateException;
import com.icloud.my_portfolio.exception.UserNameDuplicateException;
import com.icloud.my_portfolio.service.UserService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/login")
    public String login() {
        return "account/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "account/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserDto userDto, Model model, RedirectAttributes rttr) {
        User user = userDto.toEntity();
        try {
            userService.join(user);
        } catch (UserEmailDuplicateException e) {
            model.addAttribute("emailErrorMsg", user.getEmail() + "은 이미 있는 이메일입니다.");
            return "account/register";
        } catch (UserNameDuplicateException e) {
            model.addAttribute("usernameErrorMsg", user.getUsername() + "은(는) 이미 존재하는 사용자입니다.");
            return "account/register";
        }
        rttr.addFlashAttribute("result", user.getUsername() + "님의 회원가입이 완료되었습니다.");
        return "redirect:/";
    }
}
