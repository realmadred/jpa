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
	/**
	 * TABLE：使用一个特定的数据库表格来保存主键。
	 * SEQUENCE：根据底层数据库的序列来生成主键，条件是数据库支持序列。
	 * IDENTITY：主键由数据库自动生成（主要是自动增长型）
	 * AUTO：主键由程序控制。
	 */
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id ; // 

	@Column(name = "name")
	private String name ; // 姓名

	@Column(name = "age")
	private Integer age ; // 年龄

	@Column(name = "password")
	private String password ; // 密码

}