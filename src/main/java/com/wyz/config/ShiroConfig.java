package com.wyz.config;

import com.wyz.shiro.AccountRealm;
import com.wyz.shiro.JwtFilter;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
	@Autowired
	JwtFilter jwtFilter;

	@Bean
	public SessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setSessionDAO(redisSessionDAO);
		return sessionManager;
	}

	@Bean
	public SessionsSecurityManager securityManager(AccountRealm realm,
	                                               SessionManager sessionManager,
	                                               RedisCacheManager redisCacheManager) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(realm);
		//inject sessionManager
		securityManager.setSessionManager(sessionManager);
		//inject redisCacheManager
		securityManager.setCacheManager(redisCacheManager);
		return securityManager;
	}

	@Bean
	public ShiroFilterChainDefinition shiroFilterChainDefinition() {
		DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
		Map<String, String> filter = new LinkedHashMap<>();
		filter.put("/**", "jwt");
		chainDefinition.addPathDefinitions(filter);
		return chainDefinition;
	}

	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,
	                                                     ShiroFilterChainDefinition shiroFilterChainDefinition) {
		ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
		shiroFilter.setSecurityManager(securityManager);
		Map<String, Filter> filters = new HashMap<>();
		filters.put("jwt", jwtFilter);
		shiroFilter.setFilters(filters);
		Map<String, String> filterMap = shiroFilterChainDefinition.getFilterChainMap();
		shiroFilter.setFilterChainDefinitionMap(filterMap);
		return shiroFilter;
	}

}
