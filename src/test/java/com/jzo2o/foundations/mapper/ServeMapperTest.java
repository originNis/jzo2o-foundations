package com.jzo2o.foundations.mapper;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
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

    @Test
    public void queryServeListByRegionId() {
        List<ServeResDTO> res = serveMapper.queryServeListByRegionId(1686303222843662337L);
        Assert.notNull(res, "failed");
    }
}
