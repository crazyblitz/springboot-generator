package ${bussPackage}.${entityPackage}.entity;

import ${baseClassPackage}.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.alibaba.fastjson.annotation.JSONField;
#foreach($importClasses in $!{entityImportClasses})
import ${importClasses};
#end

/**
 * <b>功能：</b>${className}Entity<br>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ${className} extends BaseEntity {

#foreach($po in $!{columnDatas})
#if(${po.shortDataType} == 'Date')
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
#end
    private ${po.shortDataType} ${po.dataName};
#end

}
