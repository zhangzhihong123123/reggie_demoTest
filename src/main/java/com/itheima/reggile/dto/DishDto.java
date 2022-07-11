package com.itheima.reggile.dto;
import com.itheima.reggile.entity.Dish;
import com.itheima.reggile.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
