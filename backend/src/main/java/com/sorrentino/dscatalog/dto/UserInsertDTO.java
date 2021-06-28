package com.sorrentino.dscatalog.dto;

import com.sorrentino.dscatalog.services.vallidation.UserInsertValid;

@UserInsertValid
public class UserInsertDTO extends UserDTO {
	private static final long serialVersionUID = 1L;
	
	private String password;
	
	UserInsertDTO() {
		super();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
