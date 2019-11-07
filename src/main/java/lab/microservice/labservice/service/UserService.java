package lab.microservice.labservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lab.microservice.labservice.annotations.JWTTokenNeeded;
import lab.microservice.labservice.business.BusinessBase;
import lab.microservice.labservice.business.UserBusiness;
import lab.microservice.labservice.model.User;

@Service
@RequestMapping("/api/users")
public class UserService extends ServiceBase<User> {

	private static final long serialVersionUID = 5989481024712969249L;

	@Autowired
	UserBusiness userBusiness;
	
	@Override
	public BusinessBase<User> getBusiness() {
		return userBusiness;
	}

	@GetMapping("/")
	public ResponseEntity<String> test(){
		return ResponseEntity.ok().body("Hello Users API");
	}

}
