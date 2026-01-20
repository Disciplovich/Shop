package ru.miit.lab6mybatisdb.mapper;

import org.apache.ibatis.annotations.*;
import ru.miit.lab6mybatisdb.model.Shop;
import java.util.List;
import java.util.Map;

@Mapper
public interface ShopMapper {

    // Поиск магазина по уникальному идентификатору
    @Select("SELECT * FROM shops WHERE id = #{id}")
    Shop findById(Long id);

    // Поиск магазинов по части названия (регистронезависимый поиск)
    @Select("SELECT * FROM shops WHERE name LIKE CONCAT('%', #{name}, '%')")
    List<Shop> findByName(String name);

    // Поиск всех магазинов определенной категории
    @Select("SELECT * FROM shops WHERE category = #{category}")
    List<Shop> findByCategory(String category);

    // Фильтрация магазинов по статусу активности (true/false)
    @Select("SELECT * FROM shops WHERE active = #{active}")
    List<Shop> findByActive(boolean active);

    // Получение всех магазинов из таблицы с сортировкой по названию
    @Select("SELECT * FROM shops ORDER BY name")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "address", column = "address"),
            @Result(property = "phone", column = "phone"),
            @Result(property = "email", column = "email"),
            @Result(property = "openingDate", column = "opening_date"),
            @Result(property = "active", column = "active"),
            @Result(property = "employeeCount", column = "employee_count"),  // Важное поле
            @Result(property = "area", column = "area"),
            @Result(property = "category", column = "category"),
            @Result(property = "revenue", column = "revenue")
    })
    List<Shop> findAll();

    // Создание нового магазина в базе данных
    @Insert("""
        INSERT INTO shops (name, address, phone, email, opening_date, 
                          active, employee_count, area, category, revenue)
        VALUES (#{name}, #{address}, #{phone}, #{email}, #{openingDate},
                #{active}, #{employeeCount}, #{area}, #{category}, #{revenue})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(Shop shop);

    // Полное обновление данных магазина по его идентификатору
    @Update("""
        UPDATE shops 
        SET name = #{name},
            address = #{address},
            phone = #{phone},
            email = #{email},
            opening_date = #{openingDate},
            active = #{active},
            employee_count = #{employeeCount},
            area = #{area},
            category = #{category},
            revenue = #{revenue}
        WHERE id = #{id}
        """)
    void update(Shop shop);

    // Удаление магазина из базы данных по его идентификатору
    @Delete("DELETE FROM shops WHERE id = #{id}")
    void delete(Long id);

    // Подсчет общего количества магазинов в таблице
    @Select("SELECT COUNT(*) FROM shops")
    int count();

    // Расчет средней выручки только для активных магазинов
    @Select("SELECT AVG(revenue) FROM shops WHERE active = true")
    Double getAverageRevenue();

    // Поиск магазинов с количеством сотрудников больше заданного значения
    @Select("SELECT * FROM shops WHERE employee_count > #{minEmployees}")
    List<Shop> findByMinEmployees(int minEmployees);

    // Получение статистики по категориям: количество магазинов и средняя выручка
    @Select("SELECT category, COUNT(*) as count, AVG(revenue) as avg_revenue " +
            "FROM shops GROUP BY category")
    List<Map<String, Object>> getCategoryStats();
}