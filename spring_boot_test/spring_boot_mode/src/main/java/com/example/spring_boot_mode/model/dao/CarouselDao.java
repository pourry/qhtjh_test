package com.example.spring_boot_mode.model.dao;

import com.example.spring_boot_mode.model.entity.Carousel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 走马灯数据访问接口
 */
@Mapper
public interface CarouselDao {

    /** 新增走马灯记录 */
    int toadd(Carousel carousel);

    /** 修改走马灯记录 */
    int toedit(Carousel carousel);

    /** 根据ID删除走马灯 */
    int todelete(String id);

    /** 查询全部走马灯（按排序升序） */
    List<Carousel> selectAll();

    /** 查询启用的走马灯（按排序升序） */
    List<Carousel> selectEnabled();

    /** 根据ID查询 */
    Carousel selectById(String id);

    /** 分页查询全部走马灯 */
    List<Carousel> selectPage(@Param("offset") int offset, @Param("size") int size);

    /** 查询走马灯总数 */
    int selectTotal();
}
