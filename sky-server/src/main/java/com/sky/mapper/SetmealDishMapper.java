package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品id查询对应的套餐id
     * @param DishIds
     * @return
     */
    List<Long> getSetmealIdsByDishIds(List<Long> DishIds);


    /**
     * 批量插入套餐内菜品数据
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);


    /**
     * 根据套餐id查询其中菜品
     * @param setmealId
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getBySetmealId(Long setmealId);


    /**
     * 根据套餐id删除其中菜品
     * @param setmealId
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);


    /**
     * 批量删除套餐关联的菜品
     * @param SetmealIds
     */
    void deleteBySetmealIds(List<Long> SetmealIds);
}
