package ${bussPackage}.${entityPackage}.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ${baseClassPackage}.service.BaseService;
import ${bussPackage}.${entityPackage}.dao.${className}Dao;
import ${bussPackage}.${entityPackage}.entity.${className};
import lombok.extern.slf4j.Slf4j;
#if($pkColumnCount > 1)
import ${bussPackage}.${entityPackage}.entity.${className}Key;
#end


/**
 * <br>
 * <b>功能：</b>${className}Service<br>
 */
@Service("$!{lowerName}Service")
@Slf4j
public class ${className}Service extends BaseService<${className}, #if($pkColumnCount == 0)Void#elseif($pkColumnCount == 1)${pkColumn.shortDataType}#else${className}Key#end> {


    @Autowired(required = false)
    private ${className}Dao dao;

    public ${className}Dao getDao() {
        return dao;
    }

}
