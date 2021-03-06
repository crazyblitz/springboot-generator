package com.ley.springboot.generator.def;

import com.ley.springboot.generator.utils.GeneratorConstants;
import com.ley.springboot.generator.utils.ResourceKeyConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.ResourceBundle;

/**
 * code resource util
 *
 * @author liuenyuan
 **/
@Slf4j
public class CodeResourceUtil {

    /**
     * database configuration bundle
     **/
    private static final ResourceBundle DATABASE_CONFIG_BUNDLE = ResourceBundle.getBundle(GeneratorConstants.DATABASE_CONFIG_PATH);

    /**
     * generator configuration bundle
     **/
    private static final ResourceBundle GENERATOR_CONFIG_BUNDLE = ResourceBundle.getBundle(GeneratorConstants.GENERATOR_CONFIG_PATH);

    /**
     * driver class name
     **/
    public static String driverClassName;

    /**
     * jdbc url
     **/
    public static String jdbcUrl;

    /**
     * username
     **/
    public static String username;

    /**
     * password
     **/
    public static String password;

    /**
     * database name
     **/
    public static String databaseName;


    /**
     * source root package
     **/
    public static String sourceRootPackage;

    /**
     * resource root package
     **/
    public static String resourceRootPackage;

    /**
     * business package
     **/
    public static String businessPackage;


    /**
     * business package url
     **/
    public static String businessPackageUrl;

    /**
     * entity package and default entity package name is entity
     **/
    public static String entityPackage = "entity";


    /**
     * page package and default entity package name is page
     **/
    public static String pagePackage = "page";


    /**
     * table prefix
     **/
    public static String tablePrefix;

    /**
     * entity url
     **/
    public static String entityUrl;

    /**
     * page url
     **/
    public static String pageUrl;

    /**
     * entity url inner
     **/
    public static String entityUrlInx;

    /**
     * page url inner
     **/
    public static String pageUrlInx;


    public static String codePath;

    /**
     * core project
     **/
    public static String coreProject;

    /**
     * rest project
     **/
    public static String restProject;

    /**
     * base class package
     **/
    public static String baseClassPackage;

    /**
     * page generator type
     **/
    public static String pageGenType;

    /**
     * default table prefix
     **/
    public static final String DEFAULT_TABLE_PREFIX = "TB_";


    public static final String getDiverName() {
        return DATABASE_CONFIG_BUNDLE.getString(ResourceKeyConstants.DRIVER_NAME_KEY);
    }

    public static final String getJdbcUrl() {
        return DATABASE_CONFIG_BUNDLE.getString(ResourceKeyConstants.JDBC_URL_KEY);
    }

    public static final String getUsername() {
        return DATABASE_CONFIG_BUNDLE.getString(ResourceKeyConstants.USERNAME_KEY);
    }

    public static final String getPassword() {
        return DATABASE_CONFIG_BUNDLE.getString(ResourceKeyConstants.PASSWORD_KEY);
    }

    public static final String getDatabaseName() {
        return DATABASE_CONFIG_BUNDLE.getString(ResourceKeyConstants.DATABASE_KEY);
    }

    private static String getBusinessPackage() {
        return GENERATOR_CONFIG_BUNDLE.getString(ResourceKeyConstants.BUSINESS_PACKAGE_KEY);
    }

    public static final String getEntityPackage() {
        return GENERATOR_CONFIG_BUNDLE.getString(ResourceKeyConstants.ENTITY_PACKAGE_KEY);
    }

    public static final String getPagePackage() {
        return GENERATOR_CONFIG_BUNDLE.getString(ResourceKeyConstants.PAGE_PACKAGE_KEY);
    }


    public static final String getTablePrefix() {
        String tablePrefix = GENERATOR_CONFIG_BUNDLE.getString(ResourceKeyConstants.TABLE_PREFIX_KEY);
        //如果table_prefix not define
        if (StringUtils.isBlank(tablePrefix)) {
            tablePrefix = DEFAULT_TABLE_PREFIX;
        } else {
            tablePrefix = tablePrefix + "," + DEFAULT_TABLE_PREFIX;
        }
        boolean isDebugEnabled = log.isDebugEnabled();
        if (isDebugEnabled) {
            log.debug("table prefix: {}", tablePrefix);
        }
        return tablePrefix;
    }
    

    public static final String getSourceRootPackage() {
        return GENERATOR_CONFIG_BUNDLE.getString(ResourceKeyConstants.SOURCE_ROOT_PACKAGE_KEY);
    }

    public static final String getResourcesRootPackage() {
        return GENERATOR_CONFIG_BUNDLE.getString(ResourceKeyConstants.RESOURCES_ROOT_PACKAGE_KEY);
    }


    public static final String getCoreProject() {
        return GENERATOR_CONFIG_BUNDLE.getString(ResourceKeyConstants.CORE_PROJECT_KEY);
    }

    public static final String getRestProject() {
        return GENERATOR_CONFIG_BUNDLE.getString(ResourceKeyConstants.REST_PROJECT_KEY);
    }

    public static final String getBaseClassPackage() {
        return GENERATOR_CONFIG_BUNDLE.getString(ResourceKeyConstants.BASE_CLASS_PACKAGE_KEY);
    }

    public static final String getPageGenType() {
        return GENERATOR_CONFIG_BUNDLE.getString(ResourceKeyConstants.PAGE_GEN_TYPE_KEY);
    }


    /**
     * init resource
     * **/
    static {
        driverClassName = getDiverName();
        jdbcUrl = getJdbcUrl();
        username = getUsername();
        password = getPassword();
        databaseName = getDatabaseName();
        sourceRootPackage = getSourceRootPackage();
        resourceRootPackage = getResourcesRootPackage();
        businessPackage = getBusinessPackage();
        businessPackageUrl = businessPackage.replace(".", "/");
        tablePrefix = getTablePrefix();
        sourceRootPackage = sourceRootPackage.replace(".", "/");
        entityUrl = sourceRootPackage + "/" + businessPackageUrl + "/" + entityPackage + "/";
        pageUrl = sourceRootPackage + "/" + businessPackageUrl + "/" + pagePackage + "/";
        entityUrlInx = businessPackage + "." + entityPackage + ".";
        pageUrlInx = businessPackage + "." + pagePackage + ".";
        codePath = sourceRootPackage + "/" + businessPackageUrl + "/";
        coreProject = getCoreProject();
        restProject = getRestProject();
        baseClassPackage = getBaseClassPackage();
        pageGenType = getPageGenType();
    }
}