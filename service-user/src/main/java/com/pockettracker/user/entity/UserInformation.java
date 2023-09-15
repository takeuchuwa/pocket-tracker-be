package com.pockettracker.user.entity;

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
public class UserInformation {

    @Column(name = "first_name", table = "user_info")
    private String firstName;

    @Column(name = "last_name", table = "user_info")
    private String lastName;
}