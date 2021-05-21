package com.icloud.my_portfolio.account.service;


import com.icloud.my_portfolio.account.Account;
import com.icloud.my_portfolio.account.dto.AccountDto;
import com.icloud.my_portfolio.account.repository.UserRepository;
import com.icloud.my_portfolio.role.Role;
import com.icloud.my_portfolio.exception.UserEmailDuplicateException;
import com.icloud.my_portfolio.exception.UserNameDuplicateException;
import com.icloud.my_portfolio.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    public Account join(Account account) {
        //==비밀번호 암호화==//
        String encodedPassword = passwordEncoder.encode(account.getPassword());
        //==유저 중복 검사==//
        userValidateByEmailAndUsername(account);
        account.setPassword(encodedPassword);
        /* User 활성화 */
        account.setEnabled(true);
        Role role_user = roleRepository.findByRoleName("ROLE_USER");
        account.getRoleList().add(role_user);
        account.setCreatedDate(LocalDateTime.now());
        userRepository.save(account);
        return account;
    }


    /* 유저의 중복 검사를 위한 로직 */
    private void userValidateByEmailAndUsername(Account account) {
        if (userRepository.findByEmail(account.getEmail()) != null) {
            throw new UserEmailDuplicateException(account.getEmail() + "은 이미 있는 이메일입니다.");
        }


        if (userRepository.findByUsername(account.getUsername()) != null) {
            throw new UserNameDuplicateException(account.getUsername() + "은(는) 이미 있는 사용자 이름입니다.");
        }
    }

    public List<Account> getAccounts() {
        return userRepository.findAll();
    }


    public AccountDto getAccount(Long id) {
        Account account = userRepository.findById(id).orElse(new Account());
        AccountDto accountDto = mapper.map(account, AccountDto.class);

        if (account.getRoleList() != null) {
            List<String> roleList = account.getRoleList()
                    .stream().map(Role::getRoleName)
                    .collect(Collectors.toList());

            accountDto.setRoleList(roleList);
        }

        return accountDto;
    }

    public void removeAccount(Long id) {
        userRepository.deleteById(id);
    }

    public void modifyAccount(AccountDto accountDto) {
        Account account = mapper.map(accountDto, Account.class);

        if (accountDto.getRoleList() != null) {
            List<Role> roleList = accountDto.getRoleList()
                    .stream().map(roleRepository::findByRoleName)
                    .collect(Collectors.toList());
            account.setRoleList(roleList);
        }

        userRepository.save(account);
    }
}
