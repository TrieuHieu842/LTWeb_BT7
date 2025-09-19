package vn.iotstar.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.iotstar.Entity.CategoryEntity;

@Service
public interface CategoryService {
	void deleteById(Integer id);

	Optional<CategoryEntity> findById(Integer id);

	List<CategoryEntity> findAll();
	
	<S extends CategoryEntity> S save(S entity);

	Page<CategoryEntity> findByNameContaining(String name, Pageable pageable);

	List<CategoryEntity> findByNameContaining(String name);
	Optional<CategoryEntity> findByName(String name);

	Page<CategoryEntity> findAll(Pageable pageable);

	void delete(CategoryEntity entity);
}
