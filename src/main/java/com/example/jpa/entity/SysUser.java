package com.example.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sys_user")
@Data
public class SysUser implements Serializable {

	private static final long serialVersionUID = 7570974208667472275L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id ; // 

	@Column(name = "name")
	private String name ; // 姓名

	@Column(name = "age")
	private Integer age ; // 年龄

	@Column(name = "password")
	private String password ; // 密码

}