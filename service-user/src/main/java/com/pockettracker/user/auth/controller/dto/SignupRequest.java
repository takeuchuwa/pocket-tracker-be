package com.pockettracker.user.auth.controller.dto;

import com.pockettracker.user.auth.controller.dto.validation.ExtendedEmailValidator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SignupRequest(@Size(min = 3) @NotNull String firstName,
                            @Size(min = 3) @NotNull String lastName,
                            @ExtendedEmailValidator @NotNull String email,
                            @NotNull @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$",
                                    message = "Should contain at least 1 character: upper case, lower case, digit. Without spaces. 8 characters minimum.")
                            String password) {
}
