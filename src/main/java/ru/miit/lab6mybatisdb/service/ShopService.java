package ru.miit.lab6mybatisdb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.miit.lab6mybatisdb.mapper.ShopMapper;
import ru.miit.lab6mybatisdb.model.Shop;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopService {

    private final ShopMapper shopMapper;

    public List<Shop> findAll() {
        return shopMapper.findAll();
    }

    public Shop findById(Long id) {
        return shopMapper.findById(id);
    }

    public List<Shop> findByName(String name) {
        return shopMapper.findByName(name);
    }

    public List<Shop> findByCategory(String category) {
        return shopMapper.findByCategory(category);
    }

    @Transactional
    public void insert(Shop shop) {
        shopMapper.insert(shop);
    }

    @Transactional
    public boolean update(Shop shop) {
        if (shopMapper.findById(shop.getId()) == null) {
            return false;
        }
        shopMapper.update(shop);
        return true;
    }

    @Transactional
    public boolean delete(Long id) {
        if (shopMapper.findById(id) == null) {
            return false;
        }
        shopMapper.delete(id);
        return true;
    }
}