import axios from "axios";

const API_URL = "http://localhost:9000/ecomm";

export const getUsers = async () => {
  try {
    const response = await axios.get(`${API_URL}/user`);
    return response.data;
  } catch (error) {
    console.error("Error fetching users:", error);
  }
};

export const addUser = async (user) => {
  try {
    const response = await axios.post(`${API_URL}/auth/register`, user);
    return response.data;
  } catch (error) {
    console.error("Error adding user:", error);
  }
};

export const loginUser = async (username, password) => {
  try {
    const response = await axios.post(`${API_URL}/user/login`, {
      username,
      password,
    });
    return response.data; // Yanıt verilerini döndür
  } catch (error) {
    console.error("Error logging in:", error);
    throw error; // Hata durumunda hatayı fırlat
  }
};
export const getProducts = async () => {
  try {
    const response = await axios.get(`${API_URL}/product`); // Ürünleri çekmek için endpoint
    return response.data; // Ürün verilerini döndür
  } catch (error) {
    console.error("Error fetching products:", error);
    return []; // Hata durumunda boş bir dizi döndür
  }
};
