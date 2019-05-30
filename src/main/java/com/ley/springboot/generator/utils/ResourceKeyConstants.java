package com.ley.springboot.generator.utils;

/**
 * resource key constants
 *
 * @author liuenyuan
 **/
public interface ResourceKeyConstants {

    //database start

    /**
     * database type mysql
     * **/
    String DATABASE_TYPE_MYSQL = "mysql";

    /**
     * driver name key
     **/
    String DRIVER_NAME_KEY = "diver_name";


    /**
     * database name key
     **/
    String DATABASE_KEY = "database_name";

    /**
     * jdbc url key
     **/
    String JDBC_URL_KEY = "jdbc_url";

    /**
     * username key
     **/
    String USERNAME_KEY = "username";

    /**
     * password key
     **/
    String PASSWORD_KEY = "password";


    /**
     * table prefix key
     **/
    String TABLE_PREFIX_KEY = "table_prefix";
    //database end


    //generator start
    /**
     * business package key
     **/
    String BUSINESS_PACKAGE_KEY = "business_package";


    /**
     * core project key
     **/
    String CORE_PROJECT_KEY = "core_project";

    /**
     * rest project key
     **/
    String REST_PROJECT_KEY = "rest_project";

    /**
     * base class package
     **/
    String BASE_CLASS_PACKAGE_KEY = "base_class_package";

    /**
     * source root package key
     **/
    String SOURCE_ROOT_PACKAGE_KEY = "source_root_package";

    /**
     * resource root package key
     **/
    String RESOURCES_ROOT_PACKAGE_KEY = "resources_root_package";

    /**
     * page generator type key
     **/
    String PAGE_GEN_TYPE_KEY = "page_gen_type";

    /**
     * entity package key
     **/
    String ENTITY_PACKAGE_KEY = "entity_package";

    /**
     * page package key
     **/
    String PAGE_PACKAGE_KEY = "page_package";


    //ftl resource start


    /**
     * system encoding key
     **/
    String SYSTEM_ENCODING_KEY = "system_encoding";

    //ftl resource end
}
