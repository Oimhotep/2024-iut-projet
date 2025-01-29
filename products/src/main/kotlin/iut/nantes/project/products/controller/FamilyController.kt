package iut.nantes.project.products.controller

import iut.nantes.project.products.models.FamilyDTO
import iut.nantes.project.products.service.FamilyService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/v1/families")
class FamilyController(private val db : FamilyService) {

    @PostMapping
    fun createFamily(@Valid @RequestBody family : FamilyDTO): ResponseEntity<FamilyDTO> {
        val familySaved = db.saveFamily(family)
        return ResponseEntity.status(HttpStatus.CREATED).body(familySaved)
    }

    @GetMapping
    fun getAllFamilies(): ResponseEntity<List<FamilyDTO>> {
        val families = db.findAllFamilies()
        return ResponseEntity.ok(families)
    }

    @DeleteMapping("/{id}")
    fun deleteFamily(@PathVariable id : UUID) : ResponseEntity<Unit> {
        db.deleteFamily(id)
        return ResponseEntity.noContent().build()
    }


}
