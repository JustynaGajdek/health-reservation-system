import api from './api';

export const login = credentials =>
  api.post('/auth/login', credentials);

export const logout = () => {
  localStorage.removeItem('token');
};

export const fetchProfile = () =>
  api.get('/auth/me');
