package com.wyz.shiro;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装用户信息（非私密，登录后可展示的信息）
 */
@Data
public class AccountProfile implements Serializable {
	private Long id;

	private String username;

	private String avatar;

	private String email;

}
