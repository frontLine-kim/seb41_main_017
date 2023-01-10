package com.codestates.culinari.product.controller;

import com.codestates.culinari.pagination.PageResponseDto;
import com.codestates.culinari.pagination.service.PaginationService;
import com.codestates.culinari.product.dto.response.ProductResponseToPage;
import com.codestates.culinari.product.service.ProductService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RequestMapping("/collections")
@RestController
public class ProductCollectionsController {

    private final ProductService productService;
    private final PaginationService paginationService;

    @GetMapping("/newproduct")
    public ResponseEntity getNewestProducts(
            @RequestParam(required = false , value = "sorted_type") String sortedType,
            @RequestParam(required = false, value = "filter") String filter,
            @RequestParam(required = false) int page,
            @Positive @RequestParam(required = false) int size
    ){

        if(sortedType == null && filter == null) sortedType = "newest";

        Page<ProductResponseToPage> newestProductsPage = productService.readProductWithSortedType(sortedType, page, size).map(ProductResponseToPage::from);
        List<ProductResponseToPage> productPage = newestProductsPage.getContent();
        List<Integer> barNumber = paginationService.getPaginationBarNumbers(page, newestProductsPage.getTotalPages());

        return new ResponseEntity<>(
                new PageResponseDto<>(productPage, newestProductsPage, barNumber), HttpStatus.OK);

    }
}