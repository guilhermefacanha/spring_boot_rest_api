package lab.microservice.labservice.service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lab.microservice.labservice.exception.RetornoNegocioException;
import lab.microservice.labservice.model.util.JsonReturn;
import lab.microservice.labservice.util.KeyGenerator;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings({ "rawtypes", "static-access" })
@Slf4j
@Service
@RequestMapping("/api/auth")
public class AuthenticationService {

	@Autowired
	private KeyGenerator keyGenerator;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@PostMapping("/")
	public ResponseEntity test(@RequestBody Map<String, String> params) {

		if (!params.containsKey("user") || !params.containsKey("pass"))
			return setError("Body incomplete", "", "Authentication");

		String user = params.get("user");
		String pass = params.get("pass");

		if (user != null && !user.isEmpty() && pass != null && !pass.isEmpty()) {
			if ("admin".equals(user) && "admin".equals(pass)) {
				Key key = keyGenerator.generateKey();
				String jwtToken = Jwts.builder()
						.setClaims(getCustomClaims())
						.setSubject(user).setIssuer(httpServletRequest.getServerName().toString())
						.setIssuedAt(new Date()).setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
						.signWith(SignatureAlgorithm.HS512, key).compact();
				log.info("#### generating token for a key : " + jwtToken + " - " + key);
				return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwtToken)
						.body(JsonReturn.builder().message("User authenticated!").build());
			} else
				return setError("User invalid!", "", "Authentication");

		} else
			return setError("Body incomplete", "", "Authentication");
	}
	
	private Map<String, Object> getCustomClaims() {
		Map<String, Object> map = new HashMap<>();
		map.put("profile", "admin_profile");
		map.put("department", "HR");
		return map;
	}

	protected ResponseEntity setError(String error, String ex, String tipo) {
		return ResponseEntity.ok(RetornoNegocioException.builder().erro(error).exception(ex).tipo(tipo).build())
				.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	private Date toDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

}
