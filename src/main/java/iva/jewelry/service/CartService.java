package iva.jewelry.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import iva.jewelry.dto.CartSummary;
import iva.jewelry.dto.ProductSnapshot;
import iva.jewelry.model.CartProduct;
import iva.jewelry.model.User;
import iva.jewelry.repository.jpa.CartProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartProductRepository cartRepository;
    private final ObjectMapper mapper;
    private final SnapshotPriceSyncService snapshotPriceSyncService;
    private final AuthContextService authContextService;

    @Transactional
    public CartProduct addToCart(ProductSnapshot snapshot, int amount) throws Exception {
        User user = authContextService.getCurrentUser();
        String hash = hash(snapshot);

        Optional<CartProduct> existing =
                cartRepository.findByUserAndSnapshotHash(user, hash);

        if (existing.isPresent()) {
            CartProduct item = existing.get();
            item.setAmount(item.getAmount() + amount);
            return item;
        }

        CartProduct item = new CartProduct();
        item.setUser(user);
        item.setProductSnapshot(snapshot);
        item.setSnapshotHash(hash);
        item.setAmount(amount);

        return cartRepository.save(item);
    }
    public CartProduct updateAmount(Long cartProductId, Integer amount) {
        CartProduct item = cartRepository.findById(cartProductId)
                .orElseThrow(() -> new IllegalStateException("Товар не найден в корзине"));

        if (amount <= 0) {
            cartRepository.delete(item);
            return null;
        }

        item.setAmount(amount);
        return cartRepository.save(item);
    }

    public List<CartProduct> getCart() {
        User user = authContextService.getCurrentUser();
        List<CartProduct> cart = cartRepository.findByUser(user);
        for (CartProduct item : cart) {
            ProductSnapshot updated = snapshotPriceSyncService.refreshPrice(item.getProductSnapshot());
            item.setProductSnapshot(updated);
        }
        List<CartProduct> updated = cart.stream()
                .filter(item -> {
                    ProductSnapshot refreshed = snapshotPriceSyncService.refreshPrice(item.getProductSnapshot());
                    if (!refreshed.getPrice().equals(item.getProductSnapshot().getPrice())) {
                        item.setProductSnapshot(refreshed);
                        return true;
                    }
                    return false;
                })
                .toList();
        cartRepository.saveAll(updated);
        return cart;
    }
    public CartSummary getCartSummary() {
        List<CartProduct> items = getCart();
        BigDecimal total = items.stream()
                .map(CartProduct::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CartSummary(items, total);
    }

    public void remove(Long id) {
        cartRepository.deleteById(id);
    }

    private String hash(ProductSnapshot snapshot) {
        try {
            ProductSnapshot normalized = snapshot.normalize();

            String json = mapper.writeValueAsString(normalized);

            return org.springframework.util.DigestUtils.md5DigestAsHex(json.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}