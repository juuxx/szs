package org.example.szs.domain.user;

import org.example.szs.domain.common.AbstractBaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_user", uniqueConstraints = {
	@UniqueConstraint(name = "uk_user_user_id", columnNames = "user_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AbstractBaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String userId;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, name = "reg_no_prefix", length = 7)
	private String regNoPrefix; // 생년월일 + 성별

	@Column(nullable = false, name = "reg_no_suffix")
	private String regNoSuffix; // 뒤 6자리 암호화



	@Builder
	public User(Long id, String userId, String password, String name, String regNoPrefix, String regNoSuffix) {
		this.id = id;
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.regNoPrefix = regNoPrefix;
		this.regNoSuffix = regNoSuffix;
	}
}
