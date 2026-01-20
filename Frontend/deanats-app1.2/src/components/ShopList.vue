<template>
  <ShopSearch
    :shops="shops"
    @view-shop="viewShop"
    @clear-search="clearSearch"
  />
  <div class="container">
    <h2 class="title">🏪 Список магазинов</h2>

    <!-- Поиск магазинов -->
    <div class="search-section">
      <div class="search-bar">
        <input
          type="text"
          v-model="searchName"
          placeholder="Поиск по названию..."
          @input="searchByName"
        />
        <select v-model="searchCategory" @change="searchByCategory">
          <option value="">Все категории</option>
          <option value="продукты">Продукты</option>
          <option value="спорттовары">Спорттовары</option>
          <option value="электроника">Электроника</option>
          <option value="косметика">Косметика</option>
          <option value="одежда">Одежда</option>
        </select>
        <button class="btn-clear" @click="clearSearch">Сбросить</button>
      </div>
    </div>

    <div class="top-bar">
      <button class="add-btn" @click="$router.push('/create')">
        ➕ Добавить магазин
      </button>
    </div>

    <table class="shop-table" v-if="displayedShops.length">
      <thead>
      <tr>
        <th>ID</th>
        <th>Название</th>
        <th>Адрес</th>
        <th>Телефон</th>
        <th>Категория</th>
        <th>Сотрудники</th>
        <th>Площадь (м²)</th>
        <th>Выручка</th>
        <th>Статус</th>
        <th>Дата открытия</th>
        <th>Действия</th>
      </tr>
      </thead>

      <tbody>
      <tr v-for="shop in displayedShops" :key="shop.id">
        <td>{{ shop.id }}</td>
        <td class="bold">{{ shop.name }}</td>
        <td>{{ shop.address }}</td>
        <td>{{ shop.phone }}</td>
        <td><span class="category-tag">{{ shop.category }}</span></td>
        <td>{{ shop.employeeCount }}</td>
        <td>{{ shop.area }}</td>
        <td>{{ formatCurrency(shop.revenue) }}</td>
        <td>
          <span :class="['status', shop.active ? 'active' : 'inactive']">
            {{ shop.active ? 'Активен' : 'Неактивен' }}
          </span>
        </td>
        <td>{{ formatDate(shop.openingDate) }}</td>
        <td class="actions">
          <button class="edit-btn" @click="editShop(shop.id)">✏</button>
          <button class="delete-btn" @click="deleteShop(shop.id)">🗑</button>
        </td>
      </tr>
      </tbody>
    </table>

    <p v-else class="no-results">Магазины не найдены.</p>

    <div class="stats">
      <div class="stat-card">
        <h4>Всего магазинов</h4>
        <p class="stat-value">{{ shops.length }}</p>
      </div>
      <div class="stat-card">
        <h4>Активных</h4>
        <p class="stat-value">{{ activeShopsCount }}</p>
      </div>
      <div class="stat-card">
        <h4>Средняя выручка</h4>
        <p class="stat-value">{{ formatCurrency(avgRevenue) }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import shopService from '../services/shopService';

export default {
  data() {
    return {
      searchName: '',
      searchCategory: '',
      displayedShops: []
    };
  },
  computed: {
    ...mapGetters(['allShops']),
    shops() {
      return this.allShops;
    },
    activeShopsCount() {
      return this.shops.filter(shop => shop.active).length;
    },
    avgRevenue() {
      if (this.shops.length === 0) return 0;
      const total = this.shops.reduce((sum, shop) => sum + (shop.revenue || 0), 0);
      return total / this.shops.length;
    }
  },
  watch: {
    shops: {
      immediate: true,
      handler(newShops) {
        this.displayedShops = newShops;
      }
    }
  },
  methods: {
    ...mapActions(['fetchShops', 'deleteShop']),
    async searchByName() {
      if (this.searchName.trim()) {
        try {
          const response = await shopService.searchShopsByName(this.searchName);
          this.displayedShops = response.data;
        } catch (error) {
          console.error('Ошибка поиска:', error);
        }
      } else {
        this.displayedShops = this.shops;
      }
    },
    async searchByCategory() {
      if (this.searchCategory) {
        try {
          const response = await shopService.searchShopsByCategory(this.searchCategory);
          this.displayedShops = response.data;
        } catch (error) {
          console.error('Ошибка поиска:', error);
        }
      } else {
        this.displayedShops = this.shops;
      }
    },
    clearSearch() {
      this.searchName = '';
      this.searchCategory = '';
      this.displayedShops = this.shops;
    },
    viewShop(id) {
      this.$router.push(`/shops/${id}`);
    },
    editShop(id) {
      this.$router.push(`/shops/${id}`);
    },
    formatDate(dateStr) {
      if (!dateStr) return '';
      return new Date(dateStr).toLocaleDateString('ru-RU');
    },
    formatCurrency(value) {
      if (!value) return '0 ₽';
      return new Intl.NumberFormat('ru-RU', {
        style: 'currency',
        currency: 'RUB',
        minimumFractionDigits: 0
      }).format(value);
    }
  },
  created() {
    this.fetchShops();
  }
};
</script>

<style scoped>
.container {
  padding: 30px;
  max-width: 1400px;
  margin: auto;
}

.title {
  font-size: 28px;
  margin-bottom: 20px;
  font-weight: bold;
  color: #3f51b5;
}

.search-section {
  margin-bottom: 25px;
  padding: 20px;
  background: #f8fafc;
  border-radius: 12px;
}

.search-bar {
  display: flex;
  gap: 15px;
  align-items: center;
}

.search-bar input {
  flex: 1;
  padding: 12px 15px;
  border: 1px solid #cbd5e1;
  border-radius: 8px;
  font-size: 15px;
}

.search-bar select {
  padding: 12px 15px;
  border: 1px solid #cbd5e1;
  border-radius: 8px;
  font-size: 15px;
  min-width: 180px;
}

.btn-clear {
  background: #e0e0e0;
  color: #333;
  border: none;
  padding: 12px 20px;
  border-radius: 8px;
  cursor: pointer;
  transition: 0.3s;
}

.btn-clear:hover {
  background: #c7c7c7;
}

.top-bar {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 25px;
}

.add-btn {
  background: #673ab7;
  color: white;
  border: none;
  padding: 12px 24px;
  font-size: 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: 0.3s;
}

.add-btn:hover {
  background: #5e35b1;
  transform: scale(1.03);
}

.shop-table {
  width: 100%;
  border-collapse: collapse;
  background: #ffffff;
  box-shadow: 0 4px 14px rgba(0,0,0,0.1);
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 30px;
}

.shop-table th {
  background: #3f51b5;
  color: white;
  padding: 15px;
  font-size: 15px;
  text-align: left;
}

.shop-table td {
  padding: 15px;
  border-bottom: 1px solid #eee;
}

.shop-table tr:hover {
  background: #f5f5ff;
}

.bold {
  font-weight: bold;
  color: #303f9f;
}

.category-tag {
  display: inline-block;
  padding: 4px 10px;
  background: #e3f2fd;
  color: #1565c0;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.status {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
  display: inline-block;
}

.status.active {
  background: #a5d6a7;
  color: #1b5e20;
}

.status.inactive {
  background: #ef9a9a;
  color: #b71c1c;
}

.actions {
  display: flex;
  gap: 8px;
}

.actions button {
  border: none;
  padding: 8px 12px;
  border-radius: 6px;
  cursor: pointer;
  transition: 0.3s;
  font-size: 14px;
}

.edit-btn {
  background: #2196f3;
  color: white;
}

.edit-btn:hover {
  background: #1976d2;
}

.delete-btn {
  background: #e53935;
  color: white;
}

.delete-btn:hover {
  background: #c62828;
}

.no-results {
  text-align: center;
  padding: 40px;
  color: #666;
  font-style: italic;
}

.stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-top: 30px;
}

.stat-card {
  background: white;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 4px 10px rgba(0,0,0,0.1);
  text-align: center;
}

.stat-card h4 {
  margin: 0 0 10px 0;
  color: #666;
  font-size: 14px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #3f51b5;
  margin: 0;
}
</style>
