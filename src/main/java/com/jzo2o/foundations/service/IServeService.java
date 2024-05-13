package com.jzo2o.foundations.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 服务表 服务类
 * </p>
 *
 * @author Rybin
 * @since 2024-05-09
 */
public interface IServeService extends IService<Serve> {

    /**
     * 区域服务分页查询
     * @param dto
     */
    public PageResult<ServeResDTO> page(ServePageQueryReqDTO dto);

    /**
     * 批量新增区域服务
     * @param serveUpsertReqDTOList
     */
    public void add(List<ServeUpsertReqDTO> serveUpsertReqDTOList);

    /**
     * 修改区域服务价格
     * @param id
     * @param price
     * @return
     */
    public Serve update(Long id, BigDecimal price);

}
