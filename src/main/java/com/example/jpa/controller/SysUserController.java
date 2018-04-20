package com.example.jpa.controller;

import com.example.jpa.entity.SysUser;
import com.example.jpa.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/save")
    public SysUser save(@RequestBody SysUser sysUser) {
        return sysUserService.save(sysUser);
    }

    @GetMapping("/findById")
    public Optional<SysUser> findById(Integer id) {
        return sysUserService.findById(id);
    }

    @GetMapping("/findByPage")
    @Cacheable("user.service.all")
    public Page<SysUser> findAll(@RequestParam("page") Integer page,@RequestParam("size") Integer size) {
        return sysUserService.findAll(PageRequest.of(page,size));
    }

    @GetMapping("/count")
    public long count() {
        return sysUserService.count();
    }

    @PostMapping("/deleteById")
    public void deleteById(Integer id) {
        sysUserService.deleteById(id);
    }

    @PostMapping("/delete")
    public void delete(SysUser sysUser) {
        sysUserService.delete(sysUser);
    }

}