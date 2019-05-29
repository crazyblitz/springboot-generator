package ${bussPackage}.${entityPackage}.dao;

import ${baseClassPackage}.dao.BaseDao;
import ${bussPackage}.${entityPackage}.entity.${className};
import org.apache.ibatis.annotations.Mapper;
/**
 * <b>功能：</b>${className}Dao<br>
 **/
@Mapper
public interface ${className}Dao extends BaseDao<${className}> {

}
