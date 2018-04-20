package com.example.jpa.dao;

import com.example.jpa.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface SysUserDao extends JpaRepository<SysUser, Integer>, Serializable {

}