package com.pockettracker.auth.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCredentials {

    @Column(name = "logon_id", table = "user_credentials")
    private String email;
    @Column(name = "logon_password", table = "user_credentials")
    private String encryptedPassword;
}
