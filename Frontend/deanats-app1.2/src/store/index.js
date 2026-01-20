import { createStore } from 'vuex';
import shopService from '../services/shopService';

export default createStore({
  state: {
    shops: [],
    selectedShop: null
  },
  mutations: {
    setShops(state, shops) {
      state.shops = shops;
    },
    setSelectedShop(state, shop) {
      state.selectedShop = shop;
    },
    addShop(state, shop) {
      state.shops.push(shop);
    },
    updateShop(state, updatedShop) {
      const index = state.shops.findIndex(s => s.id === updatedShop.id);
      if (index !== -1) state.shops.splice(index, 1, updatedShop);
    },
    removeShop(state, id) {
      state.shops = state.shops.filter(s => s.id !== id);
    }
  },
  actions: {
    async fetchShops({ commit }) {
      try {
        const response = await shopService.getAllShops();
        commit('setShops', response.data);
      } catch (error) {
        console.error('Ошибка при загрузке магазинов:', error);
      }
    },
    async fetchShop({ commit }, id) {
      try {
        const response = await shopService.getShopById(id);
        commit('setSelectedShop', response.data);
      } catch (error) {
        console.error('Ошибка при загрузке магазина:', error);
      }
    },
    async addShop({ commit }, shop) {
      try {
        const response = await shopService.createShop(shop);
        commit('addShop', response.data);
        return response.data;
      } catch (error) {
        console.error('Ошибка при создании магазина:', error);
        throw error;
      }
    },
    async updateShop({ commit }, { id, shop }) {
      try {
        const response = await shopService.updateShop(id, shop);
        commit('updateShop', response.data);
        return response.data;
      } catch (error) {
        console.error('Ошибка при обновлении магазина:', error);
        throw error;
      }
    },
    async deleteShop({ commit }, id) {
      try {
        await shopService.deleteShop(id);
        commit('removeShop', id);
      } catch (error) {
        console.error('Ошибка при удалении магазина:', error);
        throw error;
      }
    }
  },
  getters: {
    allShops: state => state.shops,
    selectedShop: state => state.selectedShop
  }
});
