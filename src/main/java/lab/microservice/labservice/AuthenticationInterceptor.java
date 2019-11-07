package lab.microservice.labservice;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lab.microservice.labservice.annotations.JWTTokenNeeded;
import lab.microservice.labservice.exception.AuthenticationException;
import lab.microservice.labservice.util.KeyGenerator;
import lombok.extern.slf4j.Slf4j;

/**
 * Verify token from header if controller method has annotation JWTTokenNeeded
 * with action name, the auth can be enabled or disabled
 *
 */
@Slf4j
@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private KeyGenerator keyGenerator;

	/**
	 * Requests needs to verify token
	 */
	private final List<RequestMatcher> authRequests;

	private boolean enableAuth = true;

	public AuthenticationInterceptor() {
		List<RequestMatcher> matchers = new ArrayList<>();
		for (String s : SecurityConfig.getAllowed())
			matchers.add(new AntPathRequestMatcher(s));
		this.authRequests = matchers;
	}

	public void enable() {
		this.enableAuth = true;
	}

	public void disable() {
		this.enableAuth = false;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		HandlerMethod handlerMethod = null;
		try {
			handlerMethod = (HandlerMethod) handler;
		} catch (Exception e) {
			// unable to parse
			handlerMethod = null;
		}

		if (handler == null || handlerMethod == null) {
			return true;
		}

		// find annotation
		JWTTokenNeeded securityAnnotation = handlerMethod.getMethodAnnotation(JWTTokenNeeded.class);

		if (!enableAuth) {
			return true;
		}

		if (securityAnnotation == null) {
			return true;
		}

		// if (!isNeedToVerify(request)) {
		// return true;
		// }

		try {
			doVerify(request, handler);
			return true;
		} catch (Throwable e) {
			response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
			return false;
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// currentUser.remove();
	}

	private void doVerify(HttpServletRequest request, Object handler) throws AuthenticationException {

		// Get the HTTP Authorization header from the request
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		log.debug("#### authorizationHeader : " + authorizationHeader);

		// Check if the HTTP Authorization header is present and formatted correctly
		if (authorizationHeader == null || authorizationHeader.isEmpty()) {
			log.error("#### invalid authorizationHeader : " + authorizationHeader);
			throw new AuthenticationException("Authorization header must be provided");
		}

		// Extract the token from the HTTP Authorization header
		String token = authorizationHeader.trim();

		try {

			// Validate the token
			Key key = keyGenerator.generateKey();
			Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
			log.debug("ID: " + claims.getId());
		    log.debug("Subject: " + claims.getSubject());
		    log.debug("Issuer: " + claims.getIssuer());
		    log.debug("Expiration: " + claims.getExpiration());
			log.debug("#### valid token : " + token);

		} catch (Exception e) {
			log.error("#### invalid token : " + token);
			throw new AuthenticationException("Unauthorized");
		}

	}

	private boolean isNeedToVerify(HttpServletRequest request) {
		for (RequestMatcher matcher : authRequests) {
			if (matcher.matches(request)) {
				return true;
			}
		}
		return false;
	}
}