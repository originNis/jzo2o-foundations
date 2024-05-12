package com.jzo2o.foundations.service.impl;


import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.mysql.utils.PageHelperUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务表 服务实现类
 * </p>
 *
 * @author Rybin
 * @since 2024-05-09
 */
@Service
public class ServeServiceImpl extends ServiceImpl<ServeMapper, Serve> implements IServeService {

    /**
     * 区域服务分页查询
     *
     * @param dto
     */
    @Override
    public PageResult<ServeResDTO> page(ServePageQueryReqDTO dto) {
        return PageHelperUtils.selectPage(dto,
                () -> baseMapper.queryServeListByRegionId(dto.getRegionId()));
    }
}
