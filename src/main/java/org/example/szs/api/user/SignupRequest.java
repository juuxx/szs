package org.example.szs.api.user;

import lombok.Getter;

@Getter
public class SignupRequest {
	String userId;
	String password;
	String name;
	String regNo;
}
