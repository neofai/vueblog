package com.wyz.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wyz.common.lang.Result;
import com.wyz.entity.Blog;
import com.wyz.service.BlogService;
import com.wyz.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 前端控制器
 * </p>
 */
@RestController
public class BlogController {
	@Autowired
	BlogService blogService;

	//列表
	@GetMapping("/blogs")
	public Result list(@RequestParam(defaultValue = "1") Integer currentPage) {
		Page page = new Page(currentPage, 5);
		IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));
		return Result.success(pageData);
	}

	//查看博客
	@GetMapping("/blog/{id}")
	public Result detail(@PathVariable(name = "id") Long id) {
		Blog blog = blogService.getById(id);
		Assert.notNull(blog, "该博客已被删除");
		return Result.success(blog);
	}

	//编辑+添加（blog有id是编辑，无id是添加）
	@RequiresAuthentication
	@PostMapping("/blog/edit")
	public Result edit(@Validated @RequestBody Blog blog) {
		Blog temp = null;
		if (blog.getId() != null) {
			//编辑
			temp = blogService.getById(blog.getId());
			Assert.isTrue(Objects.equals(temp.getUserId(), ShiroUtil.getProfile().getId()), "没有权限编辑");
		} else {
			temp = new Blog();
			temp.setUserId(ShiroUtil.getProfile().getId());
			temp.setCreated(LocalDateTime.now());
			temp.setStatus(0);
		}
		BeanUtil.copyProperties(blog, temp, "id", "userId", "created", "status");
		blogService.saveOrUpdate(temp);

		return Result.success(null);
	}


}
