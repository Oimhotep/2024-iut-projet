package iut.nantes.project.products.repository.family

import iut.nantes.project.products.models.FamilyDTO

import java.util.*

interface FamilyRepository {
    /**
     * Sauvegarde une famille dans la base de données.
     *
     * @param family : La famille à sauvegarder.
     * @return La famille sauvegardée.
     */
    fun save(family: FamilyDTO): FamilyDTO

    /**
     * Récupère toutes les familles.
     *
     * @return Liste de toutes les familles présentes.
     */
    fun findAll(): List<FamilyDTO>

    /**
     * Recherche une famille par son nom.
     *
     * @param name : Le nom de la famille à rechercher.
     * @return La famille correspondante ou null si aucune famille n’est trouvée.
     */
    fun findByName(name: String): FamilyDTO?

    /**
     * Recherche une famille par son identifiant (UUID).
     *
     * @param id : L'ID de la famille à rechercher.
     * @return La famille correspondante ou null si aucune famille n’est trouvée.
     */
    fun findById(id: UUID): FamilyDTO?

    /**
     * Vérifie si une famille existe déjà avec le nom donné.
     *
     * @param name : Le nom à vérifier.
     * @return true si une famille existe avec ce nom, false sinon.
     */
    fun existsByName(name: String): Boolean

    /**
     * Supprime une famille.
     *
     * @param family : La famille à supprimer.
     */
    fun delete(family: FamilyDTO)

    /**
     * Supprime toutes les familles.
     * Utilisée dans le cadre de réinitialisation ou suppression complète des données.
     */
    fun drop()
}