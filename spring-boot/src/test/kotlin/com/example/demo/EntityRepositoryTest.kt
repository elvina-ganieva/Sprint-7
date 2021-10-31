package com.example.demo

import com.example.demo.persistance.Entity
import com.example.demo.persistance.EntityRepository
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EntityRepositoryTest {

    @Autowired
    private lateinit var entityRepository: EntityRepository

    @BeforeAll
    fun setUp() {
        val entity1 = Entity(1, "El")
        val entity2 = Entity(2, "Vi")

        entityRepository.save(entity1)
        entityRepository.save(entity2)
    }

    @Test
    fun `save should save entity`() {
        // given
        val entity = Entity(3, "Na")

        // when
        val savedEntity = entityRepository.save(entity)

        // then
        assertTrue { entity == savedEntity }

        entityRepository.delete(entity)
    }

    @Test
    fun `findById should find entity`() {
        // given
        val expectedEntity = Entity(1, "El")

        // when
        val foundEntity = entityRepository.findById(1)

        // then
        assertTrue { expectedEntity == foundEntity.get() }
    }

    @Test
    fun `findAll should return all entities`() {
        // given
        val expectedEntityList = listOf(Entity(1, "El"), Entity(2, "Vi"))

        // when
        val foundEntities = entityRepository.findAll()

        // then
        assertTrue { expectedEntityList == foundEntities }
    }

    @Test
    fun `save should update entity`() {
        // given
        val entityForUpdate = Entity(2, "Not Vi")

        // when
        val updatedEntity = entityRepository.save(entityForUpdate)

        // then
        assertTrue { updatedEntity == entityForUpdate }
    }

    @Test
    fun `deleteById should delete entity`() {
        // given
        val expectedEntityList = listOf(Entity(1, "El"))

        // when
        entityRepository.deleteById(2)

        // then
        assertTrue { expectedEntityList == entityRepository.findAll() }
    }
}