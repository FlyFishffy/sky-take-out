package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private DishMapper dishMapper;

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void saveWithDishes(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);

        //向套餐中插入一条数据
        setmealMapper.insert(setmeal);

        //获取insert语句生成的id
        Long setmealId = setmeal.getId();

        //向套餐内菜品表插入n条数据
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes != null && !setmealDishes.isEmpty()){
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);
            });
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }


    /**
     * 分页查询套餐
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }


    /**
     * 根据id查询套餐和其中菜品
     * @param id
     * @return
     */
    @Override
    public SetmealVO getByIdWithDishes(Long id) {
        //根据id查询套餐
        Setmeal setmeal = setmealMapper.getById(id);

        //根据套餐id查询其中菜品
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);

        //将查询结果封装成VO
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }


    /**
     * 修改套餐
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void updateWithDishes(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);

        //根据id修改套餐
        setmealMapper.update(setmeal);

        //删除套餐内菜品
        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());

        //重新插入菜品
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes != null && !setmealDishes.isEmpty()){
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealDTO.getId());
            });
            //向套餐内菜品表重新插入n个菜品
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }


    /**
     * 起售、停售套餐
     * @param status
     * @param id
     */
    @Override
    public void StartOrStop(Integer status, Long id) {
        //判断套餐内菜品是否有未起售的
        if(Objects.equals(status, StatusConstant.ENABLE)){
            List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
            setmealDishes.forEach(setmealDish -> {
                Dish dish = dishMapper.getById(setmealDish.getDishId());
                if(Objects.equals(dish.getStatus(), StatusConstant.DISABLE)){
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            });
        }

        //起售套餐
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        setmeal.setId(id);

        setmealMapper.update(setmeal);
    }


    /**
     * 批量删除套餐
     * @param ids
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //当前套餐是否有起售
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if(Objects.equals(setmeal.getStatus(), StatusConstant.ENABLE)){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        //删除套餐
        setmealMapper.deleteByIds(ids);

        //删除套餐关联菜品
        setmealDishMapper.deleteBySetmealIds(ids);
    }
}
