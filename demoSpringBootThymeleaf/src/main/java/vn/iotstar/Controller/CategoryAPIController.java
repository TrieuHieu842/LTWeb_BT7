package vn.iotstar.Controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.iotstar.Entity.CategoryEntity;
import vn.iotstar.Model.Response;
import vn.iotstar.Service.CategoryService;
import vn.iotstar.Service.StorageService;

@RestController
@RequestMapping("/api/category")
public class CategoryAPIController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StorageService storageService;

    // Lấy tất cả category
    @GetMapping
    public ResponseEntity<?> getAllCategory() {
        return new ResponseEntity<>(
                new Response(true, "Thành công", categoryService.findAll()),
                HttpStatus.OK);
    }

    // Lấy category theo id
    @PostMapping(path = "/getCategory")
    public ResponseEntity<?> getCategory(@Validated @RequestParam("id") Integer id) {
        Optional<CategoryEntity> category = categoryService.findById(id);
        if (category.isPresent()) {
            return new ResponseEntity<>(
                    new Response(true, "Thành công", category.get()),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new Response(false, "Thất bại", null),
                    HttpStatus.NOT_FOUND);
        }
    }

    // Thêm category
    @PostMapping(path = "/addCategory")
    public ResponseEntity<?> addCategory(
            @Validated @RequestParam("categoryName") String categoryName,
            @Validated @RequestParam("icon") MultipartFile icon) {

        Optional<CategoryEntity> optCategory = categoryService.findByName(categoryName);

        if (optCategory.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new Response(false, "Loại sản phẩm này đã tồn tại trong hệ thống", null));
        } else {
            CategoryEntity category = new CategoryEntity();

            // kiểm tra tồn tại file, lưu file
            if (!icon.isEmpty()) {
                UUID uuid = UUID.randomUUID();
                String uuString = uuid.toString();
                // lưu file vào trường icon
                category.setIcon(storageService.getSorageFilename(icon, uuString));
                storageService.store(icon, category.getIcon());
            }

            category.setName(categoryName);
            categoryService.save(category);

            return new ResponseEntity<>(
                    new Response(true, "Thêm thành công", category),
                    HttpStatus.OK);
        }
    }

    // Cập nhật category
    @PutMapping(path = "/updateCategory")
    public ResponseEntity<?> updateCategory(
            @Validated @RequestParam("categoryId") Integer categoryId,
            @Validated @RequestParam("categoryName") String categoryName,
            @Validated @RequestParam("icon") MultipartFile icon) {

        Optional<CategoryEntity> optCategory = categoryService.findById(categoryId);

        if (optCategory.isEmpty()) {
            return new ResponseEntity<>(
                    new Response(false, "Không tìm thấy Category", null),
                    HttpStatus.BAD_REQUEST);
        } else {
            CategoryEntity category = optCategory.get();

            // kiểm tra tồn tại file, lưu file
            if (!icon.isEmpty()) {
                UUID uuid = UUID.randomUUID();
                String uuString = uuid.toString();
                category.setIcon(storageService.getSorageFilename(icon, uuString));
                storageService.store(icon, category.getIcon());
            }

            category.setName(categoryName);
            categoryService.save(category);

            return new ResponseEntity<>(
                    new Response(true, "Cập nhật thành công", category),
                    HttpStatus.OK);
        }
    }

    // Xóa category
    @DeleteMapping(path = "/deleteCategory")
    public ResponseEntity<?> deleteCategory(@Validated @RequestParam("categoryId") Integer categoryId) {
        Optional<CategoryEntity> optCategory = categoryService.findById(categoryId);

        if (optCategory.isEmpty()) {
            return new ResponseEntity<>(
                    new Response(false, "Không tìm thấy Category", null),
                    HttpStatus.BAD_REQUEST);
        } else {
            categoryService.delete(optCategory.get());
            return new ResponseEntity<>(
                    new Response(true, "Xóa thành công", optCategory.get()),
                    HttpStatus.OK);
        }
    }
}
