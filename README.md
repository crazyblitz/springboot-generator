SpringBoot便捷式开发框架(springboot-generator)

该框架属于springboot便捷式开发框架，现如今支持Mysql,Oracle数据库。该框架可以通过简单的

配置生成entity,dao,service,controller四层。controller使用restful风格和swagger api文档管理，提供

单表的增删改查以及分页查询。

后期考虑增加其他数据库，或许会重构代码层次。

技术选型适合:SpringBoot+Mybatis+MySQL(Oracle)

配置实例如下：

config.properties

    #define code path
    source_root_package=src/main/java
    resources_root_package=src/main/resources
    #define project(relative workspace)
    core_project=springboot-generator-demo
    rest_project=springboot-generator-demo
    #define base class package
    base_class_package=com.ley.springboot.base
    #business package[User defined]
    business_package=com.ley.innovation.contest
    #default entity package business_package+"."+"entity"
    entity_package=
    #default page package business_package+"."+"page"
    page_package=
    #ftl resource url
    template_path=templates
    system_encoding=utf-8
    #table prefix default TB_
    table_prefix=
    #PageType 1: mapper_xml, 2: dao, 3: service, 4: rest
    page_gen_type=1,2,3,4
    

database.properties

    #mysql
    diver_name=com.mysql.jdbc.Driver
    jdbc_url=jdbc:mysql://localhost:3306/db1?characterEncoding=utf-8&&zeroDateTimeBehavior=convertToNull&&useSSL=true
    username=root
    password=root
    database_name=db1
    
    
    #oracle
    #diver_name=oracle.jdbc.OracleDriver
    #jdbc_url=jdbc:oracle:thin:@106.2.13.200:1521:ORCL
    #username=username
    #password=username
    ##填入本用户实例,即是数据库名
    #database_name=database_name

CodeUtils类(代码生成工具类)

    /**
     * code utility class
     **/
    public class CodeUtils {
    
        /**
         * code generate by table name
         *
         * @see DbCodeGenerateFactory#codeGenerate(String, String)
         **/
        public static void codeGenerate(String tableName, String entityPackge) {
            DbCodeGenerateFactory.codeGenerate(tableName, entityPackge);
        }
    
        /**
         * code generate by database all tables
         *
         * @see DbCodeGenerateFactory#codeGenerateList(String)
         **/
        public static void codeGenerate(String entityPackge) {
            try {
                DbCodeGenerateFactory.codeGenerateList(entityPackge);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
        /**
         * code generate by table list
         *
         * @see DbCodeGenerateFactory#codeGenerateList(String[], String)
         **/
        public static void codeGenerate(String[] tableList, String entityPackge) {
            DbCodeGenerateFactory.codeGenerateList(tableList, entityPackge);
        }
    
    
        public static void main(String[] args) {
            codeGenerate("sys");
        }
    
    }


