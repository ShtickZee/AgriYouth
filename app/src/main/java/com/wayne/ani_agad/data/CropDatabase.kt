package com.wayne.ani_agad.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "crop_batches")
data class CropBatch(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ownerId: String,
    val cropType: String,
    val batchName: String,
    val containerCount: Int,
    val growthMethod: String, // New field for version 3
    val datePlanted: Long
)

@Dao
interface CropDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCropBatch(cropBatch: CropBatch): Long

    @Delete
    suspend fun deleteCropBatch(cropBatch: CropBatch)

    @Query("SELECT * FROM crop_batches WHERE ownerId = :userId ORDER BY datePlanted DESC")
    fun getAllCropBatches(userId: String): Flow<List<CropBatch>>
}

@Database(entities = [CropBatch::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cropDao(): CropDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "crop_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
