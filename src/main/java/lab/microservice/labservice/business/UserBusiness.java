package lab.microservice.labservice.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lab.microservice.labservice.dao.DAOBase;
import lab.microservice.labservice.dao.UserDao;
import lab.microservice.labservice.model.User;

@Component
public class UserBusiness extends BusinessBase<User> {

	private static final long serialVersionUID = 5148889294860883230L;

	@Autowired
	private UserDao dao;
	
	@Override
	public DAOBase<User> getDao() {
		return dao;
	}

}
