package com.jzo2o.foundations.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.common.utils.ObjectUtils;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.mapper.RegionMapper;
import com.jzo2o.foundations.mapper.ServeItemMapper;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.model.domain.Region;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.domain.ServeItem;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.mysql.utils.PageHelperUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

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

    @Resource
    ServeItemMapper serveItemMapper;
    @Resource
    RegionMapper regionMapper;

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

    /**
     * 批量新增区域服务
     * @param serveUpsertReqDTOList
     */
    @Override
    public void add(List<ServeUpsertReqDTO> serveUpsertReqDTOList) {
        for (ServeUpsertReqDTO serveUpsertReqDTO : serveUpsertReqDTOList) {
            // 校验合法性
            ServeItem serveItem = serveItemMapper.selectById(serveUpsertReqDTO.getServeItemId());
            if (ObjectUtils.isNull(serveItem)) {
                throw new ForbiddenOperationException("服务项不存在");
            }
            if (serveItem.getActiveStatus() != FoundationStatusEnum.ENABLE.getStatus()) {
                throw new ForbiddenOperationException("服务项未启用");
            }

            Integer count = lambdaQuery().eq(Serve::getServeItemId, serveUpsertReqDTO.getServeItemId())
                    .eq(Serve::getRegionId, serveUpsertReqDTO.getRegionId())
                    .count();
            if (count > 0) {
                throw new ForbiddenOperationException(serveItem.getName() + "服务已存在");
            }

            // 通过校验，插入数据
            Serve serve = BeanUtils.toBean(serveUpsertReqDTO, Serve.class);
            Region region = regionMapper.selectById(serveUpsertReqDTO.getRegionId());
            serve.setCityCode(region.getCityCode());
            baseMapper.insert(serve);
        }
    }

    /**
     * 修改区域服务价格
     * @param id
     * @param price
     * @return
     */
    @Override
    public Serve update(Long id, BigDecimal price) {
        boolean success = lambdaUpdate().eq(Serve::getId, id)
                .set(Serve::getPrice, price)
                .update();
        if (!success) {
            throw new CommonException("修改区域服务价格失败");
        }
        // 返回修改后数据
        return baseMapper.selectById(id);
    }
}
