// src/components/ProductCards.jsx
import React, { useEffect, useState } from "react";
import { Card, Button, Row, Col } from "react-bootstrap";
import { getProducts } from "../api/api"; // API dosyasını içe aktar

const Products = () => {
  const [products, setProducts] = useState([]); // Ürünleri saklamak için state

  useEffect(() => {
    const fetchProducts = async () => {
      const productData = await getProducts(); // Ürün verilerini çek
      setProducts(productData); // State'i güncelle
    };

    fetchProducts(); // Fetch işlemini çağır
  }, []); // Boş bağımlılık dizisi ile yalnızca bileşen yüklendiğinde çağırılır

  return (
    <div className="container mt-4">
      <Row>
        {products.map((product, index) => (
          <Col md={4} key={index} className="mb-4">
            <Card>
              <Card.Body>
                <Card.Title>{product.name}</Card.Title>
                <Card.Text>
                  <strong>Price:</strong> ${product.price.toFixed(2)} <br />
                  <strong>Stock:</strong> {product.stockQuantity}
                </Card.Text>
                <Button variant="primary">Add to Cart</Button>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>
    </div>
  );
};

export default Products;
