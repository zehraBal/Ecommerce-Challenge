import React, { useState } from "react";
import { addUser } from "../api/api";

const AddUserForm = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    const newUser = { username, password };
    const addedUser = await addUser(newUser);
    if (addedUser) {
      alert("Kullanıcı başarıyla eklendi!");
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Kullanıcı Ekle</h2>
      <div>
        <label>Kullanıcı Adı:</label>
        <input
          type="text"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
      </div>
      <div>
        <label>Şifre:</label>
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
      </div>
      <button type="submit">Ekle</button>
    </form>
  );
};

export default AddUserForm;
