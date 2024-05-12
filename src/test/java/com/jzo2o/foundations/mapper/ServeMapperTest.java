package com.jzo2o.foundations.mapper;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: Rybin
 * @description:
 * @date: 2024/5/8
 */
@SpringBootTest
public class ServeMapperTest {
    @Resource
    private ServeMapper serveMapper;
    @Resource
    IServeService serveService;

    @Test
    public void queryServeListByRegionId() {
        List<ServeResDTO> res = serveMapper.queryServeListByRegionId(1686303222843662337L);
        Assert.notNull(res, "failed");
    }

    @Test
    public void ServePage() {
        ServePageQueryReqDTO dto = new ServePageQueryReqDTO();
        dto.setRegionId(1686303222843662337L);
        dto.setPageNo(2L);
        dto.setPageSize(2L);
        PageResult<ServeResDTO> res = serveService.page(dto);
        Assert.notNull(res, "failed");
    }
}
