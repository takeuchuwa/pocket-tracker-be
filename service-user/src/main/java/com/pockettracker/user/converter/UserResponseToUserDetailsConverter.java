package com.pockettracker.user.converter;


import com.pockettracker.user.security.UserDetailsImpl;
import com.pockettracker.user.entity.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserResponseToUserDetailsConverter implements Converter<User, UserDetails> {

    @Override
    public UserDetails convert(User source) {
        UserDetailsImpl userDetails = new UserDetailsImpl();

        userDetails.setUsername(source.getUserCredentials().getEmail());
        userDetails.setPassword(source.getUserCredentials().getEncryptedPassword());
        userDetails.setEnabled(source.getEnabled());
        userDetails.setDeleted(source.getDeleted());
        userDetails.setAuthorities(List.of(new SimpleGrantedAuthority(source.getRole().name())));

        return userDetails;
    }
}
