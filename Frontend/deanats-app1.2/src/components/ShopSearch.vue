<template>
  <div class="search-container">
    <h3 class="search-title">🔍 Поиск магазинов</h3>

    <div class="search-bar">
      <input
        type="text"
        v-model="searchName"
        placeholder="Поиск по названию..."
      />
      <select v-model="selectedCategory">
        <option value="">Все категории</option>
        <option value="продукты">Продукты</option>
        <option value="спорттовары">Спорттовары</option>
        <option value="электроника">Электроника</option>
        <option value="косметика">Косметика</option>
        <option value="одежда">Одежда</option>
        <option value="другое">Другое</option>
      </select>
      <button class="btn-search" @click="searchShops">Найти</button>
      <button class="btn-clear" @click="clearSearch">Сброс</button>
    </div>

    <!-- Статус поиска -->
    <div v-if="searching" class="search-status">
      ⌛ Поиск...
    </div>

    <!-- Результаты поиска -->
    <div v-if="showResults" class="results-container">
      <div class="results-header">
        <h4>Найдено магазинов: {{ searchResults.length }}</h4>
        <button
          v-if="searchResults.length > 0"
          class="btn-export"
          @click="exportResults"
        >
          📊 Экспорт
        </button>
      </div>

      <table class="shop-table" v-if="searchResults.length > 0">
        <thead>
        <tr>
          <th>Название</th>
          <th>Адрес</th>
          <th>Категория</th>
          <th>Телефон</th>
          <th>Выручка</th>
          <th>Статус</th>
          <th>Действия</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="shop in searchResults" :key="shop.id">
          <td class="bold">{{ shop.name }}</td>
          <td>{{ shop.address }}</td>
          <td>
            <span class="category-tag">{{ shop.category }}</span>
          </td>
          <td>{{ shop.phone }}</td>
          <td>{{ formatCurrency(shop.revenue) }}</td>
          <td>
              <span :class="['status', shop.active ? 'active' : 'inactive']">
                {{ shop.active ? 'Активен' : 'Неактивен' }}
              </span>
          </td>
          <td class="actions">
            <button class="view-btn" @click="viewShop(shop.id)">👁</button>
            <button class="edit-btn" @click="editShop(shop.id)">✏</button>
          </td>
        </tr>
        </tbody>
      </table>

      <p v-else class="no-results">
        По вашему запросу ничего не найдено.
      </p>
    </div>

    <!-- Быстрая статистика -->
    <div class="quick-stats" v-if="!hasInput">
      <div class="stat-item">
        <span class="stat-label">Всего магазинов:</span>
        <span class="stat-value">{{ shops.length }}</span>
      </div>
      <div class="stat-item">
        <span class="stat-label">Активных:</span>
        <span class="stat-value">{{ activeCount }}</span>
      </div>
      <div class="stat-item">
        <span class="stat-label">По категориям:</span>
        <span class="stat-value">{{ categoryCount }}</span>
      </div>
    </div>
  </div>
</template>

<script>
import shopService from '../services/shopService';

export default {
  props: {
    shops: {
      type: Array,
      required: true,
      default: () => []
    }
  },
  data() {
    return {
      searchName: '',
      selectedCategory: '',
      searchResults: [],
      searching: false,
      searchPerformed: false
    };
  },
  computed: {
    hasInput() {
      return this.searchName.trim() !== '' || this.selectedCategory !== '';
    },
    showResults() {
      return this.searchPerformed;
    },
    activeCount() {
      return this.shops.filter(shop => shop.active).length;
    },
    categoryCount() {
      const categories = new Set(this.shops.map(shop => shop.category));
      return categories.size;
    }
  },
  methods: {
    async searchShops() {
      this.searching = true;
      this.searchPerformed = true;

      try {
        if (this.searchName.trim()) {
          // Поиск по названию
          const response = await shopService.searchShopsByName(this.searchName);
          let results = response.data;

          // Дополнительная фильтрация по категории
          if (this.selectedCategory) {
            results = results.filter(shop =>
              shop.category === this.selectedCategory
            );
          }

          this.searchResults = results;
        } else if (this.selectedCategory) {
          // Поиск только по категории
          const response = await shopService.searchShopsByCategory(this.selectedCategory);
          this.searchResults = response.data;
        }
      } catch (error) {
        console.error('Ошибка поиска:', error);
        this.searchResults = [];
      } finally {
        this.searching = false;
      }
    },

    clearSearch() {
      this.searchName = '';
      this.selectedCategory = '';
      this.searchResults = [];
      this.searchPerformed = false;
      this.$emit('clear-search');
    },

    formatCurrency(value) {
      if (!value) return '0 ₽';
      return new Intl.NumberFormat('ru-RU', {
        style: 'currency',
        currency: 'RUB',
        minimumFractionDigits: 0
      }).format(value);
    },

    viewShop(id) {
      this.$emit('view-shop', id);
    },

    editShop(id) {
      this.$router.push(`/shops/${id}`);
    },

    exportResults() {
      const data = this.searchResults.map(shop => ({
        'Название': shop.name,
        'Адрес': shop.address,
        'Категория': shop.category,
        'Телефон': shop.phone,
        'Выручка': this.formatCurrency(shop.revenue),
        'Статус': shop.active ? 'Активен' : 'Неактивен',
        'Сотрудники': shop.employeeCount,
        'Площадь': `${shop.area} м²`
      }));

      const csvContent = [
        Object.keys(data[0]).join(','),
        ...data.map(row => Object.values(row).join(','))
      ].join('\n');

      const blob = new Blob([csvContent], { type: 'text/csv' });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `магазины_${new Date().toISOString().split('T')[0]}.csv`;
      a.click();
      window.URL.revokeObjectURL(url);
    }
  },

  watch: {
    shops: {
      immediate: true,
      handler(newShops) {
        if (!this.searchPerformed) {
          this.searchResults = newShops;
        }
      }
    }
  }
};
</script>

<style scoped>
.search-container {
  max-width: 1000px;
  margin: 30px auto;
  padding: 25px;
  background: #f8fafc;
  border-radius: 16px;
  box-shadow: 0 6px 20px rgba(0,0,0,0.08);
}

.search-title {
  font-size: 24px;
  margin-bottom: 20px;
  color: #1e293b;
  font-weight: 600;
}

.search-bar {
  display: grid;
  grid-template-columns: 1fr 1fr auto auto;
  gap: 15px;
  margin-bottom: 25px;
  align-items: center;
}

.search-bar input,
.search-bar select {
  padding: 12px 16px;
  font-size: 15px;
  border: 1px solid #cbd5e1;
  border-radius: 10px;
  background: white;
  transition: border-color 0.3s;
}

.search-bar input:focus,
.search-bar select:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59,130,246,0.1);
}

.btn-search {
  background: #3b82f6;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 10px;
  cursor: pointer;
  font-weight: 500;
  transition: background 0.3s;
}

.btn-search:hover {
  background: #2563eb;
}

.btn-clear {
  background: #e0e0e0;
  color: #333;
  border: none;
  padding: 12px 24px;
  border-radius: 10px;
  cursor: pointer;
  font-weight: 500;
  transition: background 0.3s;
}

.btn-clear:hover {
  background: #c7c7c7;
}

.search-status {
  text-align: center;
  padding: 20px;
  color: #666;
  font-style: italic;
}

.results-container {
  margin-top: 20px;
}

.results-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.results-header h4 {
  margin: 0;
  color: #334155;
  font-weight: 500;
}

.btn-export {
  background: #10b981;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: background 0.3s;
}

.btn-export:hover {
  background: #059669;
}

.shop-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 14px rgba(0,0,0,0.1);
}

.shop-table th {
  background: #3f51b5;
  color: white;
  padding: 14px;
  font-size: 14px;
  text-align: left;
  font-weight: 500;
}

.shop-table td {
  padding: 14px;
  border-bottom: 1px solid #eee;
}

.shop-table tr:hover {
  background: #f5f5ff;
}

.bold {
  font-weight: 600;
  color: #1e293b;
}

.category-tag {
  display: inline-block;
  padding: 4px 12px;
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

.view-btn {
  background: #60a5fa;
  color: white;
}

.view-btn:hover {
  background: #3b82f6;
}

.edit-btn {
  background: #f59e0b;
  color: white;
}

.edit-btn:hover {
  background: #d97706;
}

.no-results {
  text-align: center;
  padding: 40px;
  color: #666;
  font-style: italic;
  background: white;
  border-radius: 12px;
  margin: 0;
}

.quick-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
  margin-top: 25px;
  padding-top: 20px;
  border-top: 1px solid #e2e8f0;
}

.stat-item {
  background: white;
  padding: 15px;
  border-radius: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.stat-label {
  color: #64748b;
  font-size: 14px;
}

.stat-value {
  font-size: 18px;
  font-weight: 600;
  color: #3f51b5;
}

@media (max-width: 768px) {
  .search-bar {
    grid-template-columns: 1fr;
  }

  .shop-table {
    display: block;
    overflow-x: auto;
  }

  .quick-stats {
    grid-template-columns: 1fr;
  }
}
</style>
