package com.itheima.reggile.dto;

import com.itheima.reggile.entity.OrderDetail;
import com.itheima.reggile.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
