package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * 新增套餐
     * @param setmealDTO
     */
    void saveWithDishes(SetmealDTO setmealDTO);


    /**
     * 分页查询套餐
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);


    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    SetmealVO getByIdWithDishes(Long id);


    /**
     * 修改套餐
     * @param setmealDTO
     */
    void updateWithDishes(SetmealDTO setmealDTO);


    /**
     * 起售、停售套餐
     * @param status
     * @param id
     */
    void StartOrStop(Integer status, Long id);


    /**
     * 批量删除套餐
     * @param ids
     */
    void deleteBatch(List<Long> ids);


    /**
     * 根据分类id查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);


    /**
     * 根据套餐id查询菜品
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);
}
