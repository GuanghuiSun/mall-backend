package com.mall.utils;

import com.mall.exception.BusinessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static com.mall.base.ErrorCode.SYSTEM_ERROR;
import static com.mall.constant.MessageConstant.SERVER_INTERNAL_ERROR;

@Component
public class RedisIdWorker {
    /**
     * 开始时间戳
     */
    private static final long BEGIN_TIMESTAMP = 1648425600;
    /**
     * 时间戳向左移动位数
     */
    private static final int COUNT_BITS = 32;


    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 自动生成序列号
     *
     * @param keyPrefix 键值前缀
     * @return 序列号
     */
    public long nextId(String keyPrefix) {
        //获取时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timeStamp = nowSecond - BEGIN_TIMESTAMP;
        //生成序列号
        //获取当前时间   年月日
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        //获取原来的序列号
        Long count = stringRedisTemplate.opsForValue().increment(keyPrefix + ":" + date);
        if(count != null){
            //拼接 时间戳向左移32位或上count
            return timeStamp << COUNT_BITS | count;
        }
        throw new BusinessException(SYSTEM_ERROR,SERVER_INTERNAL_ERROR);
    }

}
