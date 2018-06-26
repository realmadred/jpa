package com.example.jpa.entity;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QCostTypeParam is a Querydsl query type for CostTypeParam
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCostTypeParam extends EntityPathBase<CostTypeParam> {

    private static final long serialVersionUID = -2135542824L;

    public static final QCostTypeParam costTypeParam = new QCostTypeParam("costTypeParam");

    public final StringPath costTypeCode = createString("costTypeCode");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath createOrgCode = createString("createOrgCode");

    public final StringPath createOrgName = createString("createOrgName");

    public final StringPath createUserId = createString("createUserId");

    public final StringPath createUserName = createString("createUserName");

    public final StringPath fieldName = createString("fieldName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> isRequired = createNumber("isRequired", Integer.class);

    public final NumberPath<Integer> isShow = createNumber("isShow", Integer.class);

    public final NumberPath<Integer> isUpdate = createNumber("isUpdate", Integer.class);

    public final DateTimePath<java.util.Date> modifedDatetime = createDateTime("modifedDatetime", java.util.Date.class);

    public final StringPath modifedOrgCode = createString("modifedOrgCode");

    public final StringPath modifedOrgName = createString("modifedOrgName");

    public final StringPath modifedUserId = createString("modifedUserId");

    public final StringPath modifedUserName = createString("modifedUserName");

    public final NumberPath<Integer> optimisticLock = createNumber("optimisticLock", Integer.class);

    public final StringPath remark = createString("remark");

    public final NumberPath<Integer> sort = createNumber("sort", Integer.class);

    public QCostTypeParam(String variable) {
        super(CostTypeParam.class, forVariable(variable));
    }

    public QCostTypeParam(Path<? extends CostTypeParam> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCostTypeParam(PathMetadata metadata) {
        super(CostTypeParam.class, metadata);
    }

}

