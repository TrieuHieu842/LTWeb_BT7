package vn.iotstar.Service;

import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface StorageService {
	void init();
	void delete(String storeFilename) throws Exception;
	Path load(String filename);
	Resource loadAsResource(String filename);
	void store(MultipartFile file, String storeFilename);
	String getSorageFilename(MultipartFile file, String id);
}
