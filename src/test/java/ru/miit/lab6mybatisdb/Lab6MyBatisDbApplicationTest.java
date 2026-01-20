package ru.miit.lab6mybatisdb;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.miit.lab6mybatisdb.mapper.ShopMapper;
import ru.miit.lab6mybatisdb.model.Shop;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ShopMyBatisApplicationTest {

    @Autowired
    private ShopMapper shopMapper;

    @Test
    void testAllCRUDOperations() {
        log.info("=== Тестирование CRUD операций для магазинов ===");

        //Проверка начальных данных
        List<Shop> allShops = shopMapper.findAll();
        log.info("Всего магазинов в БД: {}", allShops.size());
        assertFalse(allShops.isEmpty());

        //CREATE
        Shop newShop = new Shop();
        newShop.setName("Тестовый магазин");
        newShop.setAddress("ул. Тестовая, 1");
        newShop.setPhone("+79990000001");
        newShop.setEmail("test@shop.ru");
        newShop.setOpeningDate(LocalDate.now());
        newShop.setActive(true);
        newShop.setEmployeeCount(5);
        newShop.setArea(100.0);
        newShop.setCategory("тест");
        newShop.setRevenue(500000.0);

        shopMapper.insert(newShop);
        assertNotNull(newShop.getId());
        log.info("Создан магазин с ID: {}", newShop.getId());

        //READ
        Shop foundShop = shopMapper.findById(newShop.getId());
        assertNotNull(foundShop);
        assertEquals("Тестовый магазин", foundShop.getName());
        log.info("Найден магазин: {}", foundShop.getName());

        //UPDATE
        foundShop.setName("Обновленный магазин");
        foundShop.setRevenue(600000.0);
        shopMapper.update(foundShop);

        Shop updatedShop = shopMapper.findById(newShop.getId());
        assertEquals("Обновленный магазин", updatedShop.getName());
        assertEquals(600000.0, updatedShop.getRevenue());
        log.info("Магазин обновлен: {}", updatedShop.getName());

        //Поиск по категории
        List<Shop> productShops = shopMapper.findByCategory("продукты");
        assertFalse(productShops.isEmpty());
        log.info("Магазинов продуктовых: {}", productShops.size());

        //Поиск по названию
        List<Shop> magnetShops = shopMapper.findByName("Магнит");
        assertFalse(magnetShops.isEmpty());
        log.info("Найдено магазинов 'Магнит': {}", magnetShops.size());

        //Статистика
        int count = shopMapper.count();
        Double avgRevenue = shopMapper.getAverageRevenue();
        log.info("Статистика: всего магазинов={}, средняя выручка={}", count, avgRevenue);

        //DELETE
        shopMapper.delete(newShop.getId());
        Shop deletedShop = shopMapper.findById(newShop.getId());
        assertNull(deletedShop);
        log.info("Магазин с ID={} удален", newShop.getId());

        //Статистика по категориям
        List<Map<String, Object>> categoryStats = shopMapper.getCategoryStats();
        log.info("Статистика по категориям: {}", categoryStats);

        log.info("=== Все CRUD операции успешно протестированы ===");
    }

    @Test
    void testAdvancedQueries() {
        // Магазины с более чем 10 сотрудниками
        List<Shop> largeShops = shopMapper.findByMinEmployees(10);
        log.info("Магазинов с >10 сотрудниками: {}", largeShops.size());

        // Только активные магазины
        List<Shop> activeShops = shopMapper.findByActive(true);
        log.info("Активных магазинов: {}", activeShops.size());

        assertTrue(activeShops.size() > 0);
    }
}