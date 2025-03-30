package com.example.authy.security.mfa;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

/**
 * Service for handling Two-Factor Authentication (2FA) operations, including secret generation,
 * QR code creation, and OTP validation.
 */
@Service
@Slf4j
public class TwoFactorAuthenticationService {

    private static final String ISSUER = "Savvy";
    private static final int OTP_DIGITS = 6;
    private static final int OTP_PERIOD = 30;
    private static final HashingAlgorithm HASH_ALGORITHM = HashingAlgorithm.SHA1;

    private final TimeProvider timeProvider = new SystemTimeProvider();
    private final CodeGenerator codeGenerator = new DefaultCodeGenerator();
    private final CodeVerifier codeVerifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
    private final QrGenerator qrGenerator = new ZxingPngQrGenerator();

    /**
     * Generates a new secret key for 2FA.
     *
     * @return A newly generated secret key.
     */
    public String generateNewSecret() {
        return new DefaultSecretGenerator().generate();
    }

    /**
     * Generates a QR code URI for setting up 2FA with an authenticator app.
     *
     * @param secret The secret key used for generating OTPs.
     * @return A data URI containing the QR code image.
     */
    public String generateQRCode(String secret) {
        QrData qrData = new QrData.Builder()
                .label(ISSUER)
                .secret(secret)
                .issuer(ISSUER)
                .algorithm(HASH_ALGORITHM)
                .digits(OTP_DIGITS)
                .period(OTP_PERIOD)
                .build();

        try {
            byte[] imageData = qrGenerator.generate(qrData);
            return getDataUriForImage(imageData, qrGenerator.getImageMimeType());
        } catch (QrGenerationException e) {
            log.error("Error generating QR code: {}", e.getMessage());
            return null; // Return null or throw a custom exception to indicate failure.
        }
    }

    /**
     * Validates an OTP against the secret key.
     *
     * @param secret The secret key associated with the user.
     * @param code   The OTP code entered by the user.
     * @return {@code true} if the OTP is valid, otherwise {@code false}.
     */
    public boolean isOtpValid(String secret, String code) {
        return codeVerifier.isValidCode(secret, code);
    }

    /**
     * Checks if an OTP is invalid (logical negation of {@link #isOtpValid}).
     *
     * @param secret The secret key associated with the user.
     * @param code   The OTP code entered by the user.
     * @return {@code true} if the OTP is invalid, otherwise {@code false}.
     */
    public boolean isOtpNotValid(String secret, String code) {
        return !isOtpValid(secret, code);
    }
}
