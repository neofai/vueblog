package com.wyz.util;

import com.wyz.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

public class ShiroUtil {

	public static AccountProfile getProfile() {
		return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
	}

}
