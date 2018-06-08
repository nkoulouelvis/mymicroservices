package com.ecommerce.microservices.controller;

import com.ecommerce.microservices.dao.ProductDao;
import com.ecommerce.microservices.exception.ProduitIntrouvableException;
import com.ecommerce.microservices.model.Product;
import com.sun.org.glassfish.gmbal.Description;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Api(description = "Gestion des produits")
@RestController
public class ProductController {
    @Autowired
    private ProductDao productDao;

    //Products
    @GetMapping(value = "Products")
    public List<Product> listeProduits() {
        return productDao.findAll();
    }

    //Product/{id}
    @ApiOperation(value = "Récupère un produit selon son ID")
    @GetMapping(value = "Products/{id}")
    public Product afficherUnProduct(@PathVariable int id) throws ProduitIntrouvableException {
        Product produit = productDao.findById(id);
        if (produit == null) throw new ProduitIntrouvableException("Le produit " + id + " n'existe pas!");
        return produit;

    }

    @PostMapping(value = "/Products")
    public ResponseEntity<Void> ajouterProduct(@Valid @RequestBody Product product) {
        Product product1 = productDao.save(product);
        if (product1 == null) {
            return ResponseEntity.noContent().build();
        }
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product1.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "test/products/{prixLimit}")
    public List<Product> testeDeRequetes(@PathVariable int prixLimit) {
        //return productDao.findByPrixGreaterThan(prixLimit);
        return productDao.chercherUnProduitCher(prixLimit);
    }

}
