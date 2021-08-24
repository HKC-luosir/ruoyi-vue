package com.stdiet.custom.service;

import com.stdiet.custom.domain.SysRecipes;
import com.stdiet.custom.domain.SysRecipesDaily;
import com.stdiet.custom.domain.SysRecipesDailyDishes;

import java.sql.Array;
import java.util.List;

public interface ISysRecipesService {

    public int addRecipes(SysRecipes sysRecipes);

    public List<SysRecipes> selectSysRecipesByRecipesId(Long id);

    public List<SysRecipes> selectSysRecipesByRecipesIdShow(Long id);

    public List<SysRecipesDailyDishes> selectDishesByMenuId(Long id);

    public List<SysRecipesDailyDishes> selectDishesByMenuIdShow(Long id);

    public int updateDishesDetail(SysRecipesDailyDishes sysRecipesDaily);

    public int addDishes(SysRecipesDailyDishes sysRecipesDaily);

    public int deleteDishes(Long id);

    public Long[] replaceDishes(List<SysRecipesDailyDishes> sysRecipesDailyDishes);

}