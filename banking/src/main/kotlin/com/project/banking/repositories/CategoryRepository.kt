package com.project.banking.repositories

import com.project.banking.entities.CategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository: JpaRepository<CategoryEntity, Long> {
    fun findByName(name: String): CategoryEntity?

    @Query(
        """
    SELECT 
        c.id AS id,
        c.name AS name,
        CASE 
            WHEN COUNT(pc.id) > 0 THEN true 
            ELSE false 
        END AS hasPerks
    FROM CategoryEntity c
    LEFT JOIN PerkCategoryEntity pc ON pc.category = c
    GROUP BY c.id, c.name
    """
    )
    fun findAllWithPerkAssociation(): List<CategoryWithPerksView>

}

interface CategoryWithPerksView {
    val id: Long
    val name: String
    val hasPerks: Boolean
}
