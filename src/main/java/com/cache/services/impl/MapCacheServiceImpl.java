package com.cache.services.impl;

import com.cache.common.enums.DataTypeEnum;
import com.cache.common.exceptions.BusinessException;
import com.cache.dao.entities.Employee;
import com.cache.dao.mappers.EmployeeMapper;
import com.cache.model.CacheResultVO;
import com.cache.model.EmployeeVO;
import com.cache.services.MapCacheService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author zetu
 * @date 2021/3/24
 */
@Service
public class MapCacheServiceImpl implements MapCacheService {

    @Resource
    private EmployeeMapper employeeMapper;

    /**
     * key 为员工编号，value 为员工数据，可以为空，避免击穿
     */
    private static final Map<String, EmployeeVO> CACHE_MAP = new HashMap<>();
    /**
     * key 为员工编号，value 为一个新的 map，这个 map 的 key 为数据类型（data, timeout）
     *
     * @see com.cache.common.enums.DataTypeEnum
     */
    private static final Map<String, Map<String, Object>> EXPIRE_CACHE_MAP = new HashMap<>();
    /**
     * 默认过期时间，单位：ms
     */
    private static final long EXPIRE_MILLISECONDS = 60000;


    @Override
    public CacheResultVO getEmpByNo(String empNo) {
        CacheResultVO result = new CacheResultVO();
        if (CACHE_MAP.containsKey(empNo)) {
            result.setDesc("Map缓存数据");
            result.setEmployee(CACHE_MAP.get(empNo));
            return result;
        }
        EmployeeVO empVo = getByEmpNo(empNo);
        // 放入缓存的 map
        CACHE_MAP.put(empNo, empVo);

        result.setDesc("数据库数据");
        result.setEmployee(CACHE_MAP.get(empNo));
        return result;
    }

    @Override
    public CacheResultVO getEmpByNoWithExpireMap(String empNo) {
        CacheResultVO result = new CacheResultVO();

        checkExpireMap(empNo);

        if (EXPIRE_CACHE_MAP.containsKey(empNo)) {
            result.setDesc("带有过期Map缓存数据");
            result.setEmployee((EmployeeVO)
                    EXPIRE_CACHE_MAP.get(empNo).get(DataTypeEnum.DATA.name()));
            return result;
        }
        EmployeeVO empVo = getByEmpNo(empNo);
        // 放入缓存的 map
        this.put(empNo, empVo);

        result.setDesc("数据库数据");
        result.setEmployee((EmployeeVO)
                EXPIRE_CACHE_MAP.get(empNo).get(DataTypeEnum.DATA.name()));
        return result;
    }

    /**
     * 检查是否过期
     *
     * @param empNo
     */
    private void checkExpireMap(String empNo) {
        if (!EXPIRE_CACHE_MAP.containsKey(empNo)) {
            return;
        }
        long timeout = (long) EXPIRE_CACHE_MAP.get(empNo).get(DataTypeEnum.TIMEOUT.name());
        long current = System.currentTimeMillis();
        if (timeout < current) {
            // 如果已经过期，则移除该 key
            // EXPIRE_CACHE_MAP.remove(empNo);

            // 这里为了检验效果，我们直接抛出一个异常
            throw new BusinessException(String.format("%s 的缓存数据已过期!", empNo));
        }
    }

    /**
     * 将查询到的员工数据和过期时间放入数据 map 中
     *
     * @param empNo
     * @param empVo
     */
    private void put(String empNo, EmployeeVO empVo) {

        long current = System.currentTimeMillis();
        Map<String, Object> dataMap = new HashMap<>(4);
        dataMap.put(DataTypeEnum.TIMEOUT.name(), current + EXPIRE_MILLISECONDS);
        dataMap.put(DataTypeEnum.DATA.name(), empVo);

        EXPIRE_CACHE_MAP.put(empNo, dataMap);
    }

    /**
     * 从数据库获取数据
     *
     * @param empNo
     * @return
     */
    private EmployeeVO getByEmpNo(String empNo) {
        Employee employee = employeeMapper.getByEmpNo(empNo);
        if (Objects.isNull(employee)) {
            return null;
        }
        EmployeeVO vo = new EmployeeVO();
        BeanUtils.copyProperties(employee, vo);
        return vo;
    }
}
