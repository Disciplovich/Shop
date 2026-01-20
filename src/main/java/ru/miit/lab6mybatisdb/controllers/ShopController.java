package ru.miit.lab6mybatisdb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.miit.lab6mybatisdb.model.Shop;
import ru.miit.lab6mybatisdb.service.ShopService;

import java.util.List;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
@Tag(name = "Магазины", description = "API для управления магазинами")
public class ShopController {

    private final ShopService shopService;

    @Operation(summary = "Получить все магазины")
    @GetMapping
    public ResponseEntity<List<Shop>> getAllShops() {
        List<Shop> shops = shopService.findAll();
        return ResponseEntity.ok(shops);
    }

    @Operation(summary = "Получить магазин по ID")
    @GetMapping("/{id}")
    public ResponseEntity<Shop> getShopById(
            @Parameter(description = "ID магазина")
            @PathVariable Long id) {
        Shop shop = shopService.findById(id);
        return shop != null ? ResponseEntity.ok(shop) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Создать новый магазин")
    @PostMapping
    public ResponseEntity<Shop> createShop(
            @Parameter(description = "Данные нового магазина")
            @RequestBody Shop shop) {
        shopService.insert(shop);
        return ResponseEntity.status(HttpStatus.CREATED).body(shop);
    }

    @Operation(summary = "Обновить магазин")
    @PutMapping("/{id}")
    public ResponseEntity<Shop> updateShop(
            @Parameter(description = "ID магазина")
            @PathVariable Long id,
            @Parameter(description = "Обновленные данные магазина")
            @RequestBody Shop shop) {
        shop.setId(id);
        boolean updated = shopService.update(shop);
        return updated ? ResponseEntity.ok(shop) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Удалить магазин")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShop(
            @Parameter(description = "ID магазина")
            @PathVariable Long id) {
        boolean deleted = shopService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Поиск магазинов по названию")
    @GetMapping("/search/by-name")
    public ResponseEntity<List<Shop>> searchShopsByName(
            @Parameter(description = "Часть названия для поиска")
            @RequestParam String name) {
        List<Shop> shops = shopService.findByName(name);
        return ResponseEntity.ok(shops);
    }

    @Operation(summary = "Поиск магазинов по категории")
    @GetMapping("/search/by-category")
    public ResponseEntity<List<Shop>> searchShopsByCategory(
            @Parameter(description = "Категория магазина")
            @RequestParam String category) {
        List<Shop> shops = shopService.findByCategory(category);
        return ResponseEntity.ok(shops);
    }
}