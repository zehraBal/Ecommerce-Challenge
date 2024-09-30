// src/components/Login.jsx
import React, { useState } from "react";
import { loginUser } from "../api/api.js"; // loginUser fonksiyonunu içe aktar

const Login = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await loginUser(username, password); // Giriş isteği
      console.log("Login successful!");
      // Giriş başarılı olduğunda yönlendirme yapabilirsiniz
    } catch (error) {
      console.error("Login failed:", error);
      setError("Geçersiz kullanıcı adı veya şifre."); // Hata mesajı ayarlama
    }
  };

  return (
    <div>
      <h2>Login</h2>
      <form onSubmit={handleLogin}>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button type="submit">Login</button>
        {error && <p style={{ color: "red" }}>{error}</p>} {/* Hata mesajı */}
      </form>
    </div>
  );
};

export default Login;
