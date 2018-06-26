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
 * 费用类型参数关系表
 * 
 * @author 刘峰
 * @date 2018-05-28 09:57:39
 */
@Data
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "pmc_cost_type_param")
public class CostTypeParam implements Serializable {

    private static final long serialVersionUID = -1296485743422714511L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 费用类型编码
     */
    @Column(name = "cost_type_code")
    private String costTypeCode;

    /**
     * 费用配置编码
     */
    @Column(name = "field_name")
    private String fieldName;

    /**
     * 是否必填（0：否 1：是）
     */
    @Column(name = "is_required" ,columnDefinition = "BIT" , length = 1)
    private Integer isRequired;

    /**
     * 是否修改（0：否  1：是）
     */
    @Column(name = "is_update" ,columnDefinition = "BIT" , length = 1)
    private Integer isUpdate;

    /**
     * 是否显示（0：否  1：是）
     */
    @Column(name = "is_show" ,columnDefinition = "BIT" , length = 1)
    private Integer isShow;

    /**
     * 排序序号
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
