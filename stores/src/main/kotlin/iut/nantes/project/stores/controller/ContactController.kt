package iut.nantes.project.stores.controller

import iut.nantes.project.stores.model.Contact
import iut.nantes.project.stores.service.ContactService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/contacts")
@Validated
class ContactController(private val contactService: ContactService) {

    @PostMapping
    fun createContact(@Valid @RequestBody contact: Contact): ResponseEntity<Contact> {
        val createdContact = contactService.createContact(contact)
        println("Contact créé: $createdContact")
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(createdContact)
    }

    @GetMapping("/{id}")
    fun getContactById(@PathVariable id: Long): ResponseEntity<Contact> {
        val contact = contactService.getContactById(id)
        return ResponseEntity.ok(contact)
    }

    @GetMapping
    fun getAllContacts(@RequestParam(required = false) city: String?): ResponseEntity<List<Contact>> {
        val contacts = contactService.getAllContacts(city)
        return ResponseEntity.ok(contacts)
    }

    @PutMapping("/{id}")
    fun updateContact(@PathVariable id: Long, @Valid @RequestBody updatedContact: Contact): ResponseEntity<Contact> {
        val updated = contactService.updateContact(id, updatedContact)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteContact(@PathVariable id: Long): ResponseEntity<Void> {
        contactService.deleteContact(id)
        return ResponseEntity.noContent().build()
    }
}
