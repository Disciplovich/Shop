import axios from 'axios';

const API_URL = '/api/shops';

export default {
  getAllShops() {
    return axios.get(API_URL);
  },
  getShopById(id) {
    return axios.get(`${API_URL}/${id}`);
  },
  createShop(shop) {
    return axios.post(API_URL, shop);
  },
  updateShop(id, shop) {
    return axios.put(`${API_URL}/${id}`, shop);
  },
  deleteShop(id) {
    return axios.delete(`${API_URL}/${id}`);
  },
  searchShopsByName(name) {
    return axios.get(`${API_URL}/search/by-name?name=${name}`);
  },
  searchShopsByCategory(category) {
    return axios.get(`${API_URL}/search/by-category?category=${category}`);
  }
};
