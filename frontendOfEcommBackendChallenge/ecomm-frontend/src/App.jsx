// src/App.jsx
import React from "react";
import Register from "./components/Register";
import Login from "./components/Login";
import UserList from "./components/UserList";
import "bootstrap/dist/css/bootstrap.min.css";
import Products from "./components/Products";

const App = () => {
  return (
    <div>
      <h1>React Auth App</h1>
      <Register />
      <Login />
      <UserList />
      <Products />
    </div>
  );
};

export default App;
