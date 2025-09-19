package vn.iotstar.Controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import vn.iotstar.Entity.CategoryEntity;
import vn.iotstar.Model.CategoryModel;
import vn.iotstar.Service.CategoryService;

@Controller
@RequestMapping("admin/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    // ====== Add Category ======
    @GetMapping("add")
    public String add(ModelMap model) {
        CategoryModel cateModel = new CategoryModel();
        cateModel.setIsEdit(false);
        model.addAttribute("category", cateModel);
        return "admin/categories/addOrEdit";
    }

    // ====== List without pagination ======
    @GetMapping("list")
    public String list(ModelMap model) {
        model.addAttribute("categories", categoryService.findAll());
        return "admin/categories/list";
    }

    // ====== Save or Update ======
    @PostMapping("saveOrUpdate")
    public ModelAndView saveOrUpdate(ModelMap model,
                                     @Valid @ModelAttribute("category") CategoryModel cateModel,
                                     BindingResult result) {

        if (result.hasErrors()) {
            return new ModelAndView("admin/categories/addOrEdit");
        }

        CategoryEntity entity = new CategoryEntity();
        BeanUtils.copyProperties(cateModel, entity);
        categoryService.save(entity);

        String message = cateModel.getIsEdit() ? "Category is edited!" : "Category is saved!";
        model.addAttribute("message", message);

        return new ModelAndView("redirect:/admin/categories/list");

    }

    // ====== Edit ======
    @GetMapping("edit/{categoryId}")
    public ModelAndView edit(ModelMap model, @PathVariable("categoryId") Integer categoryId) {
        Optional<CategoryEntity> optCategory = categoryService.findById(categoryId);

        if (optCategory.isPresent()) {
            CategoryModel cateModel = new CategoryModel();
            BeanUtils.copyProperties(optCategory.get(), cateModel);
            cateModel.setIsEdit(true);

            model.addAttribute("category", cateModel);
            return new ModelAndView("admin/categories/addOrEdit", model);
        }

        model.addAttribute("message", "Category does not exist!");
        return new ModelAndView("forward:/admin/categories/list", model);
    }

    // ====== Delete ======
    @GetMapping("delete/{categoryId}")
    public ModelAndView delete(ModelMap model, @PathVariable("categoryId") Integer categoryId) {
        categoryService.deleteById(categoryId);
        model.addAttribute("message", "Category is deleted!");
        return new ModelAndView("forward:/admin/categories/list", model);
    }

    // ====== Search (no pagination) ======
    @GetMapping("search")
    public String search(ModelMap model, @RequestParam(name = "name", required = false) String name) {
        List<CategoryEntity> list;
        if (StringUtils.hasText(name)) {
            list = categoryService.findByNameContaining(name);
        } else {
            list = categoryService.findAll();
        }
        model.addAttribute("categories", list);
        return "admin/categories/search";
    }

    // ====== Search with pagination ======
    @RequestMapping("searchpaginated")
    public String searchPaginated(ModelMap model,
        @RequestParam(name = "name", required = false, defaultValue = "") String name,
        @RequestParam("page") Optional<Integer> page,
        @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(0);
        int pageSize = size.orElse(5);

        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by("name"));
        Page<CategoryEntity> resultPage;

        if (StringUtils.hasText(name)) {
            // có search
            resultPage = categoryService.findByNameContaining(name, pageable);
        } else {
            // không search -> hiển thị toàn bộ
            resultPage = categoryService.findAll(pageable);
        }

        int totalPages = resultPage.getTotalPages();
        if (totalPages > 0) {
            int start = Math.max(0, currentPage - 2);
            int end = Math.min(currentPage + 2, totalPages - 1);
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
                    .boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("categoryPage", resultPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("name", name); // giữ lại name để gõ xong search vẫn còn

        return "admin/categories/searchpaginated";
    }


}
