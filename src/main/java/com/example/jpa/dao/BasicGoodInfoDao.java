package com.example.jpa.dao;

import com.example.jpa.entity.GoodInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface BasicGoodInfoDao extends JpaRepository<GoodInfoEntity, Integer>, Serializable {

}