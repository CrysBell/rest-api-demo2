package com.example.services;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.dao.PresentationDao;
import com.example.dao.ProductDao;
import com.example.entities.Presentation;
import com.example.entities.Product;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductDao productDao;

    @Mock
    private PresentationDao presentationDao;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    Product product1, product2;

    List<Product> productsList = new ArrayList<>();

    @BeforeEach
    void setUp() {

        Presentation presentation = Presentation.builder()
                .name("unidades")
                .description("por unidades")
                .build();

        product1 = Product.builder()
                .name("Google Pixel 7")
                .description("Telefono de Google")
                .price(new BigDecimal(400))
                .stock(1000)
                .productImage(null)
                .presentation(presentation)
                .build();

        product2 = Product.builder()
                .name("iPhone 17 Pro")
                .description("Telefono de Apple")
                .price(new BigDecimal(1300))
                .stock(1500)
                .productImage(null)
                .presentation(presentation)
                .build();

        productsList.add(product1);
        productsList.add(product2);
    }

    @Test
    @DisplayName("Test para recuperar productos mediante paginación")
    void testFindAllPageable() {

        // given
        Pageable pageable = PageRequest.of(0, 2);

        Page<Product> page = new PageImpl<>(productsList);

        given(productDao.findAll(pageable))
                .willReturn(page);

        // when
        Page<Product> result = productServiceImpl.findAll(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent())
                .containsExactly(product1, product2);
    }

    @Test
    @DisplayName("Test para recuperar productos ordenados")
    void testFindAllSort() {

        // given
        Sort sort = Sort.by("name");

        given(productDao.findAll(sort))
                .willReturn(productsList);

        // when
        List<Product> result = productServiceImpl.findAll(sort);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result)
                .containsExactly(product1, product2);
    }

    @Test
    @DisplayName("Test para recuperar un producto por su ID")
    void testFindById() {

        // given
        int productId = 1;

        given(productDao.findById(productId))
                .willReturn(product1);

        // when
        Product result = productServiceImpl.findById(productId);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(product1);
    }

    @Test
    @DisplayName("Test del servicio para persistir un producto")
    void testSave() {

        // given
        given(productDao.save(product1))
                .willReturn(product1);

        // when
        Product productoGuardado = productServiceImpl.save(product1);

        // then
        assertThat(productoGuardado).isNotNull();
    }

    @Test
    @DisplayName("Test para eliminar un producto")
    void testDelete() {

        // when
        productServiceImpl.delete(product1);

        // then
        verify(productDao).delete(product1);
    }

    @Test
    @DisplayName("Test para recuperar los dos productos creados")
    void testFindAllProducts() {

        // given
        when(productDao.findAll())
                .thenReturn(productsList);

        // when
        List<Product> result = productServiceImpl.findAll();

        // then
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Test para recuperar una lista vacía de productos")
    void testEmptyProductList() {

        // given
        given(productDao.findAll())
                .willReturn(Collections.emptyList());

        // when
        List<Product> products = productServiceImpl.findAll();

        // then
        assertThat(products).isEmpty();
    }
}