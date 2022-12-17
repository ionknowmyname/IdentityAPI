package com.faithfulolaleru.IdentityAPI.otp;

import com.faithfulolaleru.IdentityAPI.appUser.AppUserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity(name = "otp")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtpEntity {

    @Id
    @SequenceGenerator(
            name = "otp_sequence",
            sequenceName = "otp_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "otp_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String otp;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(name = "app_user_id", referencedColumnName = "id")
    private AppUserEntity appUser;
}
