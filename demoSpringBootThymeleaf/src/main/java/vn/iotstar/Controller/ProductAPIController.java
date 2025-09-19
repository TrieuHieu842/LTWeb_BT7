package vn.iotstar.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.Entity.CategoryEntity;
import vn.iotstar.Entity.Product;
import vn.iotstar.Repository.CategoryRepository;
import vn.iotstar.Repository.ProductRepository;

@RestController
@RequestMapping("/api/products")
public class ProductAPIController {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @GetMapping
    public List<Product> getAll() {
        return productRepo.findAll();
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Map<String, Object> req) {
        Product p = new Product();
        p.setProductName((String) req.get("name"));
        p.setUnitPrice(Double.valueOf(req.get("price").toString()));

        Integer categoryId = Integer.valueOf(req.get("categoryId").toString());
        CategoryEntity c = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        p.setCategory(c);

        return ResponseEntity.ok(productRepo.save(p));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String, Object> req) {
        return productRepo.findById(id).map(p -> {
            p.setProductName((String) req.get("name"));
            p.setUnitPrice(Double.valueOf(req.get("price").toString()));

            Integer categoryId = Integer.valueOf(req.get("categoryId").toString());
            CategoryEntity c = categoryRepo.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            p.setCategory(c);

            return ResponseEntity.ok(productRepo.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return productRepo.findById(id).map(p -> {
            productRepo.delete(p);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
