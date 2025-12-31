import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { cartAPI, orderAPI } from '../services/api';
import './Cart.css';

const Cart = () => {
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [checkoutLoading, setCheckoutLoading] = useState(false);
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    loadCart();
  }, []);

  const loadCart = async () => {
    try {
      const response = await cartAPI.getCart();
      setCartItems(response.data);
    } catch (error) {
      console.error('Error loading cart:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleRemoveItem = async (itemId) => {
    try {
      await cartAPI.removeItem(itemId);
      setCartItems(cartItems.filter(item => item.id !== itemId));
      setMessage('✅ Item removed from cart');
      setTimeout(() => setMessage(''), 3000);
    } catch (error) {
      setMessage('❌ Failed to remove item');
      setTimeout(() => setMessage(''), 3000);
    }
  };

  const handleCheckout = async () => {
    if (cartItems.length === 0) {
      setMessage('❌ Cart is empty');
      return;
    }

    setCheckoutLoading(true);
    try {
      await orderAPI.checkout();
      setMessage('✅ Order placed successfully!');
      setTimeout(() => {
        navigate('/orders');
      }, 2000);
    } catch (error) {
      setMessage('❌ Checkout failed: ' + (error.response?.data?.message || 'Unknown error'));
      setCheckoutLoading(false);
    }
  };

  const calculateTotal = () => {
    return cartItems.reduce((total, item) => {
      return total + (item.product?.price || 0) * item.quantity;
    }, 0);
  };

  if (loading) {
    return <div className="loading">Loading cart...</div>;
  }

  return (
    <div className="cart-container">
      <h1>Shopping Cart</h1>
      
      {message && <div className="message-banner">{message}</div>}
      
      {cartItems.length === 0 ? (
        <div className="empty-cart">
          <p>Your cart is empty</p>
          <button onClick={() => navigate('/products')} className="continue-shopping-btn">
            Continue Shopping
          </button>
        </div>
      ) : (
        <div className="cart-content">
          <div className="cart-items">
            {cartItems.map((item) => (
              <div key={item.id} className="cart-item">
                <img 
                  src={item.product?.imageUrl || 'https://via.placeholder.com/100'} 
                  alt={item.product?.title} 
                />
                
                <div className="cart-item-details">
                  <h3>{item.product?.title}</h3>
                  <p className="cart-item-price">₹{item.product?.price?.toLocaleString()}</p>
                  <p className="cart-item-quantity">Quantity: {item.quantity}</p>
                </div>
                
                <div className="cart-item-actions">
                  <p className="cart-item-total">
                    ₹{(item.product?.price * item.quantity).toLocaleString()}
                  </p>
                  <button 
                    onClick={() => handleRemoveItem(item.id)}
                    className="remove-btn"
                  >
                    Remove
                  </button>
                </div>
              </div>
            ))}
          </div>
          
          <div className="cart-summary">
            <h2>Order Summary</h2>
            <div className="summary-row">
              <span>Subtotal:</span>
              <span>₹{calculateTotal().toLocaleString()}</span>
            </div>
            <div className="summary-row">
              <span>Shipping:</span>
              <span>Free</span>
            </div>
            <div className="summary-row total">
              <span>Total:</span>
              <span>₹{calculateTotal().toLocaleString()}</span>
            </div>
            
            <button 
              onClick={handleCheckout}
              className="checkout-btn"
              disabled={checkoutLoading}
            >
              {checkoutLoading ? 'Processing...' : 'Proceed to Checkout'}
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default Cart;