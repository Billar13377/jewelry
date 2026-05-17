package iva.jewelry.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import iva.jewelry.dto.CartSummary;
import iva.jewelry.dto.ProductSnapshot;
import iva.jewelry.model.CartProduct;
import iva.jewelry.model.User;
import iva.jewelry.repository.CartProductRepository;
import iva.jewelry.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartProductRepository cartRepository;
    private final UserRepository userRepository;
    private final ObjectMapper mapper;
    private final SnapshotPriceSyncService snapshotPriceSyncService;

    @Transactional
    public CartProduct addToCart(ProductSnapshot snapshot, int amount) throws Exception {
        User user = getAuthenticatedUser();

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
        User user = getAuthenticatedUser();

        List<CartProduct> cart = cartRepository.findByUser(user);
        for (CartProduct item : cart) {
            ProductSnapshot updated = snapshotPriceSyncService.refreshPrice(item.getProductSnapshot());
            item.setProductSnapshot(updated);
        }
        return cartRepository.saveAll(cart);
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
    private User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return userRepository.findByEmail(((UserDetails) principal).getUsername())
                    .orElseThrow(() -> new IllegalStateException("Пользователь не найден"));
        } else {
            throw new IllegalStateException("Ошибка аутентификации");
        }
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