package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.MyData;
import com.mycompany.myapp.repository.MyDataRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.MyData}.
 */
@RestController
@RequestMapping("/api")
public class MyDataResource {

    private final Logger log = LoggerFactory.getLogger(MyDataResource.class);

    private static final String ENTITY_NAME = "myData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MyDataRepository myDataRepository;

    public MyDataResource(MyDataRepository myDataRepository) {
        this.myDataRepository = myDataRepository;
    }

    /**
     * {@code POST  /my-data} : Create a new myData.
     *
     * @param myData the myData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new myData, or with status {@code 400 (Bad Request)} if the myData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/my-data")
    public ResponseEntity<MyData> createMyData(@Valid @RequestBody MyData myData) throws URISyntaxException {
        log.debug("REST request to save MyData : {}", myData);
        if (myData.getId() != null) {
            throw new BadRequestAlertException("A new myData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MyData result = myDataRepository.save(myData);
        return ResponseEntity.created(new URI("/api/my-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /my-data} : Updates an existing myData.
     *
     * @param myData the myData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated myData,
     * or with status {@code 400 (Bad Request)} if the myData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the myData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/my-data")
    public ResponseEntity<MyData> updateMyData(@Valid @RequestBody MyData myData) throws URISyntaxException {
        log.debug("REST request to update MyData : {}", myData);
        if (myData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MyData result = myDataRepository.save(myData);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, myData.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /my-data} : get all the myData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of myData in body.
     */
    @GetMapping("/my-data")
    public List<MyData> getAllMyData() {
        log.debug("REST request to get all MyData");
        return myDataRepository.findAll();
    }

    /**
     * {@code GET  /my-data/:id} : get the "id" myData.
     *
     * @param id the id of the myData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the myData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/my-data/{id}")
    public ResponseEntity<MyData> getMyData(@PathVariable Long id) {
        log.debug("REST request to get MyData : {}", id);
        Optional<MyData> myData = myDataRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(myData);
    }

    /**
     * {@code DELETE  /my-data/:id} : delete the "id" myData.
     *
     * @param id the id of the myData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/my-data/{id}")
    public ResponseEntity<Void> deleteMyData(@PathVariable Long id) {
        log.debug("REST request to delete MyData : {}", id);
        myDataRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
