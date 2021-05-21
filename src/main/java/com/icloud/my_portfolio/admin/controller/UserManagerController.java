package com.icloud.my_portfolio.admin.controller;

import com.icloud.my_portfolio.account.Account;
import com.icloud.my_portfolio.account.dto.AccountDto;
import com.icloud.my_portfolio.account.service.UserService;
import com.icloud.my_portfolio.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserManagerController {

    private final UserService userService;
    private final RoleService roleService;

    @GetMapping("/admin/accounts")
    public String allAccounts(Model model) {
        List<Account> accountList = userService.getAccounts();
        model.addAttribute("accountList", accountList);

        return "admin/user/list";
    }

    @GetMapping("/admin/accounts/{id}")
    public String accountDetail(@PathVariable("id") Long id, Model model) {
        AccountDto accountDto = userService.getAccount(id);
        model.addAttribute("account", accountDto);
        model.addAttribute("roleList", roleService.getRoles());

        return "admin/user/detail";
    }

    @PostMapping("/admin/accounts")
    public String modifyAccount(AccountDto accountDto) {
        userService.modifyAccount(accountDto);

        return "redirect:/admin/accounts";
    }

    @GetMapping("/admin/accounts/delete/{id}")
    public String removeAccount(@PathVariable("id") Long id) {
        userService.removeAccount(id);

        return "redirect:/admin/accounts";
    }

}
