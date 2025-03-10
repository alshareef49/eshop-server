package com.eshop.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.eshop.dto.CartProductDTO;
import com.eshop.dto.CustomerCartDTO;
import com.eshop.entity.CartProduct;
import com.eshop.entity.CustomerCart;
import com.eshop.exception.EShopException;
import com.eshop.repository.CartProductRepository;
import com.eshop.repository.CustomerCartRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class CustomerCartServiceImpl implements CustomerCartService {

    @Autowired
    private CustomerCartRepository customerCartRepository;
    @Autowired
    private CartProductRepository cartProductRepository;

    @Override
    public Integer addProductToCart(CustomerCartDTO customerCartDTO) throws EShopException {
        Set<CartProduct> cartProducts = new HashSet<>();
        Integer cartId;
        for (CartProductDTO cartProductDTO : customerCartDTO.getCartProducts()) {
            CartProduct cartProduct = new CartProduct();
            cartProduct.setProductId(cartProductDTO.getProduct().getProductId());
            cartProduct.setQuantity(cartProductDTO.getQuantity());
            cartProducts.add(cartProduct);
        }
        Optional<CustomerCart> cartOptional = customerCartRepository
                .findByCustomerEmailId(customerCartDTO.getCustomerEmailId());
        if (cartOptional.isEmpty()) {
            CustomerCart newCart = new CustomerCart();
            newCart.setCustomerEmailId(customerCartDTO.getCustomerEmailId());
            newCart.setCartProducts(cartProducts);
            customerCartRepository.save(newCart);
            cartId = newCart.getCartId();
        } else {
            CustomerCart cart = cartOptional.get();
            for (CartProduct cartProductToBeAdded : cartProducts) {
                boolean found = false;
                for (CartProduct cartProductFromCart : cart.getCartProducts()) {
                    if (cartProductFromCart.equals(cartProductToBeAdded)) {

                        cartProductFromCart
                                .setQuantity(cartProductToBeAdded.getQuantity() + cartProductFromCart.getQuantity());
                        found = true;
                    }
                }
                if (!found) {
                    cart.getCartProducts().add(cartProductToBeAdded);
                }
            }

            cartId = cart.getCartId();
        }
        return cartId;
    }


    //Get the customer cart details by using customerEmailId
    //If no cart found then throw an EKartException with message "CustomerCartService.NO_CART_FOUND"
    //Else if cart is empty then throw an EKartException with message "CustomerCartService.NO_PRODUCT_ADDED_TO_CART"
    //Otherwise return the set of cart products

    @Override
    public Set<CartProductDTO> getProductsFromCart(String customerEmailId) throws EShopException {

        // write your logic here
        return null;

    }


    //Get the customer cart details by using customerEmailId
    //If no cart found then throw an EKartException with message "CustomerCartService.NO_CART_FOUND"
    //Else if cart is empty then throw an EKartException with message "CustomerCartService.NO_PRODUCT_ADDED_TO_CART"
    //Otherwise delete the given product from the customer cart

    @Override
    public void deleteProductFromCart(String customerEmailId, Integer productId) throws EShopException {

        // write your logic here

    }

    @Override
    public void deleteAllProductsFromCart(String customerEmailId) throws EShopException {
        Optional<CustomerCart> cartOptional = customerCartRepository.findByCustomerEmailId(customerEmailId);
        CustomerCart cart = cartOptional.orElseThrow(() -> new EShopException("CustomerCartService.NO_CART_FOUND"));

        if (cart.getCartProducts().isEmpty()) {
            throw new EShopException("CustomerCartService.NO_PRODUCT_ADDED_TO_CART");
        }
        List<Integer> productIds = new ArrayList<>();
        cart.getCartProducts().parallelStream().forEach(cp -> {
            productIds.add(cp.getCartProductId());
            cart.getCartProducts().remove(cp);
        });

        productIds.forEach(pid -> {
            cartProductRepository.deleteById(pid);
        });

    }

    @Override
    public void modifyQuantityOfProductInCart(String customerEmailId, Integer productId, Integer quantity)
            throws EShopException {

        Optional<CustomerCart> cartOptional = customerCartRepository.findByCustomerEmailId(customerEmailId);
        CustomerCart cart = cartOptional.orElseThrow(() -> new EShopException("CustomerCartService.NO_CART_FOUND"));

        if (cart.getCartProducts().isEmpty()) {
            throw new EShopException("CustomerCartService.NO_PRODUCT_ADDED_TO_CART");
        }
        CartProduct selectedProduct = null;
        for (CartProduct product : cart.getCartProducts()) {
            if (product.getProductId().equals(productId)) {
                selectedProduct = product;
            }
        }
        if (selectedProduct == null) {
            throw new EShopException("CustomerCartService.PRODUCT_ALREADY_NOT_AVAILABLE");
        }
        selectedProduct.setQuantity(quantity);
    }

}