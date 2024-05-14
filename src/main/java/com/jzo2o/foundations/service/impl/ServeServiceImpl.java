package com.jzo2o.foundations.service.impl;


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

    /**
     * 上架区域服务
     *
     * @param id
     * @return
     */
    @Override
    public Serve onSale(Long id) {
        Serve serve = baseMapper.selectById(id);
        if (ObjectUtils.isNull(serve)) {
            throw new ForbiddenOperationException("区域服务信息不存在");
        }

        if (!(serve.getSaleStatus() == FoundationStatusEnum.INIT.getStatus()
                || serve.getSaleStatus() == FoundationStatusEnum.ENABLE.getStatus())) {
            throw new ForbiddenOperationException("区域服务必须在草稿/下架状态时才可上架");
        }

        ServeItem serveItem = serveItemMapper.selectById(serve.getServeItemId());
        if (serveItem.getActiveStatus() != FoundationStatusEnum.ENABLE.getStatus()) {
            throw new ForbiddenOperationException("服务项必须启用才可上架该区域服务");
        }

        boolean update = lambdaUpdate().eq(Serve::getId, id)
                .set(Serve::getSaleStatus, FoundationStatusEnum.ENABLE.getStatus())
                .update();
        if (!update) {
            throw new CommonException("区域服务上架失败");
        }

        return baseMapper.selectById(id);
    }

    /**
     * 删除区域服务
     *
     * @param id
     */
    @Override
    public void deleteServe(Long id) {
        int success = baseMapper.deleteById(id);
        if (success <= 0) {
            throw new CommonException("删除失败");
        }
    }

    /**
     * 下架区域服务
     * @param id
     * @return
     */
    @Override
    public Serve offSale(Long id) {
        lambdaUpdate().eq(Serve::getId, id)
                .set(Serve::getSaleStatus, FoundationStatusEnum.DISABLE.getStatus()).update();
        return baseMapper.selectById(id);
    }

    /**
     * 设置热门
     * 0为非热门，1为热门
     * @param id
     * @return
     */
    @Override
    public Serve setHot(Long id, Integer isHot) {
        lambdaUpdate().eq(Serve::getId, id)
                .set(Serve::getIsHot, isHot).update();
        return baseMapper.selectById(id);
    }
}
