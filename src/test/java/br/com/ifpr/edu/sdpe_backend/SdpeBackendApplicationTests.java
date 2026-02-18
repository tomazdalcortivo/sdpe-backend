package br.com.ifpr.edu.sdpe_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class SdpeBackendApplicationTests {

	@Test
	void contextLoads() {
	}


	@Test
	void gerarHashSenha() {
		System.out.println(new BCryptPasswordEncoder().encode("SenhaForte789@"));
	}

}
