package com.ley.springboot.generator;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import com.ley.springboot.generator.bean.BaseDbCreateBean;
import com.ley.springboot.generator.bean.ColumnData;
import com.ley.springboot.generator.bean.MysqlDbCreateBean;
import com.ley.springboot.generator.bean.OracleDbCreateBean;
import com.ley.springboot.generator.def.CodeResourceUtil;
import com.ley.springboot.generator.utils.GeneratorConstants;
import com.ley.springboot.generator.utils.ResourceKeyConstants;
import com.ley.springboot.generator.utils.TemplateConstants;
import com.ley.springboot.generator.utils.TemplateTypeConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * database code generate factory
 **/
@Slf4j
public class DbCodeGenerateFactory {

    /**
     * jdbc jdbcUrl
     **/
    private static String jdbcUrl;

    /**
     * user name
     **/
    private static String username;

    /**
     * password
     **/
    private static String passWord;

    /**
     * business package
     **/
    private static String businessPackage;

    /**
     * project path
     **/
    private static String projectPath = getProjectPath();

    /**
     * workspace path
     **/
    private static String workspacePath = getWorkspacePath();

    /**
     * db create bean
     **/
    private static BaseDbCreateBean createBean;

    static {
        //get concrete db create bean
        jdbcUrl = CodeResourceUtil.jdbcUrl;
        username = CodeResourceUtil.username;
        passWord = CodeResourceUtil.password;
        businessPackage = CodeResourceUtil.businessPackage;
        if (isMysql()) {
            createBean = new MysqlDbCreateBean(jdbcUrl, username, passWord);
            log.info("create bean : {}", createBean);
        } else {
            createBean = new OracleDbCreateBean(jdbcUrl, username, passWord);
            log.info("create bean : {}", createBean);
        }
        Assert.notNull(createBean, "db create bean must be not null.");
    }


    /**
     * if mysql,return true.if oracle,return false
     **/
    public static boolean isMysql() {
        if (CodeResourceUtil.getDiverName().toLowerCase().contains("mysql")) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * @param tableNames    table name list
     * @param entityPackage entity generate package
     **/
    public static void codeGenerateList(String[] tableNames, String entityPackage) {
        if (!CollectionUtils.isEmpty(Arrays.asList(tableNames))) {
            for (String tableName : tableNames) {
                codeGenerate(tableName, entityPackage);
            }
        }
    }

    /**
     * according by database all table to generate code(根据数据库中表生成所有的数据库表代码)
     *
     * @param entityPackage entity generate package
     **/
    public static void codeGenerateList(String entityPackage) throws Exception {
        List<String> tableList = createBean.getTables();
        if (!CollectionUtils.isEmpty(tableList)) {
            for (String tableName : tableList) {
                codeGenerate(tableName, entityPackage);
            }
        }
    }

    /**
     * code generator
     *
     * @param tableName     table name
     * @param entityPackage entity generate package
     **/
    public static void codeGenerate(String tableName, String entityPackage) {
        List columnDatas;
        try {
            //获取数据库的列
            columnDatas = createBean.getColumnDatas(tableName);
        } catch (Exception arg55) {
            arg55.printStackTrace();
            return;
        }
        List<ColumnData> pkColumnDatas = getPkColumns(columnDatas);
        List<ColumnData> notPkColumnDatas = getNotPkColumns(columnDatas);
        //获取数据库表主键个数
        int pkColumnCount = pkColumnDatas.size();
        String shortClassName = createBean.formatClassName(tableName);
        String className = shortClassName;
        String lowerName = className.substring(0, 1).toLowerCase() + className.substring(1, className.length());
        String pathName = shortClassName.substring(0, 1).toLowerCase()
                + shortClassName.substring(1, shortClassName.length());
        String coreProjectPath = workspacePath + "/" + CodeResourceUtil.coreProject;
        String restProjectPath = workspacePath + "/" + CodeResourceUtil.restProject;
        String coreProjectSrcPath = coreProjectPath + "/" + CodeResourceUtil.sourceRootPackage + "/"
                + CodeResourceUtil.businessPackageUrl;
        String restProjectResourcesPath = restProjectPath + "/" + CodeResourceUtil.resourceRootPackage + "/mybatis";
        String restProjectSrcPath = restProjectPath + "/" + CodeResourceUtil.sourceRootPackage + "/"
                + CodeResourceUtil.businessPackageUrl;
        String srcPath = projectPath + CodeResourceUtil.sourceRootPackage + "\\";
        (new StringBuilder(String.valueOf(srcPath))).append(CodeResourceUtil.businessPackageUrl).append("\\").toString();
        String pagePath = "/" + entityPackage + "/page/" + className + "Page.java";
        String entityPath = "/" + entityPackage + "/entity/" + className + ".java";
        String entityKeyPath = "/" + entityPackage + "/entity/" + className + "Key.java";
        String mapperPath = "/" + entityPackage + "/dao/" + className + "Dao.java";
        String servicePath = "/" + entityPackage + "/service/" + className + "Service.java";
        String controllerPath = "/" + entityPackage + "/controller/" + className + "Controller.java";
        String sqlMapperPath = "/mapper/" + entityPackage + "/" + className + "Mapper.xml";
        String lowerNames = lowerName + "s";

        if (lowerName.endsWith("y")) {
            lowerNames = lowerName.substring(0, lowerName.length() - 1) + "ies";
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = format.format(Calendar.getInstance().getTime());
        VelocityContext context = new VelocityContext();

        //define velocity context properties
        context.put("className", className);
        context.put("lowerName", lowerName);
        context.put("pathName", pathName);
        context.put("tableName", tableName);
        context.put("codeName", tableName);
        context.put("bussPackage", businessPackage);
        context.put("entityPackage", entityPackage);
        context.put("baseClassPackage", CodeResourceUtil.baseClassPackage);
        context.put("lowerNames", lowerNames);
        context.put("currentDate", currentDate);
        context.put("columnDatas", columnDatas);
        context.put("pkColumnDatas", pkColumnDatas);
        context.put("pkColumnCount", Integer.valueOf(pkColumnCount));
        context.put("notPkColumnDatas", notPkColumnDatas);
        context.put("entityImportClasses", createBean.getEntityImportClasses(columnDatas));
        context.put("velocityCount", pkColumnDatas.size());
        if (pkColumnCount == 1) {
            context.put("pkColumn", pkColumnDatas.get(0));
        }

        try {
            Map flagMapperXml = createBean.getAutoCreateSql(tableName);
            context.put("SQL", flagMapperXml);
        } catch (Exception arg54) {
            arg54.printStackTrace();
            return;
        }

        BitSet flag = new BitSet();
        String[] pageGenTypes = CodeResourceUtil.pageGenType.split(",");
        int pageGenTypeLength = pageGenTypes.length;

        for (int i = 0; i < pageGenTypeLength; ++i) {
            String genType = pageGenTypes[i];
            flag.set(Integer.parseInt(genType));
        }

        //write mapper xml
        //判断是否为Oracle数据库,Oracle数据库列最好加上"mysqlColumnName",否则可能报错ORA-00904: invalid identifier "column name"
        if (flag.get(TemplateTypeConstants.MAPPER_XML)) {
            if (isMysql()) {
                CommonPageParser.writerPage(context, TemplateConstants.MYSQL_MAPPER_XML_TEMPLATE_PATH, restProjectResourcesPath,
                        sqlMapperPath);
            } else {
                CommonPageParser.writerPage(context, TemplateConstants.ORACLE_MAPPER_XML_TEMPLATE_PATH, restProjectResourcesPath,
                        sqlMapperPath);
            }
        }

        //write entity,page,dao java
        if (flag.get(TemplateTypeConstants.DAO)) {
            if (pkColumnCount > 1) {
                CommonPageParser.writerPage(context, TemplateConstants.ENTITY_KEY_TEMPLATE_PATH, coreProjectSrcPath,
                        entityKeyPath);
                CommonPageParser.writerPage(context, TemplateConstants.ENTITY_WITH_KEY_TEMPLATE_PATH, coreProjectSrcPath,
                        entityPath);
            } else {
                CommonPageParser.writerPage(context, TemplateConstants.ENTITY_TEMPLATE_PATH, coreProjectSrcPath,
                        entityPath);
            }
            CommonPageParser.writerPage(context, TemplateConstants.PAGE_TEMPLATE_PATH, coreProjectSrcPath, pagePath);
            CommonPageParser.writerPage(context, TemplateConstants.DAO_TEMPLATE_PATH, coreProjectSrcPath, mapperPath);
        }

        //write service java
        if (flag.get(TemplateTypeConstants.SERVICE)) {
            CommonPageParser.writerPage(context, TemplateConstants.SERVICE_TEMPLATE_PATH, coreProjectSrcPath, servicePath);
        }

        //write controller java
        if (flag.get(TemplateTypeConstants.CONTROLLER)) {
            CommonPageParser.writerPage(context, TemplateConstants.CONTROLLER_TEMPLATE_PATH, restProjectSrcPath,
                    controllerPath);
        }

        log.info("----------------------------代码生成完毕---------------------------");
    }


    /**
     * get pk columns
     **/
    private static List<ColumnData> getPkColumns(List<ColumnData> columnDatas) {
        ArrayList<ColumnData> pks = new ArrayList<>();
        Iterator<ColumnData> arg1 = columnDatas.iterator();
        while (arg1.hasNext()) {
            ColumnData columnData = arg1.next();
            if ("PRI".equals(columnData.getColumnKey())) {
                pks.add(columnData);
            }
        }
        return pks;
    }


    /**
     * get not pk columns
     **/
    private static List<ColumnData> getNotPkColumns(List<ColumnData> columnDatas) {
        ArrayList<ColumnData> notPkColumns = new ArrayList();
        Iterator<ColumnData> arg1 = columnDatas.iterator();

        while (arg1.hasNext()) {
            ColumnData columnData = arg1.next();
            if (!"PRI".equals(columnData.getColumnKey())) {
                notPkColumns.add(columnData);
            }
        }
        return notPkColumns;
    }

    /**
     * get project path
     **/
    public static String getProjectPath() {
        String path = System.getProperty("user.dir").replace("\\", "/") + "/";
        boolean isDebugEnabled=log.isDebugEnabled();
        if(isDebugEnabled){
            log.info("project path: {}", path);
        }
        return path;
    }

    /**
     * get workspace path
     **/
    public static String getWorkspacePath() {
        String projectPath = getProjectPath();
        File file = new File(projectPath);
        String projectParent = file.getParent();
        boolean isDebugEnabled=log.isDebugEnabled();
        if(isDebugEnabled){
            log.debug("workspace: {}", projectParent);
        }
        return projectParent;
    }


}