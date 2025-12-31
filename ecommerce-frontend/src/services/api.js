import axios from 'axios';

// 🔗 Create Axios instance with base URL
const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// ✅ Request Interceptor - Automatically add JWT token to all requests
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
      console.log('✅ Token attached to request:', token.substring(0, 20) + '...');
    } else {
      console.warn('⚠️ No token found in localStorage!');
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// ✅ Response Interceptor - Handle errors globally
api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('API Error:', error.response?.status, error.response?.data);
    
    if (error.response?.status === 401 || error.response?.status === 403) {
      // Unauthorized/Forbidden - clear token and redirect to login
      console.error('❌ Authentication failed - clearing session');
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// ===== AUTHENTICATION APIs =====
export const authAPI = {
  register: (data) => api.post('/auth/register', data),
  login: (data) => api.post('/auth/login', data),
  getCurrentUser: () => api.get('/auth/me'),
};

// ===== PRODUCT APIs =====
export const productAPI = {
  getAll: () => api.get('/products'),
  getById: (id) => api.get(`/products/${id}`),
  create: (data) => api.post('/admin/products', data),
  update: (id, data) => api.put(`/admin/products/${id}`, data),
  delete: (id) => api.delete(`/admin/products/${id}`),
};

// ===== CART APIs =====
export const cartAPI = {
  getCart: () => api.get('/cart'),
  addToCart: (productId, qty) => api.post(`/cart?productId=${productId}&qty=${qty}`),
  removeItem: (id) => api.delete(`/cart/${id}`),
};

// ===== ORDER APIs =====
export const orderAPI = {
  checkout: () => api.post('/orders'),
  getMyOrders: () => api.get('/orders/my'),
};

export default api;