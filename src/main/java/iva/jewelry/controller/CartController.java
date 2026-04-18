package iva.jewelry.controller;

import iva.jewelry.dto.AddToCartRequest;
import iva.jewelry.dto.CartSummary;
import iva.jewelry.model.CartProduct;
import iva.jewelry.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/cart")
@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/")
    public List<CartProduct> getCart() { return cartService.getCart(); }


    @PostMapping("/add")
    public CartProduct addToCart(@RequestBody AddToCartRequest req) throws Exception {
        return cartService.addToCart(req.getSnapshot(), req.getQuantity());
    }

    @DeleteMapping("/{itemId}")
    public void remove(@PathVariable Long itemId) { cartService.remove(itemId); }

    @PutMapping("/{id}")
    public CartProduct updateCart(@PathVariable Long id,
                                  @RequestParam Integer amount) {
        return cartService.updateAmount(id, amount);
    }
    @GetMapping("/summary")
    public CartSummary getCartSummary() {
        return cartService.getCartSummary();
    }
}
