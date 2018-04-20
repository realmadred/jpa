package com.example.jpa.jdbc;

import com.alibaba.fastjson.JSONObject;
import com.example.jpa.entity.SysUser;
import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.CriteriaBuilder;
import javax.sound.midi.Soundbank;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JdbcTest {

    @Autowired
    private JdbcTemplate template;
    @Autowired
    private DataSource dataSource;

    private static final String PATTEN = "_";
    private static final String LINE = "\n";
    private static final String TAB = "\t";
    private static final String DAO = "Dao";
    private static final String SERVICE = "Service";
    private static final String CONTROLLER = "Controller";


    @Test
    public void test1() {
        List<Map<String, Object>> map = template.queryForList("SELECT * FROM sys_user WHERE  id = 1", new Object[0]);
        System.out.println(map.size());
    }

    @Test
    public void getFields() {
        String sql = "SELECT * FROM sys_user LIMIT 1";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet resultSet = ps.executeQuery()
        ) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int count = metaData.getColumnCount();
            if (count > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < count; i++) {
                    String columnName = metaData.getColumnName(i + 1);
                    stringBuilder.append(columnName).append(" ")
                            .append(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName));
                    if (i < count - 1) {
                        stringBuilder.append(",");
                    }
                }
                System.out.println(stringBuilder);
                System.out.println(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, stringBuilder.toString()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getDataPuts() {
        String sql = "SELECT * FROM sys_user LIMIT 1";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet resultSet = ps.executeQuery()
        ) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int count = metaData.getColumnCount();
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    String columnName = metaData.getColumnName(i + 1);
                    if ("id".equals(columnName)) continue;
                    System.out.println("data.put(\"" + columnName + "\",1);");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void primaryKeys() {
        ResultSet tables = null;
        ResultSet primaryKeys = null;
        try (
                Connection connection = dataSource.getConnection();
        ) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            // 获取说有表
            tables = databaseMetaData.getTables(connection.getCatalog(), "%", "%", new String[]{"TABLE"});
            while(tables.next()){
                String tableName = tables.getString("TABLE_NAME");
                System.err.println("****** table name ******");
                System.out.println(tableName);
                primaryKeys = databaseMetaData.getPrimaryKeys(null, null, tableName);
                while (primaryKeys.next()) {
                    System.err.println("****** Comment ******");
                    System.err.println("TABLE_CAT : " + primaryKeys.getObject(1));
                    System.err.println("TABLE_NAME : " + primaryKeys.getObject(3));
                    System.err.println("COLUMN_NAME: " + primaryKeys.getObject(4));
                    System.err.println("KEY_SEQ : " + primaryKeys.getObject(5));
                    System.err.println("PK_NAME : " + primaryKeys.getObject(6));
                    System.err.println("****** ******* ******");
                }
            }
            tables.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (tables != null){
                try {
                    tables.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (primaryKeys != null){
                try {
                    primaryKeys.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void createBean() {
        String table = "basic_good_info";// 表名
        String sql = "SELECT * FROM " + table + " LIMIT 1";
        String sqlColumns = "show full columns from " + table;
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                PreparedStatement psColumns = connection.prepareStatement(sqlColumns);
                ResultSet resultSet = ps.executeQuery();
                ResultSet resultSetColumns = psColumns.executeQuery()
        ) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int count = metaData.getColumnCount();
            String tableName = metaData.getTableName(1);
            tableName = getStr1(tableName, PATTEN);
            String entityName = StringUtils.capitalize(tableName);
            String daoName = entityName+DAO;
            String serviceName = entityName+SERVICE;
            String controllerName = entityName+CONTROLLER;
//            tableName = tableName.substring(tableName.indexOf(PATTEN)+1);
            StringBuilder entityStr = new StringBuilder("@Entity").append(LINE)
                    .append("@Table(name = \"")
                    .append(table)
                    .append("\")").append(LINE)
                    .append("@Data").append(LINE)
                    .append("public class " + entityName
//					+ " extends BaseInputVO implements Serializable {" + LINE + LINE +
                            + " implements Serializable {" + LINE + LINE +
                            TAB + "private static final long serialVersionUID = "
                            + new Random().nextLong() + "L;" + LINE + LINE);
            if (count > 0) {
                for (int i = 1; i <= count; i++) {
                    String columnName = metaData.getColumnName(i);
                    String typeName = metaData.getColumnTypeName(i);
                    String remarks = "";
                    if (resultSetColumns.next()) remarks = resultSetColumns.getString("Comment");
                    if ("INT".equalsIgnoreCase(typeName) || "TINYINT".equalsIgnoreCase(typeName)) {
                        typeName = "Integer";
                    } else if ("VARCHAR".equalsIgnoreCase(typeName)) {
                        typeName = "String";
                    } else if ("DATETIME".equalsIgnoreCase(typeName)) {
                        typeName = "Date";
                    } else if ("DOUBLE".equalsIgnoreCase(typeName)) {
                        typeName = "Double";
                    }

                    if (i == 1){
                        entityStr.append(TAB).append("@Id").append(LINE).append(TAB).append("@GeneratedValue").append(LINE);
                    }
                    entityStr.append(TAB).append("@Column(name = \"").append(columnName).append("\")").append(LINE)
                            .append(TAB + "private ").append(typeName + " ").append(getStr1(columnName, PATTEN) + " ;")
                            .append(" // " + remarks + LINE).append(LINE);
                }
            }
            entityStr.append("}" + LINE+LINE);


            System.out.println(LINE+LINE);
            System.out.println("--------------------entity--------------------");
            System.out.println(entityStr);

            System.out.println("--------------------jpa--------------------");
            System.out.println(LINE+LINE);

            String daoStr = "public interface "+daoName+" extends JpaRepository<"+entityName+", Integer> , Serializable {"
                    +LINE+LINE+"}";
            System.out.println(daoStr);

            System.out.println("--------------------service--------------------");
            System.out.println(LINE+LINE);
            String serviceStr = "public interface "+serviceName+"{"
                    +LINE+LINE+"}";
            System.out.println(serviceStr);

            System.out.println("--------------------controller--------------------");
            System.out.println(LINE+LINE);
            String controllerStr = "import org.springframework.web.bind.annotation.RequestMapping;"
                    +LINE
                    +"import org.springframework.web.bind.annotation.RestController;"
                    +LINE+LINE
                    +"@RestController"+LINE
                    +"@RequestMapping(value = \"/"+StringUtils.uncapitalize(entityName)+"\")"
                    +"public class "+controllerName+"{"
                    +LINE+LINE+"}";
            System.out.println(controllerStr);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getStr(String str, String patten) {
        if (StringUtils.isBlank(str) || patten == null) {
            return "";
        }
        int i = str.indexOf(patten);
        if (i >= 0) {
            String substring = str.substring(i + patten.length());
            substring = StringUtils.capitalize(substring);
            str = str.substring(0, i) + substring;
            return getStr(str, patten);
        } else {
            return str;
        }
    }

    public static String getStr1(String str, String patten) {
        if (StringUtils.isBlank(str) || patten == null) {
            return "";
        }
        StringTokenizer tokenizer = new StringTokenizer(str, patten);
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        while (tokenizer.hasMoreElements()) {
            String s = tokenizer.nextToken();
            if (i > 0) {
                s = StringUtils.capitalize(s);
            } else {
                i = 1;
            }
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }

    @Test
    public void getRemarks() {
        try (
                Connection connection = dataSource.getConnection()
        ) {
            String tableName = "sys_user";

            DatabaseMetaData metaData = connection.getMetaData();
            String userName = metaData.getUserName().toUpperCase();
            ResultSet rs = metaData.getColumns(null, userName, tableName, "%");
            Map map = new HashMap();
            while (rs.next()) {
                String colName = rs.getString("COLUMN_NAME");
                String remarks = rs.getString("REMARKS");
                if (remarks == null || remarks.equals("")) {
                    remarks = colName;
                }
                map.put(colName, remarks);
            }
            System.out.println(map);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void test() {
        final String s = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "total_pay_money");
        System.out.println(s);
        final StringBuilder stringBuilder = Joiner.on(",").appendTo(new StringBuilder(""), "ccc", "dd", "aa");
        System.out.println(stringBuilder);
        final ArrayList<Integer> integers = Lists.newArrayList(1, 21, 2, 3, 5, 6);
        System.out.println(Joiner.on(",").join(integers));
    }

}