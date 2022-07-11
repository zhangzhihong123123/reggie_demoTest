package com.itheima.reggile.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggile.entity.AddressBook;
import com.itheima.reggile.mapper.AddressBookMapper;;
import com.itheima.reggile.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
