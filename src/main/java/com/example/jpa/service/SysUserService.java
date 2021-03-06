package com.example.jpa.service;

import com.example.jpa.entity.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SysUserService{

    SysUser save(SysUser sysUser);

    Optional<SysUser> findById(Integer id);

    Page<SysUser> findAll(Pageable pageable);

    long count();

    void deleteById(Integer id);

    void delete(SysUser sysUser);
}