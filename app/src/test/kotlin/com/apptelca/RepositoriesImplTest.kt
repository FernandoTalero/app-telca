package com.apptelca

import androidx.room.withTransaction
import com.apptelca.data.local.dao.MeasurementDao
import com.apptelca.data.local.dao.NoteDao
import com.apptelca.data.local.dao.WorkDao
import com.apptelca.data.local.database.DataBase
import com.apptelca.data.local.entities.MeasurementEntity
import com.apptelca.data.local.entities.WorkEntity
import com.apptelca.data.local.entities.WorkWithMeasurementsEntity
import com.apptelca.data.mapper.toDomain
import com.apptelca.data.mapper.toEntity
import com.apptelca.data.mapper.toEntityList
import com.apptelca.data.repository.MeasurementRepositoryImpl
import com.apptelca.data.repository.NoteRepositoryImpl
import com.apptelca.data.repository.WorkRepositoryImpl
import com.apptelca.domain.model.Measurement
import com.apptelca.domain.model.Note
import com.apptelca.domain.model.Work
import com.apptelca.domain.model.garment.GarmentType
import com.apptelca.domain.model.garment.subgarment.GarmentSubType
import com.apptelca.domain.model.garment.subgarment.MeasurementType
import com.apptelca.domain.model.garment.subgarment.MeasurementValue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant

/**
 * Realiza pruebas unitarias locales de las funciones de las clases
 * que implementan los repositorios de la base de datos.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Pruebas unitarias locales de las clases que implementan los repositorios")
class RepositoriesImplTest {
    /**
     * Realiza pruebas unitarias locales de las funciones de la clase
     * WorkRepositoryImpl.
     *
     * @author Fernando Talero
     * @version 1.0
     * @since 1.0
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @DisplayName("WorkRepositoryImpl")
    @Nested
    inner class WorkRepositoryImplTests {
        private lateinit var dataBase: DataBase
        private lateinit var workDao: WorkDao
        private lateinit var measurementDao: MeasurementDao
        private lateinit var repository: WorkRepositoryImpl
        private val testDispatcher = UnconfinedTestDispatcher()

        @BeforeEach
        fun setup() {
            dataBase = mockk<DataBase>()
            workDao = mockk<WorkDao>()
            measurementDao = mockk<MeasurementDao>()
            repository = WorkRepositoryImpl(
                dataBase,
                workDao,
                measurementDao,
                testDispatcher,
                testDispatcher
            )

            // Mock de la función de extensión withTransaction de Room
            mockkStatic("androidx.room.RoomDatabaseKt")
            // Slot para capturar la expresión lambda de withTransaction
            val transactionSlot = slot<suspend () -> Any?>()
            // Definimos la función y capturamos la lambda
            coEvery { dataBase.withTransaction(capture(transactionSlot)) } coAnswers {
                // Ejecutamos la lambda capturada
                transactionSlot.captured.invoke()
            }
        }

        @AfterEach
        fun unmockk() {
            unmockkAll()
        }

        @DisplayName("Inserta un objeto Work junto con las medidas correspondientes")
        @Test
        fun insertWork_isCorrect() = runTest {
            val work = Work(
                type = GarmentType.CAMISA,
                subtype = GarmentSubType.CAMISA_BASICA,
                name = "Mi camisa",
                dateTime = Instant.now()
            )

            val measurementsValues = listOf(
                MeasurementValue(MeasurementType.ANCHO_TELA, 150.0)
            )

            // Definimos la función workDao.insertWork()
            coEvery { workDao.insertWork(work.toEntity()) } returns 1L
            // Definimos la función measurementDao.insertMeasurements()
            coEvery { measurementDao.insertMeasurements(any()) } returns Unit

            repository.insertWork(work, measurementsValues)

            // Verificamos la llamada a las funciones definidas
            coVerify {
                workDao.insertWork(match {
                    it.type == GarmentType.CAMISA && it.name == "Mi camisa"
                })
            }
            coVerify {
                measurementDao.insertMeasurements(match {
                    it.size == 1 && it[0].workId == 1L && it[0].value == 150.0
                })
            }
        }

        @DisplayName("Actualiza un objeto Work junto con las medidas correspondientes")
        @Test
        fun updateWork_isCorrect() = runTest {
            val work = Work(
                id = 1L,
                type = GarmentType.CAMISA,
                subtype = GarmentSubType.CAMISA_BASICA,
                name = "Mi camisa",
                dateTime = Instant.now()
            )

            val measurementsValues = listOf(
                MeasurementValue(MeasurementType.ANCHO_TELA, 150.0)
            )

            // Definimos la función workDao.updateWork()
            coEvery { workDao.updateWork(work.toEntity()) } returns Unit
            // Definimos la función measurementDao.getMeasurements()
            coEvery { measurementDao.getMeasurements(1) } returns listOf(
                MeasurementEntity(
                    id = 1,
                    workId = 1,
                    measurementType = MeasurementType.ANCHO_TELA,
                    value = 100.0
                )
            )
            // Definimos la función measurementDao.updateMeasurements()
            coEvery { measurementDao.updateMeasurements(any()) } returns Unit

            repository.updateWork(work, measurementsValues)

            // Verificamos la llamada a las funciones definidas
            coVerify {
                workDao.updateWork(match {
                    it.id == 1L && it.name == "Mi camisa"
                })
            }
            coVerify { measurementDao.getMeasurements(match { it == 1L }) }
            coVerify {
                measurementDao.updateMeasurements(match {
                    it.size == 1 && it[0].id == 1L && it[0].value == 150.0
                })
            }
        }

        @DisplayName("Elimina uno o varios objetos Work")
        @Test
        fun deleteWorks_isCorrect() = runTest {
            val works = listOf(
                Work(
                    id = 1L,
                    type = GarmentType.CAMISA,
                    subtype = GarmentSubType.CAMISA_BASICA,
                    name = "Mi camisa",
                    dateTime = Instant.now()
                )
            )

            // Definimos la función workDao.deleteWorks()
            coEvery { workDao.deleteWorks(works.toEntityList()) } returns 1
            coEvery { workDao.deleteWorks(emptyList()) } returns 0

            val affectedRows = repository.deleteWorks(works)
            val affectedEmptyRows = repository.deleteWorks(emptyList())

            // Verificamos la llamada a la función definida
            coVerify { workDao.deleteWorks(match { it.size == 1 && it[0].id == 1L }) }
            coVerify { workDao.deleteWorks(match { it.isEmpty() }) }

            assertAll(
                "Valores retornados por deleteWorks()",
                {
                    assertEquals(1, affectedRows)
                }, { assertEquals(0, affectedEmptyRows) }
            )
        }

        @DisplayName("Obtiene todas los objetos Work")
        @Test
        fun getWorks_isCorrect() = runTest {
            val works = listOf(
                Work(
                    id = 1L,
                    type = GarmentType.CAMISA,
                    subtype = GarmentSubType.CAMISA_BASICA,
                    name = "Mi camisa",
                    dateTime = Instant.now()
                )
            )

            // Definimos la función workDao.getWorks()
            coEvery { workDao.getWorks() } returns flowOf(works.toEntityList())

            val repoWorks = repository.getWorks().first()

            // Verificamos la llamada a la función definida
            coVerify {
                @Suppress("UnusedFlow")
                workDao.getWorks()
            }

            assertEquals(works, repoWorks)
        }

        @DisplayName("Obtiene un objeto Work a partir de su id")
        @Test
        fun getWork_isCorrect() = runTest {
            val work = Work(
                id = 1L,
                type = GarmentType.CAMISA,
                subtype = GarmentSubType.CAMISA_BASICA,
                name = "Mi camisa",
                dateTime = Instant.now()
            )

            // Definimos la función workDao.getWork()
            coEvery { workDao.getWork(1) } returns work.toEntity()
            coEvery { workDao.getWork(2) } returns null

            val repoWork = repository.getWork(1)
            val repoNullWork = repository.getWork(2)

            // Verificamos la llamada a la función definida
            coVerify { workDao.getWork(match { it == 1L }) }
            coVerify { workDao.getWork(match { it == 2L }) }

            assertAll(
                "Valores retornados por getWork()",
                { assertEquals(work, repoWork) },
                { assertEquals(null, repoNullWork) }
            )
        }

        @DisplayName("Obtiene un objeto WorkWithMeasurements")
        @Test
        fun getWorkWithMeasurements_isCorrect() = runTest {
            val workWithMeasurementsEntity = WorkWithMeasurementsEntity(
                workEntity = WorkEntity(
                    id = 1L,
                    type = GarmentType.CAMISA,
                    subtype = GarmentSubType.CAMISA_BASICA,
                    name = "Mi camisa",
                    dateTime = Instant.now()
                ),
                measurementEntities = listOf(
                    MeasurementEntity(
                        id = 1,
                        workId = 1,
                        measurementType = MeasurementType.ANCHO_TELA,
                        value = 150.0
                    )
                )
            )

            // Definimos la función workDao.getWorkWithMeasurements()
            coEvery { workDao.getWorkWithMeasurements(1) } returns workWithMeasurementsEntity
            coEvery { workDao.getWorkWithMeasurements(2) } returns null

            val repoWorkWithMeasurements = repository.getWorkWithMeasurements(1)
            val repoNullWorkWithMeasurements = repository.getWorkWithMeasurements(2)

            // Verificamos la llamada a la función definida
            coVerify { workDao.getWorkWithMeasurements(match { it == 1L }) }
            coVerify { workDao.getWorkWithMeasurements(match { it == 2L }) }

            assertAll(
                "Valores retornados por getWorkWithMeasurements()",
                {
                    assertEquals(
                        workWithMeasurementsEntity.toDomain(),
                        repoWorkWithMeasurements
                    )
                }, { assertEquals(null, repoNullWorkWithMeasurements) }
            )
        }
    }

    /**
     * Realiza pruebas unitarias locales de las funciones de la clase
     * MeasurementRepositoryImpl.
     *
     * @author Fernando Talero
     * @version 1.0
     * @since 1.0
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @DisplayName("MeasurementRepositoryImpl")
    @Nested
    inner class MeasurementRepositoryImplTests {
        private lateinit var dataBase: DataBase
        private lateinit var measurementDao: MeasurementDao
        private lateinit var repository: MeasurementRepositoryImpl
        private val testDispatcher = UnconfinedTestDispatcher()

        @BeforeEach
        fun setup() {
            dataBase = mockk<DataBase>()
            measurementDao = mockk<MeasurementDao>()
            repository = MeasurementRepositoryImpl(measurementDao, testDispatcher)
        }

        @AfterEach
        fun unmockk() {
            unmockkAll()
        }

        @DisplayName("Inserta uno o varios objetos Measurement")
        @Test
        fun insertMeasurements_isCorrect() = runTest {
            val measurements = listOf(
                Measurement(
                    workId = 1,
                    measurementType = MeasurementType.ANCHO_TELA,
                    value = 150.0
                )
            )

            // Definimos la función measurementDao.insertMeasurement()
            coEvery {
                measurementDao.insertMeasurements(measurements.toEntityList())
            } returns Unit

            repository.insertMeasurements(measurements)

            // Verificamos la llamada a la función definida
            coVerify {
                measurementDao.insertMeasurements(match {
                    it.size == 1 && it[0].workId == 1L && it[0].value == 150.0
                })
            }
        }

        @DisplayName("Actualiza uno o varios objetos Measurement")
        @Test
        fun updateMeasurements_isCorrect() = runTest {
            val measurements = listOf(
                Measurement(
                    id = 1,
                    workId = 1,
                    measurementType = MeasurementType.ANCHO_TELA,
                    value = 150.0
                )
            )

            // Definimos la función measurementDao.updateMeasurement()
            coEvery {
                measurementDao.updateMeasurements(measurements.toEntityList())
            } returns Unit

            repository.updateMeasurements(measurements)

            // Verificamos la llamada a la función definida
            coVerify {
                measurementDao.updateMeasurements(match {
                    it.size == 1 && it[0].id == 1L && it[0].value == 150.0
                })
            }
        }
    }

    /**
     * Realiza pruebas unitarias locales de las funciones de la clase
     * NoteRepositoryImpl.
     *
     * @author Fernando Talero
     * @version 1.0
     * @since 1.0
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @DisplayName("NoteRepositoryImpl")
    @Nested
    inner class NoteRepositoryImplTests {
        private lateinit var dataBase: DataBase
        private lateinit var noteDao: NoteDao
        private lateinit var repository: NoteRepositoryImpl
        private val testDispatcher = UnconfinedTestDispatcher()

        @BeforeEach
        fun setup() {
            dataBase = mockk<DataBase>()
            noteDao = mockk<NoteDao>()
            repository = NoteRepositoryImpl(
                noteDao, testDispatcher, testDispatcher
            )
        }

        @AfterEach
        fun unmockk() {
            unmockkAll()
        }

        @DisplayName("Inserta un objeto Note")
        @Test
        fun insertNote_isCorrect() = runTest {
            val note = Note(
                workId = 1,
                text = "Mi nota",
                dateTime = Instant.now()
            )

            // Definimos la función noteDao.insertNote()
            coEvery { noteDao.insertNote(note.toEntity()) } returns Unit

            repository.insertNote(note)

            // Verificamos la llamada a la función definida
            coVerify {
                noteDao.insertNote(match { it.workId == 1L && it.text == "Mi nota" })
            }
        }

        @DisplayName("Actualiza un objeto Note")
        @Test
        fun updateNote_isCorrect() = runTest {
            val note = Note(
                id = 1,
                workId = 1,
                text = "Mi nota",
                dateTime = Instant.now()
            )

            // Definimos la función noteDao.updateNote()
            coEvery { noteDao.updateNote(note.toEntity()) } returns Unit

            repository.updateNote(note)

            // Verificamos la llamada a la función definida
            coVerify {
                noteDao.updateNote(match { it.id == 1L && it.text == "Mi nota" })
            }
        }

        @DisplayName("Elimina uno o más objetos Note")
        @Test
        fun deleteNotes_isCorrect() = runTest {
            val notes = listOf(
                Note(
                    id = 1,
                    workId = 1,
                    text = "Mi nota",
                    dateTime = Instant.now()
                )
            )

            // Definimos la función noteDao.deleteNotes()
            coEvery { noteDao.deleteNotes(notes.toEntityList()) } returns 1
            coEvery { noteDao.deleteNotes(emptyList()) } returns 0

            val affectedRows = repository.deleteNotes(notes)
            val affectedEmptyRows = repository.deleteNotes(emptyList())

            // Verificamos la llamada a la función definida
            coVerify {
                noteDao.deleteNotes(match {
                    it.size == 1 && it[0].id == 1L && it[0].text == "Mi nota"
                })
            }
            coVerify { noteDao.deleteNotes(match { it.isEmpty() }) }

            assertAll(
                "Valores retornados por deleteNotes()",
                { assertEquals(1, affectedRows) },
                { assertEquals(0, affectedEmptyRows) }
            )
        }

        @DisplayName("Obtiene todas las notas de un objeto Work")
        @Test
        fun getNotes_isCorrect() = runTest {
            val notes = listOf(
                Note(
                    id = 1,
                    workId = 1,
                    text = "Mi nota",
                    dateTime = Instant.now()
                )
            )

            // Definimos la función noteDao.getNotes()
            coEvery { noteDao.getNotes(1) } returns flowOf(notes.toEntityList())
            coEvery { noteDao.getNotes(2) } returns emptyFlow()

            val notesFlowValue = repository.getNotes(1).first()
            val notesEmptyFlowCount = repository.getNotes(2).count()

            // Verificamos la llamada a la función definida
            coVerify {
                @Suppress("UnusedFlow")
                noteDao.getNotes(match { it == 1L })
            }
            coVerify {
                @Suppress("UnusedFlow")
                noteDao.getNotes(match { it == 2L })
            }

            assertAll(
                "Valores retornados por el Flow de notas",
                { assertEquals(notes, notesFlowValue) },
                { assertEquals(0, notesEmptyFlowCount) }
            )
        }

        @DisplayName("Obtiene un objeto Note a partir de su id")
        @Test
        fun getNote_isCorrect() = runTest {
            val note = Note(
                id = 1,
                workId = 1,
                text = "Mi nota",
                dateTime = Instant.now()
            )

            // Definimos la función noteDao.getNote()
            coEvery { noteDao.getNote(1) } returns note.toEntity()
            coEvery { noteDao.getNote(2) } returns null

            val repoNote = repository.getNote(1)
            val repoNullNote = repository.getNote(2)

            // Verificamos la llamada a la función definida
            coVerify { noteDao.getNote(match { it == 1L }) }
            coVerify { noteDao.getNote(match { it == 2L }) }

            assertAll(
                "Valores retornados por getNote()",
                { assertEquals(note, repoNote) },
                { assertEquals(null, repoNullNote) }
            )
        }
    }
}