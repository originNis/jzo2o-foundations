package com.jzo2o.foundations.controller.operation;

import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation("区域服务分页查询")
    @GetMapping("/page")
    public List<ServeResDTO> page(ServePageQueryReqDTO dto) {
        return null;
    }
}