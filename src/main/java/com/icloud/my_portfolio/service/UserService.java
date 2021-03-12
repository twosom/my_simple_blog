package com.icloud.my_portfolio.service;


import com.icloud.my_portfolio.domain.Role;
import com.icloud.my_portfolio.domain.User;
import com.icloud.my_portfolio.exception.UserEmailDuplicateException;
import com.icloud.my_portfolio.exception.UserNameDuplicateException;
import com.icloud.my_portfolio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;

    public User join(User user) {


        //==비밀번호 암호화==//
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        //==유저 중복 검사==//
        userValidateByEmailAndUsername(user);

        user.setPassword(encodedPassword);
        /* User 활성화 */
        user.setEnabled(true);
        user.setRole(Role.USER);
        user.setCreatedDate(LocalDateTime.now());
        userRepository.save(user);
        sendMailMessage(user);
        return user;
    }





    /* 유저의 중복 검사를 위한 로직 */
    private void userValidateByEmailAndUsername(User user) {
        List<User> findByEmail = userRepository.findByEmail(user.getEmail());
        if (findByEmail.size() > 0) {
            throw new UserEmailDuplicateException(user.getEmail() + "은 이미 있는 이메일입니다.");
        }

        List<User> findByName = userRepository.findByName(user.getUsername());
        if (findByName.size() > 0) {
            throw new UserNameDuplicateException(user.getUsername() + "은(는) 이미 있는 사용자 이름입니다.");
        }
    }

    private void sendMailMessage(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("if0rever@naver.com");
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(user.getUsername() + "님의 가입을 축하드립니다.");
        mailMessage.setText(user.getUsername() + "님의 가입을 진심으로 축하드립니다.\n 현재 " + user.getUsername() + "님의 등급은 " +
                user.getRole() + "입니다.");
        mailSender.send(mailMessage);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .get(0);
        UserDetails userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(user.getRole());
                return authorities;
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getUsername();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return user.getEnabled();
            }
        };

        return userDetails;
    }
}
