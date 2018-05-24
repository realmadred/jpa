package com.example.jpa.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 商品范畴表
 * 
 * @author 刘峰
 * @date 2018-05-23 10:27:03
 */
@Data
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "goods_category")
public class GoodsCategory implements Serializable {

    private static final long serialVersionUID = 4436623781031784272L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 商品范畴编号
     */
    @Column(name = "goods_category_code")
    private String goodsCategoryCode;

    /**
     * 商品范畴名称
     */
    @Column(name = "goods_category_name")
    private String goodsCategoryName;

    /**
     * 商品范畴简称
     */
    @Column(name = "goods_category_short_name")
    private String goodsCategoryShortName;

    /**
     * 是否有效（0：否  1：是）
     */
    @Column(name = "is_valid",columnDefinition = "BIT")
    private Integer isValid;

    /**
     * 是否删除（0：否  1：是）
     */
    @Column(name = "is_delete",columnDefinition = "BIT")
    private Integer isDelete;

    /**
     * 排序排序序号
     */
    @Column(name = "sort")
    private Integer sort;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    private String createUserId;

    /**
     * 创建人名称
     */
    @Column(name = "create_user_name")
    private String createUserName;

    /**
     * 创建人机构编号
     */
    @Column(name = "create_org_code")
    private String createOrgCode;

    /**
     * 创建人机构名称
     */
    @Column(name = "create_org_name")
    private String createOrgName;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(name = "create_datetime", insertable = false, updatable = false)
    private Date createDatetime;

    /**
     * 更新人ID
     */
    @Column(name = "modifed_user_id")
    private String modifedUserId;

    /**
     * 更新人名称
     */
    @Column(name = "modifed_user_name")
    private String modifedUserName;

    /**
     * 更新人机构编号
     */
    @Column(name = "modifed_org_code")
    private String modifedOrgCode;

    /**
     * 更新人机构名称
     */
    @Column(name = "modifed_org_name")
    private String modifedOrgName;

    /**
     * 更新时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @Column(name = "modifed_datetime", insertable = false, updatable = false)
    private Date modifedDatetime;

    /**
     * 乐观锁
     */
    @Version
    @Column(name = "optimistic_lock")
    private Integer optimisticLock;


}
