package com.sorrentino.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sorrentino.dscatalog.dto.CategoryDTO;
import com.sorrentino.dscatalog.dto.ProductDTO;
import com.sorrentino.dscatalog.entities.Category;
import com.sorrentino.dscatalog.entities.Product;
import com.sorrentino.dscatalog.repositories.CategoryRepository;
import com.sorrentino.dscatalog.repositories.ProductRepository;
import com.sorrentino.dscatalog.services.exceptions.DataBaseException;
import com.sorrentino.dscatalog.services.exceptions.ResourceEntityNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository  categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
		Page<Product> list = repository.findAll(pageRequest);		
		return list.map(x -> new ProductDTO(x));
		
		//Ou.
		/*List<ProductDTO> listDTO = new ArrayList<>();
		for (Product cat : list) {
			listDTO.add(new ProductDTO(cat));
		}*/		
		//return listDTO;
		
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceEntityNotFoundException("Entity not found"));
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ProductDTO(entity);
	}	

	@Transactional
	public ProductDTO update(Long id,ProductDTO dto) {
		try {
		Product entity = repository.getOne(id);
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ProductDTO(entity);
		}
		catch(EntityNotFoundException e){
			throw new ResourceEntityNotFoundException("Id not found " + id);			
		}
	}
	
	public void delete(Long id) {
		try {
		 	repository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceEntityNotFoundException("Id not found " + id);
		}
		
		catch(DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity violltion");
		}
		
	}	
	
	private void copyDtoToEntity(ProductDTO dto, Product entity) {

		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());
		
		entity.getCategories().clear();
		for (CategoryDTO catDto : dto.getCategories()) {
			Category category = categoryRepository.getOne(catDto.getId());
			entity.getCategories().add(category);
		}
		
	}
}






