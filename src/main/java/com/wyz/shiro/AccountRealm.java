package com.wyz.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.wyz.entity.User;
import com.wyz.service.UserService;
import com.wyz.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountRealm extends AuthorizingRealm {
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	UserService userService;

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof JwtToken;
	}

	//授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		return null;
	}

	//认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		JwtToken jwtToken = (JwtToken) token;
		Claims claim = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal());
		String userId = claim.getSubject();

		User user = userService.getById(Long.valueOf(userId));

		if (user == null) {
			throw new UnknownAccountException("账户不存在");
		}
		if (user.getStatus() == -1) {
			throw new LockedAccountException("账户已被锁定");
		}
		AccountProfile accountProfile = new AccountProfile();
		BeanUtil.copyProperties(user, accountProfile);

		// user.getName()??
		return new SimpleAuthenticationInfo(accountProfile, jwtToken.getCredentials(), getName());
	}
}
