package com.example.jpa.service.impl;

import com.example.jpa.dao.SysUserDao;
import com.example.jpa.entity.SysUser;
import com.example.jpa.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserDao sysUserDao;

    @Override
    public SysUser save(SysUser sysUser) {
        return sysUserDao.save(sysUser);
    }

    @Override
    public Optional<SysUser> findById(Integer id) {
        return sysUserDao.findById(id);
    }

    @Override
    public Page<SysUser> findAll(Pageable pageable) {
        return sysUserDao.findAll(pageable);
    }

    @Override
    public long count() {
        return sysUserDao.count();
    }

    @Override
    public void deleteById(Integer id) {
        sysUserDao.deleteById(id);
    }

    @Override
    public void delete(SysUser sysUser) {
        sysUserDao.delete(sysUser);
    }
}
