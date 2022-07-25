package com.mobiroller.core.interfaces.ecommerce;

public interface ShoppingCartListener {

    void onClickRemoveItem(String shoppingCartItemId);

    void onClickUpdateQuantity(String shoppingCartItemId, int quantity);
}
