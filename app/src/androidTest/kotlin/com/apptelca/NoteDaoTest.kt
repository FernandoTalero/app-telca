package com.apptelca

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.apptelca.data.local.dao.NoteDao
import com.apptelca.data.local.dao.WorkDao
import com.apptelca.data.local.database.DataBase
import com.apptelca.data.local.entities.NoteEntity
import com.apptelca.data.local.entities.WorkEntity
import com.apptelca.domain.model.garment.GarmentType
import com.apptelca.domain.model.garment.subgarment.GarmentSubType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant

/**
 * Realiza pruebas unitarias instrumentadas para la interfaz NoteDao.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@RunWith(AndroidJUnit4::class)
class NoteDaoTest {
    private lateinit var dataBase: DataBase
    private lateinit var workDao: WorkDao
    private lateinit var noteDao: NoteDao

    @Before
    fun createDataBase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dataBase = Room.inMemoryDatabaseBuilder(context, DataBase::class.java).build()
        workDao = dataBase.workDao()
        noteDao = dataBase.noteDao()
    }

    @After
    fun closeDataBase() {
        dataBase.close()
    }

    @Test
    fun insertNote_isCorrect() = runTest {
        val workEntity = WorkEntity(
            type = GarmentType.CAMISA,
            subtype = GarmentSubType.CAMISA_BASICA,
            name = "Test work",
            dateTime = Instant.now()
        )

        // Almacenamos el objeto WorkEntity
        val workId = workDao.insertWork(workEntity)

        val noteId = 1L
        val noteEntity = NoteEntity(
            workId = workId,
            text = "Test note text",
            dateTime = Instant.now()
        )

        // Almacenamos el objeto NoteEntity
        noteDao.insertNote(noteEntity)
        // Obtenemos el objeto NoteEntity almacenado
        val retrievedNoteEntity = noteDao.getNote(noteId)

        requireNotNull(retrievedNoteEntity)
        assertEquals(workId, retrievedNoteEntity.workId)
    }

    @Test
    fun updateNote_isCorrect() = runTest {
        val workEntity = WorkEntity(
            type = GarmentType.CAMISA,
            subtype = GarmentSubType.CAMISA_BASICA,
            name = "Test work",
            dateTime = Instant.now()
        )

        // Almacenamos el objeto WorkEntity
        val workId = workDao.insertWork(workEntity)

        val noteId = 1L
        val noteEntity = NoteEntity(
            workId = workId,
            text = "Test note text",
            dateTime = Instant.now()
        )

        // Almacenamos el objeto NoteEntity
        noteDao.insertNote(noteEntity)
        // Obtenemos el objeto NoteEntity almacenado
        val retrievedNoteEntity = noteDao.getNote(noteId)

        // Actualizamos el objeto NoteEntity
        val updatedNoteEntity = retrievedNoteEntity?.copy(text = "Updated test note text")
        updatedNoteEntity?.let { noteDao.updateNote(it) }

        // Obtenemos el objeto NoteEntity actualizado
        val retrievedUpdatedNoteEntity = noteDao.getNote(noteId)

        requireNotNull(retrievedUpdatedNoteEntity)
        assertEquals("Updated test note text", retrievedUpdatedNoteEntity.text)
    }

    @Test
    fun deleteNotes_isCorrect() = runTest {
        val workEntity = WorkEntity(
            type = GarmentType.CAMISA,
            subtype = GarmentSubType.CAMISA_BASICA,
            name = "Test work",
            dateTime = Instant.now()
        )

        // Almacenamos el objeto WorkEntity
        val workId = workDao.insertWork(workEntity)

        val noteId = 1L
        val noteEntity = NoteEntity(
            workId = workId,
            text = "Test note text",
            dateTime = Instant.now()
        )

        // Almacenamos el objeto NoteEntity
        noteDao.insertNote(noteEntity)
        // Obtenemos el objeto NoteEntity almacenado
        val retrievedNoteEntity = noteDao.getNote(noteId)

        requireNotNull(retrievedNoteEntity)
        val notesList = listOf(retrievedNoteEntity)

        // Eliminamos el objeto NoteEntity almacenado
        noteDao.deleteNotes(notesList)

        val deletedNoteEntity = noteDao.getNote(noteId)
        assertNull(deletedNoteEntity)
    }

    @Test
    fun getNotes_isCorrect() = runTest {
        val workEntity = WorkEntity(
            type = GarmentType.CAMISA,
            subtype = GarmentSubType.CAMISA_BASICA,
            name = "Test work",
            dateTime = Instant.now()
        )

        // Almacenamos el objeto WorkEntity
        val workId = workDao.insertWork(workEntity)

        val noteEntity = NoteEntity(
            workId = workId,
            text = "Test note text",
            dateTime = Instant.now()
        )

        // Almacenamos el objeto NoteEntity
        noteDao.insertNote(noteEntity)
        // Obtenemos una lista de objetos NoteEntity a partir del Flow correspondiente
        val retrievedNoteEntityList = noteDao.getNotes(workId).first()

        assertEquals(1, retrievedNoteEntityList.size)
    }

    @Test
    fun getNote_isCorrect() = runTest {
        val workEntity = WorkEntity(
            type = GarmentType.CAMISA,
            subtype = GarmentSubType.CAMISA_BASICA,
            name = "Test work",
            dateTime = Instant.now()
        )

        // Almacenamos el objeto WorkEntity
        val workId = workDao.insertWork(workEntity)

        val noteId = 1L
        val noteEntity = NoteEntity(
            workId = workId,
            text = "Test note text",
            dateTime = Instant.now()
        )

        // Almacenamos el objeto NoteEntity
        noteDao.insertNote(noteEntity)
        // Obtenemos el objeto NoteEntity almacenado
        val retrievedNoteEntity = noteDao.getNote(noteId)

        assertNotNull(retrievedNoteEntity)
    }
}