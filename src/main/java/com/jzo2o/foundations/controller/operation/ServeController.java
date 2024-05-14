package com.jzo2o.foundations.controller.operation;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author: Rybin
 * @description:
 * @date: 2024/5/8
 */
@RestController("operationServeController") // 设置了BeanID，以防默认ID与其他类重复
@RequestMapping("/operation/serve")
@Api(tags = "运营端 - 区域服务管理相关接口")
public class ServeController {
    @Resource
    IServeService serveService;

    @ApiOperation("区域服务分页查询")
    @GetMapping("/page")
    public PageResult<ServeResDTO> page(ServePageQueryReqDTO dto) {
        return serveService.page(dto);
    }

    @ApiOperation("批量添加区域服务")
    @PostMapping("/batch")
    public void add(@RequestBody List<ServeUpsertReqDTO> serveUpsertReqDTOList) {
        serveService.add(serveUpsertReqDTOList);
    }

    @ApiOperation("修改区域服务价格")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "区域服务id", required = true, dataTypeClass = Long.class),
            @ApiImplicitParam(name = "price", value = "价格", required = true, dataTypeClass = BigDecimal.class)
    })
    @PutMapping("/{id}")
    public Serve update(@PathVariable("id") Long id, BigDecimal price) {
        return serveService.update(id, price);
    }

    @ApiOperation("上架区域服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "区域服务id", required = true, dataTypeClass = Long.class)
    })
    @PutMapping("/onSale/{id}")
    public Serve onSale(@PathVariable("id") Long id) {
        return serveService.onSale(id);
    }

    @ApiOperation("删除区域服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "区域服务id", required = true, dataTypeClass = Long.class)
    })
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        serveService.deleteServe(id);
    }

    @ApiOperation("下架区域服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "区域服务id", required = true, dataTypeClass = Long.class)
    })
    @PutMapping("/offSale/{id}")
    public void offSale(@PathVariable("id") Long id) {
        serveService.offSale(id);
    }

    @ApiOperation("设置热门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "区域服务id", required = true, dataTypeClass = Long.class)
    })
    @PutMapping("/onHot/{id}")
    public void onHot(@PathVariable("id") Long id) {
        serveService.setHot(id, 1);
    }

    @ApiOperation("取消热门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "区域服务id", required = true, dataTypeClass = Long.class)
    })
    @PutMapping("/offHot/{id}")
    public void offHot(@PathVariable("id") Long id) {
        serveService.setHot(id, 0);
    }
}
