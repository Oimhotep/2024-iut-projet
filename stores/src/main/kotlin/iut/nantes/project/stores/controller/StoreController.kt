package iut.nantes.project.stores.controller

import iut.nantes.project.stores.exception.EntityNotFoundException
import iut.nantes.project.stores.model.Contact
import iut.nantes.project.stores.model.Store
import iut.nantes.project.stores.service.StoreService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/stores")
@Validated
class StoreController(private val storeService: StoreService) {

    @PostMapping
    fun createStore(@Valid @RequestBody store: Store): ResponseEntity<Store> {
        val createdStore = storeService.createStore(store)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStore)
    }

    @GetMapping
    fun getAllStores(): ResponseEntity<List<Store>> {
        val stores = storeService.getAllStores()
        return ResponseEntity.ok(stores)
    }

    @GetMapping("/{id}")
    fun getStoreById(@PathVariable id: Long): ResponseEntity<Store> {
        val store = storeService.getStoreById(id)

        return ResponseEntity.ok(store)
    }

    @PutMapping("/{id}")
    fun updateStore(@PathVariable id: Long, @Valid @RequestBody store: Store): ResponseEntity<Store> {
        val updatedStore = storeService.updateStore(id, store)
        return ResponseEntity.ok(updatedStore)
    }

    @DeleteMapping("/{id}")
    fun deleteStore(@PathVariable id: Long): ResponseEntity<Void> {
        storeService.deleteStore(id)
        return ResponseEntity.noContent().build()
    }
}
