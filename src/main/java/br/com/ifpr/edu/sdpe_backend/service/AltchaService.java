package br.com.ifpr.edu.sdpe_backend.service;

import org.altcha.altcha.Altcha;
import org.altcha.altcha.Altcha.Algorithm;
import org.altcha.altcha.Altcha.Challenge;
import org.altcha.altcha.Altcha.ChallengeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AltchaService {

    @Value("${app.altcha.hmac-key}")
    private String hmacKey;

    public Challenge createChallenge() {
        try {
            ChallengeOptions options = new ChallengeOptions();
            options.algorithm = Algorithm.SHA256;
            options.hmacKey = hmacKey;

            return Altcha.createChallenge(options);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar desafio Altcha", e);
        }
    }

    public boolean verifySolution(String payload) {
        if (payload == null || payload.isBlank()) {
            return false;
        }
        try {
            return Altcha.verifySolution(payload, hmacKey, true);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}