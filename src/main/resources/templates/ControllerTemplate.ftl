package ${bussPackage}.${entityPackage}.controller;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import ${baseClassPackage}.web.BaseController;
import ${bussPackage}.${entityPackage}.entity.${className};
#if($pkColumnCount > 1)
import ${bussPackage}.${entityPackage}.entity.${className}Key;
#end
import ${bussPackage}.${entityPackage}.page.${className}Page;
import ${bussPackage}.${entityPackage}.service.${className}Service;
import org.springframework.util.CollectionUtils;
import com.ley.springboot.base.utils.ResponseMessage;
import com.ley.springboot.base.utils.Result;
import com.ley.springboot.base.page.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import java.lang.Boolean;
/**
 * <b>功能：</b>${className}Controller<br>
 **/
@RestController
@RequestMapping("/api/${entityPackage}/${pathName}s")
@Api(description = "|${className}|")
@Slf4j
public class ${className}Controller extends BaseController<${className}>{


    @Autowired
    private ${className}Service ${lowerName}Service;

	@ApiOperation(value = "|${className}|分页查询(默认第一页,每页20条)")
    @GetMapping("/page")
    public ResponseMessage<PageInfo<${className}>> page(${className}Page page) throws Exception {
        List<${className}> rows = ${lowerName}Service.queryByPage(page);
        //在没有异常的情况下
        if (!CollectionUtils.isEmpty(rows)) {
            return Result.success(String.valueOf(HttpStatus.OK.value()), "返回分页数据成功",
                    getPageInfo(page.getPager(), rows));
        } else {
            return Result.success(String.valueOf(HttpStatus.OK.value()), "没有可分页的数据",
                    null);
        }
    }

	@ApiOperation(value = "|${className}|查询")
    @GetMapping("")
    public ResponseMessage<List<${className}>> list(${className}Page page) throws Exception {
        List<${className}> ${lowerName}s=${lowerName}Service.queryByList(page);
        if (!CollectionUtils.isEmpty(${lowerName}s)) {
            return Result.success(String.valueOf(HttpStatus.OK.value()), "返回数据集合成功",
                    ${lowerName}s);
        } else {
            return Result.success(String.valueOf(HttpStatus.OK.value()), "没有可返回的数据集合",
                    null);
        }
	}

#if($pkColumnCount == 1)
    @ApiOperation(value = "|${className}|详情")
    @GetMapping("/${pathName}/{${pkColumn.dataName}}")
    public ResponseMessage<${className}> find(@PathVariable ${pkColumn.shortDataType} ${pkColumn.dataName}) throws Exception {
        ${className} findOne=${lowerName}Service.selectByPrimaryKey(${pkColumn.dataName});
        if(findOne!=null){
            return Result.success(String.valueOf(HttpStatus.OK.value()), "返回单条数据成功",
                 findOne);
        }else{
            return Result.success(String.valueOf(HttpStatus.OK.value()), "没有可返回的单条数据",
                    null);
        }
    }
#elseif($pkColumnCount > 1)
    @ApiOperation(value = "|${className}|详情")
    @PostMapping("/${pathName}/${lowerName}Key")
    public ResponseMessage find(${className}Key key) throws Exception {
        ${className} findOne=${lowerName}Service.selectByPrimaryKey(key);
        if(findOne!=null){
            return Result.success(String.valueOf(HttpStatus.OK.value()), "返回单条数据成功",
                 findOne);
        }else{
            return Result.success(String.valueOf(HttpStatus.OK.value()), "没有可返回的单条数据",
                    null);
        }
    }
#end


    @ApiOperation(value = "|${className}|新增")
    @PostMapping("/${pathName}")
    public ResponseMessage<Boolean> create(${className} ${lowerName}) throws Exception {
        int result=${lowerName}Service.insertSelective(${lowerName});
        if(result==1){
           return Result.success(String.valueOf(HttpStatus.OK.value()), "插入数据成功",
                true);
        }else{
           return Result.success(String.valueOf(HttpStatus.OK.value()), "插入数据失败",
                false);
        }
    }

#if($pkColumnCount > 0)
    @ApiOperation(value = "|${className}|修改")
    @PutMapping("/${pathName}")
    public ResponseMessage<Boolean> update(${className} ${lowerName}) throws Exception {
        int result=${lowerName}Service.updateByPrimaryKeySelective(${lowerName});
        if(result==1){
          return Result.success(String.valueOf(HttpStatus.OK.value()), "更新数据成功",
                true);
        }else{
          return Result.success(String.valueOf(HttpStatus.OK.value()), "更新数据失败",
                false);
        }
    }
#end


#if($pkColumnCount == 1)
    @ApiOperation(value = "|${className}|删除")
    @DeleteMapping("/${pathName}/{${pkColumn.dataName}}")
    public ResponseMessage<Boolean> delete(@PathVariable ${pkColumn.shortDataType} ${pkColumn.dataName}) throws Exception {
        int result=${lowerName}Service.deleteByPrimaryKey(${pkColumn.dataName});
        log.info("delete from ${tableName} where ${pkColumn.dataName} = {}", ${pkColumn.dataName});
        return result==1?Result.success(String.valueOf(HttpStatus.OK.value()), "删除数据成功",
            true):Result.success(String.valueOf(HttpStatus.OK.value()), "删除数据失败",
            false);
    }
#elseif($pkColumnCount > 1)
    @ApiOperation(value = "|${className}|删除")
    @DeleteMapping("/${pathName}/${lowerName}Key")
    public ResponseMessage<Boolean> delete(${className}Key key) throws Exception {
        int result=${lowerName}Service.deleteByPrimaryKey(key);
        log.info("delete from ${tableName} where key: {}", key);
        return result==1?Result.success(String.valueOf(HttpStatus.OK.value()), "删除数据成功",
            true):Result.success(String.valueOf(HttpStatus.OK.value()), "删除数据失败",
            false);
    }
#end
}
