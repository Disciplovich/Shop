<template>
  <div class="form-container">
    <h2 class="title">➕ Добавить магазин</h2>

    <form @submit.prevent="submitForm" class="form-card">
      <div class="form-group">
        <label>Название магазина *</label>
        <input v-model="shop.name" required />
      </div>

      <div class="form-group">
        <label>Адрес</label>
        <input v-model="shop.address" />
      </div>

      <div class="form-row">
        <div class="form-group">
          <label>Телефон</label>
          <input v-model="shop.phone" placeholder="+7 XXX XXX-XX-XX" />
        </div>
        <div class="form-group">
          <label>Email</label>
          <input type="email" v-model="shop.email" />
        </div>
      </div>

      <div class="form-row">
        <div class="form-group">
          <label>Дата открытия</label>
          <input type="date" v-model="shop.openingDate" />
        </div>
        <div class="form-group">
          <label>Категория</label>
          <select v-model="shop.category">
            <option value="">Выберите категорию</option>
            <option value="продукты">Продукты</option>
            <option value="спорттовары">Спорттовары</option>
            <option value="электроника">Электроника</option>
            <option value="косметика">Косметика</option>
            <option value="одежда">Одежда</option>
            <option value="другое">Другое</option>
          </select>
        </div>
      </div>

      <div class="form-row">
        <div class="form-group">
          <label>Количество сотрудников</label>
          <input type="number" v-model.number="shop.employeeCount" min="0" />
        </div>
        <div class="form-group">
          <label>Площадь (м²)</label>
          <input type="number" v-model.number="shop.area" step="0.1" min="0" />
        </div>
      </div>

      <div class="form-group">
        <label>Выручка (₽)</label>
        <input type="number" v-model.number="shop.revenue" min="0" step="1000" />
      </div>

      <div class="form-group">
        <label>Статус</label>
        <div class="radio-group">
          <label>
            <input type="radio" v-model="shop.active" :value="true" />
            Активен
          </label>
          <label>
            <input type="radio" v-model="shop.active" :value="false" />
            Неактивен
          </label>
        </div>
      </div>

      <div class="form-actions">
        <button type="button" class="btn-secondary" @click="$router.push('/')">Отмена</button>
        <button type="submit" class="btn-primary">Сохранить</button>
      </div>
    </form>
  </div>
</template>

<script>
import { mapActions } from 'vuex';

export default {
  data() {
    return {
      shop: {
        name: '',
        address: '',
        phone: '',
        email: '',
        openingDate: new Date().toISOString().split('T')[0],
        active: true,
        employeeCount: 0,
        area: 0,
        category: '',
        revenue: 0
      }
    };
  },
  methods: {
    ...mapActions(['addShop']),
    async submitForm() {
      try {
        await this.addShop(this.shop);
        this.$router.push('/');
      } catch (error) {
        alert('Ошибка при создании магазина');
      }
    }
  }
};
</script>

<style scoped>
.form-container {
  max-width: 800px;
  margin: 30px auto;
  padding: 30px;
  background: #f8fafc;
  border-radius: 16px;
  box-shadow: 0 8px 30px rgba(0,0,0,0.1);
}

.title {
  font-size: 28px;
  font-weight: 600;
  margin-bottom: 30px;
  text-align: center;
  color: #1e293b;
}

.form-card {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

label {
  font-size: 15px;
  margin-bottom: 8px;
  color: #334155;
  font-weight: 500;
}

input, select {
  padding: 14px 16px;
  border-radius: 10px;
  border: 1px solid #cbd5e1;
  font-size: 15px;
  background: #ffffff;
  transition: all 0.2s;
}

input:focus, select:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59,130,246,0.2);
}

.radio-group {
  display: flex;
  gap: 20px;
  margin-top: 5px;
}

.radio-group label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: normal;
  cursor: pointer;
}

.radio-group input[type="radio"] {
  margin: 0;
  width: 18px;
  height: 18px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #e2e8f0;
}

.btn-primary, .btn-secondary {
  padding: 14px 28px;
  font-size: 16px;
  font-weight: 500;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-primary {
  background: #3b82f6;
  color: white;
}

.btn-primary:hover {
  background: #2563eb;
  transform: translateY(-2px);
}

.btn-secondary {
  background: #e2e8f0;
  color: #475569;
}

.btn-secondary:hover {
  background: #cbd5e1;
}

@media (max-width: 768px) {
  .form-container {
    margin: 15px;
    padding: 20px;
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .title {
    font-size: 24px;
  }

  .btn-primary, .btn-secondary {
    padding: 12px 20px;
    font-size: 15px;
  }
}
</style>
