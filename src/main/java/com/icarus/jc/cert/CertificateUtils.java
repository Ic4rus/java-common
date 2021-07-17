package com.icarus.jc.cert;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.icarus.jc.string.StringUtils;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemReader;
import sun.security.pkcs10.PKCS10;
import sun.security.util.PendingException;
import sun.security.x509.X500Name;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

public class CertificateUtils {

    private static final String PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIIEpAIBAAKCAQEAxH5aFYr9n8Ovp+EFvdbg/RbPWxOmfR8NyrE0cXwwh91f7nGY\n" +
            "yMZDMBYVEJ0AXghEPbFdVTs8qXv64mo+SsmULxp897xgAb+S9RPOB7Hff1kQ9kJ+\n" +
            "g287R0tfyjLwD1inAIVWtDKgGRumDCTBDZAWw4uBIgXdQmvUZRc2Za7GvTBMksKp\n" +
            "83IoUWXaAQ2DCaJuTEY8+sRoD11BehNqQpFGLBLt+n5g/ny6AWeCfpRUN/vUkpxi\n" +
            "HPgID+1HadaGS4TCU3gNjhWI4qpnR8fF2VkzxnYVgxh+2aZzQOarf2Vhx2Ses37l\n" +
            "KbhCsitPF4+u/ScWYWk74ISQeX4oc+P/E3vEXQIDAQABAoIBAQCdmpL68I7ZlIXR\n" +
            "68VFLZMsrR0Iekxx03SmfVgJJolukHufr3HSqGO/sBaZSrS+t3a03Qx1jpvrfUwQ\n" +
            "SxNHvUOkPGS7+FYxUM240XfR6DPR20ilnVQIbDFpz+U3M4AlbwAUhN60jl6nhOu8\n" +
            "lcZa8JWxprWe6oE8A21K4EF7caJqn2YT7RNMghMxXUT+158ilPddiTmZxXxPlSbb\n" +
            "JC1qnuTQenIvm+heoYU5QK3iHfvT7xsCsS2Nlkx4W/pfkk7Yslw3mR3UpSgHZFNd\n" +
            "QyG48oR/xLRH0Vfj6LQZoclZUQgBYYumj/4o62v9kiTevyRP/GSOWdJxgjAH6G4q\n" +
            "mvxdHUvpAoGBAP93iP8Uclq3IHx3Z4WWHUPqgBqG8kNoUo6fnr6clsIIA+mk8/sQ\n" +
            "oRzS7Elfrin8InkoVM4neAZ/DKrMaHgSPvY9fnrs7R89MqpJ2JdHlcenv2ACWDDv\n" +
            "f2ZR3dIJcaXcUsesIP2wGHJ+ta6bVRWGajiLS++4OMMpt8q9ioA/qbfHAoGBAMTn\n" +
            "UICK4HD8hHhQEkptMsYkzD7v/hmaHAixFZglDACh8Zu0kJ9JQoTsw96IlwbRrVE8\n" +
            "2ca2CzO+cIdJSBOk4rJVR38kAwRbI5brdHEQniPJ0BTxqSJNTeJoGuInP/uvVesh\n" +
            "w6pmZjcmQE6nb6ObiFXZyN26HNPbRVaO2QSvhUq7AoGAM29SwuDQAVm7jbPQwQGv\n" +
            "VLxDHJQs9MttS8/xezk11U0d1I3knZkEhzQkZMWB4GwmWLR5FqS1ssEjwMO1CO/L\n" +
            "g3JwI7nOKCr/J3AEzriX/IHWszLlpLyjrusuuRozqoxIknEil1E8Ktk+JTCbGeFd\n" +
            "Q5MwL8ysiQa+PXCaESZAOoMCgYAI/IGZDD2ICo5FK2paSTeLAXzs1mkjY/wpjWNt\n" +
            "6SruEHYFZC1Bh8doZ6Hc0yEJZ6JgVcWeowpJ10QteHO4qcrK1seHju4wUbSDG8XZ\n" +
            "ryd4fKppekn8zchjL9siPSZXZz4lmAnlCf1aQP2oWU3oCp4jROg87lSIerZkZ+qR\n" +
            "oy0TJwKBgQDaNri93QvwSihxxz1DL+OwTrB9lS3/Hj22HMrNjqpf3HWGGiT4V5pv\n" +
            "3q0e988d64xp5uuGsWFT/r2Wsne9januFiFRycGTqobkXx/ASfjeO7z/ArwshY7N\n" +
            "Q+wE/Obsq+hNG0UPWb5O6WOr1BxW0ArbL6LPxO3gcOU70NDJ8DRgtg==\n" +
            "-----END RSA PRIVATE KEY-----\n";

    private static final String CSR = "-----BEGIN CERTIFICATE REQUEST-----\n" +
            "MIICmjCCAYICAQAwVTELMAkGA1UEBhMCVk4xDjAMBgNVBAgMBUhhbm9pMQ4wDAYD\n" +
            "VQQHDAVIYW5vaTEPMA0GA1UECgwGaWNhcnVzMRUwEwYDVQQDDAxvX3RvX2RvX2No\n" +
            "b2kwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDEfloViv2fw6+n4QW9\n" +
            "1uD9Fs9bE6Z9Hw3KsTRxfDCH3V/ucZjIxkMwFhUQnQBeCEQ9sV1VOzype/riaj5K\n" +
            "yZQvGnz3vGABv5L1E84Hsd9/WRD2Qn6DbztHS1/KMvAPWKcAhVa0MqAZG6YMJMEN\n" +
            "kBbDi4EiBd1Ca9RlFzZlrsa9MEySwqnzcihRZdoBDYMJom5MRjz6xGgPXUF6E2pC\n" +
            "kUYsEu36fmD+fLoBZ4J+lFQ3+9SSnGIc+AgP7Udp1oZLhMJTeA2OFYjiqmdHx8XZ\n" +
            "WTPGdhWDGH7ZpnNA5qt/ZWHHZJ6zfuUpuEKyK08Xj679JxZhaTvghJB5fihz4/8T\n" +
            "e8RdAgMBAAGgADANBgkqhkiG9w0BAQsFAAOCAQEASP6IP7GgwDhXBMjCUGsqM+8R\n" +
            "M9dgVD7d40SeHimXNZ2tO9/A5NBr1YPYX6qrIirClH1ORWeIGS+f0jh1WN8f6Pm1\n" +
            "x3rjVWdWnV21/AEdlA5OZOWjNsImJNw1KzKzK+0K/aoOZVyMeWFEE2mgV2QGvzgN\n" +
            "YMX/CamVnDSfAQuLkTb4Yuj8UsTVRI+VpYqlafY2fIJs7mD+O7X+5BCSsGHP/ANy\n" +
            "lQUpcI0XygocASEjbMudEJjM/uVNF0s2G5YaMadixGmCQXFgeZ4X5mCcqBxy/L/P\n" +
            "XN6yJ3eQ7q6Jwf2VSo5PbXSr/7h7LFXrmdcwwEHBv1kWLrofzMvm0gqrMnyyDA==\n" +
            "-----END CERTIFICATE REQUEST-----";

    private static final String CERTIFICATE = "-----BEGIN CERTIFICATE-----\n" +
            "MIIDfzCCAmegAwIBAgIUZ8ztBcJBwbKXPq3wpDNqb06vuSAwDQYJKoZIhvcNAQEL\n" +
            "BQAwTzELMAkGA1UEBhMCVk4xDjAMBgNVBAgMBUhhbm9pMQ4wDAYDVQQHDAVIYW5v\n" +
            "aTEPMA0GA1UECgwGaWNhcnVzMQ8wDQYDVQQDDAZpY2FydXMwHhcNMjEwNTA1MDIy\n" +
            "NjI1WhcNMjQwMjIzMDIyNjI1WjBPMQswCQYDVQQGEwJWTjEOMAwGA1UECAwFSGFu\n" +
            "b2kxDjAMBgNVBAcMBUhhbm9pMQ8wDQYDVQQKDAZpY2FydXMxDzANBgNVBAMMBmlj\n" +
            "YXJ1czCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBANEr/wfj4qtEH+Ep\n" +
            "B+4iiP099NH/caJh/RHocjuIMpHmULAtRLR/86Cy8O8wK10+7snwdAi7pXMg3aGa\n" +
            "wDDl1g/rDtt2Ek6dhllsxulOLjxDHG0zhlGXu/sTAQNewlNeMawJOGBN7TfjBPPl\n" +
            "MLrYxzfTHfRrPNgRFIFTn7cbPvc6mbUIMi1Ywj8PTchO4cUkMYNS2ZLMPWdh9rdb\n" +
            "sCElp4/1LmiEjhDZ6XCvhN4ebHXkUeoHyv0DFXwe2wei4rWSuE/WTGHr3uW+XgSq\n" +
            "+HhDxzT21/hSAa/eVUcmnEf0eaAd6zQTH5PS7MAHxzaUG3VFUI35jJWeWtEANUpy\n" +
            "c5/jdYcCAwEAAaNTMFEwHQYDVR0OBBYEFGl0zSUraNICUAVWepeW2KVB1SfNMB8G\n" +
            "A1UdIwQYMBaAFGl0zSUraNICUAVWepeW2KVB1SfNMA8GA1UdEwEB/wQFMAMBAf8w\n" +
            "DQYJKoZIhvcNAQELBQADggEBAJoENI54uTMK8aQIDG3D+LqCahD/Aq2TGv8q2W+/\n" +
            "sesq8J6ihHY0fli7LipyQ+YnKhduZJ1HSkmTzT1MCzX/fUPEZS7L5Sv20tsCnwGi\n" +
            "Np+7H1zRRwWS9yt8C7LhUQbNHrcffHtpxZIAXUOodpnoFwYO3T5obPkPID3DfksG\n" +
            "iIyTE6GRhftgHpuf9FCi8IwfqIhsMRktLl9llxj/VsmD5/e/DwQ+vtAx+Va/8nZW\n" +
            "+8eIQTm22Q+VCe/AdXuqzQUpBpTmA+9UcnltgVqEUxXeRp+2mgd8KZyqLH7nx1Z9\n" +
            "4SincpCAvzYDP3MyuANYCGSI38FJIRK1GZ6u1RAiwHuWjlM=\n" +
            "-----END CERTIFICATE-----\n";

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    public static String convertPublicKey2Pem(PublicKey publicKey) {
        byte[] encodedBytes = publicKey.getEncoded();
        String encodedBase64 = Base64.getEncoder().encodeToString(encodedBytes);
        String encodedBase64WithLineFeed = StringUtils.middlePad(encodedBase64, 64, "\n");
        String pemFormat = "-----BEGIN PUBLIC KEY-----\n%s\n-----END PUBLIC KEY-----\n";
        return String.format(pemFormat, encodedBase64WithLineFeed);
    }

    public static String convertPrivateKey2Pem(PrivateKey privateKey) {
        byte[] encodedBytes = privateKey.getEncoded();
        String encodedBase64 = Base64.getEncoder().encodeToString(encodedBytes);
        String encodedBase64WithLineFeed = StringUtils.middlePad(encodedBase64, 64, "\n");
        String pemFormat = "-----BEGIN RSA PRIVATE KEY-----\n%s\n-----END RSA PRIVATE KEY-----\n";
        return String.format(pemFormat, encodedBase64WithLineFeed);
    }

    public static String convertPKCS10ToPem(PKCS10 pkcs10) {
        byte[] encodedBytes = pkcs10.getEncoded();
        String encodedBase64 = Base64.getEncoder().encodeToString(encodedBytes);
        String encodedBase64WithLineFeed = StringUtils.middlePad(encodedBase64, 64, "\n");
        String pemFormat = "-----BEGIN CERTIFICATE REQUEST-----\n%s\n-----END CERTIFICATE REQUEST-----\n";
        return String.format(pemFormat, encodedBase64WithLineFeed);
    }

    public static X509Certificate loadX509Certificate() throws CertificateException, UnsupportedEncodingException {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(CERTIFICATE.getBytes(StandardCharsets.UTF_8));
        return (X509Certificate) factory.generateCertificate(inputStream);
    }

    public static PKCS10CertificationRequest loadPKCS10CertificationRequest() throws IOException {
        PemReader pemReader = new PemReader(new StringReader(CSR));
        return new PKCS10CertificationRequest(pemReader.readPemObject().getContent());
    }

    public static RSAPrivateKey loadRSAPrivateKey() throws IOException {
        Security.addProvider(new BouncyCastleProvider());


        PEMParser pemParser = new PEMParser(new StringReader(PRIVATE_KEY));
        PEMKeyPair pemKeyPair = (PEMKeyPair) pemParser.readObject();

        KeyPair keyPair = new JcaPEMKeyConverter().getKeyPair(pemKeyPair);
        return null;
    }

    public static void generatePKCS10()
            throws NoSuchAlgorithmException, InvalidKeyException, IOException, CertificateException,
            SignatureException {
        // Generate key
        KeyPair keyPair = generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        System.out.println(convertPublicKey2Pem(publicKey));
        System.out.println(convertPrivateKey2Pem(privateKey));

        String sigAlg = "MD5WithRSA";
        PKCS10 pkcs10 = new PKCS10(publicKey);
        Signature signature = Signature.getInstance(sigAlg);
        signature.initSign(privateKey);
        X509Principal principal = new X509Principal("CN=Icarus, OU=Icarus, O=Icarus, L=Hanoi, ST=Hanoi, C=VN");
        X500Name x500Name = new X500Name(principal.getEncoded());
        pkcs10.encodeAndSign(x500Name, signature);

        System.out.println(convertPKCS10ToPem(pkcs10));
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeyException,
            CertificateException, SignatureException {
        generatePKCS10();
    }

}
