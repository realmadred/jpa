package com.example.jpa.dao;

import com.example.jpa.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface SysUserDao extends JpaRepository<SysUser, Integer>, Serializable {

}