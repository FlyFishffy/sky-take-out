package com.sky.controller.user;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Api(tags = "C端用户相关接口")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * 添加购物车
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("添加购物车")
    public Result<List<ShoppingCart>> add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车: {}", shoppingCartDTO);
        shoppingCartService.addShoppingCart(shoppingCartDTO);

        List<ShoppingCart> list = shoppingCartService.showShoppingCart();
        return Result.success(list);
    }


    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查看购物车")
    public Result<List<ShoppingCart>> list(){
        log.info("查看购物车...");
        List<ShoppingCart> list = shoppingCartService.showShoppingCart();
        return Result.success(list);
    }


    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result cleanShoppingCart() {
        log.info("清空购物车...");

        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }


    /**
     * 删除单个购物车商品
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/sub")
    @ApiOperation("删除单个购物车商品")
    public Result deleteShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("删除单个购物车商品: {}", shoppingCartDTO);

        shoppingCartService.deleteShoppingCart(shoppingCartDTO);
        return Result.success();
    }
}
