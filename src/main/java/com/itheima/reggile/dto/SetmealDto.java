package com.itheima.reggile.dto;
import com.itheima.reggile.entity.Setmeal;
import com.itheima.reggile.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
