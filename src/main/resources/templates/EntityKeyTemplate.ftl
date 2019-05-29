package ${bussPackage}.${entityPackage}.entity;

import ${baseClassPackage}.entity.BaseEntity;
import lombok.Data;
#foreach($importClasses in $!{entityImportClasses})
import ${importClasses};
#end

/**
 * <b>功能：</b>${className}Key Entity<br>
 **/
@Data
public class ${className}Key extends BaseEntity {

#foreach($po in $!{pkColumnDatas})
    private ${po.shortDataType} ${po.dataName};
#end
}
