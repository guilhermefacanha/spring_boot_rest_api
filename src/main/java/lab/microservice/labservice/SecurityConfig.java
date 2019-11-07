package lab.microservice.labservice;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	public static String[] getAllowed() {
	    String[] s = {"/***"};
//	    String[] s = {"/api/auth/",
//	   		 "/**/*.ico",
//			 "/**/*.html",
//			 "/**/*.png",
//			 "/**/*.gif",
//			 "/**/*.svg",
//			 "/**/*.jpg",
//			 "/**/*.css",
//			 "/**/*.js"};
	    
	    return s;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// http
		// .cors()
		// .and()
		// .csrf()
		// .disable()
		// .sessionManagement()
		// .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		// .and()
		// .authorizeRequests()
		// .antMatchers("/",
		// "/favicon.ico",
		// "/**/*.png",
		// "/**/*.gif",
		// "/**/*.svg",
		// "/**/*.jpg",
		// "/**/*.html",
		// "/**/*.css",
		// "/**/*.js")
		// .permitAll()
		// .antMatchers("/api/auth/**")
		// .permitAll()
		// //
		// .antMatchers("/api/user/checkUsernameAvailability","/api/user/checkEmailAvailability")
		// // .permitAll()
		// // .antMatchers(HttpMethod.GET, "/api/polls/**", "/api/users/**")
		// // .permitAll()
		// .anyRequest()
		// .authenticated();
		//
		// // Add our custom JWT security filter
		// http.addFilterBefore(jwtAuthenticationFilter(),
		// UsernamePasswordAuthenticationFilter.class);

		// We don't need CSRF for this example
		
		http.cors()
		 .and()
		 .csrf()
		 .disable()
		 .anonymous().and().authorizeRequests().antMatchers("/***").permitAll();
		
//		http
//		.anonymous()
//		.and()
//		 .authorizeRequests()
//		 .antMatchers(getAllowed())
//		 .permitAll()
//		.and().authorizeRequests().anyRequest().authenticated().and().
//		exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
//		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//		.and().addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

	}
}